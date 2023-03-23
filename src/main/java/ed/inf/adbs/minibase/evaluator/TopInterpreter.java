package ed.inf.adbs.minibase.evaluator;

import ed.inf.adbs.minibase.base.Query;
import ed.inf.adbs.minibase.dbStructure.DatabaseCatalog;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.Tuple;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class TopInterpreter extends Operator {


    private  Operator childOperator;
    private  String databaseDir;

    private  String inputFile;
    private  String OutFile;
    private HashMap<String,Schema> schemaHashMap;

    @Override
    public Tuple getNextTuple() throws IOException {
        return childOperator.getNextTuple();
    }

    public TopInterpreter(Operator childOperator) throws IOException {
        this.childOperator=childOperator;
    }

    @Override
    public void reset() {
    }

}
