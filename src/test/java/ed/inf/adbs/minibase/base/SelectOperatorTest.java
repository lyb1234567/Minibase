package ed.inf.adbs.minibase.base;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Relation;
import ed.inf.adbs.minibase.dbStructure.Tuple;
import ed.inf.adbs.minibase.evaluator.Operator;
import ed.inf.adbs.minibase.evaluator.ScanOperator;
import ed.inf.adbs.minibase.parser.QueryParser;
import ed.inf.adbs.minibase.evaluator.SelectionOperator;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SelectOperatorTest {
    @Test
    public void test_SelectOperator_getNextTuple() throws IOException {
        String schemaFilePath = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"schema.txt";
        Path queryPath = Paths.get("." + File.separator + "data" + File.separator + "evaluation" + File.separator + "input" + File.separator + "query4.txt");
        System.out.println(queryPath);
        Query query = QueryParser.parse(queryPath);
        DatabaseCatalog databaseCatalog = DatabaseCatalog.getCatalog();
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> SchemaMap = databaseCatalog.getSchemaMap();
        Schema testSchema1 = SchemaMap.get("R");
        Schema testSchema2 = SchemaMap.get("T");
        Schema testSchema3 = SchemaMap.get("S");
        List<Atom> body =  query.getBody();
        List<RelationalAtom> relationalAtoms = new ArrayList<>();
        List<ComparisonAtom> comparisonAtoms = new ArrayList<>();
        for(Atom atom:body)
        {

            try
            {
                RelationalAtom temp = (RelationalAtom) atom;
                relationalAtoms.add(temp);
            }
            catch (Exception ex)
            {
                ComparisonAtom temp = (ComparisonAtom) atom;
                comparisonAtoms.add(temp);
            }
        }
        String fileName1="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"R.csv";
        RelationalAtom relationalAtom = relationalAtoms.get(0);
        ComparisonAtom comparisonAtom = comparisonAtoms.get(0);
        System.out.println(comparisonAtom);
        ScanOperator scanOperator_1 = new ScanOperator(fileName1,testSchema1,relationalAtom);
        SelectionOperator selectionOperator = new SelectionOperator(relationalAtom,scanOperator_1,comparisonAtoms);
        System.out.println(selectionOperator.getNextTuple().getFields());
        System.out.println(selectionOperator.getNextTuple().getFields());

        // compare

    }
}
