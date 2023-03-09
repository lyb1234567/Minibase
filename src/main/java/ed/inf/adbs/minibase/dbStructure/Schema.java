package ed.inf.adbs.minibase.dbStructure;

import ed.inf.adbs.minibase.base.Constant;
import ed.inf.adbs.minibase.base.StringConstant;

import java.util.ArrayList;
import java.util.List;
/**
 * This class if for sotring shcema, which contains relation name and data type
 */
public class Schema {
    private String name;

    // store any class that extens the Constant class:String Constant and Integer Constatn
    List<String> dataTypes;

    /**
     * The schema class takes relation name such as R. It also takes a list of Constant data type such as String Constant and Integer Constant
     * @param name Relation name
     * @param dataTypes List of Constant Object
     */
    public Schema(String name, List<String> dataTypes) {
        this.name = name;
        this.dataTypes = dataTypes;
    }

    /**
     * Return the relation name
     * @return string name
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Return data types, which should be a list of constant object
     * @return
     */
    public List<String> getDataTypes()
    {
        return this.getDataTypes();
    }

    /**
     * Set a new string name to a schema
     * @param name
     */
    public void setName(String name)
    {
        this.name=name;
    }

    /**
     * Set new datatyps to scheme
     * @param dataTypes
     */
    public void setDataTypes(List<String> dataTypes)
    {
        this.dataTypes=dataTypes;
    }

    /**
     * Override equals method for schema to check if two schemas are the same later.
     * @param obj another schema object
     * @return return a boolean
     */
    @Override
    public boolean equals (Object obj)
    {
        if(this==obj)
        {
            return true;
        }
        Schema schema=(Schema) obj;
        return (this.name.equals(((Schema) obj).name) && this.dataTypes.equals(((Schema) obj).dataTypes));
    }

    /**
     * Override hashCode() method for scheme for later use in hashset and hashMap
     * @return return muplication of hashcode of name and dataTypes.
     */
    @Override
    public int hashCode()
    {
        return this.dataTypes.hashCode()* this.name.hashCode();
    }

    /**
     * deep copy for Shema object just in case we want to use it later
     */

    public Schema deepcopy()
    {
        String addName = this.getName();
        List<String> newDataTypes=new ArrayList<>();
        for(int i=0;i<this.getDataTypes().size();i++)
        {
            String newDataType=this.getDataTypes().get(i);
            newDataTypes.add(newDataType);
        }
        return new Schema(addName,newDataTypes);
    }

}
