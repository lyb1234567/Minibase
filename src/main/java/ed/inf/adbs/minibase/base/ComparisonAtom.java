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

    public ComparisonAtom deepcopy()
    {
        Term new_1 = this.term1.deepcopy();
        Term new_2 = this.term2.deepcopy();
        ComparisonOperator new_op = this.op;
        return new ComparisonAtom(new_1,new_2,new_op);
    }

}
