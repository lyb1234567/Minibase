package ed.inf.adbs.minibase.evaluator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.dbStructure.Tuple;

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

        if(checkNum!=combinedTuples.getFields().size())
        {
            throw new IllegalArgumentException("The total number of terms form the relationAtom list does not match the number of constants of combined tuples");
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
            return operator.equals(ComparisonOperator.EQ);
        }
        else
        {
            return (operator.equals(ComparisonOperator.LEQ) || operator.equals(ComparisonOperator.LT));
        }
    }

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
}
