package ed.inf.adbs.minibase.base;

import ed.inf.adbs.minibase.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public  static  boolean is_distinguished(String check_value, Head head)
    {
        List<Variable> temp = head.getVariables();
        for(int i=0;i<temp.size();i++)
        {
            if (temp.get(i).toString().equals(check_value))
            {
                return true;
            }
        }
        return false;
    }
    public boolean Is_similar( Object obj,Head head)
    {
        if(obj==null || !(obj instanceof RelationalAtom))
            return false;

        String obj_name=((RelationalAtom) obj).getName();
        if (!obj_name.equals(this.name))
        {
            return false;
        }
        List<Term> obj_terms= this.getTerms();
        if(obj_terms.size()!=this.getTerms().size())
        {
            return false;
        }
        boolean flag=true;
        for(int i=0;i<obj_terms.size();i++)
        {
            Term body_term=this.getTerms().get(i);
            Term check_term=obj_terms.get(i);
            if (is_distinguished(body_term.toString(),head) || is_distinguished(check_term.toString(),head))
            {
                return false;
            }
            if ((body_term instanceof  Variable) && (check_term instanceof Variable))
            {
                continue;
            }
            else if ((body_term instanceof Constant) &&(check_term instanceof Constant))
            {
                if (body_term.toString().equals(check_term.toString()))
                {
                    continue;
                }
                else
                {
                    return false;
                }
            }
            else if ((body_term instanceof Constant)&& (check_term instanceof Variable))
            {
                continue;
            }
            else if ((body_term instanceof Variable)&& (check_term instanceof Constant))
            {
                continue;
            }

        }

        return flag;
    }

    @Override
    public String toString() {
        return name + "(" + Utils.join(terms, ", ") + ")";
    }


    @Override
    public boolean equals(Object obj)
    {

//        if(obj==null || !(obj instanceof RelationalAtom))
//            return false;
        List<Term>  check = ((RelationalAtom) obj).getTerms();
        if( ((RelationalAtom) obj).getTerms().size()!= this.terms.size())
        {
            return false;
        }
        for(int i=0;i<this.terms.size();i++)
        {
            if (this.terms.get(i).toString().equals(check.get(i).toString()))
            {
                continue;
            }
            else
            {
              return false;
            }
        }
        return (this.name).equals(((RelationalAtom) obj).getName());
    }

    /**
     * Override hashcode for relational atom, so that it can be removed from the hashset(), if there is a duplicate relationalatom object
     * @return hashCode is an integer
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.name, this.terms);
    }

    /**
     * Deep copy for RelationalAtom Object, where deep copy of terms and names are considered
     * @return return a new RelationalAtom Object
     */
    public RelationalAtom deepcopy()
    {
        List<Term> termList = new ArrayList<>();
        for(int i=0;i<this.getTerms().size();i++)
        {
            Term new_term= this.getTerms().get(i).deepcopy();
            termList.add(new_term);
        }
        String name=this.getName();
        return new RelationalAtom(name,termList);
    }



}
