/*

  Author: Sasithorn Hannarong
  Email: shannarong2015@my.fit.deu
  Course: CSE2010
  Section: 03
  Description:  SkipListMap.java is used to creat a skip list and extends Node from DoublyLinkedList.
    
*/

public class SkipListMap<E> extends DoublyLinkedList{
    
    Node head = null;
    Node tail = null;
    public int n;                   // number of entries in the Skip List   
    public static int h = 0;        // Height
    FakeRandomHeight random = new FakeRandomHeight();             // Coin toss

// Constructor new SkipList
    public SkipListMap()
     {  
        Node p1 = new Node(Double.NEGATIVE_INFINITY, null);
        Node p2 = new Node(Double.POSITIVE_INFINITY, null);
   	head = p1;
   	tail = p2;
        p1.setNext(p2);
        p2.setPrev(p1);
   	n = 0;
   	h = 0;
     }
    
//Search key and return its position in the skiplist
    public Node search (Double key) {
        Node p = head;
        while(p.hasBelow()) {
            p = p.getBelow();           //Drop down
            while (key >= p.getNext().getKey() && !p.getKey().equals(Double.POSITIVE_INFINITY)) {
                p = p.getNext();        //Go forward
            }
        }
        return p;
    }
    
//Return element of the key located in the skiplist
    public String get (Double key) {
     Node p = new Node();
     p = search(key);
     if ( key.equals( p.getKey() ) )
        return (String) p.getElement();
     else
        return(null);
    }
    
//insert key(k) with element (v) to the skiplist
    public String put (Double k, String v) {
        // Get the position to insert k at the bottom of skiplist (at lv.0)
        Node p = search(k);
        //Check if k is already exist in the skip list
        if ( k.equals(p.getKey()) ) { 
            return "existingDateError";	 // Return the old value and done
        }
        //insert Node(k, v) at the bottom (h=0) of skiplist
        Node q = new Node(k, v);
        q.setPrev(p);
        q.setNext(p.getNext());
        p.getNext().setPrev(q);
        p.setNext(q);
        int r = random.get();   //Flip a coin
        // If random > current height, add new cover empty list and increase h by 1
        if ( r >= h || h == 0){
                Node p1 = new Node(Double.NEGATIVE_INFINITY, null);
                Node p2 = new Node(Double.POSITIVE_INFINITY, null);
                
                //Update new head
                p1.setBelow(head);
                head.setAbove(p1);
                head = p1;
 
                //Update new tail
                p2.setBelow(tail);
                tail.setAbove(p2);
                tail = p2;
 
                //Link heand and tail
                p1.setNext(p2);
                p2.setPrev(p1);
   
                //increase skiplist height by 1
                h++;
                
            }
        //Add new node from h=1 to the top of skiplist
        for (int i=1; i<=r; i++) { 
            //Find first element that have above pointer
            while ( p.getAbove() == null ) {
                p = p.getPrev();
            }
            
            //Make p point to this above element (which mean we go upward by 1 level)
            p = p.getAbove();
        
            //Add the new node (e) to the current level
            Node e = new Node(k, v);
            e.setPrev(p);
            e.setNext(p.getNext());
            e.setBelow(q);
   		 
            //Rearrange nodes that have to point to e
            p.getNext().setPrev(e);
            p.setNext(e);
            q.setAbove(e);
        
            // Set q up to e that located in current level for next iteration (if need)
                q = e;
        }
        // Increase entry in the Skip List by 1
        n = n + 1;
        return "success";   // No old value
    }
  
    //remove k from the list and return its element if removing succeeds or null if its unsucceeds
    public String remove (Double k) {
        Node p = search(k);
        
        //If k isn't in the list, don't remove
        if(!p.getKey().equals(k)) return "noDateError";
        
        //Travelling upward and unlink the element around p 
        while ( p != null ) {
            //If that node located at the top of skiplist, delete whole S(h), update new head to h-1 and decrease h by 1
            if(p.getPrev().getKey().equals(Double.NEGATIVE_INFINITY) && p.getNext().getKey().equals(Double.POSITIVE_INFINITY)) {
                head = p.getPrev();
                tail = p.getNext();
                head.setAbove(null);
                tail.setAbove(null);
                head.setNext(tail);
                tail.setNext(head);
                p = null;
                h--;
            } else { //unlink the element around p
                p.getPrev().setNext(p.getNext());
                p.getNext().setPrev(p.getPrev());
                p = p.getAbove();
            }
        }
        return "success";
    }
    
    //ceilingEntry(k). Find Entry with the least key value greater than or equal to k (The key at or after k)
    public String ceilingEntry(Double k) {
        Node p = search(k);
        if(p.getKey()<k) p = p.getNext();            //Search give the less number so we have to move forward
        //If there is no key greater than k (no ceiling)
        if(p.getKey().equals(Double.POSITIVE_INFINITY)) return "none";
        //print all keys and elements that located at or after k
        while ( !p.getKey().equals(Double.POSITIVE_INFINITY) ) {
            printKeyAndElement(p);
            p = p.getNext();
            if(p.hasNext()) System.out.print(" ");
        }
        return "";
    }

    
    //floorEntry(k) Entry with the greatest key less than or equal to k (The key “at or before” k)
     public String floorEntry(Double k) {
        Node p = search(k);
        //If there is no key lesser than k (no floor)
        if(p.getKey().equals(Double.NEGATIVE_INFINITY)) return "none";
        
        //print all keys and elements that located at or before k
        Node q = search(Double.NEGATIVE_INFINITY);
        while ( !q.getKey().equals(p.getKey()) ) {
            q = q.getNext();
            printKeyAndElement(q);
            if(q.hasNext()) System.out.print(" ");
        }
        return "";
    }
    
    //subMap(k1, k2) Entries between k1 and k2
    public String subMap(Double k1, Double k2) {
        Node p1 = search(k1);
        Node p2 = search(k2);
        if(p1.getKey()<k1) p1 = p1.getNext();            //Search give the less number so we have to move forward
        
        //If the interval is the exact time
        if(p1.getKey().equals(p2.getKey())) printKeyAndElement(p1);
        else {
            //If there is no node between k1 and k2
            if(!p1.hasNext() || !p2.hasPrev()) return "none";
            while(p1.getKey().compareTo(p2.getKey()) <= 0) {
                if(!p1.getKey().equals(Double.NEGATIVE_INFINITY) || p1.getKey().equals(p2)) 
                    printKeyAndElement(p1);
                p1 = p1.getNext();
                if(p1.hasNext()) System.out.print(" ");
            }
        }
        return "";
    }
    
    //Print all keys and its elements in a skiplist
    public String printAllKeys() {
        Node p = head;
        while(p.hasBelow()) {               //Go to the bottom of the skip list
            p = p.getBelow();
        }
        if(!p.hasNext() || p.getNext().getKey().equals(Double.POSITIVE_INFINITY)) {
            return "none";                  //No node avaible
        }
        p = p.getNext();                    //Skip -infinity
        while(p.hasNext()) {
            printKeyAndElement(p);
            p = p.getNext();
            if(p.hasNext()) System.out.print(" ");
        }
        return "";
    }
    
    //Print in from of a SkipList  (S0) to (Sh)
    public void printSkipList(){
        Node p = head;
        for(int i = h; i >= 0; i--) {
            System.out.printf("(S%d) ",i);
            if(p.getNext().getKey().equals(Double.POSITIVE_INFINITY)) {
                System.out.print("empty");
            } else {
                Node s = p.getNext();
                while(s.hasNext()) {
                    printKeyAndElement(s);
                    s = s.getNext();
                    if(s.hasNext()) System.out.print(" ");
                }
            }
            if(p.hasBelow()) {
                p = p.getBelow();
            }
            System.out.println();
        }
    }
    
    //Print in form KEY:ELEMENT while KEY is 4 digit
    public void printKeyAndElement (Node p) {
        Double date =  p.getKey();
        System.out.print(String.format("%04.0f", date) + ":" + p.getElement());
    }
}
