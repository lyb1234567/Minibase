package ed.inf.adbs.minibase.base;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Relation;
import ed.inf.adbs.minibase.dbStructure.Tuple;
import org.junit.Test;

import javax.print.attribute.standard.SheetCollate;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class DatabaseCatologTest {

    public boolean schemaMapEquals(HashMap<String,Schema> map1, HashMap<String,Schema> map2)
    {
        for (Map.Entry<String, Schema> entry : map1.entrySet())
        {
            String key= entry.getKey();
            if(map2.containsKey(key))
            {

                if (map1.get(key).equals(map2.get(key)))
                {
                    continue;
                }
                else
                {
                    System.out.println(map1);
                    System.out.println(map2);
                    return false;
                }
            }
            else
            {

                return false;

            }
        }
        return true;
    }


    public boolean relationMapEquals(HashMap<String,Relation> map1, HashMap<String,Relation> map2)
    {
        for (Map.Entry<String, Relation> entry : map1.entrySet())
        {
            String key= entry.getKey();
            if(map2.containsKey(key))
            {

                if (map1.get(key).equals(map2.get(key)))
                {
                    continue;
                }
                else
                {
                    System.out.println(map1);
                    System.out.println(map2);
                    return false;
                }
            }
            else
            {

                return false;

            }
        }
        return true;
    }
    @Test
    public void test_constructSchemaMap()
    {
        String schemaFilePath = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"schema.txt";
        DatabaseCatalog databaseCatalog = DatabaseCatalog.getCatalog();
        try
        {
            databaseCatalog.constructSchemaMap(schemaFilePath);
            HashMap<String,Schema> testMap=new HashMap<String,Schema>();
            List<String> lst1=new ArrayList<>();
            List<String> lst2=new ArrayList<>();
            List<String> lst3=new ArrayList<>();
            lst1.add("int");
            lst1.add("int");
            lst1.add("string");

            lst2.add("int");
            lst2.add("string");
            lst2.add("int");

            lst3.add("int");
            lst3.add("int");

            String name1="R";
            String name2="S";
            String name3="T";

            Schema new_schema1 = new Schema(name1,lst1);
            Schema new_schema2 = new Schema(name2,lst2);
            Schema new_schema3 = new Schema(name3,lst3);
            testMap.put(name1,new_schema1);
            testMap.put(name2,new_schema2);
            testMap.put(name3,new_schema3);

            HashMap<String,Schema> SchemaMap = databaseCatalog.getSchemaMap();
            assertEquals(schemaMapEquals(testMap,SchemaMap),true);
        }
        catch (IOException ex)
        {
            System.out.println("File not found!!!");
        }
    }

    @Test
    public void test_constructRelationMap()
    {
        String dirPath = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator;
        DatabaseCatalog databaseCatalog = DatabaseCatalog.getCatalog();
        try
        {
            HashMap<String,Relation> testMap=new HashMap<String,Relation>();
            List<String> lst1=new ArrayList<>();
            List<String> lst2=new ArrayList<>();
            List<String> lst3=new ArrayList<>();
            lst1.add("int");
            lst1.add("int");
            lst1.add("string");

            lst2.add("int");
            lst2.add("string");
            lst2.add("int");

            lst3.add("int");
            lst3.add("int");
            Schema new_schema1 = new Schema("R",lst1);
            Schema new_schema2 = new Schema("S",lst2);
            Schema new_schema3 = new Schema("T",lst3);
            Relation new_relation1 = new Relation("R",new_schema1,dirPath);
            Relation new_relation2 = new Relation("S",new_schema2,dirPath);
            Relation new_relation3 = new Relation("T",new_schema3,dirPath);
            databaseCatalog.constructRelationMap(dirPath);
            HashMap <String, Relation> RelationMap = databaseCatalog.getRelationMap();
            testMap.put("R",new_relation1);
            testMap.put("S",new_relation2);
            testMap.put("T",new_relation3);

            boolean check = relationMapEquals(testMap,RelationMap);
            assertEquals(check,true);



        }
        catch (IOException ex)
        {
            System.out.println("File not found!!!");
        }
    }

}
