package ed.inf.adbs.minibase.evaluator;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.*;
import java.util.stream.Collectors;

public abstract class Operator {
    /**
     * Abstract method for Opeartor to return next tuple
     * @return it will return next tuple
     */
    public abstract Tuple getNextTuple() throws IOException;

    /**
     * This abstract reset method is used to reset the sate of the operator
     */
    public abstract void reset();

    /**
     * This dump method is used to check print of different operators, it will call different getnext tuples for different operators that extend Operators
     * @param relName
     * @throws IOException
     */
    public void dump(String relName,String OutFile,String extension) throws IOException {
        Tuple nextTuple = getNextTuple();
        String writename = relName+"."+extension;
        // Write to a file
        try
        {
            String fileName = OutFile;
            File file = new File(fileName);
            PrintStream out = new PrintStream(fileName);
            while (nextTuple !=null)
            {
                String result=nextTuple.getFields().stream().map(Object::toString).collect(Collectors.joining(", "));
                out.println(result);
                nextTuple=getNextTuple();
            }
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
