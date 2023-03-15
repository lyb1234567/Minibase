package ed.inf.adbs.minibase.evaluator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.IOException;
import java.util.List;

public class JoinOperator extends Operator {
    private Operator leftChidOperator;
    private Operator rightChildOperator;

    private List<RelationalAtom> leftChildRelationAtoms;

    private RelationalAtom rightChildRelationAtom;

    private List<ComparisonAtom> Predicates;


    @Override
    public Tuple getNextTuple() throws IOException {
        return null;
    }

    @Override
    public void reset() {

    }

    public static boolean passesSelectionPredicatesRelationalAtomLists(Tuple leftTuple, Tuple rightTuple, List<RelationalAtom> leftChildAtoms, RelationalAtom rightChildAtom, List<ComparisonAtom> joinConditions)
    {
        boolean sameNameVariableNotEqual = false;
        for (Term term : rightChildAtom.getTerms())
        {
            if (term instanceof Constant) {
                throw new IllegalArgumentException("Shouldn't be getting constants embedded in relational atoms at this stage!");
            }
            if (term instanceof Variable)
            {
                Variable var = (Variable) term;
                List<Constant> instances = UltsForEvaluator.substitutionForVariableinRelationAtom(leftTuple, var, leftChildAtoms);
                for (Constant constant : instances)
                {
                    if (!constant.equals(UltsForEvaluator.getConstantFromSingleRelationalAtom(rightChildAtom, rightTuple, var)))
                    {
                        sameNameVariableNotEqual = true;
                        break;
                    }
                }
            }
            if (sameNameVariableNotEqual)
            {
                break;
            }
        }
        return sameNameVariableNotEqual;
    }
}
