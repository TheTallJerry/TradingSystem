package core.account;

/**
 * A function that takes objects of type E and gives boolean (i.e it evaluates the object to true/false), this is
 * used in <code>getAccounts</code> for account querying.
 *
 * @param <E> the type of object to be taken in.
 */
public interface Criterion<E> {
    /**
     * Takes objects of type E and gives boolean (i.e it evaluates the object to true/false), this is used in
     * <code>getAccounts</code> for account querying.
     *
     * @param e the type of object to be taken in.
     * @return true iff the expression (upon implementation) related to the passed
     * in Object <code>e</code> evaluates to true. Return false otherwise.
     */
    boolean accepts(E e);
}
