ASSIGNMENT 3: B-TREES with DUPLICATES



Goal: The goal of this assignment is to get some practice with binary search trees, specifically B-Trees. B-Trees are one of the most important data structures we will study in this class – they are regularly used in large database systems for storing indexes on the records. However, just for fun, we will make one significant departure from standard B-trees. We will allow duplicate keys, i.e., a tree can have an unbounded number of keys that have the same value.




Problem Statement: Your task is to implement a generic DuplicateBTree class. As is to be expected, DuplicateBTree must implement the BTree ADT we studied in class, except that it can contain multiple copies of the same key (at times with exactly the same value). This implies you will have to change the algorithms for searching, inserting and deleting suitably. The exact interface will be as follows:

public interface DuplicateBTree<Key, Value> {

public BTree(int b) throws bNotEvenException; /* Initializes an empty b-tree. Assume b is even. */

public boolean isEmpty(); /* Returns true if the tree is empty. */ public int size(); /* Returns the number of key-value pairs */ public int height(); /* Returns the height of this B-tree */

public Vector<Value> search(Key key) throws IllegalKeyException; /* Returns all values associated with a given key in a vector */

public void insert(Key key, Value val); /* Inserts the key-value pair */ public void delete(Key key) throws IllegalKeyException; /* Deletes all

occurrences of key */
public String toString(); /* Prints all the tree in the format listed below */

}

Your implementation will have to define a node class which will maintain all keys in the node (you may use an array for this), and references to all children. The format for toString function defined above is as follows:

To convert a tree to string, just convert its root to string. Converting a node to string is defined recursively. Nodes that are null, are mapped to empty strings. The mapping of a node, is just the concatenation of the mappings of its children, separated by the key value pairs present at the node, and surrounded by rectangular brackets []. For example, neglecting the values,
















will be converted as:

”[[1, 2, 3], 4, [6, 7, 9], 10, [11, 12, 15], 16, [17, 22, 36]]”
Now, we have key value pairs instead of simple set of keys, we just replace a single key output in the above string by the key value pair. For example, if in the above tree, the values were the string representations of the numbers, then the answer would be:

”[[1=One, 2=Two, 3=Three], 4=Four, [........”
