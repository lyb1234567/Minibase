package ed.inf.adbs.minibase.base;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Relation;
import ed.inf.adbs.minibase.evaluator.Operator;
import ed.inf.adbs.minibase.evaluator.ScanOperator;
import ed.inf.adbs.minibase.parser.QueryParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ScanOpearatorTest {

    @Test
    public void test_ScanOperator_getNextTuple() throws IOException {
        Query query = QueryParser.parse("Q(x,y,z) :- R(x, y, z)");
        String schemaFilePath = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"schema.txt";
        DatabaseCatalog databaseCatalog = DatabaseCatalog.getCatalog();
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> SchemaMap = databaseCatalog.getSchemaMap();
        Schema testSchema1 = SchemaMap.get("R");
        Schema testSchema2 = SchemaMap.get("T");
        Schema testSchema3 = SchemaMap.get("S");
        RelationalAtom relationalAtom = (RelationalAtom) query.getBody().get(0);
        String fileName1="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"R.csv";
        ScanOperator scanOperator_1 = new ScanOperator(fileName1,testSchema1,relationalAtom);
        scanOperator_1.dump("R");

        System.out.println("\n\n");

        String fileName2="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"T.csv";
        ScanOperator scanOperator_2 = new ScanOperator(fileName2,testSchema2,relationalAtom);
        scanOperator_2.dump("T");


        System.out.println("\n\n");
        String fileName3="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"S.csv";
        ScanOperator scanOperator_3 = new ScanOperator(fileName3,testSchema3,relationalAtom);
        scanOperator_3.dump("S");
        System.out.println("\n\n");

        // compare

    }

    @Test
    public void test_ScanOperator_reset() throws IOException {

        Query query = QueryParser.parse("Q(x,y,z) :- R(x, y, z)");
        String schemaFilePath = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"schema.txt";
        DatabaseCatalog databaseCatalog = DatabaseCatalog.getCatalog();
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> SchemaMap = databaseCatalog.getSchemaMap();
        Schema testSchema1 = SchemaMap.get("R");
        Schema testSchema2 = SchemaMap.get("T");
        Schema testSchema3 = SchemaMap.get("S");
        RelationalAtom relationalAtom = (RelationalAtom) query.getBody().get(0);
        String fileName1="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"R.csv";
        ScanOperator scanOperator_1 = new ScanOperator(fileName1,testSchema1,relationalAtom);
        List<Constant> firstFields=scanOperator_1.getNextTuple().getFields();
        System.out.println(scanOperator_1.getNextTuple().getFields());
        System.out.println(scanOperator_1.getNextTuple().getFields());
        scanOperator_1.reset();
        assert (scanOperator_1.getNextTuple().getFields().equals(firstFields));

    }


}
