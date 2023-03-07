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

    /**
     * Override hashcode() for String constant, so that it can be removed in hashset,if there is a duplicate StringConstant Object
     * @return Hashcode is a Integer
     */
    @Override
    public int hashCode()
    {
        return this.getValue().hashCode();
    }

    public StringConstant deepcopy()
    {
        return new StringConstant(this.getValue());
    }



}