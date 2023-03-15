package ed.inf.adbs.minibase.base;

import ed.inf.adbs.minibase.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Head {
    private String name;

    private List<Variable> variables;

    private SumAggregate agg;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Head head = (Head) o;
        return name.equals(head.name) && variables.equals(head.variables) && agg.equals(head.agg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, variables, agg);
    }

    public Head(String name, List<Variable> variables, SumAggregate agg) {
        this.name = name;
        this.variables = variables;
        this.agg = agg;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public SumAggregate getSumAggregate() {
        return agg;
    }

    @Override
    public String toString() {
        if (agg == null) {
            return name + "(" + Utils.join(variables, ", ") + ")";
        }
        if (variables.isEmpty()) {
            return name + "(" + agg + ")";
        }
        return name + "(" + Utils.join(variables, ", ") + ", " + agg + ")";
    }

    /**
     * Deep copy of head object
     * @return return a new head object, where the variable and SumAggregate object are all deep copied
     */
    public Head deepcopy()
    {
        SumAggregate new_agg= this.agg.deepcopy();
        List<Variable> var_lst=new ArrayList<>();
        for(int i=0;i<this.variables.size();i++)
        {
            Variable var = this.variables.get(i).deepcopy();
            var_lst.add(var);
        }
        String copy_name=this.getName();
        return new Head(copy_name,var_lst,agg);

    }
}
