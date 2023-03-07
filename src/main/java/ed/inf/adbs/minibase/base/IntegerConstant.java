package ed.inf.adbs.minibase.base;

public class IntegerConstant extends Constant {
    private Integer value;

    public IntegerConstant(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        // if it does not satisfy the default equals, then return false
        if (!super.equals(obj))
        {
            return false;
        }
        else
        {
            //Then compare the value of the two objects
            return ((IntegerConstant) obj).getValue().equals(this.getValue());
        }
    }

    /**
     * Override hashcode for Integer Constant, so that it can be removed if there is a duplicate Integer constant in hashset
     * @return Hashcode is a integer
     */
    @Override public int hashCode()
    {
        return this.getValue().hashCode();

    }
    @Override
    public IntegerConstant deepcopy()
    {
        return new IntegerConstant(this.getValue());
    }
}
