package problem2;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import utils.Node2;
import utils.SimpleList;

/**
 * Simple circular doubly-linked lists.
 *
 * These do *not* (yet) support the Fail Fast policy.
 */
public class SimpleCircularDLL<T> implements SimpleList<T> {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The dummy node.
   */
  Node2<T> dummy;

  /**
   * The number of values in the list.
   */
  int size;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty list.
   */
  public SimpleCircularDLL() {
    this.dummy = Node2.dummyNode();

    this.size = 0;
  } // SimpleCircularDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      /**
       * The position in the list of the next value to be returned.
       * Included because ListIterators must provide nextIndex and
       * prevIndex.
       */
      int pos = 0;

      /**
       * A cursor.  This is generally on the node *before* the one
       * that will be returned by next.
       */
      Node2<T> cursor = SimpleCircularDLL.this.dummy;

      /**
       * The node to be updated by remove or set.  Has a value of
       * null when there is no such node.
       */
      Node2<T> update = null;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      public void add(T val) throws UnsupportedOperationException {
        this.cursor.insertAfter(val);
        this.cursor = this.cursor.next();
        this.pos++;

        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++SimpleCircularDLL.this.size;
      } // add(T)

      public boolean hasNext() {
        return (this.pos < SimpleCircularDLL.this.size);
      } // hasNext()

      public boolean hasPrevious() {
        return (this.pos > 0);
      } // hasPrevious()

      public T next() {
        if (!this.hasNext()) {
         throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.cursor.next();
        // Note the movement
        ++this.pos;
        // Advance the cursor
        this.cursor = this.cursor.next();
        // And return the value
        return this.update.value();
      } // next()

      public int nextIndex() {
        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        if (!this.hasPrevious()) {
          throw new NoSuchElementException();
        } // if
        this.update = this.cursor;
        --this.pos;
        this.cursor = this.cursor.prev();
        return this.update.value();
      } // previous()

      public void remove() {
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Update the cursor
        if (this.update == this.cursor) {
          this.cursor = this.cursor.prev();
          --this.pos;
        } // if

        this.update.remove();

        --SimpleCircularDLL.this.size;

        // Note that no more updates are possible
        this.update = null;
      } // remove()

      public void set(T val) {
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.setValue(val);
        // Note that no more updates are possible
        this.update = null;
      } // set(T)
    };
  } // listIterator()

} // class SimpleCircularDLL<T>
