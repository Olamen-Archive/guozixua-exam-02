package problem2;

/**
 * Sometimes it's easier to run tests in debugging mode.  This class helps us
 * do so.
 */
public class TestsForDebugging {
  
  public static void main(String[] args) {
    SimpleCircularDLLTests tester = new SimpleCircularDLLTests();
    tester.setup();
    tester.setupSimpleList();
    tester.testRandomly();
  } // main(String[])
  
} // TestsForDebugging
