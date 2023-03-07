package ed.inf.adbs.minibase.base;

public abstract class Atom {

    /**
     * Override equals for Atom to compare two different atoms
     * @param obj Atom object
     * @return return a boolean to check if two atoms are the same
     */
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

    /**
     * A abstract method, since Atom is an abstract class
     * @return
     */
    public abstract Atom deepcopy();


}
