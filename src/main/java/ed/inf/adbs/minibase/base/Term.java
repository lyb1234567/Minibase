package ed.inf.adbs.minibase.base;

public abstract class Term {

    /**
     * Deep copy for Term Object,but since it is an abstract class,it does nothing
     */
    @Override
    public boolean equals(Object object) {
        if(this == object)
        {
            return true;
        }
        if(object==null)
        {
            return false;
        }
        return getClass()==object.getClass();
    }
    public abstract Term deepcopy();
}


