package ed.inf.adbs.minibase.evaluator;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.*;

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
    public void dump(String relName) throws IOException {
        Tuple nextTuple = getNextTuple();
        String writename = relName+".csv";
        // Write to a file
        try
        {
            String fileName = "."+ File.separator+"data"+File.separator+"evaluation"+File.separator+"db"+File.separator+"test"+File.separator+writename;
            File file = new File(fileName);
            PrintStream out = new PrintStream(fileName);
            while (nextTuple !=null)
            {
                String result="";
                System.out.println(nextTuple.getFields());
                for(int i=0;i<nextTuple.getFields().size();i++)
                {
                    if( i == nextTuple.getFields().size()-1)
                    {
                        result=result+" "+nextTuple.getFields().get(i);
                    }
                    else
                    {
                        result=result+" "+nextTuple.getFields().get(i)+",";
                    }
                }
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
