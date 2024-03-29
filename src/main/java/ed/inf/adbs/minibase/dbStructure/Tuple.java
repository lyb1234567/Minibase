package ed.inf.adbs.minibase.dbStructure;
import ed.inf.adbs.minibase.base.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * This Tuple class is used to take take a list of constants as its "fields"
 */
public class Tuple {

    private List<Constant>fields;

    /**
     * A tuple object will be generated by taking a list of constants
     * @param fields represents the fields of query like in SELECT name, age, major FROM students, where name,age and major are the fields
     */
    public Tuple(List<Constant>fields)
    {
        this.fields=fields;
    }

    /**
     * This set function is sued to change the fields of a tuple
     * @param fields represents the fields of query like in SELECT name, age, major FROM students, where name,age and major are the fields
     */
    public void setFields( List<Constant> fields)
    {
        this.fields=fields;
    }

    /**
     * Override the equals method to compare two different tuples.
     * @param obj another tuple
     * @return return a boolean to check if these two tuples are the same
     */
    @Override
    public boolean equals(Object obj)
    {
        // If the address of these two objects is the same, they must be the same, return true directly.
        if(this==obj)
        {
            return true;
        }
        //if the object being compared to this is null, or if it is not an instance of the same class as "this".
        // If either of these conditions are true, then the two objects cannot be equal, so the method returns false.
        if(obj==null || getClass()!=obj.getClass())
        {
            return false;
        }
        //compares the fields instance variable of this and the fields instance variable of the other Tuple object using the equals() method
        Tuple tuple=(Tuple) obj;

        // Check if the fields are the same,where the filed are list of constans and the equal method of each Terms have been overriden.
        return fields.equals(tuple.fields);
    }

    // Override hashcode for Tuple object so that we can remove the duplicate Term ovject.
    @Override
    public int hashCode()
    {
        return Objects.hashCode(fields);
    }

    public List<Constant> getFields() {
        return fields;
    }

    /**
     * This method is used to deep copy the tuple object
     * @return it return a tuple type
     */
    public Tuple deepcopy()
    {
        List<Constant> new_fields= new ArrayList<>();
        for(int i=0;i< fields.size();i++)
        {
            Constant constant = (Constant) this.fields.get(i).deepcopy();
            new_fields.add(constant);
        }
        return new Tuple(new_fields);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "fields=" + fields +
                '}';
    }
}
