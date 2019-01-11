/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
//package net.datastructures;

/**
 * A basic doubly linked list implementation.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */

/**
 * new linked list implementation by Sasithorn.
 * next(p) : return position following p on the same level 
 * prev(p) : return position preceding p on the same level 
 * above(p) : return position above p in the same tower 
 * below(p) : return position below p in the same tower 
 */

public class DoublyLinkedList<E> {
    
public class Node<E> {

    /** The element stored at this node */
    private Double key;
    private E element;               // reference to the element stored at this node

    /** A reference to the preceding node in the list */
    private Node<E> prev;            // reference to the previous node in the list

    /** A reference to the subsequent node in the list */
    private Node<E> next;            // reference to the subsequent node in the list
    
    /** A reference to the above node in the tower */
    private Node<E> above;            // reference to the above node in the tower
    
    /** A reference to the below node in the tower */
    private Node<E> below;            // reference to the below node in the tower
    /**
     * Creates a node with the given element and next node.
     *
     * @param e  the element to be stored
     * @param p  reference to a node that should precede the new node
     * @param n  reference to a node that should follow the new node
     * @param a  reference to a node that should located above the new node
     * @param b  reference to a node that should located below the new node
     */
    
    //
    public Node() {
      key = null;
      element = null;
      prev = null;
      next = null;
      above = null;
      below = null;
    }
    public Node(Double k, E e) {
      key = k;
      element = e;
      prev = null;
      next = null;
      above = null;
      below = null;
    }
    public Node(Double k,E e, Node<E> p, Node<E> n, Node<E> a, Node<E> b) {
      key = k;
      element = e;
      prev = p;
      next = n;
      above = a;
      below = b;
    }

    // Return element method 
    public Double getKey() { return key; }
    public E getElement() { return element; }
    public Node<E> getPrev() { return prev; }
    public Node<E> getNext() { return next; }
    public Node<E> getAbove() { return above; }
    public Node<E> getBelow() { return below; }
    
    // Update methods
    public void setElement(E e) { element = e; }
    public void setPrev(Node<E> p) { prev = p; }
    public void setNext(Node<E> n) { next = n; }
    public void setAbove(Node<E> n) { above = n; }
    public void setBelow(Node<E> n) { below = n; }
    
    //Check and return boolean
    public boolean hasBelow() { return !(below==null); }
    public boolean hasAbove() { return !(above==null); }
    public boolean hasNext() { return !(next==null); }
    public boolean hasPrev() { return !(prev==null); }
  } //----------- end of nested Node class -----------

  /** Sentinel node at the beginning of the list */
  private Node<E> header;                    // header sentinel

  /** Sentinel node at the end of the list */
  private Node<E> trailer;                   // trailer sentinel
  

  /** Number of elements in the list (not including sentinels) */
  private int size = 0;                      // number of elements in the list

  /** Constructs a new empty list. */
  public DoublyLinkedList() {
    header = new Node<>();      // create header
    trailer = new Node<>(null, null, header, null, null, null);   // trailer is preceded by header
    header.setNext(trailer);                    // header is followed by trailer
  }

  // public accessor methods
  /**
   * Returns the number of elements in the linked list.
   * @return number of elements in the linked list
   */
  public int size() { return size; }

  /**
   * Tests whether the linked list is empty.
   * @return true if the linked list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the first element of the list.
   * @return element at the front of the list (or null if empty)
   */
  public E first() {
    if (isEmpty()) return null;
    return header.getNext().getElement();   // first element is beyond header
  }

  /**
   * Returns (but does not remove) the last element of the list.
   * @return element at the end of the list (or null if empty)
   */
  public E last() {
    if (isEmpty()) return null;
    return trailer.getPrev().getElement();    // last element is before trailer
  }

  // public update methods
  /**
   * Adds an element to the front of the list.
   * @param e   the new element to add
   */
  public void addFirst(Double k, E e, Node<E> above, Node<E> below) {
    addBetween(k, e, header, header.getNext(), above, below);    // place just after the header
  }

  /**
   * Adds an element to the end of the list.
   * @param e   the new element to add
   */
  public void addLast(Double k, E e, Node<E> above, Node<E> below) {
    addBetween(k, e, trailer.getPrev(), trailer, above, below);  // place just before the trailer
  }

  /**
   * Removes and returns the first element of the list.
   * @return the removed element (or null if empty)
   */
  public E removeFirst() {
    if (isEmpty()) return null;                  // nothing to remove
    return remove(header.getNext());             // first element is beyond header
  }

  /**
   * Removes and returns the last element of the list.
   * @return the removed element (or null if empty)
   */
  public E removeLast() {
    if (isEmpty()) return null;                  // nothing to remove
    return remove(trailer.getPrev());            // last element is before trailer
  }

  // private update methods
  /**
   * Adds an element to the linked list in between the given nodes.
   * The given predecessor and successor should be neighboring each
   * other prior to the call.
   *
   * @param predecessor   node just before the location where the new element is inserted
   * @param successor     node just after the location where the new element is inserted
   */
  private void addBetween(Double k, E e, Node<E> predecessor, Node<E> successor, Node<E> above, Node<E> below) {
    // create and link a new node
    Node<E> newest = new Node<>(k, e, predecessor, successor, above, below);
    predecessor.setNext(newest);
    successor.setPrev(newest);
    size++;
  }

  /**
   * Removes the given node from the list and returns its element.
   * @param node    the node to be removed (must not be a sentinel)
   */
  private E remove(Node<E> node) {
    Node<E> predecessor = node.getPrev();
    Node<E> successor = node.getNext();
    predecessor.setNext(successor);
    successor.setPrev(predecessor);
    size--;
    return node.getElement();
  }

  /**
   * Produces a string representation of the contents of the list.
   * This exists for debugging purposes only.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    Node<E> walk = header.getNext();
    while (walk != trailer) {
      sb.append(walk.getElement());
      walk = walk.getNext();
      if (walk != trailer)
        sb.append(", ");
    }
    sb.append(")");
    return sb.toString();
  }
  
  public boolean hasNext() {
      if (isEmpty()) return false;
      return header.hasNext();
  }
  
  public Node get(int n) {
      if (isEmpty()) return null;
      Node nSearch = header;
      for (int i = 0; i < n; i++) {
          nSearch = nSearch.next;
      }
      return nSearch;
  }
  
  public Node search(int target) {
      if (isEmpty()) return null;
      Node nSearch = null;
      if (header.getKey() == target) {
            return header;
      }else {
           nSearch = header.next;
           for (int i = size; nSearch.getKey() != target & i>0; i--) {
                nSearch = nSearch.next;
            }
      }
      return nSearch;
  }
  
} //----------- end of DoublyLinkedList class -----------