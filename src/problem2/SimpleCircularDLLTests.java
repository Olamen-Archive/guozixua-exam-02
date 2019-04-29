package problem2;

import utils.SimpleListTests;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests of circular DLLs.
 */
public class SimpleCircularDLLTests extends SimpleListTests {
  /**
   * Prepare for each test.
   */
  @BeforeEach
  public void setupSimpleList() {
    slist = new SimpleCircularDLL<String>();
    slistIt = slist.listIterator();
  } // setupSimpleList()
} // class SimpleCircularDLLTests
