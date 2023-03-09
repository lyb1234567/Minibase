package ed.inf.adbs.minibase.dbStructure;

//  relation takes  name, schema and relation's location
public class Relation {

    private String name;
    private Schema schema;
    private String Location;


    public Relation(String name, Schema schema, String Location) {
        this.name = name;
        this.schema = schema;
        this.Location = Location;
    }


    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String Location() {
        return Location;
    }

    public void setFileLocation(String Location) {
        this.Location = Location;
    }
}