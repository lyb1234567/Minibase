package ed.inf.adbs.minibase.base;

public class ComparisonAtom extends Atom {

    private Term term1;

    private Term term2;

    private ComparisonOperator op;

    public ComparisonAtom(Term term1, Term term2, ComparisonOperator op) {
        this.term1 = term1;
        this.term2 = term2;
        this.op = op;
    }

    public Term getTerm1() {
        return term1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ComparisonAtom that = (ComparisonAtom) o;
        return term1.equals(that.term1) && term2.equals(that.term2) && op == that.op;
    }

    public Term getTerm2() {
        return term2;
    }

    public ComparisonOperator getOp() {
        return op;
    }

    @Override
    public String toString() {
        return term1 + " " + op + " " + term2;
    }


    /**
     * Override hashCode for ComparisonAtom and hence it can be remove from a hashset,if there is a duplicate ComparisonAtom object
     * @return hashCode should be an integer, since it takse two terms and one ComparisonOperater object, the hashcode will be multiplication of their hashcode
     */
    @Override
    public int hashCode()
    {
        return this.term1.hashCode()*this.term2.hashCode()*this.op.hashCode();
    }

    public void setTerm1(Term term1) {
        this.term1 = term1;
    }

    public void setTerm2(Term term2) {
        this.term2 = term2;
    }

    public void setOp(ComparisonOperator op) {
        this.op = op;
    }

    /**
     * Deep copy of ComparisonAtom Object
     * @return return a new ComparisonAtom Object
     */
    public ComparisonAtom deepcopy()
    {
        Term new_1 = this.term1.deepcopy();
        Term new_2 = this.term2.deepcopy();
        ComparisonOperator new_op = this.op;
        return new ComparisonAtom(new_1,new_2,new_op);
    }

}
