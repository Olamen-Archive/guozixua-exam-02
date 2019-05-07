package problem1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import utils.MiscUtils;
import utils.Pair;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * Some tests of our simple immutable BSTs.
 *
 * Note that it is comparatively easy to write something that meets these tests
 * but does not correctly implement immutable BSTs. The tests assume good faith
 * on the part of the tree implementation.
 */
class SimpleImmutableBSTTests {

  // +--------------+------------------------------------------------
  // | Useful lists |
  // +--------------+

  /**
   * A word list stolen from some tests that SamR wrote in the distant past.
   */
  static final String[] words = {"aardvark", "anteater", "antelope", "bear",
      "bison", "buffalo", "chinchilla", "cat", "dingo", "elephant", "eel",
      "flying squirrel", "fox", "goat", "gnu", "goose", "hippo", "horse",
      "iguana", "jackalope", "kestrel", "llama", "moose", "mongoose", "nilgai",
      "orangutan", "opossum", "red fox", "snake", "tarantula", "tiger",
      "vicuna", "vulture", "wombat", "yak", "zebra", "zorilla"};

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * Are we observing steps? (Sometimes print statements are better than a
   * debugger.)
   */
  static final boolean OBSERVE = false;

  /**
   * Key/value pairs for the current test.
   */
  Pair<String, Integer>[] pairs;

  /**
   * Trees for the current test.
   */
  SimpleImmutableBST<String, Integer>[] trees;

  /**
   * When we want to dump stuff
   */
  PrintWriter pen = new PrintWriter(System.out, true);

  // +-------+-------------------------------------------------------
  // | Setup |
  // +-------+

  /**
   * Prepare the key and value arrays.
   */
  @SuppressWarnings("unchecked")
  @BeforeEach
  void setup() {
    Random rand = new Random();
    // Set up the array of key/value pairs
    pairs = (Pair<String, Integer>[]) new Pair[words.length];
    for (int i = 0; i < pairs.length; i++) {
      pairs[i] = new Pair<String, Integer>(words[i], rand.nextInt(100));
    } // for
    MiscUtils.randomlyPermute(pairs);
  } // setup()


  // +-------+-------------------------------------------------------
  // | Tests |
  // +-------+

  /**
   * Check that the new tree is empty.
   */
  @Test
  void testEmpty() {
    SimpleImmutableBST<String, Integer> tree =
        new SimpleImmutableBST<String, Integer>();
    assertEquals(0, tree.size());
  } // testEmpty()

  /**
   * Test that setting works correctly.
   */
  @SuppressWarnings("unchecked")
  @Test
  void testSet() {
    // Set up an array of trees of gradually increasing size
    trees = new SimpleImmutableBST[pairs.length + 1];
    SimpleImmutableBST<String, Integer> bst =
        new SimpleImmutableBST<String, Integer>();
    checkTree(bst, 0);
    trees[0] = bst;
    for (int i = 0; i < pairs.length; i++) {
      bst = bst.set(pairs[i].key(), pairs[i].value());
      checkTree(bst, i + 1);
      trees[i + 1] = bst;
    } // for

    // Recheck all the trees to make sure that none were mutated.
    checkTrees();
  } // testSet()

  /**
   * Test that setting works correctly.
   */
  @SuppressWarnings("unchecked")
  @Test
  void testRemove() {
    if (OBSERVE) {
      pen.println("Using " + Arrays.toString(pairs));
    } //if
    // Build a tree with all of the keys.
    SimpleImmutableBST<String, Integer> bst =
        new SimpleImmutableBST<String, Integer>();
    for (int i = 0; i < pairs.length; i++) {
      bst = bst.set(pairs[i].key(), pairs[i].value());
    } // for
    checkTree(bst, pairs.length);
    
    // Fill in the trees array by gradually removing elements from
    // the tree.
    MiscUtils.randomlyPermute(pairs);
    trees = new SimpleImmutableBST[pairs.length + 1];
    trees[pairs.length] = bst;
    if (OBSERVE) {
      pen.println("Original tree");
      bst.dump(pen);
    } // if
    for (int i = pairs.length - 1; i >= 0; i--) {
      String key = pairs[i].key();
      if (OBSERVE) {
        pen.println();
        pen.println("Removing " + key);
      } // if
      bst = bst.remove(key);
      trees[i] = bst;
      if (OBSERVE) {
        bst.dump(pen);
      } // if

      checkTree(bst, i);
    } // for

    // Make sure that none of the trees were mutated along the way.
    checkTrees();
  } // testRemove()

  // +-------+-------------------------------------------------------
  // | Utils |
  // +-------+

  /**
   * Make sure that a single tree meets expectations (correct size, contains
   * appropriate key/value pairs, does not contain other key/value pairs).
   */
  void checkTree(SimpleImmutableBST<String, Integer> bst, int size) {
    // Checking size
    assertEquals(size, bst.size(), "Incorrect size");

    // Checking key/value pairs in tree
    for (int i = 0; i < size; i++) {
      assertEquals(pairs[i].value(), bst.get(pairs[i].key()),
          "Looking for " + pairs[i].key());
    } // for

    // Checking keys not in tree
    for (int i = size; i < pairs.length; i++) {
      try {
        bst.get(pairs[i].key());
        fail(pairs[i].key() + " should not be in the tree");
      } catch (Exception e) {
        // Success!
      } // try/catch
    } // for
  } // checkTree

  /**
   * Make sure that all of our trees match expectations (that is, that trees[n]
   * has size n, and contains the key/value pairs from indices [0..n) of keys
   * and values.
   */
  void checkTrees() {
    for (int size = 0; size < pairs.length; size++) {
      checkTree(tree(size), size);
    } // for
  } // checkTrees()

  /**
   * Get the ith tree in the correct type
   */
  SimpleImmutableBST<String, Integer> tree(int i) {
    return (SimpleImmutableBST<String, Integer>) trees[i];
  } // tree(int)

} // SimpleImmutableBSTTests
