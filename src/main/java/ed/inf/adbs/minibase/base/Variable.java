package ed.inf.adbs.minibase.base;

public class Variable extends Term {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) return false;

        String name1=this.getName();
        String name2= ((Variable)object).getName();
        return name1.equals(name2);
    }

    /**
     * Override hashCode for Variable object,so it can be removed from the hashset if there is a duplicate
     * @return hashCode should be an integer
     */

    @Override
    public int hashCode()
    {
        return this.getName().hashCode();
    }

    /**
     * Deep copy for Variable Object, where only name is considered
     * @return return a new Variable Object
     */
    public Variable deepcopy()
    {
        return new Variable(this.name);
    }
}
