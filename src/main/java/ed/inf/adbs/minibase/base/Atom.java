package ed.inf.adbs.minibase.base;

public abstract class Atom {

    @Override
    public boolean equals(Object obj)
    {
        if (obj==null)
        {
            return false;
        }
        if(this == obj)
        {
            return true;
        }
        return obj.getClass()==getClass();
    }

    public abstract Atom deepcopy();


}
