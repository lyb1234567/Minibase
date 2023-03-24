package ed.inf.adbs.minibase.evaluator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SumOperator extends Operator {

    private  Operator childOperator;

    private  List<Variable> groupByVariables;

    SumAggregate sumAggregate;
    private HashMap<List<Constant>,Integer> groupByHashMap = new HashMap<>();

    public SumOperator(Operator childOperator, List<Variable> groupByVariables, SumAggregate sumAggregate) throws IOException {
        this.childOperator = childOperator;
        this.groupByVariables = groupByVariables;
        this.sumAggregate = sumAggregate;
        this.constructgroupHashMap();
    }

    @Override
    public Tuple getNextTuple() throws IOException {
        return childOperator.getNextTuple();
    }


    public SumAggregate getSumAggregate() {
        return sumAggregate;
    }

    public void setSumAggregate(SumAggregate sumAggregate) {
        this.sumAggregate = sumAggregate;
    }

    public HashMap<List<Constant>, Integer> getGroupByHashMap() {
        return groupByHashMap;
    }

    public void setGroupByHashMap(HashMap<List<Constant>, Integer> groupByHashMap) {
        this.groupByHashMap = groupByHashMap;
    }

    public Operator getChildOperator() {
        return childOperator;
    }

    public void setChildOperator(Operator childOperator) {
        this.childOperator = childOperator;
    }

    public List<Variable> getGroupByVariables() {
        return groupByVariables;
    }

    public void setGroupByVariables(List<Variable> groupByVariables) {
        this.groupByVariables = groupByVariables;
    }

    @Override
    public void reset() {
        childOperator.reset();
    }

    /**
     * This method is used to store the tuples form the child Opeartor in the hash map using keys of group by varibales.
     * We first need to extract the list of constants for the corresponding list of variables. Then calculate the corresponding product value for each tuple and add them together
     * An example can be like this, if we get a combined tuple like this:
     * SUM(2 * r)
     * {[8, 9]=36, [2, 7]=6, [1, 9]=12, [8, 1]=18, [4, 2]=10}
     * ----------------------------------------------------------------
     * SUM(2)
     * {[8, 9]=4, [2, 7]=2, [1, 9]=6, [8, 1]=2, [4, 2]=2}
     * ----------------------------------------------------------------
     * SUM(x)
     * {[8, 9]=16, [2, 7]=2, [1, 9]=3, [8, 1]=8, [4, 2]=4}
     * ----------------------------------------------------------------
     * SUM(x * y)
     * {[8, 9]=144, [2, 7]=14, [1, 9]=27, [8, 1]=8, [4, 2]=8}
     * ----------------------------------------------------------------
     * [1, 9, 'adbs', 1, 1]
     * [1, 9, 'adbs', 1, 3]
     * [1, 9, 'adbs', 1, 2]
     * [2, 7, 'anlp', 2, 3]
     * [4, 2, 'ids', 4, 5]
     * [8, 1, 'mlpr', 8, 9]
     * [8, 9, 'rl', 8, 9]
     * [8, 9, 'ppls', 8, 9]
     * @throws IOException
     */
    public void constructgroupHashMap() throws IOException {
        Tuple nextTuple = this.childOperator.getNextTuple();
        List<RelationalAtom> childCombinedrelationalAtoms = extractRelationalAtomListFromChild(this.childOperator);
        while(nextTuple!=null)
        {
            // We first extract the group by constant like [1,2] corresponding to [x,y]
            List<Constant> groupByConstantList = new ArrayList<>();
            for(Variable variable: this.groupByVariables)
            {
                Constant constant = extractConstVaribaleRelationalAtom(nextTuple,variable,childCombinedrelationalAtoms);
                groupByConstantList.add(constant);
            }
            List<Term> productTerms =this.sumAggregate.getProductTerms();
            int Product =1;
            // compute the product value for one line of tuple
            for(Term productTerm : productTerms)
            {
                if( productTerm instanceof IntegerConstant)
                {
                    int productValue =((IntegerConstant) productTerm).getValue();
                    Product=Product*productValue;
                }
                else if(productTerm instanceof  StringConstant)
                {
                  throw new IllegalArgumentException("In the SumAggregation there shouldn't be any string constant!!");
                }
                else if (productTerm instanceof Variable)
                {
                    Constant productTermConstant = extractConstVaribaleRelationalAtom(nextTuple,(Variable) productTerm,childCombinedrelationalAtoms);
                    if (productTermConstant instanceof  IntegerConstant)
                    {
                        Product=Product*((IntegerConstant) productTermConstant).getValue();
                    }
                    else
                    {
                        throw new IllegalArgumentException("In the SumAggregation there shouldn't be any string constant!!");
                    }
                }
            }

            if (this.groupByHashMap.containsKey(groupByConstantList))
            {
                int Sum = this.groupByHashMap.get(groupByConstantList)+Product;
                groupByHashMap.put(groupByConstantList,Sum);
            }
            else
            {
                groupByHashMap.put(groupByConstantList,Product);
            }
            nextTuple=this.childOperator.getNextTuple();
        }
        reset();
    }

    /**
     * This method is use to extract combined relational atoms form the sum Operator's child Operator.
     */
    public static List<RelationalAtom> extractRelationalAtomListFromChild(Operator childOperator)
    {
        List<RelationalAtom>relationalAtomList=new ArrayList<>();
        if (childOperator instanceof ScanOperator)
        {
            relationalAtomList.add(((ScanOperator) childOperator).getRelationalAtom());
            return relationalAtomList;
        }

        if (childOperator instanceof SelectionOperator)
        {
            relationalAtomList.add(((SelectionOperator) childOperator).getRelationalAtom());
        }
        if (childOperator instanceof  JoinOperator)
        {
            List<RelationalAtom> leftRelationalAtoms = ((JoinOperator) childOperator).leftChildRelationAtoms;
            RelationalAtom rightRelationAtom = ((JoinOperator) childOperator).getRightChildRelationAtom();
            relationalAtomList.addAll(leftRelationalAtoms);
            relationalAtomList.add(rightRelationAtom);
            return relationalAtomList;
        }
        if(childOperator instanceof ProjectionOperator)
        {
            List<RelationalAtom> relationalAtomsFromProject = ((ProjectionOperator) childOperator).getRelationalAtomList();
            List<RelationalAtom> relationalAtomsTemp=QueryPlanner.deepCopyRelationalAtomList(relationalAtomsFromProject);
            List<Variable> projectVaribales =((ProjectionOperator) childOperator).getProjectionVariables();
            UltsForEvaluator.reduceRelationAtomListProjectVaribales(projectVaribales,relationalAtomsTemp);
            relationalAtomList.addAll(relationalAtomsTemp);
            System.out.println(relationalAtomList);
            return relationalAtomList;
        }
        return null;
    }

    /**
     * This method is used to extract tuple form corresponding relationAtom list.
     */
    public static Constant extractConstVaribaleRelationalAtom (Tuple combinedTuples, Variable variable, List<RelationalAtom> relationalAtomList)
    {

        // First we need to check the number of the terms in the relationalAtom list should match the number of constants form the combined tuple
        int checkNum=0;
        for(RelationalAtom relationalAtom : relationalAtomList)
        {
            for(Term term : relationalAtom.getTerms())
            {
                checkNum++;
            }
        }
        if(checkNum!=combinedTuples.getFields().size())
        {
            throw new IllegalArgumentException("The total number of terms form the relationAtom list does not match the number of constants of combined tuples");
        }
        int offset=0;
        for(RelationalAtom relationalAtom : relationalAtomList)
        {
            // get the index of specific variable in relationalAtom
            List<Term> termList = relationalAtom.getTerms();
            int indexVariable = termList.indexOf(variable);
            if (indexVariable >= 0) {
                return combinedTuples.getFields().get(indexVariable+offset);
            }
            offset=offset+termList.size();
        }
        return null;
    }

}
