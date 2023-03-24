package ed.inf.adbs.minibase.base;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.Tuple;
import ed.inf.adbs.minibase.evaluator.*;
import ed.inf.adbs.minibase.parser.QueryParser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static ed.inf.adbs.minibase.evaluator.JoinOperator.subFromLeftandRight;
import static org.junit.Assert.*;

public class SumOperatorTest {

    private  Variable variablex = new Variable("x");
    private  Variable variabley = new Variable("y");
    private  Variable variablez = new Variable("z");
    private  Variable variablew = new Variable("w");
    private  Variable variablet = new Variable("t");
    private  Variable variabler = new Variable("r");

    private  Variable variablem = new Variable("m");

    IntegerConstant integerConstant1 = new IntegerConstant(1);
    IntegerConstant integerConstant2 = new IntegerConstant(2);
    IntegerConstant integerConstant3 = new IntegerConstant(3);
    IntegerConstant integerConstant4 = new IntegerConstant(4);
    IntegerConstant integerConstant5 = new IntegerConstant(5);
    IntegerConstant integerConstant6 = new IntegerConstant(6);
    IntegerConstant integerConstant7 = new IntegerConstant(7);
    IntegerConstant integerConstant8 = new IntegerConstant(8);

    IntegerConstant integerConstant9 = new IntegerConstant(9);

    StringConstant stringConstant1 = new StringConstant("adbs");

    final List<String> dataTypesR =new ArrayList<String>()
    {
        {
            add("int");
            add("int");
            add("string");
        }
    };

    final List<String> dataTypesS =new ArrayList<String>()
    {
        {
            add("int");
            add("string");
            add("int");
        }
    };

    final List<String> dataTypesT =new ArrayList<String>()
    {
        {
            add("int");
            add("int");
        }
    };

    private  String fileNameR="."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"R.csv";

    private  String fileNameS="."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"S.csv";
    private  String fileNameT="."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"T.csv";
    Schema schemaR = new Schema("R",dataTypesR);
    Schema schemaS = new Schema("S",dataTypesS);
    Schema schemaT = new Schema("T",dataTypesT);



    private final List<Term> termListR = new ArrayList<Term>()
    {
        {
            add(variablex);
            add(variabley);
            add(variablez);
        }
    };

    private final List<Term> termListT = new ArrayList<Term>()
    {
        {
            add(variablex);
            add(variabler);
        }
    };


    private  final RelationalAtom relationalAtomR = new RelationalAtom("R",termListR);

    private  final RelationalAtom relationalAtomT = new RelationalAtom("T",termListT);

    private final List<RelationalAtom> relationalAtomList=new ArrayList<RelationalAtom>()
    {
        {
          add(relationalAtomR);
          add(relationalAtomT);
        }
    };

    private  final List<Constant> constantList=new ArrayList<Constant>()
    {
        {
          add(integerConstant1);
          add(integerConstant9);
          add(stringConstant1);
          add(integerConstant1);
          add(integerConstant1);
        }
    };

    ScanOperator scanOperatorR = new ScanOperator(fileNameR,schemaR,relationalAtomR);
    ScanOperator scanOperatorT = new ScanOperator(fileNameT,schemaT,relationalAtomT);
    Tuple combinedTuple = new Tuple(constantList);

    final List<RelationalAtom> relationalAtomListR=new ArrayList<RelationalAtom>()
    {
        {
            add(relationalAtomR);
        }

    };


    final List<Variable> groupByVariable= new ArrayList<Variable>()
    {
        {
            add(variablex);
            add(variabley);
        }

    };

    final List<Term> productTermTest1 =new ArrayList<Term>()
    {
        {
          add(integerConstant2);
          add(variabler);
        }
    };

    final List<Term> productTermTest2 =new ArrayList<Term>()
    {
        {
            add(integerConstant2);
        }
    };

    final List<Term> productTermTest3 =new ArrayList<Term>()
    {
        {
            add(variablex);
        }
    };

    final List<Term> productTermTest4 =new ArrayList<Term>()
    {
        {
            add(variablex);
            add(variabley);
        }
    };
    SumAggregate sumAggregate1 = new SumAggregate(productTermTest1);

    SumAggregate sumAggregate2 = new SumAggregate(productTermTest2);

    SumAggregate sumAggregate3 = new SumAggregate(productTermTest3);

    SumAggregate sumAggregate4 = new SumAggregate(productTermTest4);

    JoinOperator joinOperator = new JoinOperator(scanOperatorR,scanOperatorT,relationalAtomListR,relationalAtomT,new ArrayList<>());
    public SumOperatorTest() throws FileNotFoundException {
    }


    @Test
    public void testextractConstantRelationAtoms() throws IOException {

        Constant constantTest1 = SumOperator.extractConstVaribaleRelationalAtom(combinedTuple,variablex,relationalAtomList);
        assertEquals(constantTest1,integerConstant1);

        Constant constantTest2 = SumOperator.extractConstVaribaleRelationalAtom(combinedTuple,variabley,relationalAtomList);
        assertEquals(constantTest2,integerConstant9);

        Tuple nextTuple = joinOperator.getNextTuple();
        while(nextTuple!=null)
        {
            System.out.println(nextTuple.getFields());
            nextTuple=joinOperator.getNextTuple();
        }
    }
    @Test
    public void TestConstructMap1() throws IOException {
        System.out.println(sumAggregate1);
        SumOperator sumOperator = new SumOperator(joinOperator,groupByVariable,sumAggregate1);
        System.out.println(sumOperator.getGroupByHashMap());
        System.out.println("----------------------------------------------------------------");
    }

    @Test
    public void TestConstructMap2() throws IOException {
        System.out.println(sumAggregate2);
        SumOperator sumOperator = new SumOperator(joinOperator,groupByVariable,sumAggregate2);
        System.out.println(sumOperator.getGroupByHashMap());
        System.out.println("----------------------------------------------------------------");
    }


    @Test
    public void TestConstructMap3() throws IOException {
        System.out.println(sumAggregate3);
        SumOperator sumOperator = new SumOperator(joinOperator,groupByVariable,sumAggregate3);
        System.out.println(sumOperator.getGroupByHashMap());
        System.out.println("----------------------------------------------------------------");
    }

    @Test
    public void TestConstructMap4() throws IOException {
        System.out.println(sumAggregate4);
        SumOperator sumOperator = new SumOperator(joinOperator,groupByVariable,sumAggregate4);
        System.out.println(sumOperator.getGroupByHashMap());
        System.out.println("----------------------------------------------------------------");
    }
}
