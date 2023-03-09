package ed.inf.adbs.minibase.evaluator;
import ed.inf.adbs.minibase.dbStructure.Tuple;

import java.io.FileWriter;
import java.io.IOException;
public abstract class Operator {
    /**
     * Abstract method for Opeartor to return next tuple
     * @return it will return next tuple
     */
    public abstract Tuple getNextTuple();

    /**
     * This abstract reset method is used to reset the sate of the operator
     */
    public abstract void reset();

}
