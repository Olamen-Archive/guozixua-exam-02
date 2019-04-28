package problem3;

import java.io.PrintWriter;
import java.util.Iterator;

import utils.MiscUtils;
import utils.Pair;
import utils.SimpleMap;
import utils.SimpleStack;


/**
 * A simple implementation of tries.
 */
public class Trie
    implements SimpleMap<String, String>, Iterable<Pair<String, String>> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The root of the trie.
   */
  TrieNode root;

  /**
   * The number of pairs in the tree.
   */
  int size;

  /**
   * The cached value for remove and set operation
   */
  String cachedValue;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty, trie.
   */
  public Trie() {
    this.root = new TrieNode();
    this.size = 0;
  } // Trie()

  // +-------------------+-------------------------------------------
  // | SimpleMap methods |
  // +-------------------+

  public String set(String key, String value) {
    if (key == null) {
      throw new NullPointerException("key is null");
    }

    cachedValue = null;
    this.root = set(root, key, value, 0);
    return cachedValue;
  } // set(String,String)

  private TrieNode set(TrieNode node, String key, String value, int index) {
    if (node == null) {
      node = new TrieNode();
    }
    if (key.length() == index) {
      if (node.hasKey()) {
        cachedValue = node.value();
      }
      node.setContents(key, value);
      return node;
    }

    char ch = key.charAt(index);
    node.setNext(ch, set(node.next(ch), key, value, index + 1));

    return node;
  }

  public String get(String key) {
    if (key == null) {
      throw new NullPointerException("key is null");
    }

    TrieNode node = find(key);
    if (node == null || !node.hasKey()) {
      throw new IndexOutOfBoundsException("key not found");
    }

    return node.key();
  } // get(String)

  public int size() {
    return this.size;
  } // size()

  public boolean containsKey(String key) {
    return find(key) != null;
  } // containsKey(String)

  public String remove(String key) {
    cachedValue = null;
    this.root = remove(root, key, 0);
    return cachedValue;
  } // remove(String)

  private TrieNode remove(TrieNode node, String key, int index) {
     if (node == null) {
       return null;
     }

     if (key.length() == index) {
       /* clean up the node */
       if (node.value() != null) {
         cachedValue = node.value();
       }
       node.setContents(null, null);
     } else {
       char ch = key.charAt(index);
       node.setNext(ch, remove(node.next(ch), key, index + 1));
     }

     if (node.isEmpty()) {
       return null;
     }

     return node;
  }

  /**
   * Iterate all the keys in the tree, returning them in alphabetical order by
   * key.
   */
  public Iterator<String> keys() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.key());
  } // keys()

  /**
   * Iterate all the values in the tree, returning them in some undetermined
   * order.
   */
  public Iterator<String> values() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.value());
  } // keys()

  /**
   * Dump the trie.
   */
  public void dump(PrintWriter pen) {
    dump(pen, this.root, "");
  } // dump(PrintWriter)

  // +------------------+--------------------------------------------
  // | Iterable methods |
  // +------------------+

  /**
   * Iterate all the key/value pairs in the tree, returning them in alphabetical
   * order by key.
   */
  public Iterator<Pair<String, String>> iterator() {
    return new Iterator<Pair<String, String>>() {
      /**
       * A stack that will store all of the nodes that have keys.
       */
      SimpleStack<TrieNode> remaining;

      /**
       * Have we initialized the iterator? See checkInit().
       */
      boolean initialized = false;

      public boolean hasNext() {
        checkInit();
        return !remaining.isEmpty();
      } // hasNext()

      public Pair<String, String> next() {
        checkInit();
        TrieNode temp = remaining.get();
        addChildren(temp);
        return temp.contents;
      } // next()

      /**
       * Add all the children of a node to the stack.
       */
      void addChildren(TrieNode node) {
        // Sanity check
        if (node == null) {
          // Do nothing.
        } // if
        for (char ch = 'z'; ch >= 'a'; ch--) {
          addNode(node.next(ch));
        } // for
      } // addChildren(TrieNode)

      /**
       * Add a node to the stack.
       */
      void addNode(TrieNode node) {
        if (node == null) {
          // Do nothing; nothing to add
        } // No node
        // Nodes that have keys belong
        else if (node.hasKey()) {
          remaining.put(node);
        } // It belongs
        // If it doesn't have a key, add all the children.
        else {
          addChildren(node);
        } // if/else
      } // addNode

      /**
       * Make sure that the iterator is initialized.
       */
      void checkInit() {
        if (initialized) {
          return;
        }
        initialized = true;
        remaining = new SimpleStack<TrieNode>();
        addNode(Trie.this.root);
      } // checkInit()

    }; // new Iterator
  } // iterator();

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Dump the subtrie rooted at node, indented by the specified indent.
   */
  void dump(PrintWriter pen, TrieNode node, String indent) {
    if (node == null) {
      return;
    } // if (node == null)
    for (char ch = 'A'; ch <= 'Z'; ch++) {
      TrieNode next = node.next(ch);
      if (next != null) {
        String prefix = indent + ch;
        Pair<String, String> pair = next.contents;
        if ((pair != null) && (pair.key() != null)) {
          pen.println(prefix + ": " + pair);
        } else {
          pen.println(prefix);
        } // if/else)
        dump(pen, next, indent + ch + "-");
      } // if
    } // for
  } // dump(PrintWriter, TrieNode, String)

  /**
   * Find the node associated with a key. Returns null if the node is not found.
   */
  TrieNode find(String key) {
    int len = key.length();
    TrieNode current = this.root;
    for (int i = 0; i < len; i++) {
      char ch = key.charAt(i);
      if (current == null) {
        return null;
      }
      current = current.next(ch);
    } // for
    return current;
  } // find

} // class Trie


/**
 * Nodes for our trie.
 */
class TrieNode {
  /**
   * The key and value of the node.
   */
  Pair<String, String> contents;

  /**
   * All of the next nodes.
   */
  private TrieNode[] next;

  /**
   * Radix of R-trie
   */
  public final static int RADIX = 27;

  /**
   * Create a trie node with a specified key and value.
   */
  public TrieNode(String key, String value) {
    if (key == null) {
      this.contents = null;
    } else {
      this.contents = new Pair<String, String>(key, value);
    } // if/else
    this.next = new TrieNode[RADIX];
    // The loop is probably not necessary, but I like to be careful.
    for (int i = 0; i < RADIX; i++) {
      this.next[i] = null;
    } // for
  } // TrieNode(String, String)

  /**
   * Create a trie node with no key.
   */
  public TrieNode() {
    this(null, null);
  } // TrieNode()

  /**
   * Determine if this node has a key.
   */
  public boolean hasKey() {
    return ((this.contents != null) && (this.contents.key() != null));
  } // hasKey()

  /**
   * Get the key in the node.
   */
  public String key() {
    return this.contents.key();
  } // key()

  /**
   * Get the next node for a particular letter.
   */
  public TrieNode next(char ch) {
    return this.next[index(ch)];
  } // next(char)

  /**
   * Set the contents of the node.
   */
  public void setContents(String key, String value) {
    this.contents = new Pair<String, String>(key, value);
  } // setContents(String, String)

  /**
   * Set the next node for a particular letter.
   */
  public void setNext(char ch, TrieNode next) {
    this.next[index(ch)] = next;
  } // setNext(char, TrieNode)

  /**
   * Set the value in the node. The node must have a key for this to work.
   */
  public void setValue(String value) {
    this.contents = new Pair<String, String>(this.contents.key(), value);
  } // setValue()

  /**
   * Get the value in the node.
   */
  public String value() {
    return this.contents.value();
  } // value()

  /**
   * Check if the node is empty (therefore safe to delete)
   * @return true if empty
   */
  public boolean isEmpty() {
    if (this.hasKey()) {
      return false;
    }

    for (int i = 0; i < RADIX; i++) {
      if (this.next[i] != null) {
        return false;
      }
    }

    return true;
  }

  /**
   * Convert a character into an index in the next array.
   */
  private int index(char ch) {
    // Lowercase letters
    if ((ch >= 'a') && (ch <= 'z')) {
      return ch - 'a';
    } // lowercase

    // Uppercase letters
    else if ((ch >= 'A') && (ch <= 'Z')) {
      return ch - 'A';
    } // uppercase

    // Everything else is unsupported and maps to 26
    else {
      return 26;
    } // if/else
  } // index(char)
} // class TrieNode
