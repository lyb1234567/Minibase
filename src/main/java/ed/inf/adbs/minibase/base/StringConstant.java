package ed.inf.adbs.minibase.base;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StringConstant that = (StringConstant) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Deep copy for StringConstant Object, where only value is considered
     * @return return a new StringConstant Object
     */
    public StringConstant deepcopy()
    {
        return new StringConstant(this.getValue());
    }




}