package ed.inf.adbs.minibase.base;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IntegerConstant that = (IntegerConstant) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Deep copy for InterConstant Object, where the value of the constant is only considered
     * @return return a new IntegerConstant Object
     */
    @Override
    public IntegerConstant deepcopy()
    {
        return new IntegerConstant(this.getValue());
    }

}
