package ed.inf.adbs.minibase.base;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.Tuple;
import ed.inf.adbs.minibase.evaluator.JoinOperator;
import ed.inf.adbs.minibase.evaluator.ScanOperator;
import ed.inf.adbs.minibase.evaluator.SelectionOperator;
import ed.inf.adbs.minibase.parser.QueryParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ed.inf.adbs.minibase.evaluator.JoinOperator.subFromLeftandRight;
import static org.junit.Assert.*;

public class JoinOperatorTests {


    // Used variables

    private  Variable variablex=new Variable("x");
    private  Variable variabley=new Variable("y");
    private  Variable variablez=new Variable("z");
    private  Variable variabler=new Variable("r");
    private  Variable variablea=new Variable("a");
    private  Variable variableb=new Variable("b");
    private  Variable variablec=new Variable("c");



    // Used Integer Constants
    private  IntegerConstant integerConstant11= new IntegerConstant(11);
    private  IntegerConstant integerConstant12= new IntegerConstant(12);
    private  IntegerConstant integerConstant13= new IntegerConstant(13);
    private  IntegerConstant integerConstant14= new IntegerConstant(14);
    private  IntegerConstant integerConstant5= new IntegerConstant(5);

    private  IntegerConstant integerConstant2= new IntegerConstant(2);


    // Used String Constants
    private  StringConstant stringConstant1= new StringConstant("abc");
    private  StringConstant stringConstant2= new StringConstant("xyz");
    private  StringConstant stringConstant3= new StringConstant("name");
    private  StringConstant stringConstant4= new StringConstant("Mike");


    // Used RelationalAtoms
    private final List<Term> variableTermsR = new ArrayList<Term>() {{
        add(variablex);
        add(variabley);
        add(variablez);
    }};


    private  final List<Term> variableTermsS=new ArrayList<Term>() {{
        add(variablea);
        add(variableb);
        add(variablec);
    }};



    private  final List<Term> variableTermsT=new ArrayList<Term>() {{
        add(variablex);
        add(variabler);
    }};

    private  RelationalAtom relationalAtomR = new RelationalAtom("R",variableTermsR);
    private  RelationalAtom relationalAtomS = new RelationalAtom("S",variableTermsS);


    private  RelationalAtom relationalAtomT = new RelationalAtom("T",variableTermsT);



    private final List<RelationalAtom> relationalAtomList = new ArrayList<RelationalAtom>()
    {
        {
            add(relationalAtomR);
            add(relationalAtomS);
        }
    };

    private final List<Constant> constantListLeft = new ArrayList<Constant>(){
        {
            add(integerConstant12);
            add(integerConstant12);
            add(stringConstant1);
            add(integerConstant13);
            add(integerConstant12);
            add(stringConstant2);
        }
    };

    private final  List<Constant> constantListRight= new ArrayList<Constant>(){{
        add(integerConstant12);
        add(integerConstant12);
    }};

    // Test Left Tuple
    private final Tuple LeftTuple = new Tuple(constantListLeft);

    // Test Right Tuple
    private final Tuple RightTuple = new Tuple(constantListRight);

    // Used Comparison Atoms
    private  final ComparisonAtom comparisonAtom1 = new ComparisonAtom(variablex,integerConstant11,ComparisonOperator.GT);
    private  final ComparisonAtom comparisonAtom2 = new ComparisonAtom(variablex,stringConstant2,ComparisonOperator.EQ);
    private  final ComparisonAtom comparisonAtom3 = new ComparisonAtom(variablex,stringConstant3,ComparisonOperator.LT);
    private  final ComparisonAtom comparisonAtom4 = new ComparisonAtom(variablex,stringConstant4,ComparisonOperator.LEQ);

    private  final ComparisonAtom comparisonAtom5 = new ComparisonAtom(variabley,integerConstant2,ComparisonOperator.GT);


    @Test
    public void testpassesSelectionPredicatesRelationalAtomLists() throws IOException {
        Query query1 = QueryParser.parse("Q(x,y,z) :- R(x, y, z)");
        Query query2 = QueryParser.parse("Q(x,r) :- T(x, r)");
        Query query3 = QueryParser.parse("Q(a,b,c) :- S(a, b, c)");
        String schemaFilePath = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"schema.txt";
        DatabaseCatalog databaseCatalog = DatabaseCatalog.getCatalog();
        databaseCatalog.constructSchemaMap(schemaFilePath);
        HashMap<String, Schema> SchemaMap = databaseCatalog.getSchemaMap();
        Schema testSchema1 = SchemaMap.get("R");
        Schema testSchema2 = SchemaMap.get("T");
        Schema testSchema3 = SchemaMap.get("S");
        RelationalAtom relationalAtom1 = (RelationalAtom) query1.getBody().get(0);
        RelationalAtom relationalAtom2 = (RelationalAtom) query2.getBody().get(0);
        RelationalAtom relationalAtom3 = (RelationalAtom) query3.getBody().get(0);
        String fileName1="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"R.csv";
        String fileName2="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"T.csv";
        String fileName3="."+File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"files"+File.separator+"S.csv";

        ScanOperator scanOperator_1 = new ScanOperator(fileName1,testSchema1,relationalAtom1);
        ScanOperator scanOperator_2 = new ScanOperator(fileName2,testSchema2,relationalAtom2);
        ScanOperator scanOperator_3 = new ScanOperator(fileName3,testSchema3,relationalAtom3);

//        Tuple tuple1=scanOperator_1.getNextTuple();
//        Tuple tuple2=scanOperator_2.getNextTuple();
//        Tuple tuple3=scanOperator_3.getNextTuple();
        List<Constant> leftConstant = new ArrayList<>();
//        leftConstant.addAll(tuple1.getFields());
//        leftConstant.addAll(tuple2.getFields());
        Tuple leftTuple=new Tuple(leftConstant);
        RelationalAtom rightRelationAtom= relationalAtom3;
//        Tuple rightTuple= tuple2;
        ComparisonAtom testComparison=new ComparisonAtom(variablex,integerConstant5,ComparisonOperator.GT);
        List<RelationalAtom> leftRelationalAtoms=new ArrayList<>();
        leftRelationalAtoms.add(relationalAtom1);

        List<ComparisonAtom>comparisonAtoms=new ArrayList<ComparisonAtom>();
        // Left Combined Tuples: 1, 9, 'adbs', 1, 1
        // Right Tuple: 1,1
        // Left RelationalAtoms: [R(x, y, z), T(x, r)]
        // Right RelationalAtom: S(a, b, c)
//        assertTrue(JoinOperator.passesSelectionPredicatesRelationalAtomLists(leftTuple,rightTuple,leftRelationalAtoms,rightRelationAtom,comparisonAtoms));
        comparisonAtoms.add(comparisonAtom5);
        System.out.println(scanOperator_1.getRelationalAtom());
        System.out.println(scanOperator_2.getRelationalAtom());
        SelectionOperator selectionOperator_1 = new SelectionOperator(relationalAtom1,scanOperator_1,comparisonAtoms);
        SelectionOperator selectionOperator_2 = new SelectionOperator(relationalAtom2,scanOperator_2,comparisonAtoms);
        comparisonAtoms.remove(comparisonAtom5);
        JoinOperator joinOperator= new JoinOperator(selectionOperator_1,selectionOperator_2,leftRelationalAtoms,relationalAtom2,comparisonAtoms);
        joinOperator.dump("crossProductTest3","join","txt");
        System.out.println(comparisonAtoms);

    }

    @Test
    public void testPassSinglePredicateAccrossRelationAtoms()
    {
        /**
         * Left Tuple: 12, 12, 'abc', 13, 12, 'xyz'
         * Right Tuple : 12 ,12
         * LeftRelationAtoms: [R(x, y, z), S(a, b, c)]
         * RightRelationalAtom: T(x, r)
         */
        boolean check = JoinOperator.passSinglePredicate(LeftTuple,RightTuple,comparisonAtom1,relationalAtomList,relationalAtomT);
        assertTrue(check);
    }
    
    
    @Test
    public void testsubFromLeftandRight()
    {
        ComparisonAtom comparisonAtom = subFromLeftandRight(comparisonAtom1,LeftTuple,RightTuple,relationalAtomList,relationalAtomT);
        ComparisonAtom testComparisonAtom = new ComparisonAtom(integerConstant12,integerConstant11,ComparisonOperator.GT);
        assertEquals(comparisonAtom,testComparisonAtom);
    }

}
