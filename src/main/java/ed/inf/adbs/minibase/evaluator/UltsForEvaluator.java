package ed.inf.adbs.minibase.evaluator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UltsForEvaluator {
    /**
     * This method is used to get the substitution constant list for combineed tuples given a specific variable. For example:
     * We have a list of relational atoms : R(x, y, z), S(x, w, t), T(x, r). And the combined tuple can be (1, 9, 'adbs', 1, 'smith', 8, 1, 1) and we have a given specific
     * variable x. The extracted constant should be 1, 1 ,1 . To achieve this, we scan each relationAtom from the relationAtomList and get the index of specifi variable in the
     * current relationAtom and to get the index of the next source relation, we will use a offset varibale to get this, which is the length of the terms of the relationatom
     * @param combinedTuples the combined tuple is a list of constants combined by corresponding schemas.
     * @param variable a specific varible from head like x,y,z
     * @param relationalAtomList a list of relational atoms such as [R(x, y, z), S(x, w, t), T(x, r)]
     * @return return a list of constants extracted from the combined tuples such as [1,1,1]
     */
    public static List<Constant> substitutionForVariableinRelationAtom (Tuple combinedTuples, Variable variable, List<RelationalAtom> relationalAtomList)
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

        int offset=0;
        List<Constant> constantList=new ArrayList<>();
        for(RelationalAtom relationalAtom : relationalAtomList)
        {
            // get the index of specific variable in relationalAtom
            List<Term> termList = relationalAtom.getTerms();
            int indexVariable = termList.indexOf(variable);

            if (indexVariable >= 0) {
                constantList.add(combinedTuples.getFields().get(indexVariable + offset));
            }
            offset=offset+termList.size();
        }
        return constantList;
    }


    /**
     * This method is used to get a corresponding constant mapping a specific variable to tuple. For example, R(x,y,z) and a corresponding tuple is (1,2,3), then if this
     * method takes x,then it will return 1.
     * @param relationalAtom a relation atom which can be used to map varibale to corresponding tuple
     * @param tuple a list of constants which can be used to map the corresponding atom
     * @param variable a variable from a corresponding relational atom
     * @return return corresponding constant mapped form tuple and relational
     */
    public static Constant getConstantFromSingleRelationalAtom(RelationalAtom relationalAtom,Tuple tuple,Variable variable)
    {
        if (relationalAtom.getTerms().size()!=tuple.getFields().size())
        {
            throw new IllegalArgumentException("The length of tuple fields doesn't match the number of terms in relation atom!!");
        }
        int indexVariable= relationalAtom.getTerms().indexOf(variable);
        if(indexVariable>=0)
        {
            return tuple.getFields().get(indexVariable);
        }
        else
        {
            return null;
        }
    }


    /**
     * This method is used to check a specific relationalAtom contain only one term
     * @param relationalAtom a specific relational Atom for comparing
     * @param term1 term1 from comparison
     * @param term2 term2 from comparison
     * @return return a boolean to check
     */
    public static  boolean checkRelationSingleComparisonAtom(RelationalAtom relationalAtom,Term term1, Term term2)
    {
        boolean hasOnlyOneTerm = true;
        for (Term term : relationalAtom.getTerms()) {
            if (term.equals(term1) || term.equals(term2))
            {
                if (!hasOnlyOneTerm)
                {
                    return false;
                }
                hasOnlyOneTerm = false;
            }
        }
        return !hasOnlyOneTerm;
    }


    /**
     * This method is used to compare two constants like 1>3 or "string" < "ABC" by using compare To method.
     * @param constant1 the first constant
     * @param constant2 the second constant
     * @param operator comparison operator like EQ NEQ GT
     * @return return boolean to check if the predicate can pass
     */
    public static boolean compareConstants(Constant constant1, Constant constant2, ComparisonOperator operator) {
        if (operator.equals(ComparisonOperator.EQ)) {
            return constant1.equals(constant2);
        }
        if (operator.equals(ComparisonOperator.NEQ)) {
            return !constant1.equals(constant2);
        }

        int compareResult = checkConstantComparison(constant1,constant2);
        if (compareResult==Integer.MAX_VALUE)
        {
            throw new UnsupportedOperationException("Not supported constant operation!!!");
        }
        if (compareResult>0)
        {
            return (operator.equals(ComparisonOperator.GT) || operator.equals(ComparisonOperator.GEQ));
        }
        if(compareResult==0)
        {
            return operator.equals(ComparisonOperator.EQ) || (operator.equals(ComparisonOperator.GEQ)) || operator.equals(ComparisonOperator.LEQ);
        }
        else
        {
            return (operator.equals(ComparisonOperator.LEQ) || operator.equals(ComparisonOperator.LT));
        }
    }

    /**
     * This method is used to check the compareTo value of two constants.
     * @param constant1 the first constant of comparison
     * @param constant2 the second constant of comparison
     * @return return a int value of  comparto of two different constants
     */
    public static int checkConstantComparison(Constant constant1, Constant constant2) {
        if (!constant1.getClass().equals(constant2.getClass())) {
            throw new IllegalArgumentException("The class of the constants have to be the same");
        }
        // If they are Integer Constant
        if (constant1.getClass().equals(IntegerConstant.class)) {
            Integer value1 = ((IntegerConstant) constant1).getValue();
            Integer value2 = ((IntegerConstant) constant2).getValue();
            return value1.compareTo(value2);
        }
        if (constant1.getClass().equals(StringConstant.class)) {
            String value1 = ((StringConstant)constant1).getValue();
            String value2 =((StringConstant)constant2).getValue();
            return value1.compareTo(value2);
        }
        else
        {
            return Integer.MAX_VALUE;
        }
    }

    /**

     Returns the absolute file path of a CSV file in a specified directory,
     with a specified name.
     @param folderPath the absolute path of the directory containing the CSV file
     @param relationalName the name of the CSV file without the file extension
     @return the absolute file path of the CSV file, or null if it cannot be found
     */
    public static String csvFilePathGet(String folderPath,String relationalName)
    {
        String target=relationalName+".csv";
        folderPath=folderPath+File.separator+"files";
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                String filePath= file.getAbsolutePath();
                if(filePath.contains(target))
                {
                    return filePath;
                }
            }
        }
        return null;
    }

    /**

     Returns the query number from a file path by extracting the substring between the last
     occurrence of "/" and "." in the path.
     @param path the file path containing the query number
     @return the query number extracted from the file path
     */
    public static String getqueryNumber(String path)
    {
        int startIndex = path.lastIndexOf("/") + 1; // Find the index of the last occurrence of "/"
        int endIndex = path.lastIndexOf("."); // Find the index of the last occurrence of "."
        String query = path.substring(startIndex, endIndex);
        return query;
    }


    /**

     Returns the absolute file path of a "schema.txt" file located in the specified directory.
     @param dbDir the absolute path of the directory containing the "schema.txt" file
     @return the absolute file path of the "schema.txt" file, or null if it cannot be found
     */
    public static  String getschemaPath(String dbDir)
    {
        File folder = new File(dbDir);
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                String filePath= file.getAbsolutePath();
                if(filePath.contains("schema.txt"))
                {
                    return filePath;
                }
            }
        }
        return null;
    }

    /**
     * This method can be used to reduce the corresponding relationalAtom after project, which can be necessarily used after projecttion
     * @param projectVaribales project variables from project operators
     * @param relationalAtomList relational List from previous operator's relational Atom list
     * @return return a reduced version of relational Atom
     */
    public static List<RelationalAtom> reduceRelationAtomListProjectVaribales(List<Variable>projectVaribales,List<RelationalAtom> relationalAtomList)
    {
        List<Term> checkVisited=new ArrayList<>();
        for(RelationalAtom relationalAtom:relationalAtomList)
        {
            List<Term> termList = relationalAtom.getTerms();
            termList.removeIf(term -> !projectVaribales.contains((Variable) term));
            termList.removeIf(checkVisited::contains);
            checkVisited.addAll(termList);
        }
        return relationalAtomList;
    }

}
