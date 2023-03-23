package ed.inf.adbs.minibase.evaluator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Schema;
import  ed.inf.adbs.minibase.base.Query;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
public class QueryPlanner extends Operator{


    private  String dbDir;
    private  Query inputQuery;
    private  Operator root;

    private DatabaseCatalog databaseCatalog = DatabaseCatalog.getCatalog();

    private List<ScanOperator> scanOperatorList = new ArrayList<>();


    private HashMap<String,Schema> SchemaMap = new HashMap<>();

    private List<RelationalAtom>relationalAtomList=new ArrayList<>();
    private List<ComparisonAtom> comparisonAtomList = new ArrayList<>();

    private  String fileNameR;

    private  String fileNameS;
    private  String fileNameT;

    private  String schemaFilePath;

    public QueryPlanner(Query inputQuery,String dbDir) throws IOException {
        this.inputQuery = inputQuery;
        this.dbDir=dbDir;
        this.schemaFilePath = UltsForEvaluator.getschemaPath(dbDir);
        databaseCatalog.constructSchemaMap(this.schemaFilePath);
        this.SchemaMap = databaseCatalog.getSchemaMap();
        this.fileNameR=UltsForEvaluator.csvFilePathGet(this.dbDir,"R");
        this.fileNameS=UltsForEvaluator.csvFilePathGet(this.dbDir,"S");
        this.fileNameT=UltsForEvaluator.csvFilePathGet(this.dbDir,"T");
        initializeRelationalAtomListComparisonList();
        setScanOpeartorList();
        constructTree();
    }


    public void constructTree() throws FileNotFoundException {

         // Set the scan operator list and predicate list
          List<ScanOperator> scanOperatorList = this.getScanOperatorList();
          List<ComparisonAtom> comparisonAtoms = this.getComparisonAtomList();

          // Select those predicates that can apply to the scan operator list directly
        List<ComparisonAtom> directCondition = filterNonJoinCondition(comparisonAtoms,scanOperatorList);


        // Set Hash map, which can map each comparison atom to each corresponding scan oprator list
        HashMap<ScanOperator,List<ComparisonAtom>> scanOperatorSingleSelectionMap = getRelevantScanOperatorMap(directCondition,scanOperatorList);

        // Set combined selection and scan operator list
        List<Operator> combinedOperatorList = CombinScanSelectionOperator(scanOperatorList,scanOperatorSingleSelectionMap);

        // If it is just a single atom query, we can just go directly to projection process. First need to extract relational Atom from the corresponding atom, then construct
        // the projection operator and add it as the root
        if (combinedOperatorList.size()==1)
        {
            Operator operator = combinedOperatorList.get(0);
            RelationalAtom extractedrelationalAtom=null;
            extractedrelationalAtom= extractRelationalAtomOperator(operator);
            List<Variable> variableHead = extractVaribaleHead(inputQuery.getHead());
            this.root = new ProjectionOperator(operator,variableHead,extractedrelationalAtom);
            return ;
        }

        //Get the join conditions
        List<ComparisonAtom> joinConditions = filterOutComparisonMultipleRelationalAtoms(comparisonAtoms,scanOperatorList);


        // Use first two operators from the operator list to join
        Operator first = combinedOperatorList.remove(0);
        Operator second = combinedOperatorList.remove(0);


        // get the joinConditionMap, which can be used to get the last relationAtom that can be applied by the comparisonAtom list
        HashMap<RelationalAtom,List<ComparisonAtom>> joinConditionMap = constructJoinConditionMap(joinConditions,scanOperatorList);

        // construct the leftRelationAtoms for join Operator

        List<RelationalAtom> leftRelationAtoms = new ArrayList<>();
        RelationalAtom firstRelationAtom = extractRelationalAtomOperator(first);
        leftRelationAtoms.add(firstRelationAtom);

        // construct the rightRelationalAtom for join Operator
        RelationalAtom rightRelationalAtom = extractRelationalAtomOperator(second);


        // Initialize the first JoinOperator
        List<ComparisonAtom> DefaultPredicate = joinConditionMap.getOrDefault(rightRelationalAtom,new ArrayList<>());
        JoinOperator curJoinOperator = new JoinOperator(first,second,leftRelationAtoms,rightRelationalAtom,DefaultPredicate);

        // after combining them together, then add the right relationalAtom to the left
        List<RelationalAtom> TempLeftRelational = deepCopyRelationalAtomList(leftRelationAtoms);
        TempLeftRelational.add(rightRelationalAtom);
        while(combinedOperatorList.size()!=0)
        {
            Operator rightChild = combinedOperatorList.remove(0);

            rightRelationalAtom = extractRelationalAtomOperator(rightChild);

            curJoinOperator = new JoinOperator(curJoinOperator, rightChild, new ArrayList<>(TempLeftRelational), rightRelationalAtom, joinConditionMap.getOrDefault(rightRelationalAtom, new ArrayList<>()));

            // Deep copy the tempLeftRelational
            TempLeftRelational= deepCopyRelationalAtomList(TempLeftRelational);
            TempLeftRelational.add(rightRelationalAtom);
        }
        // Construct the final projectionOperator for setting root.
        ProjectionOperator finalProjectionOperator = new ProjectionOperator(curJoinOperator,extractVaribaleHead(this.inputQuery.getHead()),TempLeftRelational);
        this.root = finalProjectionOperator;
        return;
    }


    /**
     * This method is used to deep copy a list of relational atoms, which is necessary when the previous leftrealtional atoms are changed
     * @param leftRelationAtoms
     * @return
     */
    public List<RelationalAtom> deepCopyRelationalAtomList(List<RelationalAtom>leftRelationAtoms)
    {
        List<RelationalAtom> temp =new ArrayList<>();
        for(RelationalAtom relationalAtom : leftRelationAtoms)
        {
            RelationalAtom copy = relationalAtom.deepcopy();
            temp.add(copy);
        }
        return temp;
    }

    @Override
    public String toString()
    {
        return this.root.toString();
    }

    /**
     * This method is used to get the last relationalAtom, that can by applied by the corresponding join condition.
     * For exmaple in this case : In the case of R(x,y,z) S(x,w,t), T(m,r), [x>t,m>r], this method will return a map: {S(x,w,t)=x>t,T(m,r)=m>2}
     * @param joinConditions a list of join conditions
     * @param scanOperatorList a list of scanOperators
     * @return return a Map which can be used to get a corresponding list of comparison atoms
     */
    public static HashMap<RelationalAtom,List<ComparisonAtom>> constructJoinConditionMap(List<ComparisonAtom>joinConditions,List<ScanOperator> scanOperatorList)
    {
        HashMap<RelationalAtom, List<ComparisonAtom>> joinConditionMap = new HashMap<>();
        for (ComparisonAtom comparisonAtom : joinConditions)
        {
            RelationalAtom lastRelationalAtom = getLastRelationalAtom(comparisonAtom, scanOperatorList);
            List<ComparisonAtom> comparisonAtoms = joinConditionMap.computeIfAbsent(lastRelationalAtom, k -> new ArrayList<>());
            comparisonAtoms.add(comparisonAtom);
        }
        return joinConditionMap;
    }
    /**
     * This method can be used to find the relation atom from the scan Operator that can be applied by the join condition
     * For example in the case of R(x,y,z) S(x,w,t), T(m,r), x>t, this method should return S(x,w,t).
     * To achieve this, we should first make sure that the comparison Atom has two variables. Then we need to make sure that it is a join a condition by using isSingleSelectionComparisonAtom()
     * Then we iterate through each scanOperator if is satifies that input predicate, we store it.
     * @param comparisonAtom input predicate
     * @param scanOperators checked list of scan operator
     * @return return the relevant relationAtom
     */
    public static RelationalAtom getLastRelationalAtom(ComparisonAtom comparisonAtom, List<ScanOperator> scanOperators) {
        if (comparisonAtom.getTerm1() instanceof Constant || comparisonAtom.getTerm2() instanceof Constant) {
            throw new IllegalArgumentException("This function should be called where the comparisonAtom has two varibale terms");
        }
        if (isSingleSelectionComparisonAtom(comparisonAtom, scanOperators)) {
            throw new UnsupportedOperationException("The comparison should  be a join condition !!!");
        }
        ScanOperator lastWithEitherVariable = null;
        for (ScanOperator scanOperator : scanOperators) {
            Term comparisonTerm1 = comparisonAtom.getTerm1();
            Term comparisonTerm2 = comparisonAtom.getTerm2();
            List<Term> relationAtomTerms = scanOperator.getRelationalAtom().getTerms();
            boolean checkContainTerm1 = relationAtomTerms.contains(comparisonTerm1);
            boolean checkContainTerm2 = relationAtomTerms.contains(comparisonTerm2);
            if (checkContainTerm1 || checkContainTerm2)
            {
                lastWithEitherVariable = scanOperator;
            }
        }
        if(lastWithEitherVariable==null)
        {
            throw new IllegalArgumentException("In the scan operator list, it doesn't have any relation that contains any of the varibale in the comparison atom");
        }
        return lastWithEitherVariable.getRelationalAtom();
    }
    // Extract relationalAtom from the corresponding operator
    public static RelationalAtom extractRelationalAtomOperator(Operator operator)
    {
        RelationalAtom extractedrelationalAtom = null;
        if (operator instanceof SelectionOperator)
        {
            extractedrelationalAtom= ((SelectionOperator) operator).getRelationalAtom();
        }
        else if (operator instanceof ScanOperator)
        {
            extractedrelationalAtom= ((ScanOperator) operator).getRelationalAtom();
        }
        else
        {
            throw new IllegalArgumentException("Invalid operator type!!!");
        }
        return extractedrelationalAtom;
    }
    // Extract the varibale from the head
    public static List<Variable> extractVaribaleHead(Head head)
    {
        return head.getVariables();
    }
    /**
     * This method is used to construct selection operator based on appliable fi. Iterate through the whole scanOperator List
     * If the current ScanOperator is in the map, it means it has corresponding appliable predicate list, which can hence build a corresponding select operator.
     * This method will finally return a list of operators, where some of them can be select Operators.
     * @param scanOperatorList a list of leaf Operators: Scan Operators
     * @param ScanOperatorSingleSelectionMap the map can be used to get corresponing predicate for a specific scanOperator
     * @return return a list of Operators.
     */
    public static  List<Operator> CombinScanSelectionOperator (List<ScanOperator> scanOperatorList,  HashMap<ScanOperator,List<ComparisonAtom>> ScanOperatorSingleSelectionMap)
    {
        List<Operator> operatorList = new ArrayList<>();
        for(ScanOperator scanOperator: scanOperatorList)
        {
            Operator currentOperator = scanOperator;
            boolean checkContainKey = ScanOperatorSingleSelectionMap.containsKey(scanOperator);
            if (checkContainKey)
            {
                List<ComparisonAtom> relevantComparisonList= ScanOperatorSingleSelectionMap.get(scanOperator);
                if (relevantComparisonList.size()>0)
                {
                    currentOperator= new SelectionOperator(scanOperator.getRelationalAtom(),scanOperator,relevantComparisonList);
                }
                if (scanOperator.getRelationalAtom().getTerms().stream().anyMatch(Constant.class::isInstance))
                {
                    currentOperator=new SelectionOperator(scanOperator.getRelationalAtom(),scanOperator,relevantComparisonList);
                }
            }
            operatorList.add(currentOperator);
        }
        return operatorList;
    }


    /**
     * This method is used to filter out the joinConditions such as x>m in  R(x,y,z) S(x,w,t) T(m,r).
     * Since we already have the method to check if there is a single atom comparison. We can just use to filter out the join conditions.
     * @param comparisonAtoms a list of comparison lists(a list of predicates)
     * @param scanOperators a list of scan operators
     * @return return joinConditions
     */
    public static List<ComparisonAtom> filterOutComparisonMultipleRelationalAtoms(List<ComparisonAtom> comparisonAtoms,List<ScanOperator> scanOperators)
    {
        List<ComparisonAtom> joinConditions = new ArrayList<>();
        for (ComparisonAtom comparisonAtom: comparisonAtoms)
        {
            boolean checkSingleAtomComparison = isSingleSelectionComparisonAtom(comparisonAtom,scanOperators);
            if (!checkSingleAtomComparison)
            {
                joinConditions.add(comparisonAtom);
            }
        }
        return joinConditions;
    }
    /**
     * This method is used to construct a map for the scanOperator list, which can be used to find aplliable predicate for each scan operator
     *  R(x,r,z) S(x,w,t) T(x,r) x > 1, r > 2, w > 2 , z > 2.
     *  In this case, it will build a Map: Scan(R): [x > 1, r > 2 ,z > 2] Scan(S) :[ x>1,w>2], Scan(T): [x>1,r>2]
     * @param comparisonAtomList inout predicate list
     * @param scanOperatorList a list of scan operators
     * @return return a map
     */
    public static  HashMap<ScanOperator,List<ComparisonAtom>> getRelevantScanOperatorMap(List<ComparisonAtom>comparisonAtomList,List<ScanOperator>scanOperatorList)
    {
        HashMap<ScanOperator,List<ComparisonAtom>> resultMap=new HashMap<>();
        for( ScanOperator scanOperator : scanOperatorList)
        {
            List<ComparisonAtom> comparisonAtoms=new ArrayList<>();
            for(ComparisonAtom comparisonAtom : comparisonAtomList)
            {
                List<ScanOperator> scanOperators = getRelevantScanOperator(comparisonAtom,scanOperatorList);
                if (scanOperators.contains(scanOperator))
                {
                    comparisonAtoms.add(comparisonAtom);
                }
            }
            resultMap.put(scanOperator,comparisonAtoms);
        }
        return resultMap;
    }

    /**
     * This method will generate corresponding scan Operator list based on input a predicate comparison.
     * For example, if we have a predicate r>1 and we have a R(x, r, z),S(x, w, t), T(x, r), it will generate [  R(x, r, z), T(x, r)]
     * @param comparisonAtom predicate
     * @param scanOperators scanOperator List
     * @return return a list of scanOperator
     */
    public static List<ScanOperator> getRelevantScanOperator(ComparisonAtom comparisonAtom,List<ScanOperator>scanOperators)
    {
        List<ScanOperator> relevantScanOperatorList =new ArrayList<>();
        for (ScanOperator scanOperator: scanOperators)
        {
            RelationalAtom scanRelationAtom = scanOperator.getRelationalAtom();
            Term comparisonTerm1 = comparisonAtom.getTerm1();
            Term comparisonTerm2 = comparisonAtom.getTerm2();

            boolean checkVariable1=comparisonTerm1 instanceof Variable;
            boolean checkVariable2 = comparisonTerm2 instanceof Variable;

            boolean containTerm1 = scanRelationAtom.getTerms().contains(comparisonTerm1);
            boolean containTerm2 = scanRelationAtom.getTerms().contains(comparisonTerm2);

            if ( (checkVariable1 && containTerm1) || (checkVariable2 && containTerm2) )
            {
                relevantScanOperatorList.add(scanOperator);
            }
        }
        return relevantScanOperatorList;
    }
    /**
     * This method is used to filter out those predicates, which can apply to scanOperator list directly, where the predicate should be either single Atom selection or just
     * can be applied directly. For example :
     * R(x,a,z) S(x,w,t) T(x,r) x > 1, x < 1, x = 1, x <= 1, x >= 1, x >= y, x >= z
     * Filtered: R(x,a,z) S(x,w,t) T(x,r) x > 1, x < 1, x = 1, x <= 1, x >= 1, x >= z
     */
    public static  List<ComparisonAtom> filterNonJoinCondition(List<ComparisonAtom> comparisonAtoms, List<ScanOperator>scanOperators )
    {
        List <ComparisonAtom> nonJoinComparisonAtom = new ArrayList<>();
        for (ComparisonAtom comparisonAtom: comparisonAtoms)
        {
            boolean checkApply = checkIfCanBeApplietoScanOperators(comparisonAtom,scanOperators);
            boolean checkSingle = isSingleSelectionComparisonAtom(comparisonAtom,scanOperators);
            if (checkApply || checkSingle)
            {
                nonJoinComparisonAtom.add(comparisonAtom);
            }
        }
        return nonJoinComparisonAtom;
    }

    /**
     * Used to check if a comparison is a single selection atom.
     */
    public static boolean checkSingleRelationAtoms(ComparisonAtom comparisonAtom,List<RelationalAtom>relationalAtomList)
    {
        Term term1=comparisonAtom.getTerm1();
        Term term2=comparisonAtom.getTerm2();
        int checkNum=0;
        if (term1 instanceof  Variable)
        {
            checkNum++;
        }
        if (term2 instanceof  Variable)
        {
            checkNum++;
        }
        if (checkNum == 0 || checkNum == 1) return true;

        for(RelationalAtom relationalAtom : relationalAtomList)
        {
            boolean checkOnlyOneTerm = UltsForEvaluator.checkRelationSingleComparisonAtom(relationalAtom,term1,term2);
            if(!checkOnlyOneTerm)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to check if the current comparison is a single atom selection .e.g: x>1.
     * It first check if the comparison only has one varibale. If so, it will just return a check.
     * If there are two varibales in the comparison atom, it will then check if any of the scanOpeartor in the scanOpeartor List only has one term, if so return false.
     * In the case of S(x,w,t) T(x,r) x>z, it will return a false, then it can not apply to it.
     */
    public static boolean isSingleSelectionComparisonAtom(ComparisonAtom comparisonAtom, List<ScanOperator> scanOperators)
    {
        int numVaribaleFromComparisonAtom = getNumVariablesInComparisonAtom(comparisonAtom);
        if (numVaribaleFromComparisonAtom==1 || numVaribaleFromComparisonAtom==0)
        {
            return true;
        }
        else
        {
            boolean noneMatch = true;
            for (ScanOperator scanOperator : scanOperators) {
                RelationalAtom relationalAtomFromScanOperator = scanOperator.getRelationalAtom();
                Term comparisonTerm1 = comparisonAtom.getTerm1();
                Term comparisonTerm2 = comparisonAtom.getTerm2();
                boolean checkOnlyOneContain = UltsForEvaluator.checkRelationSingleComparisonAtom(relationalAtomFromScanOperator,comparisonTerm1,comparisonTerm2);
                if (checkOnlyOneContain)
                {
                    noneMatch = false;
                    break;
                }
            }
            return noneMatch;
        }
    }


    /**
     * This method is used to check if the current predicate can be applied directly to the current scanOperator list.
     * For example: R(x,y,z) S(x,w,t) T(x,r) x>1, in this case, it is going to return true since x>1 can be applied directly to the the relationalAtomlist, which means
     * it can be applied directly to all the Scan Operators.
     * While in this case : S(x,w,t) T(x,r) x>z, the predicate can not be applied to the relationAtom list, since they do not have corresponding varibale.
     * @param comparisonAtom comparisonAtom for passed predicate.
     * @param scanOperators ScanOpeartor lists
     * @return return a boolean for check
     */

    public static boolean checkIfCanBeApplietoScanOperators(ComparisonAtom comparisonAtom, List<ScanOperator> scanOperators) {
        int numVariables = getNumVariablesInComparisonAtom(comparisonAtom);
        if (numVariables == 0 || numVariables == 1) return true;

        else
        {
            boolean found = false;
            for (ScanOperator scanOperator : scanOperators)
            {
                RelationalAtom scanRelationalAtom = scanOperator.getRelationalAtom();
                Term comparisonTerm1=comparisonAtom.getTerm1();
                Term comparisonTerm2 = comparisonAtom.getTerm2();
                boolean checkContainBothTerms =scanRelationalAtom.getTerms().contains(comparisonTerm1) && scanRelationalAtom.getTerms().contains(comparisonTerm2);
                if (checkContainBothTerms)
                {
                    found = true;
                    break;
                }
            }
            return found;
        }
    }


    // get the number of variables in specific comparison.
    public static int getNumVariablesInComparisonAtom(ComparisonAtom comparisonAtom) {
        int Num = 0;
        Term term1 = comparisonAtom.getTerm1();
        Term term2 = comparisonAtom.getTerm2();
        boolean checkTerm1 =  term1 instanceof Variable;
        boolean checkTerm2 =  term2 instanceof Variable;
        if (checkTerm1)
        {
            Num++;
        }
        if (checkTerm2)
        {
            Num++;
        }
        return Num;
    }
    public Query getInputQuery() {
        return inputQuery;
    }

    public void initializeRelationalAtomListComparisonList()
    {
        List<Atom> atomList= this.inputQuery.getBody();
        for( Atom atom: atomList)
        {
            try
            {
                RelationalAtom temp = (RelationalAtom) atom;
                this.relationalAtomList.add(temp);
            }
            catch (Exception ex)
            {
                ComparisonAtom temp = (ComparisonAtom) atom;
                this.comparisonAtomList.add(temp);
            }
        }
    }


    /**
     * Construct a list of scan operators, by using existing relational Lists.
     * @throws FileNotFoundException
     */
    public void setScanOpeartorList() throws FileNotFoundException {
        List<ScanOperator> scanOperatorList = new ArrayList<>();
        for( RelationalAtom relationalAtom : this.relationalAtomList )
        {
            String name = relationalAtom.getName();
            List<Term> termList = relationalAtom.getTerms();
            Schema schema=null;
            String filePath=null;
            if (name.equals("R"))
            {
                schema = this.SchemaMap.get("R");
                filePath=fileNameR;
            }
            else if(name.equals("S"))
            {
                schema = this.SchemaMap.get("S");
                filePath=fileNameS;
            }
            else if(name.equals("T"))
            {
                schema = this.SchemaMap.get("T");
                filePath=fileNameT;
            }
            ScanOperator scanOperator = new ScanOperator(filePath,schema,relationalAtom);
            scanOperatorList.add(scanOperator);
        }
        this.scanOperatorList = scanOperatorList;
    }
    public void setInputQuery(Query inputQuery) {
        this.inputQuery = inputQuery;
    }

    public Operator getRoot() {
        return root;
    }

    public void setRoot(Operator root) {
        this.root = root;
    }

    public List<RelationalAtom> getRelationalAtomList() {
        return relationalAtomList;
    }

    public void setRelationalAtomList(List<RelationalAtom> relationalAtomList) {
        this.relationalAtomList = relationalAtomList;
    }

    public List<ComparisonAtom> getComparisonAtomList() {
        return comparisonAtomList;
    }

    public List<ScanOperator> getScanOperatorList() {
        return scanOperatorList;
    }

    public DatabaseCatalog getDatabaseCatalog() {
        return databaseCatalog;
    }

    public void setDatabaseCatalog(DatabaseCatalog databaseCatalog) {
        this.databaseCatalog = databaseCatalog;
    }

    public HashMap<String, Schema> getSchemaMap() {
        return SchemaMap;
    }

    public void setSchemaMap(HashMap<String, Schema> schemaMap) {
        SchemaMap = schemaMap;
    }

    public String getFileNameR() {
        return fileNameR;
    }

    public void setFileNameR(String fileNameR) {
        this.fileNameR = fileNameR;
    }

    public String getFileNameS() {
        return fileNameS;
    }

    public void setFileNameS(String fileNameS) {
        this.fileNameS = fileNameS;
    }

    public String getFileNameT() {
        return fileNameT;
    }

    public void setFileNameT(String fileNameT) {
        this.fileNameT = fileNameT;
    }

    public String getSchemaFilePath() {
        return schemaFilePath;
    }

    public void setSchemaFilePath(String schemaFilePath) {
        this.schemaFilePath = schemaFilePath;
    }

    public void setScanOperatorList(List<ScanOperator> scanOperatorList) {
        this.scanOperatorList = scanOperatorList;
    }

    public void setComparisonAtomList(List<ComparisonAtom> comparisonAtomList) {
        this.comparisonAtomList = comparisonAtomList;
    }

    @Override
    public Tuple getNextTuple() throws IOException {
        return this.root.getNextTuple();
    }

    @Override
    public void reset() {
    }
}
