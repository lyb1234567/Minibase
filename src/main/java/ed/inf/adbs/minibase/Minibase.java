package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.Atom;
import ed.inf.adbs.minibase.base.Query;
import ed.inf.adbs.minibase.base.Head;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.evaluator.QueryPlanner;
import ed.inf.adbs.minibase.evaluator.TopInterpreter;
import ed.inf.adbs.minibase.evaluator.UltsForEvaluator;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * In-memory database system
 *
 */
public class Minibase {

    public static void main(String[] args) throws IOException {

        if (args.length != 3) {
            System.err.println("Usage: Minibase database_dir input_file output_file");
            return;
        }

        String databaseDir = args[0];
        String inputFile = args[1];
        String outputFile = args[2];


        evaluateCQ(databaseDir, inputFile, outputFile);

        parsingExample(inputFile);
    }

    public static void evaluateCQ(String databaseDir, String inputFile, String outputFile) throws IOException {

        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
        String queryNumber = UltsForEvaluator.getqueryNumber(inputFile);
        QueryPlanner queryPlanner =new QueryPlanner(inputQuery,databaseDir);
        TopInterpreter topInterpreter = new TopInterpreter(queryPlanner);
        topInterpreter.dump(queryNumber,outputFile,".csv");
    }



    public static void parsingExample(String filename) {
        try {
            Query query = QueryParser.parse(Paths.get(filename));
            // Query query = QueryParser.parse("Q(x, y) :- R(x, z), S(y, z, w), z < w");
            // Query query = QueryParser.parse("Q(SUM(x * 2 * x)) :- R(x, 'z'), S(4, z, w), 4 < 'test string' ");

            System.out.println("Entire query: " + query);
            Head head = query.getHead();
            System.out.println("Head: " + head);

            List<Atom> body = query.getBody();
            System.out.println("Body: " + body);
        }
        catch (Exception e)
        {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

}
