package ed.inf.adbs.minibase.dbStructure;

import ed.inf.adbs.minibase.base.Constant;
import ed.inf.adbs.minibase.base.IntegerConstant;
import ed.inf.adbs.minibase.base.StringConstant;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class uses singleton design pattern, so that there will be only one global database calog instance
 */
public class DatabaseCatalog {
    // Singleton instance
    private static DatabaseCatalog databaseCatalog = new DatabaseCatalog();



    // Map for Relation, String is  used as the Relation name,
    private HashMap<String,Relation> relationMap = new HashMap<String,Relation>();


    // Map for Schema, String is is also used as the  Relation name, for example {R=schema}
    private HashMap<String, Schema> schemaMap;

    /**
     *  returns if the instance if it is already initialised. Otherwise initialises a new instance and returns it.
     * @return return a DatabaseCatalog type
     */
    public static DatabaseCatalog getCatalog() {
        if(databaseCatalog !=null)
        {
            return databaseCatalog;
        }
        databaseCatalog = new DatabaseCatalog();
        databaseCatalog.relationMap=new HashMap<String,Relation>();
        databaseCatalog.schemaMap=new HashMap<String,Schema>();
        return databaseCatalog;
    }

    public HashMap<String,Schema> getSchemaMap()
    {
        return this.schemaMap;
    }

    public HashMap<String,Relation> getRelationMap()
    {
        return this.relationMap;
    }

    /**
     * This method will take a path as a input, in the method it will first try to read the schema file. If it works, it will then scan the whole schema file
     * It will try to read each line of schema file, the first elem is always a relation name. Then the following elements are the datatype, which is a string
     * Then the schemaMap will be constructed
     * @param path Take a path as input
     * @throws IOException If the method fails to read the schema file, then it will throw an IOException outside the method
     */
    public void constructSchemaMap(String path) throws  IOException
    {
           // SchemaFile

           File schemaFile =new File(path);
           // Read the SchemaFile
           BufferedReader reader = new BufferedReader(new FileReader(schemaFile));

           // Read the shcema file line by line
           String line;
           HashMap<String, Schema> newMap = new HashMap<String,Schema>();
           while ((line = reader.readLine()) != null)
           {
               // Split the line: R int int String
               String [] lineSplit=line.split(" ");
               List<String> lineList= Arrays.asList(lineSplit);
               List<String> typeList = new ArrayList<>();
               String relationName= lineList.get(0);

               for(int i=1;i< lineList.size();i++)
               {
                   typeList.add(lineList.get(i));
               }
              Schema addSchema = new Schema(relationName,typeList);
              newMap.put(relationName,addSchema);
           }
           this.schemaMap=newMap;
    }

    /**
     * This method will take a path as a input, in the method it will first try to read the schema file. If it works, it will then scan the whole schema file
     * It will try to read the schema file first, then will extract the reltionName from a bunch of csv files, then set the relations
     * @param dirPath take a path for directory
     * @throws IOException if it catches any Exception, it will throw
     */
    public void constructRelationMap(String dirPath) throws IOException
    {
        // Get the filePath for Schema first
        String schemaFilePath = dirPath+"schema.txt";

        // Try to set shcema first
        this.constructSchemaMap(schemaFilePath);

        // find the path for all of the relations
        String relationCsvPath = dirPath+"files";
        File relationCsv = new File(relationCsvPath);

        // Put all the files into the list
        File [] fileList = relationCsv.listFiles();

        HashMap<String, Relation> newMap = new HashMap<String,Relation>();
        assert fileList != null;
        for( File file: fileList)
        {
            // name contains .csv
            String filename=file.getName();

            // Extrac relationname from the .csv name
            String [] nameList = filename.split("\\.");

            // extract relationName like R S T
            String relationName = nameList[0];

            // Now check the schema for a specific relationName. We first need to check if the key is contained in the schema Map
            if (!this.getSchemaMap().containsKey(relationName))
            {
                throw new IllegalArgumentException("You are trying to extract a CSV file that does not match any parsed schmea");
            }
            // Set the relationMap, which contains relationName, Schema and corresponding relationPath
            String relationPath = file.getPath();
            Relation setRelation = new Relation(relationName, this.schemaMap.get(relationName), relationPath);
            newMap.put(relationName,setRelation);
        }
        this.relationMap=newMap;

    }

    public void setRelationMap(HashMap<String,Relation> relationMap)
    {
        this.relationMap=relationMap;
    }
}
