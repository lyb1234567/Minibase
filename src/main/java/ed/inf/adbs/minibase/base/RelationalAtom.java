package ed.inf.adbs.minibase.base;

import ed.inf.adbs.minibase.Utils;

import java.util.List;

public class RelationalAtom extends Atom {
    private String name;

    private List<Term> terms;

    public RelationalAtom(String name, List<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    public String getName() {
        return name;
    }

//    public void change(String variable,String constants)
//    {
//        List<Term> terms=this.terms;
//        for (int i=0;i< terms.size();i++)
//        {
//            if (terms.get(i).toString().equals(variable))
//            {
//                terms.get(i)=(Term) variable;
//            }
//        }
//    }

    public List<Term> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        return name + "(" + Utils.join(terms, ", ") + ")";
    }
}
