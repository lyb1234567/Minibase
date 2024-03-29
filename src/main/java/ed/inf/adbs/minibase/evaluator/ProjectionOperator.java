package ed.inf.adbs.minibase.evaluator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


public class ProjectionOperator extends Operator{

    /**
     * It will first get next tuple form its child operator(it can be scanOperator or Select Operator, which is determined if there is a predicate or not)
     * If the next tuple from the child operator is null, which means it reaches the end.
     * It will has a potential projecttion tuple, which will be check if it is already reported.
     * If the current potential tuple has been reported before, the project operator will try to get next tuple from its child operator, until there are no childTuples or it
     * find another tuple that has not been reported before.The hash set will store the hashcode of the potential projection tuple to save the sapce.
     * @return return the next tuple form project Operator.
     * @throws IOException
     */
    @Override
    public Tuple getNextTuple() throws IOException {
        Tuple childTuple=childOperator.getNextTuple();
        if (childTuple==null)
        {
            return null;
        }
        Tuple potentialProjectionTuple = this.getResultFromProjection(childTuple);
        String hashcodePotentialProjectionTuple = potentialProjectionTuple.toString();

        while(this.tupleReportedHashCodeString.contains(hashcodePotentialProjectionTuple))
        {
            Tuple nextChildTuple = childOperator.getNextTuple();
            if (nextChildTuple==null)
            {
                return null;
            }
            potentialProjectionTuple  = this.getResultFromProjection(nextChildTuple);
            hashcodePotentialProjectionTuple = potentialProjectionTuple.toString();
        }
        this.tupleReportedHashCodeString.add(hashcodePotentialProjectionTuple);
        List<Constant> key = new ArrayList<>();
        if (this.childOperator instanceof SumOperator)
        {
            key.addAll(potentialProjectionTuple.getFields());
            int Sum = ((SumOperator) this.childOperator).getGroupByHashMap().get(key);
            Constant added = new IntegerConstant(Sum);
            key.add(added);
            potentialProjectionTuple=new Tuple(key);
        }
        return potentialProjectionTuple;
    }

    @Override
    public void reset() {

        childOperator.reset();

    }

    private  Operator childOperator;

    private  List<Variable> projectionVariables;

    private RelationalAtom relationalAtom;

    private List<RelationalAtom> relationalAtomList;

    private Set<String> tupleReportedHashCodeString;

    private  List<RelationalAtom> reducedRelationAtomList;

    //  this constructor creates a projection operator projecting over tuples using only one single relationAtom. For example, Q(y) :- R(8, y, z), z != 'mlpr'
    public ProjectionOperator(Operator childOperator, List<Variable> projectionVariables, RelationalAtom relationalAtom) {

        this.childOperator=childOperator;
        this.projectionVariables=projectionVariables;
        this.relationalAtom=relationalAtom;
        this.tupleReportedHashCodeString = new HashSet<>();
    }

    /**
     * This method is used to project the tuple out of the projection variable. For each variable, it should only one specific constant, and it will be added to a constant list
     * @param tuple input tuple
     * @return return a new tuple for this projection.
     */
    public Tuple getResultFromProjection(Tuple tuple)
    {
        int fieldsSize = tuple.getFields().size();
        int numTers= getNumberFromRelationAtom();
        if (numTers !=fieldsSize)
        {
            System.out.println(numTers);
            System.out.println(fieldsSize);
            System.out.println(this.relationalAtom);
            throw  new UnsupportedOperationException("The size of the fields of tuple doesn't match the total number of terms in the relation atoms");
        }
        List<Constant> constantList = new ArrayList<>();

        for(Variable projectionVariable : this.projectionVariables)
        {
            Constant variableConstant = getConstantFromVariable(projectionVariable,tuple);
            if (variableConstant==null)
            {
                continue;
            }
            constantList.add(variableConstant);
        }
        return new Tuple(constantList);

    }
    // this constructor creates a projection operator projecting over tuples using only multiple relationAtom.
    // For example, Q(x, y, z, u, w, t) :- R(x, y, z), S(u, w, t), x = u
    public ProjectionOperator(Operator childOperator, List<Variable> projectionVariables, List<RelationalAtom> relationalAtoms) {

        this.childOperator = childOperator;
        this.projectionVariables = projectionVariables;
        this.relationalAtomList = relationalAtoms;
        this.tupleReportedHashCodeString = new HashSet<>();
    }


    /**
     * This method is used to substitude specific variable for constant. It will check if the projectOperator is initialized by using a list of relation atoms or just one relation atom
     * If it is initialised by multiple relational atoms, it will return a list of constatns, where they are should be the same constans
     * If it is initialised by one single relational atom, it will just return a constant of corresponding index in relation atom
     * @param variable specific variable in the head
     * @param inputTuple input tuple
     * @return return a constant
     */
    public Constant getConstantFromVariable(Variable variable,Tuple inputTuple)
    {
        // if the Projection Operator is initialized using a list of relationalAtoms
         if (this.relationalAtom==null)
         {
             List<Constant> substitutionVariable = UltsForEvaluator.substitutionForVariableinRelationAtom(inputTuple,variable,this.relationalAtomList);
             long distinctCount=substitutionVariable.stream().distinct().count();
             if (distinctCount>1)
             {
                 throw new IllegalArgumentException("For a specific variable, there is more than one different different constant!!");
             }
             return substitutionVariable.get(0);
         }
         else
         {
               return UltsForEvaluator.getConstantFromSingleRelationalAtom(this.relationalAtom,inputTuple,variable);
         }

    }

    /**

     This method takes in two parameters: a list of projection variables and a list of relational atoms. It returns a list of relational atoms with any unused relational atoms removed.
     A relational atom is considered unused if it contains no constants and all of its variables are not in the projection variables list.
     @param projectionVariables the list of variables to project onto
     @param inputRelationalAtomList the list of relational atoms to remove unused atoms from
     @return the updated list of relational atoms with unused atoms removed
     */
    public static List<RelationalAtom> removeUnusedRelationalAtom(List<Variable> projectionVariables,List<RelationalAtom> inputRelationalAtomList)
    {
        for(int i=0;i<inputRelationalAtomList.size();i++)
        {
            RelationalAtom relationalAtom = inputRelationalAtomList.get(i);
            List<Term> termList = relationalAtom.getTerms();
            int cnt=0;
            for (Term term : termList)
            {
                if (term instanceof Constant)
                {
                    break;
                }
                else if(term instanceof  Variable)
                {
                    if(!projectionVariables.contains(term))
                    {
                        cnt++;
                    }
                }
            }
            if (cnt==termList.size())
            {
                inputRelationalAtomList.remove(i);
                i=0;
            }
        }
        return inputRelationalAtomList;
    }

    @Override
    public String toString() {
        return "Projection ( " + this.getChildOperator().toString()+")";
    }

    /*
        This method is used get the total number of terms from the relationalatom list.
        For example: if we have relational list: R(x, y, z), S(x, w, t), T(x, r), in this case the number of terms form this relationAtom list is 6
        And if we only have single relational list: R(x,y,z), then the number of terms from this single relationAtom is 3.
         */
    public int getNumberFromRelationAtom()
    {
        if (this.relationalAtomList==null)
        {
            if(this.relationalAtom!=null)
            {
                return relationalAtom.getTerms().size();
            }
            else
            {
                throw new UnsupportedOperationException("Both relationAtomList and relationAtom are null!!!");
            }
        }
        int numTerms=0;
        for( RelationalAtom relationalAtom: this.relationalAtomList )
        {
            for(Term term : relationalAtom.getTerms())
            {
                numTerms++;
            }
        }
        return numTerms;
    }

    public Operator getChildOperator() {
        return childOperator;
    }


    public List<Variable> getProjectionVariables() {
        return projectionVariables;
    }

    public RelationalAtom getRelationalAtom() {
        return relationalAtom;
    }

    public void setRelationalAtom(RelationalAtom relationalAtom) {
        this.relationalAtom = relationalAtom;
    }

    public List<RelationalAtom> getRelationalAtomList() {
        return relationalAtomList;
    }



}
