package ed.inf.adbs.minibase.base;

import ed.inf.adbs.minibase.Utils;

import java.util.ArrayList;
import java.util.List;

public class SumAggregate extends Term {

    private List<Term> productTerms;

    public SumAggregate(List<Term> terms) {
        this.productTerms = terms;
    }

    public List<Term> getProductTerms() {
        return productTerms;
    }

    @Override
    public String toString() {
        return "SUM(" + Utils.join(productTerms, " * ") + ")";
    }

    /**
     * Override hashcode for SumAggregate so that it can be removed, if there is a duplicate SumAggregate object in the hashset
     * @return hashCode should be a integer
     */
    @Override
    public int hashCode()
    {
        int hashCode=1;
        for(int i=0;i<productTerms.size();i++)
        {
            hashCode=hashCode*productTerms.get(i).hashCode();
        }
        return hashCode;
    }
    public SumAggregate deepcopy()
    {
        List<Term> termList = new ArrayList<>();
        for(int i=0;i<getProductTerms().size();i++)
        {
            Term cur=getProductTerms().get(i).deepcopy();
            termList.add(cur);
        }
        return new SumAggregate(termList);
    }

}
