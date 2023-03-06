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

    public Variable deepcopy()
    {
        return new Variable(this.name);
    }
}
