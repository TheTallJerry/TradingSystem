package genericdatatype;

import java.io.Serializable;

/**
 * Represents a generic data type that holds a pair of values.
 */
public class Pair<A, B> implements Serializable {

    /**
     * the first slot of the pair.
     */
    public A value1;

    /**
     * the second slot of the pair.
     */
    public B value2;

    /**
     * Constructs a Pair instance with input <code>value1</code> and <code>value2</code>, and sets the inputs to the corresponding slot.
     *
     * @param value1 value in the first slot of the pair.
     * @param value2 value in the second slot of the pair.
     */
    public Pair(A value1, B value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    /**
     * .equals() method for <code>Pair</code>, return true if and only if the given <code>Object</code> is a instance
     * of <code>Pair</code> and its <code>value1</code> and <code>value2</code> are equal to this <code>Pair</code>
     *
     * @param obj the given <code>Object</code>
     * @return boolean value which indicate whether the given <code>obj</code> is <code>Pair</code> or not, if it is,
     * does it equal to this <code>Pair</code> or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        if ((value1 == null && pair.value1 != null) || (value2 == null && pair.value2 != null))
            return false;
        if ((pair.value1 == null && value1 != null) || (pair.value2 == null && value2 != null))
            return false;
        return ((Pair<?, ?>) obj).value1.equals(value1) && ((Pair<?, ?>) obj).value2.equals(value2);
    }

    /**
     * Override the .hashCode() method.
     *
     * @return int of hashCode of this Pair
     */
    @Override
    public int hashCode() {
        return (value1 == null ? 0 : value1.hashCode()) + (value2 == null ? 0 : value2.hashCode());
    }
}