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
import java.util.*;
import java.util.stream.Collectors;

import static ed.inf.adbs.minibase.evaluator.JoinOperator.subFromLeftandRight;
import static org.junit.Assert.*;
public class QueryPlannerTest {

    private String inputFile = "."+File.separator+"data"+File.separator+"evaluation"+File.separator+"input"+File.separator+"query6.txt";
    private Query inputQuery = QueryParser.parse(Paths.get(inputFile));
    private QueryPlanner queryPlanner = new QueryPlanner(inputQuery);

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
            add(variabler);
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
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        Schema schemaM=schemaHashMap.get("M");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);
        ScanOperator scanOperatorM = new ScanOperator(fileNameT,schemaT,relationalAtomM);

        final List<ComparisonAtom> comparisonAtomList = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom8);
                add(comparisonAtom9);
                add(comparisonAtom10);
                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        final List<ScanOperator> scanOperatorListC = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorM);
            }
        };

        /*
        R(x,r,z) S(x,w,t) T(x,r) x > 1, r > 2, w>2, z > 2.
        We should get
         */

        final List<ComparisonAtom> testComparisonAtoms = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom10);
            }
        };

        final List<ComparisonAtom> check = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };
    }

    @Test
    public void testcheckSingleRelationAtoms()
    {
        List<ComparisonAtom> comparisonAtoms =this.queryPlanner.getComparisonAtomList();
        List<RelationalAtom> relationalAtomList = this.queryPlanner.getRelationalAtomList();
        assertTrue(QueryPlanner.checkSingleRelationAtoms(comparisonAtoms.get(0),relationalAtomList));
    }

    @Test
    public void testcheckApplyScanOpeartor() throws IOException {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);
        ScanOperator scanOperatorM = new ScanOperator(fileNameT,schemaT,relationalAtomM);
        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        /**
         * R(x,y,z) S(x,w,t) T(x,r) x>1
         */
        boolean checkA = QueryPlanner.isSingleSelectionComparisonAtom(comparisonAtom2,scanOperatorListA);
        assertTrue(checkA);

        final List<ScanOperator> scanOperatorListB = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        /**
         * S(x,w,t) T(x,r) x>z
         */
        boolean checkB = QueryPlanner.isSingleSelectionComparisonAtom(comparisonAtom7,scanOperatorListB);
        assertFalse(checkB);
    }

    @Test
    public void testCheckIfCanBeApplietoScanOperators() throws IOException {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        /**
         * R(x,y,z) S(x,w,t) T(x,r) x>1
         */
        boolean checkA = QueryPlanner.checkIfCanBeApplietoScanOperators(comparisonAtom2,scanOperatorListA);


        /**
         * S(x,w,t) T(x,r) x>z
         */
        boolean checkB = QueryPlanner.checkIfCanBeApplietoScanOperators(comparisonAtom7,scanOperatorListB);

        assertTrue(checkA);
        assertFalse(checkB);
    }

    @Test
    public void testfilterNonJoinCondition() throws IOException {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);

        final List<ComparisonAtom> comparisonAtomList = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom2);
                add(comparisonAtom3);
                add(comparisonAtom4);
                add(comparisonAtom5);
                add(comparisonAtom6);
                add(comparisonAtom7);
            }
        };

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        /**
         * R(x,r,z) S(x,w,t) T(x,r) x > 1, x < 1, x = 1, x <= 1, x >= 1, x >= y, x >= z
         * Filtered: R(x,ar,z) S(x,w,t) T(x,r) x > 1, x < 1, x = 1, x <= 1, x >= 1, x >= z
         */
        List<ComparisonAtom> nonJoinCondition = QueryPlanner.filterNonJoinCondition(comparisonAtomList,scanOperatorListA);
        comparisonAtomList.remove(comparisonAtom6);
        assertEquals(nonJoinCondition,comparisonAtomList);

    }


    @Test
    public void testgetRelevantScanOperator() throws IOException {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);

        final List<ComparisonAtom> comparisonAtomList = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom2);
                add(comparisonAtom3);
                add(comparisonAtom4);
                add(comparisonAtom5);
                add(comparisonAtom6);
                add(comparisonAtom7);
            }
        };

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        /**
         * R(x,r,z) S(x,w,t) T(x,r) r > 1
         * Filtered: R(x,r,z) T(x,r)
         */

        List<ScanOperator> scanOperators = QueryPlanner.getRelevantScanOperator(comparisonAtom8,scanOperatorListA);
        scanOperatorListA.remove(1);
        assertEquals(scanOperators,scanOperatorListA);
    }

    @Test
    public void testgetRelevantScanOperatorMap() throws IOException
    {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);

        final List<ComparisonAtom> comparisonAtomList = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom8);
                add(comparisonAtom9);
                add(comparisonAtom10);
            }
        };

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        /*
        R(x,r,z) S(x,w,t) T(x,r) x > 1, r > 2, w>2, z > 2.
        We should get
         */
        HashMap<ScanOperator,List<ComparisonAtom>> getRelevantScanOperatorMap = QueryPlanner.getRelevantScanOperatorMap(comparisonAtomList,scanOperatorListA);

        final List<ComparisonAtom> testComparisonAtoms = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom8);
                add(comparisonAtom10);
            }
        };

        assertEquals(testComparisonAtoms,getRelevantScanOperatorMap.get(scanOperatorListA.get(0)));
    }


    @Test
    public void testCombinScanSelectionOperator() throws IOException {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        Schema schemaM=schemaHashMap.get("M");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);
        ScanOperator scanOperatorM = new ScanOperator(fileNameT,schemaT,relationalAtomM);

        final List<ComparisonAtom> comparisonAtomList = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom8);
                add(comparisonAtom9);
                add(comparisonAtom10);
                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        final List<ScanOperator> scanOperatorListC = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorM);
            }
        };

        /*
        R(x,r,z) S(x,w,t) T(x,r) x > 1, r > 2, w>2, z > 2.
        We should get
         */

        final List<ComparisonAtom> testComparisonAtoms = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom10);
            }
        };

        HashMap<ScanOperator,List<ComparisonAtom>> getRelevantScanOperatorMap = QueryPlanner.getRelevantScanOperatorMap(testComparisonAtoms,scanOperatorListA);
        List<Operator> operatorList = QueryPlanner.CombinScanSelectionOperator(scanOperatorListA,getRelevantScanOperatorMap);
        Operator firstOperator = operatorList.remove(0);
    }

    @Test
    public void testfilterOutComparisonMultipleRelationalAtoms()throws IOException
    {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        Schema schemaM=schemaHashMap.get("M");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);
        ScanOperator scanOperatorM = new ScanOperator(fileNameT,schemaT,relationalAtomM);

        final List<ComparisonAtom> comparisonAtomList = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom8);
                add(comparisonAtom9);
                add(comparisonAtom10);
                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        final List<ScanOperator> scanOperatorListC = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorM);
            }
        };

        /*
        R(x,r,z) S(x,w,t) T(x,r) x > 1, r > 2, w>2, z > 2.
        We should get
         */

        final List<ComparisonAtom> testComparisonAtoms = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom10);
            }
        };

        final List<ComparisonAtom> check = new ArrayList<ComparisonAtom>()
        {
            {
              add(comparisonAtom11);
              add(comparisonAtom12);
            }
        };

        /**
         * R(x, r, z), S(x, w, t), T(m, r). [x > 1, r > 2, w > 2, z > 2, m > r, x > t]
         * Join Condition: [ m > r, x > t]
         */
        List<ComparisonAtom> joinConditions = QueryPlanner.filterOutComparisonMultipleRelationalAtoms(comparisonAtomList,scanOperatorListC);
        assertEquals(check,joinConditions);
    }

    @Test
    public void testetLastRelationalAtomUtilisingComparison() throws IOException
    {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        Schema schemaM=schemaHashMap.get("M");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);
        ScanOperator scanOperatorM = new ScanOperator(fileNameT,schemaT,relationalAtomM);

        final List<ComparisonAtom> comparisonAtomList = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom8);
                add(comparisonAtom9);
                add(comparisonAtom10);
                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        final List<ScanOperator> scanOperatorListC = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorM);
            }
        };

        /*
        R(x,r,z) S(x,w,t) T(x,r) x > 1, r > 2, w>2, z > 2.
        We should get
         */

        final List<ComparisonAtom> testComparisonAtoms = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom10);
            }
        };

        final List<ComparisonAtom> check = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };
        // In the case of R(x,y,z) S(x,w,t), T(m,r), x>t, this method should return S(x,w,t).
        RelationalAtom relationalAtom =QueryPlanner.getLastRelationalAtom(comparisonAtom12,scanOperatorListC);
        assertEquals(relationalAtom,scanOperatorListC.get(1).getRelationalAtom());

        HashMap<Integer,String> map =new HashMap<>();
    }

    @Test
    public void testconstructJoinConditionMap() throws IOException
    {
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String,Schema> schemaHashMap = databaseCatalog.getSchemaMap();
        Schema schemaR=schemaHashMap.get("R");
        Schema schemaS=schemaHashMap.get("S");
        Schema schemaT=schemaHashMap.get("T");
        Schema schemaM=schemaHashMap.get("M");
        ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
        ScanOperator scanOperatorS = new ScanOperator(fileNameS,schemaS,relationalAtomS);
        ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);
        ScanOperator scanOperatorM = new ScanOperator(fileNameT,schemaT,relationalAtomM);

        final List<ComparisonAtom> comparisonAtomList = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom1);
                add(comparisonAtom8);
                add(comparisonAtom9);
                add(comparisonAtom10);
                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };

        final List<ScanOperator> scanOperatorListA = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };


        final List<ScanOperator> scanOperatorListB= new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorS);
                add(scanOperatorT);
            }
        };

        final List<ScanOperator> scanOperatorListC = new ArrayList<ScanOperator>()
        {
            {
                add(scanOperatorR);
                add(scanOperatorS);
                add(scanOperatorM);
            }
        };

        /*
        R(x,r,z) S(x,w,t) T(x,r) x > 1, r > 2, w>2, z > 2.
        We should get
         */

        final List<ComparisonAtom> testComparisonAtoms = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom10);
            }
        };

        final List<ComparisonAtom> check = new ArrayList<ComparisonAtom>()
        {
            {
                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };

        final List<ComparisonAtom> joinConditions = new ArrayList<ComparisonAtom>()
        {
            {

                add(comparisonAtom11);
                add(comparisonAtom12);
            }
        };

        HashMap<RelationalAtom,List<ComparisonAtom>> testMap1 = QueryPlanner.constructJoinConditionMap(joinConditions,scanOperatorListC);
        Map<RelationalAtom, List<ComparisonAtom>> testMap2 = joinConditions.stream().collect(Collectors.groupingBy(comparisonAtom -> QueryPlanner.getLastRelationalAtom(comparisonAtom, scanOperatorListC)));
        assertEquals(testMap1,testMap2);
        ScanOperator first = scanOperatorListC.get(0);
        ScanOperator second = scanOperatorListC.get(1);
        RelationalAtom relationalAtomfirst=first.getRelationalAtom();
        RelationalAtom relationalAtomsecond =second.getRelationalAtom();
        List<RelationalAtom> relationalAtomList=new ArrayList<>();
        relationalAtomList.add(relationalAtomfirst);
//        System.out.println(relationalAtomfirst);
//        System.out.println(relationalAtomsecond);
//        joinConditions.remove(comparisonAtom11);
//        System.out.println(joinConditions);
//        JoinOperator joinOperator = new JoinOperator(first,second,relationalAtomList,relationalAtomsecond,joinConditions);
//        System.out.println(joinOperator.getNextTuple().getFields());
    }




}
