package ed.inf.adbs.minibase.evaluator;

//import com.sun.tools.javac.util.RichDiagnosticFormatter;
import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JoinOperator extends Operator {
    Operator leftChidOperator;
    Operator rightChildOperator;

    public Operator getLeftChidOperator() {
        return leftChidOperator;
    }

    public void setLeftChidOperator(Operator leftChidOperator) {
        this.leftChidOperator = leftChidOperator;
    }

    public Operator getRightChildOperator() {
        return rightChildOperator;
    }

    public void setRightChildOperator(Operator rightChildOperator) {
        this.rightChildOperator = rightChildOperator;
    }

    public List<RelationalAtom> getLeftChildRelationAtoms() {
        return leftChildRelationAtoms;
    }

    public void setLeftChildRelationAtoms(List<RelationalAtom> leftChildRelationAtoms) {
        this.leftChildRelationAtoms = leftChildRelationAtoms;
    }

    public RelationalAtom getRightChildRelationAtom() {
        return rightChildRelationAtom;
    }

    public void setRightChildRelationAtom(RelationalAtom rightChildRelationAtom) {
        this.rightChildRelationAtom = rightChildRelationAtom;
    }

    public List<ComparisonAtom> getPredicates() {
        return Predicates;
    }

    public void setPredicates(List<ComparisonAtom> predicates) {
        Predicates = predicates;
    }

    public Tuple getOuterTuple() {
        return outerTuple;
    }

    public void setOuterTuple(Tuple outerTuple) {
        this.outerTuple = outerTuple;
    }

    public Tuple getInnerTuple() {
        return innerTuple;
    }

    public void setInnerTuple(Tuple innerTuple) {
        this.innerTuple = innerTuple;
    }

    List<RelationalAtom> leftChildRelationAtoms;

    RelationalAtom rightChildRelationAtom;

    List<ComparisonAtom> Predicates;

    Tuple outerTuple;
    Tuple innerTuple;

    public JoinOperator(Operator leftChidOperator, Operator rightChildOperator, List<RelationalAtom> leftChildRelationAtoms, RelationalAtom rightChildRelationAtom, List<ComparisonAtom> predicates) {
        this.leftChidOperator = leftChidOperator;
        this.rightChildOperator = rightChildOperator;
        this.leftChildRelationAtoms = leftChildRelationAtoms;
        this.rightChildRelationAtom = rightChildRelationAtom;
        Predicates = predicates;
        this.outerTuple = null;
        this.innerTuple = null;
    }


    /**
     * This getNextTuple() method returns the next tuple, which passes all the join conditions cross all the relationalAtoms.
     * For each outer tuple, this method will  try to find a matched innertuple by iterating through the right childAtom's tuple. So, for each line of left tuple, it will first
     * rest right child operator and iterate all the tuples from the right operators to find a match.
     * If there is a match (this.outerTuple != null && innerTuple != null), then it will check if the left combined tuples and right tuple satisfy all the predicates across
     * all the relational Atoms.If so, it will glue them together and return.
     * @return return a combined tuples from left and right
     * @throws IOException
     */
    @Override
    public Tuple getNextTuple() throws IOException {

        if (this.outerTuple != null && innerTuple != null) {

            innerTuple=rightChildOperator.getNextTuple();
            while (innerTuple != null) {
                boolean checkPass =passesSelectionPredicatesRelationalAtomLists(outerTuple, innerTuple, this.leftChildRelationAtoms, this.rightChildRelationAtom, this.Predicates);
                if (checkPass)
                {
                    List<Constant> combinedFields = new ArrayList<>();
                    combinedFields.addAll(outerTuple.getFields());
                    combinedFields.addAll(innerTuple.getFields());
                    return new Tuple(combinedFields);
                }
                innerTuple=rightChildOperator.getNextTuple();
            }
        }

        outerTuple=leftChidOperator.getNextTuple();
        while ((outerTuple != null) ){
            rightChildOperator.reset();
            innerTuple=rightChildOperator.getNextTuple();
            while (innerTuple != null) {
                boolean checkPass=passesSelectionPredicatesRelationalAtomLists(outerTuple, innerTuple, this.leftChildRelationAtoms, this.rightChildRelationAtom, this.Predicates);
                if (checkPass)
                {
                    List<Constant> combinedFields = new ArrayList<>(outerTuple.getFields());
                    combinedFields.addAll(innerTuple.getFields());
                    return new Tuple(combinedFields);
                }
                innerTuple=rightChildOperator.getNextTuple();
            }
            outerTuple=leftChidOperator.getNextTuple();
        }

        return null;
    }


    @Override
    public void reset() {
        leftChidOperator.reset();
        rightChildOperator.reset();
    }

    /**
     * This method checks that if the constants in the corresponding position of left childAtoms are eqaul to that of the corresponding of right ChildAtom.
     * For example, if we have a Query: Q(y) :-  R(x,y),S(y,r), T(y,z). We can think use R and S as the left CildAtoms, and try to extract a list corresponding constants.
     * Then we can try to extract the constant of the corresponding position of variable in the right childAtom. And since we need to join them,
     * we need to make sure they are the same.
     * Then checks that all the join predicates are all satisfied.
     * @param leftTuple left tuple, which can be a combination of multiple tuples.
     * @param rightTuple rigth tuple
     * @param leftChildAtoms a list of realtional Atoms.
     * @param rightChildAtom A single relation atom
     * @param joinConditions condition for join operation.
     * @return return a boolean to check if can all the join operation.
     */
    public static boolean passesSelectionPredicatesRelationalAtomLists(Tuple leftTuple, Tuple rightTuple, List<RelationalAtom> leftChildAtoms, RelationalAtom rightChildAtom, List<ComparisonAtom> joinConditions)
    {
        boolean sameNameVariableNotEqual = false;
        for (Term term : rightChildAtom.getTerms())
        {
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

        boolean checkPass = passesPredicatList(leftTuple,rightTuple,joinConditions,leftChildAtoms,rightChildAtom);
        return !sameNameVariableNotEqual&& checkPass;
    }

    /**
     * Check if all the predicates can be pass for all the left child relational atoms and right child atom.
     * @param leftTuple combination of left tuples
     * @param rightTuple tuple from right relation
     * @param predicates join conditions for join operation
     */
    public static  boolean passesPredicatList(Tuple leftTuple, Tuple rightTuple, List<ComparisonAtom> predicates, List<RelationalAtom> leftChildAtoms, RelationalAtom rightChildAtom)
    {
        for(ComparisonAtom predicate : predicates )
        {
              boolean checkSinglePredicate = passSinglePredicate(leftTuple,rightTuple,predicate,leftChildAtoms,rightChildAtom);
              if (!checkSinglePredicate)
              {
                  return false;
              }
        }
        return true;
    }

    /**
     * Check if one predicate can be satisfied across all the atoms. For example if we have R(x,y,z), S(x,r), x>3
     * For each predicate, it should only one varibale in the predicate, instead of example lke x>y
     */
    public static  boolean passSinglePredicate(Tuple leftTuple, Tuple rightTuple, ComparisonAtom predicate, List<RelationalAtom> leftChildAtoms, RelationalAtom rightChildAtom)
    {
        ComparisonAtom comparisonAtomConstantSubstitudeVariable = subFromLeftandRight(predicate,leftTuple,rightTuple,leftChildAtoms,rightChildAtom);
        Constant constant1=(Constant) comparisonAtomConstantSubstitudeVariable.getTerm1();
        Constant constant2=(Constant) comparisonAtomConstantSubstitudeVariable.getTerm2();
        ComparisonOperator op = comparisonAtomConstantSubstitudeVariable.getOp();
        return UltsForEvaluator.compareConstants(constant1,constant2,op);
    }

    /**
     * This method is to substitute varibale from either leftside or rightside based on corresponding prediacte.
     * For example, if left constant list based on left childAtoms is [1,1,1] and right is 1, it means they can be joined,then we will check it also satisfies the
     * predicate. So, in this case, if the predicate is x>0, then it will become 1>0, where we will use Static method to compare the substitute constants form both sides of the
     * operator, then in this case the predicate is satisfied.
     */
    public static  ComparisonAtom subFromLeftandRight(ComparisonAtom predicate, Tuple leftTuple, Tuple rightTuple, List<RelationalAtom> leftChildAtoms, RelationalAtom rightChildAtom)
    {
          Constant subTerm1=null;
          Constant subTerm2=null;
          Term term1= predicate.getTerm1();
          Term term2 =predicate.getTerm2();
          if (term1 instanceof Variable)
          {
              List <Constant> leftSubConstants1= UltsForEvaluator.substitutionForVariableinRelationAtom(leftTuple,(Variable) term1, leftChildAtoms);
              List<Term> termList = rightChildAtom.getTerms();
              Constant rightSubConstant1 = null;
              // if right child atom contains the varibale, check the corresponding constant
              if (termList.contains(term1))
              {
                  rightSubConstant1=UltsForEvaluator.getConstantFromSingleRelationalAtom(rightChildAtom,rightTuple,(Variable) term1);
              }
              // Check the substitude constant based on right relational atom is equal to that based on the left relational atim
              if (rightSubConstant1!=null)
              {
                  subTerm1 = rightSubConstant1;
              }
              else if (rightSubConstant1 == null)
              {
                  // if the length of the constant list from left relational atoms
                  if( leftSubConstants1.size()==0)
                  {
                      throw new IllegalArgumentException("Neither Right and Left doesn't have any corresponding substitution constants");
                  }
                  else
                  {
                      // Otherwise, the constants form the list should be the same.
                      subTerm1 = leftSubConstants1.get(0);
                  }
              }
          }
          // if it is not a varibale, then just use it as a constant
          else
          {
              subTerm1 = (Constant) predicate.getTerm1();
          }
        if (term2 instanceof Variable)
        {
            List <Constant> leftSubConstants2= UltsForEvaluator.substitutionForVariableinRelationAtom(leftTuple,(Variable) term2, leftChildAtoms);
            List<Term> termList = rightChildAtom.getTerms();
            Constant rightSubConstant2 = null;
            // if right child atom contains the varibale, check the corresponding constant
            if (termList.contains(term2))
            {
                rightSubConstant2=UltsForEvaluator.getConstantFromSingleRelationalAtom(rightChildAtom,rightTuple,(Variable) term2);
            }
            // Check the substitude constant based on right relational atom is equal to that based on the left relational atim
            if (rightSubConstant2!=null)
            {
                subTerm2 = rightSubConstant2;
            }
            else if (rightSubConstant2 == null)
            {
                // if the length of the constant list from left relational atoms
                if( leftSubConstants2.size()==0)
                {
                    throw new IllegalArgumentException("Neither Right and Left doesn't have any corresponding substitution constants");
                }
                else
                {
                    // Otherwise, the constants form the list should be the same.
                    subTerm2 = leftSubConstants2.get(0);
                }
            }
        }
        // if it is not a varibale, then just use it as a constant
        else
        {
            subTerm2 = (Constant) predicate.getTerm2();
        }

        return new ComparisonAtom(subTerm1,subTerm2,predicate.getOp());
    }

    @Override
    public String toString() {
        Operator leftChild =this.getLeftChidOperator();
        Operator rightChild = this.getRightChildOperator();
        return "Join ( "+ "Left : "+ leftChild.toString() +" Right : "+rightChild.toString()+" )";
    }
}



