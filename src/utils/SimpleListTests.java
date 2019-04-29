package utils;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * A simple test of SimpleList objects.
 *
 * It is intended that you will subclass this class and implement the
 * a @BeforeEach method which will initialize the slist and slistIt fields. For
 * example,
 *
 * <pre>
 * &#64;BeforeEach
 * public void setupMySimpleList() {
 *   slist = new MySimpleList<String>();
 *   slistIt = slist.listIterator();
 * } // setupMySimpleList ()
 * </pre>
 * 
 * @author Samuel A. Rebelsky
 */
public class SimpleListTests {
  // +-------+-------------------------------------------------------
  // | Notes |
  // +-------+

  /*
   * Most of our checking is done by building an ArrayList and comparing the
   * results.
   */

  // +---------------+-----------------------------------------------
  // | Useful arrays |
  // +---------------+

  static final String[] letters =
      {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
          "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * A brand new list of strings , created before each test.
   */
  protected SimpleList<String> slist;

  /**
   * A matching ArrayList, for comparison.
   */
  ArrayList<String> alist;

  /**
   * A common iterator for the simple list.
   */
  protected ListIterator<String> slistIt;

  /**
   * A common interator for the ArrayList.
   */
  ListIterator<String> alistIt;

  /**
   * For reporting errors: a list of the operations we performed.
   */
  ArrayList<String> operations;

  /**
   * A random number generator for the randomized testing.
   */
  static Random rand = new Random();

  // +-------------+-------------------------------------------------
  // | Preparation |
  // +-------------+

  @BeforeEach
  public void setup() {
    alist = new ArrayList<String>();
    alistIt = alist.listIterator();
    operations = new ArrayList<String>();
  } // setup()

  // +-----------+---------------------------------------------------
  // | Utilities |
  // +-----------+

  /**
   * Check to make sure that the two lists are in synch.
   */
  public void checkStatus() {
    ListIterator<String> alistCheck = alist.listIterator();
    ListIterator<String> slistCheck = slist.listIterator();
    while (slistCheck.hasNext() && alistCheck.hasNext()) {
      int aNI = alistCheck.nextIndex();
      int sNI = slistCheck.nextIndex();
      if (aNI != sNI) {
        printFailedTest();
        fail("a.nextIndex()=" + aNI + ", s.nextIndex()=" + sNI);
      }
      String aNext = alistCheck.next();
      String sNext = slistCheck.next();
      if (!aNext.equals(sNext)) {
        printFailedTest();
        fail("a.next()=\"" + aNext + "\", s.next()=\"" + sNext + "\"");
      } // if
    } // while
    if (slistCheck.hasNext()) {
      printFailedTest();
      fail("Elements remain in s but not a");
    } else if (alistCheck.hasNext()) {
      printFailedTest();
      fail("Elements remain in a but not s");
    } // if/else
  } // checkStatus()

  // +---------+-----------------------------------------------------
  // | Logging |
  // +---------+
  
  /**
   * Add an instruction to the log.
   */
  void log(String instruction) {
    operations.add(instruction);
  } // log(String)

  /**
   * Print code from a failing test.
   */
  void printFailedTest() {
    String testName = "generatedTest" + rand.nextInt(100000);
    System.err.println("  @Test");
    System.err.println("  public void " + testName + "() {");
    for (String op : operations) {
      System.err.println("    " + op);
    } // for
    System.err.println("  } // " + testName);
    System.err.println();
  } // printFailedTest()

  // +------------+--------------------------------------------------
  // | Operations |
  // +------------+
  
  /*
   * This code needs refactoring.  Perhaps another day.
   */

  /**
   * Add a string using the iterator, logging the call.
   */
  public void add(String str) {
    log("add(\"" + str + "\");");
    try {
      alistIt.add(str);
    } catch (Exception e) {
      printFailedTest();
      fail("a.add(\"" + str + "\") failed: " + e);
    } // try/catch
    try {
      slistIt.add(str);
    } catch (Exception e) {
      printFailedTest();
      fail("s.add(\"" + str + "\") failed: " + e);
    } // try/catch
  } // add(String)

  /**
   * Check if the iterator can move forward
   */
  public boolean hasNext() {
    boolean aHasNext = false;
    boolean sHasNext = false;
    try {
      aHasNext = alistIt.hasNext();
    } catch (Exception e) {
      log("hasNext();");
      printFailedTest();
      fail("Failed in alistIt.hasNext(): " + e);
    } // try/catch
    try {
      sHasNext = slistIt.hasNext();
    } catch (Exception e) {
      log("hasNext();");
      printFailedTest();
      fail("Failed in slistIt.hasNext(): " + e);
    } // try/catch
    if (aHasNext != sHasNext) {
      log("hasNext();");
      printFailedTest();
      fail("a.hasNext()=" + aHasNext + ", s.hasNext()=" + sHasNext);
    } // if
    return aHasNext;
  } // hasNext()

  /**
   * Check if the iterator can move forward
   */
  public boolean hasPrev() {
    boolean aHasPrev = false;
    boolean sHasPrev = false;
    try {
      aHasPrev = alistIt.hasPrevious();
    } catch (Exception e) {
      log("hasPrev();");
      printFailedTest();
      fail("Failed in alistIt.hasPrev(): " + e);
    } // try/catch
    try {
      sHasPrev = slistIt.hasPrevious();
    } catch (Exception e) {
      log("hasPrev();");
      printFailedTest();
      fail("Failed in slistIt.hasPrev(): " + e);
    } // try/catch
    if (aHasPrev != sHasPrev) {
      log("hasPrev();");
      printFailedTest();
      fail("aList.hasPrev()=" + aHasPrev + ", sList.hasPrev()=" + sHasPrev);
    } // if
    return aHasPrev;
  } // hasPrev()

  /**
   * Move forward using the iterator.
   */
  public void next() {
    log("next();");
    String aNext = "";
    String sNext = "";
    try {
      aNext = alistIt.next();
    } catch (Exception e) {
      printFailedTest();
      fail("Failed in alistIt.next(): " + e);
    } // try/catch
    try {
      sNext = slistIt.next();
    } catch (Exception e) {
      printFailedTest();
      fail("Failed in slistIt.next(): " + e);
    } // try/catch
    if (!aNext.equals(sNext)) {
      printFailedTest();
      fail("a.next()=\"" + aNext + "\", s.next()=\"" + sNext + "\"");
    } // if
  } // next()

  /**
   * Make sure that the next indices are in sync.
   */
  public void nextIndex() {
    int aNI = 0;
    int sNI = 0;
    try {
      aNI = alistIt.nextIndex();
    } catch (Exception e) {
      log("nextIndex();");
      printFailedTest();
      fail("Failed in alistIt.nextIndex(): " + e);
    } // try/catch
    try {
      sNI = slistIt.nextIndex();
    } catch (Exception e) {
      log("nextIndex();");
      printFailedTest();
      fail("Failed in slistIt.nextIndex(): " + e);
    } // try/catch

    if (aNI != sNI) {
      log("nextIndex();");
      printFailedTest();
      fail("alistIt.ni()=" + aNI + ", slistIt.ni()=" + sNI);
    } // if
  } // nextIndex()

  /**
   * Move backward using the iterator.
   */
  public void prev() {
    log("prev();");
    String aPrev = "";
    String sPrev = "";
    try {
      aPrev = alistIt.previous();
    } catch (Exception e) {
      printFailedTest();
      fail("Failed in alistIt.previous(): " + e);
    } // try/catch
    try {
      sPrev = slistIt.previous();
    } catch (Exception e) {
      printFailedTest();
      fail("Failed in slistIt.previous(): " + e);
    } // try/catch
    if (!aPrev.equals(sPrev)) {
      printFailedTest();
      fail("aList.prev()=\"" + aPrev + "\", sList.prev()=\"" + sPrev + "\"");
    } // if
  } // prev()

  /**
   * Verify that the previous indices match.
   */
  void prevIndex() {
    int aPI = 0;
    int sPI = 0;
    try {
      aPI = alistIt.previousIndex();
    } catch (Exception e) {
      log("prevIndex();");
      printFailedTest();
      fail("Failed in alistIt.previousIndex(): " + e);
    } // try/catch
    try {
      sPI = slistIt.previousIndex();
    } catch (Exception e) {
      log("prevIndex();");
      printFailedTest();
      fail("Failed in slistIt.previousIndex(): " + e);
    } // try/catch
    if (aPI != sPI) {
      log("prevIndex();");
      printFailedTest();
      fail("alistIt.pi()=" + aPI + ", slistIt.pi()=" + sPI);
    } // if
  } // prevIndex()

  /**
   * Remove a string using the iterator, logging the call.
   */
  public void remove() {
    log("remove();");
    try {
      alistIt.remove();
    } catch (Exception e) {
      printFailedTest();
      fail("Failed in alistIt.remove(): " + e);
    } // try/catch
    try {
      slistIt.remove();
    } catch (Exception e) {
      printFailedTest();
      fail("Failed in slistIt.remove(): " + e);
    } // try/catch
  } // remove()

  /**
   * Set a string using the iterator, logging the call.
   */
  public void set(String str) {
    log("set(\"" + str + "\");");
    try {
      alistIt.set(str);
    } catch (Exception e) {
      printFailedTest();
      fail("a.set(\"" + str + "\") failed: " + e);
    } // try/catch
    try {
      slistIt.set(str);
    } catch (Exception e) {
      printFailedTest();
      fail("s.set(\"" + str + "\") failed: " + e);
    } // try/catch
  } // add(String)

  // +------------------+--------------------------------------------
  // | Systematic tests |
  // +------------------+

  /**
   * Make sure that the initial state seems correct.
   */
  @Test
  public void testInitialState() {
    assertEquals(0, slistIt.nextIndex());
    assertEquals(-1, slistIt.previousIndex());
  } // testInitialState()

  /**
   * Add elements without moving the iterator and make sure that everything is
   * okay.
   */
  @Test
  public void testBasicAdd() {

    for (String str : letters) {
      add(str);
    } // for

    checkStatus();
  } // testBasicAdd()

  /**
   * Add elements, moving the iterator after each.
   */
  @Test
  public void testBasicAddAndNext() {
    for (String str : letters) {
      add(str);
      prev();
    } // for

    checkStatus();
  } // testBasicAddAndNext()

  /**
   * Add elements, moving the iterator after every-other one.
   */
  @Test
  public void testAlternatingAdd() {
    int i = 0;

    for (String str : letters) {
      add(str);
      if ((i++ % 2) == 0) {
        prev();
      } // if
    } // for

    checkStatus();
  } // testAlternatingAdd()

  /**
   * Add a few elements, removing each immediately after adding it.
   */
  @Test
  public void testBasicAddAndRemove() {
    for (String str : letters) {
      add(str);
      prev();
      remove();
    } // for

    checkStatus();
  } // testBasicAddAndRemove()

  /**
   * Add a few elements, removing every other one.
   */
  @Test
  public void testBasicRemoveAlternates() {
    int i = 0;
    for (String str : letters) {
      add(str);
      if ((i++ % 2) == 0) {
        prev();
        remove();
      } // if
    } // for

    checkStatus();
  } // testBasicRemoveAlternate()

  /**
   * Remove alternate elements from the back.
   */
  @Test
  public void testBasicRemoveFromBack() {
    testBasicAddAndNext();

    // Move the iterators to the end.
    while (hasNext()) {
      next();
    } // while

    // Move backwards, removing every other elements.
    int i = 0;
    while (hasPrev()) {
      prev();
      if ((i++ % 2) == 0) {
        remove();
      } // if
    } // while

    checkStatus();
  } // testBasicRemoveFromBack()


  // +------------------+--------------------------------------------
  // | Randomized tests |
  // +------------------+

  /**
   * A somewhat strange random test.
   */
  @Test
  public void testRandomly() {
    boolean canRemove = false;

    for (int i = 0; i < 2000; i++) {
      double r = rand.nextDouble();
      // 20% chance of each of the five key operations
      if (r < 0.2) {
        if (canRemove) {
          remove();
          canRemove = false;
        } // if
      } else if (r < 0.4) {
        if (canRemove) {
          set(letters[i % letters.length]);
          canRemove = false;
        }
      } else if (r < 0.6) {
        add(letters[i % letters.length]);
        canRemove = false;
      } else if ((r < 0.8)) {
        if (hasPrev()) {
          prev();
          canRemove = true;
        }
      } else {
        if (hasNext()) {
          next();
          canRemove = true;
        }
      } // if/else

      // Check indices!
      nextIndex();
      prevIndex();

    } // for

    checkStatus();
  } // testRandomly()

  // +---------------------------------------+-----------------------
  // | A few tests generated by testRandomly |
  // +---------------------------------------+

  @Test
  public void generatedTest5340() {
    add("E");
    prev();
    next();
    set("H");
    nextIndex();
  } // generatedTest5340
} // class SimpleListTests
