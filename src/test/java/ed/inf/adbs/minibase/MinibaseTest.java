package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.Query;
import ed.inf.adbs.minibase.base.TestUlits;
import ed.inf.adbs.minibase.evaluator.QueryPlanner;
import ed.inf.adbs.minibase.evaluator.TopInterpreter;
import ed.inf.adbs.minibase.evaluator.UltsForEvaluator;
import ed.inf.adbs.minibase.parser.QueryParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for Minibase.
 */

public class MinibaseTest {

    String databaseDir = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db";
    @Test
    public void Testquery1() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query1.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query1.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query1.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }

    @Test
    public void Testquery2() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query2.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query2.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query2.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }

    @Test
    public void Testquery3() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query3.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query3.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query3.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery4() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query4.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query4.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query4.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery6() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query6.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query6.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query6.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery7() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query7.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query7.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query7.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery8() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query8.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query8.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query8.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery9() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query9.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query9.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query9.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery10() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query10.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query10.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query10.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery11() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query11.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query11.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query11.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery12() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query12.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query12.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query12.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery13() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query13.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query13.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query13.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery14() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query14.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query14.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query14.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery15() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query15.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query15.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query15.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery16() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query16.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query16.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query16.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery17() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query17.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query17.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query17.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery18() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query18.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query18.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query18.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery19() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query19.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query19.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query19.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery20() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query20.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query20.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query20.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }

    @Test
    public void Testquery21() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query21.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query21.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query21.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }
    @Test
    public void Testquery22() throws IOException {
        String inputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query22.txt";
        String outputFile = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query22.csv";
        String expectedOutput = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query22.csv";
        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
        boolean check=TestUlits.compareFileDifferentOrder(outputFile,expectedOutput);
        assertTrue(check);
    }

}

