package ed.inf.adbs.minibase.base;

public class StringConstant extends Constant {
    private String value;

    public StringConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "'" + value + "'";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!super.equals(obj))
        {
            return false;
        }
        else
        {
            return ((StringConstant) obj).getValue().equals(this.getValue());
        }
    }

    public StringConstant deepcopy()
    {
        return new StringConstant(this.getValue());
    }


}