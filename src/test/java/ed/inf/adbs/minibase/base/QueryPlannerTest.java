package ed.inf.adbs.minibase.base;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.Tuple;
import ed.inf.adbs.minibase.evaluator.*;
import ed.inf.adbs.minibase.parser.QueryParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

import static ed.inf.adbs.minibase.evaluator.JoinOperator.subFromLeftandRight;
import static org.junit.Assert.*;
public class QueryPlannerTest {



    private  Variable variablex = new Variable("x");
    private  Variable variabley = new Variable("y");
    private  Variable variablez = new Variable("z");
    private  Variable variablew = new Variable("w");
    private  Variable variablet = new Variable("t");
    private  Variable variabler = new Variable("r");

    private  Variable variablem = new Variable("m");



    private final List<Term> termListR = new ArrayList<Term>()
    {
        {
            add(variablex);
            add(variabley);
            add(variablez);
        }
    };

    private final List<Term> termListS = new ArrayList<Term>()
    {
        {
            add(variablex);
            add(variablew);
            add(variablet);
        }
    };

    private final List<Term> termListT = new ArrayList<Term>()
    {
        {
            add(variablex);
            add(variabler);
        }
    };

    private final List<Term> termListM = new ArrayList<Term>()
    {
        {
            add(variablem);
            add(variabler);
        }
    };


    private  final RelationalAtom relationalAtomR = new RelationalAtom("R",termListR);
    private  final RelationalAtom relationalAtomS = new RelationalAtom("S",termListS);
    private  final RelationalAtom relationalAtomT = new RelationalAtom("T",termListT);

    private  final RelationalAtom relationalAtomM = new RelationalAtom("T",termListM);


    String schemaFilePath = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"schema.txt";

    private  String fileNameR="."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"R.csv";

    private  String fileNameS="."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"S.csv";
    private  String fileNameT="."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"T.csv";
    public DatabaseCatalog databaseCatalog = DatabaseCatalog.getCatalog();







    IntegerConstant integerConstant1 = new IntegerConstant(1);
    IntegerConstant integerConstant2 = new IntegerConstant(2);
    IntegerConstant integerConstant3 = new IntegerConstant(3);
    IntegerConstant integerConstant4 = new IntegerConstant(4);

    private ComparisonAtom comparisonAtom1 = new ComparisonAtom(variablex,integerConstant1,ComparisonOperator.GT);
    private ComparisonAtom comparisonAtom2 = new ComparisonAtom(variablex,integerConstant1,ComparisonOperator.LT);
    private ComparisonAtom comparisonAtom3 = new ComparisonAtom(variablex,integerConstant1,ComparisonOperator.EQ);
    private ComparisonAtom comparisonAtom4 = new ComparisonAtom(variablex,integerConstant1,ComparisonOperator.LEQ);
    private ComparisonAtom comparisonAtom5 = new ComparisonAtom(variablex,integerConstant1,ComparisonOperator.GEQ);

    private ComparisonAtom comparisonAtom6 = new ComparisonAtom(variablex,variabley,ComparisonOperator.GEQ);

    private ComparisonAtom comparisonAtom7 = new ComparisonAtom(variablex,variablez,ComparisonOperator.GEQ);

    private ComparisonAtom comparisonAtom8 = new ComparisonAtom(variabler,integerConstant2,ComparisonOperator.GT);
    private ComparisonAtom comparisonAtom9 = new ComparisonAtom(variablew,integerConstant2,ComparisonOperator.GT);
    private ComparisonAtom comparisonAtom10 = new ComparisonAtom(variablez,integerConstant2,ComparisonOperator.GT);

    private ComparisonAtom comparisonAtom11 = new ComparisonAtom(variablem,variabler,ComparisonOperator.GT);

    private ComparisonAtom comparisonAtom12 = new ComparisonAtom(variablex,variablet,ComparisonOperator.GT);

    public QueryPlannerTest() throws IOException {
    }


    @Test
    public void test()
    {
        final List<Variable> variables = new ArrayList<Variable>()
        {
            {
                add(variablex);
                add(variabley);
                add(variablez);
            }

        };
        final List<RelationalAtom> relationalAtoms = new ArrayList<RelationalAtom>()
        {
            {
             add(relationalAtomR);
             add(relationalAtomS);
            }
        };

        System.out.println(ProjectionOperator.removeUnusedRelationalAtom(variables,relationalAtoms));
    }

//    @Test
//    public void testQueryPlannerQuery1() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query1.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query1","queryPlanner","txt");
//        String  outputFile="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_queryPlanner"+File.separator+"query1.txt";
//        String Compare="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query1.csv";
//        boolean check= TestUlits.compareFile(outputFile,Compare);
//        assertTrue(check);
//    }
//
//    @Test
//    public void testQueryPlannerQuery2() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query2.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query2","queryPlanner","txt");
//        String  outputFile="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_queryPlanner"+File.separator+"query2.txt";
//        String Compare="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query2.csv";
//        boolean check= TestUlits.compareFile(outputFile,Compare);
//        assertTrue(check);
//    }
//
//    @Test
//    public void testQueryPlannerQuery3() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query3.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query3","queryPlanner","txt");
//        String  outputFile="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_queryPlanner"+File.separator+"query3.txt";
//        String Compare="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query3.csv";
//        boolean check= TestUlits.compareFile(outputFile,Compare);
//        assertTrue(check);
//    }
//
//    @Test
//    public void testQueryPlannerQuery4() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query4.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query4","queryPlanner","txt");
//        String  outputFile="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_queryPlanner"+File.separator+"query4.txt";
//        String Compare="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query4.csv";
//        boolean check= TestUlits.compareFile(outputFile,Compare);
//        assertTrue(check);
//    }
//
//    @Test
//    public void testQueryPlannerQuery5() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query5.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query5","queryPlanner","txt");
//        String  outputFile="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_queryPlanner"+File.separator+"query5.txt";
//        String Compare="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query5.csv";
//        boolean check= TestUlits.compareFile(outputFile,Compare);
//        assertTrue(check);
//    }
//
//    @Test
//    public void testQueryPlannerQuery6() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query6.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query6","queryPlanner","txt");
//        String  outputFile="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test_queryPlanner"+File.separator+"query6.txt";
//        String Compare="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"expected_output"+File.separator+"query6.csv";
//        boolean check= TestUlits.compareFile(outputFile,Compare);
//        assertTrue(check);
//    }
//
//    @Test
//    public void testQueryPlannerQuery10() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query10.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query10","queryPlanner","txt");
//    }
//
//    @Test
//    public void testQueryPlannerQuery11() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query11.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query11","queryPlanner","txt");
//    }
//
//    @Test
//    public void testQueryPlannerQuery12() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query12.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query12","queryPlanner","txt");
//    }
//
//    @Test
//    public void testQueryPlannerQuery13() throws IOException
//    {
//        String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query13.txt";
//        Query inputQuery = QueryParser.parse(Paths.get(inputFile));
//        QueryPlanner queryPlanner = new QueryPlanner(inputQuery);
//        queryPlanner.constructTree();
//        queryPlanner.dump("query13","queryPlanner","txt");
//    }



}
