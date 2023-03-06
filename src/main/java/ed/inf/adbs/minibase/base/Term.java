package ed.inf.adbs.minibase.base;

public abstract class Term {
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


