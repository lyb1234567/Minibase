package ed.inf.adbs.minibase.evaluator;
import ed.inf.adbs.minibase.base.Constant;
import ed.inf.adbs.minibase.base.IntegerConstant;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.StringConstant;
import ed.inf.adbs.minibase.dbStructure.Schema;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScanOperator extends Operator{

    private  String fileName;
    private  RelationalAtom relationalAtom;

    private Schema schema;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public RelationalAtom getRelationalAtom() {
        return relationalAtom;
    }

    public void setRelationalAtom(RelationalAtom relationalAtom) {
        this.relationalAtom = relationalAtom;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    private BufferedReader bufferedReader;

    /**
     * The Scan Opeartor will first check if the input filename is valid. If so, it will then initialize filename, schema and relationAtom
     * @param fileName
     * @param schema
     * @param relationalAtom
     * @throws FileNotFoundException it will throw a filenotfoundexcpetion
     */
    public ScanOperator(String fileName,Schema schema,RelationalAtom relationalAtom) throws FileNotFoundException
    {
        boolean isValidFile = isFileValid(fileName,schema);

        if (! isValidFile)
        {
            throw new IllegalArgumentException("The input file is invalid!!!");
        }

        BufferedReader bufferedReader= new BufferedReader(new FileReader(fileName));
        this.fileName=fileName;
        this.schema = schema;
        this.relationalAtom = relationalAtom;
        this.bufferedReader = bufferedReader;
    }

    /**
     * For each line of database file, the it will parse each tuple separated by comma. It will check if the number of tuples match the size of list of data type in schema
     * For example, if we have a schema : R int int, while we scan a tuple like 1, 9, 'adbs', then it doesn't match
     * @param tupleLine tuple line is a string of elemnts
     * @return return the form this tuple line
     */
    public Tuple scanTuple(String tupleLine)
    {
        String [] tupleLineSplit = tupleLine.split(",");
        int datTypeListSize = schema.getDataTypes().size();
        int tupleSize = tupleLineSplit.length;

        if (datTypeListSize !=tupleSize)
        {
            throw new IllegalArgumentException("The number of tuple elements doesn't match the size of data type list in schema");
        }
        List <Constant> addList=new ArrayList<>();
        for(int i=0;i<tupleSize;i++)
        {
            String schemaElement =schema.getDataTypes().get(i);
            if (schemaElement.equals("int"))
            {
                IntegerConstant tupleConstant = new IntegerConstant(Integer.parseInt(tupleLineSplit[i].trim()));
                addList.add(tupleConstant);
            }
            else if (schemaElement.equals("string"))
            {
                StringConstant tupleConstant = new StringConstant(tupleLineSplit[i].trim());
                addList.add(tupleConstant);
            }
        }
        return new Tuple(addList);
    }

    /**
     * This method is used to scan the nextline of tuple using the buffer reader
     * @return it will return a tuple object
     * @throws IOException
     */

    @Override
    public Tuple getNextTuple() throws IOException {
        String nextLine = this.bufferedReader.readLine();
        if (nextLine != null) {
            return this.scanTuple(nextLine);
        }
        else
        {
            return null;
        }

    }


    /**
     * Reset the the buffer read to read the next line
     */
    @Override
    public void reset() {
        try {
            this.bufferedReader = new BufferedReader(new FileReader(this.fileName));
        }
        catch (FileNotFoundException ex) {
            throw new RuntimeException(" The file is not found!!!");
        }
    }


    /**
     * This method is used to check if the input filenmae is valid: 1. It should match the name of schema 2.It should be a csv file
     * 3. It must be contained in the dir called files

     * @param fileName take the filename as input
     * @param schema take the schema to compare
     * @return return a boolean value to check
     */
    public boolean isFileValid(String fileName, Schema schema)
    {
        // get the schema name
        String shcemaName = schema.getName();

        // creat the file
        File file= new File(fileName);

        //check if it is a valid file
        boolean isFile = file.isFile();

        // check if it is a csv file
        int dotIndex = file.getName().lastIndexOf(".");
        String extension =  file.getName().substring(dotIndex+1);
        String relationName =  file.getName().substring(0,dotIndex);

        boolean isCsv= extension.equals("csv");

        // check if the csv file is contained in the directory called files
        boolean isContainedFiles = file.getParentFile().getName().equals("files");
        boolean isValid = isFile && isCsv && relationName.equals(schema.getName()) && isContainedFiles;
        return (isFile && isCsv && relationName.equals(schema.getName()) && isContainedFiles);
    }

}
