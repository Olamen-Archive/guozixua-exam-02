package utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * A simple set of tests for SimpleMaps. Based on experiments that I wrote for
 * hash tables and tests I wrote for skip lists (and, perhaps, some other things
 * I've done).
 *
 * It is intended that you will subclass this class and implement the
 * a @BeforeEach method which will initialize the stringMap field. For example,
 *
 * <pre>
 * &#64;BeforeEach
 * public void setupMyMap() {
 *   stringMap = new MyMap<String, String>();
 * } // setupMap ()
 * </pre>
 * 
 * You can turn off all iterator tests by setting runIteratorTests to false.
 * 
 * You can turn off the iterator tests involving removal by setting
 * runIteratorRemoveTests to false.
 *
 * A few of the tests print useful errors when they fail. (Or at least I think
 * they are useful.)
 *
 * @author Samuel A. Rebelsky
 */
public class SimpleMapTests {

  // +--------------------+------------------------------------------
  // | Some useful arrays |
  // +--------------------+

  /**
   * A word list stolen from some tests that SamR wrote in the distant past.
   */
  static final String[] words = {"aardvark", "anteater", "antelope", "bear",
      "bison", "buffalo", "chinchilla", "cat", "dingo", "elephant", "eel",
      "flying squirrel", "fox", "goat", "gnu", "goose", "hippo", "horse",
      "iguana", "jackalope", "kestrel", "llama", "moose", "mongoose", "nilgai",
      "orangutan", "opossum", "red fox", "snake", "tarantula", "tiger",
      "vicuna", "vulture", "wombat", "yak", "zebra", "zorilla"};

  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * Do we run all the iterator tests?
   */
  protected boolean runIteratorTests = true;

  /**
   * Do we run the iterator tests that involve removal?
   */
  protected boolean runIteratorRemoveTests = true;

  /**
   * A random number generator for the randomized tests.
   */
  Random random = new Random();

  /**
   * A of stringMap for tests. (Gets set by the subclasses.)
   */
  protected SimpleMap<String, String> stringMap;

  /**
   * For reporting errors: a list of the operations we performed.
   */
  ArrayList<String> operations;

  /**
   * A place for reporting things.
   */
  PrintWriter dumpPen = new PrintWriter(System.err, true);

  // +---------+---------------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Set up the operations.
   */
  @BeforeEach
  public void setupOperations() {
    this.operations = new ArrayList<String>();
  } // setupOperations()

  /**
   * Generate a value from a string.
   */
  static String value(String str) {
    return str.toUpperCase();
  } // key(String)

  /**
   * Assert that we successfully remove a string from the table.
   */
  void assertRemove(String str) {
    stringMap.remove(str);
    assertThrows(java.lang.IndexOutOfBoundsException.class,
        () -> stringMap.get(str));
  } // assertRemove
  
  /**
   * Generate a random key
   */
  String randomKey() {
    int len = 1 + random.nextInt(10);
    StringBuilder result = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      result.append((char) 'a' + random.nextInt(26));
    } // for
    return result.toString();
  } // randomKey()

  // +--------------------+------------------------------------------
  // | Logging operations |
  // +--------------------+

  /**
   * Set a string.
   */
  void set(String str) {
    operations.add("set(\"" + str + "\");");
    stringMap.set(str, value(str));
  } // set(String)

  /**
   * Remove a string from the stringMap list.
   */
  void remove(String str) {
    operations.add("remove(\"" + str + "\");");
    stringMap.remove(str);
  } // remove(String)

  /**
   * Log a failure.
   */
  void log(String str) {
    System.err.println(str);
    operations.add("// " + str);
  } // log

  /**
   * Print code from a failing test.
   */
  void printTest() {
    System.err.println("@Test");
    System.err.println("  public void test" + random.nextInt(1000) + "() {");
    for (String op : operations) {
      System.err.println("    " + op);
    } // for
    System.err.println("  }");
    System.err.println();
  } // printTest()

  // +-------------+-----------------------------------------------------
  // | Basic Tests |
  // +-------------+

  /**
   * A really simple test. Add an element and make sure that it's there.
   */
  @Test
  public void simpleTest() {
    set("hello");
    assertTrue(stringMap.containsKey("hello"));
    assertFalse(stringMap.containsKey("goodbye"));
  } // simpleTest()

  /**
   * Another simple test. The list should not contain anything when we start
   * out.
   */
  @Test
  public void emptyTest() {
    assertFalse(stringMap.containsKey("empty"));
  } // emptyTest()

  // +---------------------------+-----------------------------------
  // | Somewhat systematic tests |
  // +---------------------------+

  /**
   * Test that a sequence of set operations succeeds.
   */
  @Test
  void multipleSetTest() {
    for (int i = 0; i < words.length; i++) {
      stringMap.set(words[i], words[i]);
      for (int j = 0; j <= i; j++) {
        assertEquals(words[j], stringMap.get(words[j]));
      } // for j
    } // for i
  } // multipleSetExpt(PrintWriter, HashTable)

  /**
   * Explore what happens when we remove elements.
   */
  @Test
  void multipleRemoveTest() {
    // Populate the table
    multipleSetTest();

    // Remove words one by one.
    for (int i = 0; i < words.length; i++) {
      assertRemove(words[i]);

      // Make sure that the remaining elements are still there.
      for (int j = i + 1; j < words.length; j++) {
        assertEquals(words[j], stringMap.get(words[j]));
      } // for j
    } // for i
  } // multipleRemoveExpt(PrintWriter, HashTable)

  // +------------------+--------------------------------------------
  // | Randomized tests |
  // +------------------+

  /**
   * Verify that a randomly created list contains all the values we added to the
   * list.
   */
  @Test
  public void testContainsOnlyAdd() {
    ArrayList<String> keys = new ArrayList<String>();

    // Add a bunch of values
    for (int i = 0; i < 100; i++) {
      String key = randomKey();
      keys.add(key);
      set(key);
    } // for i
    // Make sure that they are all there.
    for (String key : keys) {
      if (!stringMap.containsKey(key.toString())) {
        log("contains(" + key + ") failed");
        printTest();
        stringMap.dump(new PrintWriter(System.out, true));
        fail(key + " is not in the map");
      } // if (!stringMap.contains(val))
    } // for key
  } // testContainsOnlyAdd()

  /**
   * An extensive randomized test.
   */
  @Test
  public void randomTest() {
    // Keep track of the values that are currently in the map.
    ArrayList<String> keys = new ArrayList<String>();

    // Add a bunch of values
    boolean ok = true;
    for (int i = 0; ok && i < 1000; i++) {
      String key = randomKey();
      // Half the time we add
      if (random.nextBoolean()) {
        if (!stringMap.containsKey(key)) {
          set(key);
        } // if it's not already there.
        if (!stringMap.containsKey(key)) {
          log("After adding " + key + ", containsKey(" + key + ") fails");
          ok = false;
        } // if (!ints.contains(rand))
      } // if we add
      // Half the time we remove
      else {
        remove(key);
        keys.remove(key);
        if (stringMap.containsKey(key.toString())) {
          log("After removing " + key + ", containsKey(" + key + ") succeeds");
          ok = false;
        } // if ints.contains(rand)
      } // if we remove
      // See if all of the appropriate elements are still there
      for (String key2 : keys) {
        if (!stringMap.containsKey(key2)) {
          log("stringMap no longer contains " + key2);
          ok = false;
          break;
        } // if the value is no longer contained
      } // for each key
    } // for i
    // Dump the instructions if we've encountered an error
    if (!ok) {
      printTest();
      stringMap.dump(new PrintWriter(System.out, true));
      fail("Operations failed");
    } // if (!ok)
  } // randomTest()

  /**
   * A slightly randomized test of iterators.
   */
  @Test
  public void randomlyTestIterator() {
    if (!runIteratorTests) {
      return;
    } // if

    ArrayList<String> expected = new ArrayList<String>();
    ArrayList<String> actual = new ArrayList<String>();
    String[] source = words.clone();
    // MiscUtils.randomlyPermute(source);

    // Fill in the map and the array of expected values.
    for (int i = 0; i < source.length; i++) {
      stringMap.set(source[i], source[i]);
      expected.add(source[i]);
    } // for

    // Iterate the map
    stringMap.dump(new PrintWriter(System.out, true));
    for (Pair<String, String> pair : stringMap) {
      System.err.println(pair.key());
      actual.add(pair.key());
    } // for

    // Sort both array lists
    expected.sort((s1, s2) -> s1.compareTo(s2));
    actual.sort((s1, s2) -> s1.compareTo(s2));
    assertEquals(expected, actual);
  } // randomlyTestIterator()

  // +---------------------+-----------------------------------------
  // | More iterator tests |
  // +---------------------+

  @Test
  void testRemoveSingleton() throws Exception {
    if (!runIteratorTests || !runIteratorRemoveTests) {
      return;
    } // if

    stringMap.set("A", "A");
    assertEquals(1, stringMap.size());
    Iterator<Pair<String, String>> si = stringMap.iterator();
    si.next();
    si.remove();
    assertEquals(0, stringMap.size());
    assertFalse(stringMap.iterator().hasNext());
  } // testRemoveSingleton

  @Test
  void testExceptionsRemove() throws Exception {
    if (!runIteratorTests || !runIteratorRemoveTests) {
      return;
    } // if

    // Removing from an empty array
    assertThrows(java.lang.Exception.class,
        () -> stringMap.iterator().remove());

    // Removing before iterating
    stringMap.set("A", "A");
    stringMap.set("B", "B");
    assertThrows(java.lang.Exception.class,
        () -> stringMap.iterator().remove());

    // Removing twice in a row
    stringMap.set("A", "A");
    stringMap.set("B", "B");
    Iterator<Pair<String, String>> sit = stringMap.iterator();
    sit.next();
    sit.remove(); // valid
    assertThrows(java.lang.Exception.class, () -> sit.remove());
    assertThrows(java.lang.Exception.class, () -> sit.remove());

    // Removing twice in a row later in the stack
    stringMap.set("A", "A");
    stringMap.set("B", "B");
    stringMap.set("C", "C");
    stringMap.set("D", "D");
    Iterator<Pair<String, String>> sit2 = stringMap.iterator();
    sit2.next();
    sit2.next();
    sit2.remove(); // valid
    assertThrows(java.lang.Exception.class, () -> sit2.remove());
    assertThrows(java.lang.Exception.class, () -> sit2.remove());
  } // testExceptionsRemove

  @Test
  void testExceptionsModification() throws Exception {
    if (!runIteratorTests || !runIteratorRemoveTests) {
      return;
    } // if

    stringMap.set("A", "A");
    stringMap.set("B", "B");
    stringMap.set("C", "C");
    stringMap.set("D", "D");

    // Modification by addition
    Iterator<Pair<String, String>> sit1 = stringMap.iterator();
    sit1.next();
    stringMap.set("E", "E");
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit1.remove());
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit1.hasNext());
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit1.next());

    // Modification by removal
    Iterator<Pair<String, String>> sit2 = stringMap.iterator();
    stringMap.remove("A");
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit2.remove());
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit2.hasNext());
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit2.next());

    // Modification by replacement
    Iterator<Pair<String, String>> sit3 = stringMap.iterator();
    stringMap.set("B", "C");
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit3.remove());
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit3.hasNext());
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit3.next());

    // Simultaneous modification
    Iterator<Pair<String, String>> sit4 = stringMap.iterator();
    Iterator<Pair<String, String>> sit5 = stringMap.iterator();
    sit4.next();
    sit4.next();
    sit5.next();
    sit4.remove();
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit5.remove());
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit5.hasNext());
    assertThrows(java.util.ConcurrentModificationException.class,
        () -> sit5.next());
  } // testExceptionsModification

} // class SimpleMapTests
