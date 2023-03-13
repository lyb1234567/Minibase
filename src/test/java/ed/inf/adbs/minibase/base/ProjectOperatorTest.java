package ed.inf.adbs.minibase.base;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Relation;
import ed.inf.adbs.minibase.dbStructure.Tuple;
import ed.inf.adbs.minibase.evaluator.Operator;
import ed.inf.adbs.minibase.evaluator.ProjectionOperator;
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

public class ProjectOperatorTest {

    @Test
    public void test_ProjectionOperator_getNextTuple() throws IOException
    {
        String schemaFilePath = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"schema.txt";
        Path queryPath = Paths.get("." + File.separator + "data" + File.separator + "evaluation" + File.separator + "input_project" + File.separator + "query6.txt");
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

        List<Variable> ProjectionVariable =query.getHead().getVariables();
        Schema schema = SchemaMap.get(relationalAtoms.get(0).getName());
        String fileName1="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+relationalAtoms.get(0).getName()+".csv";
        ScanOperator scanOperator = new ScanOperator(fileName1,schema,relationalAtoms.get(0));
        SelectionOperator selectionOperator = new SelectionOperator(relationalAtoms.get(0),scanOperator,comparisonAtoms);
        ProjectionOperator projectionOperator = new ProjectionOperator(selectionOperator,ProjectionVariable, relationalAtoms.get(0));
        projectionOperator.dump("query6","project","csv");



        // Compare four output files for project operator
        String  outputFile1="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_project"+File.separator+"query1.csv";
        String  outputFile2="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_project"+File.separator+"query2.csv";
        String  outputFile3="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_project"+File.separator+"query3.csv";
        String  outputFile4="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_project"+File.separator+"query4.csv";
        String  outputFile5="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_project"+File.separator+"query5.csv";
        String  outputFile6="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_project"+File.separator+"query6.csv";


        String Compare1="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output_project"+File.separator+"query1.csv";
        String Compare2="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output_project"+File.separator+"query2.csv";
        String Compare3="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output_project"+File.separator+"query3.csv";
        String Compare4="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output_project"+File.separator+"query4.csv";
        String Compare5="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output_project"+File.separator+"query5.csv";
        String Compare6="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output_project"+File.separator+"query6.csv";

        assert (TestUlits.compareFile(outputFile1,Compare1));
        assert (TestUlits.compareFile(outputFile2,Compare2));
        assert (TestUlits.compareFile(outputFile3,Compare3));
        assert (TestUlits.compareFile(outputFile4,Compare4));
        assert (TestUlits.compareFile(outputFile5,Compare5));
        assert (TestUlits.compareFile(outputFile6,Compare6));

    }

}
