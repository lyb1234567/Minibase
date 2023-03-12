package ed.inf.adbs.minibase.evaluator;

import ed.inf.adbs.minibase.base.Constant;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.Term;
import ed.inf.adbs.minibase.base.Variable;
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
            else
            {
              throw new IllegalArgumentException("Do not find relevant index in the current relationAtom !!!");
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
        if(indexVariable>0)
        {
            return tuple.getFields().get(indexVariable);
        }
        else
        {
            throw new IllegalArgumentException("Does not find relevant index of the varibale in the corresponding relation!!");
        }
    }
}
