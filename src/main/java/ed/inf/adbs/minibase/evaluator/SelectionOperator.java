package ed.inf.adbs.minibase.evaluator;

//import com.sun.org.apache.bcel.internal.Const;
import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.IOException;
import java.util.List;

import ed.inf.adbs.minibase.dbStructure.Tuple;

public class SelectionOperator extends Operator {

    private RelationalAtom relationalAtom;
    private ScanOperator childOperator;
    private List<ComparisonAtom> Predicates;

    public SelectionOperator(RelationalAtom relationalAtom, ScanOperator childOperator, List<ComparisonAtom> Predicates) {
        this.relationalAtom = relationalAtom;
        this.childOperator = childOperator;
        this.Predicates = Predicates;
    }


    @Override
    public Tuple getNextTuple() throws IOException {
        Tuple nextTuple = null;
        Tuple tupleInQuestion;
        while ((tupleInQuestion  = childOperator.getNextTuple()) != null) {
            if (checkAllPredicate(tupleInQuestion, this.Predicates ,this.relationalAtom)) {
                nextTuple = tupleInQuestion;
                break;
            }
        }
        return nextTuple;
    }

    @Override
    public void reset() {
        childOperator.reset();
    }

    public RelationalAtom getRelationalAtom() {
        return relationalAtom;
    }

    public void setRelationalAtom(RelationalAtom relationalAtom) {
        this.relationalAtom = relationalAtom;
    }

    public Operator getChildOperator() {
        return childOperator;
    }

    public void setChildOperator(ScanOperator childOperator) {
        this.childOperator = childOperator;
    }

    public List<ComparisonAtom> getSelectionPredicates() {
        return Predicates;
    }

    public void setSelectionPredicates(List<ComparisonAtom> Predicates) {
        this.Predicates = Predicates;
    }

    public boolean isPredicateTrue(List<ComparisonAtom> Predicates) {
        return true;
    }

    /**
     * Check if variable, which is in comparsion atom is also contained in relationAtom term list
     *
     * @param relationalAtom relaitonAtom to check if two terms in comparisonAto are contained in the relationAtom
     * @return return boolean to check
     */
    public boolean IsContainedComparisonAtomRelationalAtom(RelationalAtom relationalAtom, ComparisonAtom comparisonAtom) {

        Term term1 = comparisonAtom.getTerm1();
        Term term2 = comparisonAtom.getTerm2();
        List<Term> relationTerm = relationalAtom.getTerms();
        if (term1 instanceof Variable && !(term2 instanceof Variable)) {
            return relationTerm.contains(term1);
        }
        if (term2 instanceof Variable && !(term1 instanceof Variable)) {
            return relationTerm.contains(term2);
        }
        if (term1 instanceof Variable && term2 instanceof Variable) {
            return relationTerm.contains(term1) && relationTerm.contains(term2);
        }
        else
        {
            return false;
        }
    }

    /**
     * This method is used to check if all the predicates holds for one relation
     * @param tuple tuple type which has list of constants like (1,2,'abds')
     * @param comparisonAtomList predicates list
     * @param sourceRelationAtom relation like R(x,y,z)
     * @return return a boolean to check if all the predicates hold for one relation
     */
    public boolean checkAllPredicate(Tuple tuple, List<ComparisonAtom> comparisonAtomList, RelationalAtom sourceRelationAtom)
    {
        for(int i=0;i<comparisonAtomList.size();i++)
        {
            ComparisonAtom curPredicate = comparisonAtomList.get(i);
            boolean checkPredicate = passPredicate(tuple,curPredicate,sourceRelationAtom);
            if(!checkPredicate)
            {
                return false;
            }
        }
        return true;
    }
    /**
     * This method is used to generate a tuple constant of  corresponding index of the source relationAtom.
     * For example the source relationAtom is R(x,y,z), and input tuple is (1,2,'abds'), we want to find what z map with, so in this case, it should return 'adbs'
     */
    public static Constant tupleSubstitutionRelationAtom(RelationalAtom relationalAtom, Variable variable, Tuple tuple) {
        int index = relationalAtom.getTerms().indexOf(variable);
        if (index >=0) {
            return tuple.getFields().get(index);
        } else {
            throw new IllegalArgumentException("The input tuple can not be matched to the souce relationAtom");
        }
    }

    /**
     * This method is used to changed the variable type in comparsionAtom into constant type. For example : if we have a comparsionAtom: x>3
     * If we have a tuple 1,2,3 then x=1, then we can map it to x then the original predicate becomes 1>3
     */
    public static ComparisonAtom tupleSubstitutionComparisonAtom(ComparisonAtom comparisonAtom, RelationalAtom relationalAtom, Tuple tuple) {
        Term comparisonAtom1 = comparisonAtom.getTerm1();
        Term comparisonAtom2 = comparisonAtom.getTerm2();
        Constant term1 = null;
        Constant term2 = null;

        if (comparisonAtom1 instanceof Variable) {
            term1 = tupleSubstitutionRelationAtom(relationalAtom, (Variable) comparisonAtom1, tuple);
        } else {
            term1 = (Constant) comparisonAtom.getTerm1();
        }

        if (comparisonAtom2 instanceof Variable) {
            term2 = tupleSubstitutionRelationAtom(relationalAtom, (Variable) comparisonAtom2, tuple);
        } else {
            term2 = (Constant) comparisonAtom.getTerm2();
        }

        if (comparisonAtom1 instanceof Variable && comparisonAtom2 instanceof Variable) {
            term1 = tupleSubstitutionRelationAtom(relationalAtom, (Variable) comparisonAtom1, tuple);
            term2 = tupleSubstitutionRelationAtom(relationalAtom, (Variable) comparisonAtom2, tuple);
        }
        ComparisonOperator operator = comparisonAtom.getOp();
        return new ComparisonAtom(term1, term2, operator);
    }

    /**
     * This method is used to pass one predicate for each relation. In one specific situation, where variable in relation is set as constant
     * Such as R(8,y,z) which is equivalent to x=8 will also be considered
     * @param tuple The tuple will be used to map the corresponding variable to compare with the condition
     * @param comparisonAtom it will be used to compare two different constants
     * @param sourceRelationAtom Example like R(x,y,z)
     * @return
     */
    public boolean passPredicate(Tuple tuple, ComparisonAtom comparisonAtom, RelationalAtom sourceRelationAtom) {
        int lenTupleSize = tuple.getFields().size();
        int lenRelationAtom = sourceRelationAtom.getTerms().size();
        if (lenRelationAtom != lenTupleSize) {
            throw new IllegalArgumentException("The length of tuple should match the length of the corresponding relational Atom");
        }
        int index=0;
        boolean checkRelation=true;
        // Check if the relation atom contains any constant class
        if (sourceRelationAtom.getTerms().stream().anyMatch(Constant.class::isInstance))
        {
            List<Term> termList = sourceRelationAtom.getTerms();
            for( Term relationAtom : termList)
            {
                // if some atoms in the term list are Integer constants, change it to Integer constants
                if(relationAtom.getClass()==IntegerConstant.class)
                {
                    IntegerConstant rightIntegerConstant = new IntegerConstant(((IntegerConstant) relationAtom).getValue());
                    if(tuple.getFields().get(index).getClass() ==IntegerConstant.class)
                    {
                        checkRelation= compareConstants(tuple.getFields().get(index),rightIntegerConstant,ComparisonOperator.EQ);
                    }
                    else
                    {
                        checkRelation=false;
                        break;
                    }
                }
                // if some atoms in the term list are String constants, change it to String constants
                else if(relationAtom.getClass()==StringConstant.class)
                {
                    StringConstant rightStringConstant = new StringConstant(((StringConstant) relationAtom).getValue());
                    if(tuple.getFields().get(index).getClass() ==StringConstant.class)
                    {
                        checkRelation= compareConstants(tuple.getFields().get(index),rightStringConstant,ComparisonOperator.EQ);
                    }
                    else
                    {
                        checkRelation=false;
                        break;
                    }
                }
            }
        }
        // if the ComparisonAtom contains relational atoms, then compare
        if (IsContainedComparisonAtomRelationalAtom(sourceRelationAtom, comparisonAtom)) {
            ComparisonAtom comparisonSubstitution = tupleSubstitutionComparisonAtom(comparisonAtom, sourceRelationAtom, tuple);
            Term comparisonSubstitutionTerm1 = comparisonSubstitution.getTerm1();
            Term comparisonSubstitutionTerm2 = comparisonSubstitution.getTerm2();
            if ((comparisonSubstitutionTerm1 instanceof Variable) || (comparisonSubstitutionTerm2 instanceof Variable)) {
                throw new UnsupportedOperationException("The comparsionAtom after substitution must have two constants!!");
            }
            // Make sure that the term1 and term2 are now the constants
            else
            {
                return compareConstants((Constant) comparisonSubstitutionTerm1,(Constant) comparisonSubstitutionTerm2,comparisonAtom.getOp()) && checkRelation;
            }

        }
        return false;
    }

    /**
     * Check the comparsion result of two different constants. If the operator is EQ or NEQ, just simply use eqauls.
     * If the checkConstantComparison(constant1,constatn2) return MAX_VALUE, then it means these constants are niether StringConstant or IntegerConstant
     * @param constant1
     * @param constant2
     * @param operator
     * @return
     */
    public boolean compareConstants(Constant constant1, Constant constant2, ComparisonOperator operator) {
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

    @Override
    public String toString() {
        Operator child = this.getChildOperator();
        return " Selection ( " + child.toString() + ", Conditions:"+this.Predicates+ " )";
    }

    /**
     * This method is used to compare two different constants. First, we need to check if these two constants have the same class
     * Both String and Integer Constant have override compareTo method to compare their values, which return 0,1,-1
     */
    private int checkConstantComparison(Constant constant1, Constant constant2) {
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