// Ronnie Tam
// March 21, 1999
// SkipList.java
// Computer Science 241
// Washington University in St. Louis

/*
This is an implementation of a Skip List.
Notice that I have implemented the coin flip inside the SkipNode and
that the random number generator is in fact declared STATIC, so that
it will not generate the same numbers over and over.
*/

import java.util.*;

public class SkipList {

    private int maxheight;
    private final int MIN = Integer.MIN_VALUE;
    private final int MAX = Integer.MAX_VALUE;
    private final int NOTFOUND = -4;
    private final int NONE = -5;
    private SkipNode head, tail, ptr;
    private boolean debug;  //debug is true if debug mode is on

    public SkipList(boolean debug) {
        this.debug = debug;
        maxheight = 2;
        head = new SkipNode(MIN, maxheight);
        tail = new SkipNode(MAX, maxheight);
        for(int i = 0; i < maxheight; i++) {
            head.prev[i] = null;
            head.next[i] = tail;
            tail.prev[i] = head;
            tail.next[i] = null;
        }
    }

    // print() will print out the skip list given that debug is true
    // Note that for debugging purposes and thoroughness, it will print
    // all levels, including those that have no keys.
    private void print() {
        if(debug) {
            for (int i = (head.size-1); i >=0; i--) { //iterate through levels
                ptr = head;
                Terminal.print("Level " + i + ":");
                while (ptr.next[i] != tail) {       //iterate through nodes
                    ptr = ptr.next[i];
                    Terminal.print(" " + ptr.key);  //print out key
                }
                Terminal.println("");               //print a line feed
            }
        }
    }

    //insert will insert a SkipNode into the list
    public void insert(int key) {
        Terminal.println("Inserting " + key);
        SkipNode newItem = new SkipNode(key);
        if(newItem.size > maxheight)        //set maxheight
            maxheight = newItem.size;
        if(head.size < maxheight)
            increaseHeadTailSize();         //increase the head & tail size
        Terminal.println("HEIGHT ="+newItem.size);
        ptr = head;
        int l = maxheight - 1;
        int k = 0;
        boolean inserted = false;
        while(l >= 0) {
            if (k == 0) {   //k is a flag, indicating a change in levels
                Terminal.print("At level " + l + " compared " + key + " to:");
                k++;
            }
            if (ptr.next[l].key == MAX) {
                Terminal.print(" infinity");
            }else {
                Terminal.print(" " + ptr.next[l].key);
            }
            if(ptr.next[l].key == key) {
                Terminal.println("The key you wish to insert is in use.");
            }
            if(ptr.next[l].key < key) {
                ptr = ptr.next[l];
            }else {
                if(l <= newItem.size-1) {
                    newItem.next[l] = ptr.next[l];  //insert and initialize
                    ptr.next[l] = newItem;          //new item
                    newItem.prev[l] = ptr;
                    newItem.next[l].prev[l] = newItem;
                }
                l--;
                Terminal.println("");
                k = 0;
            }
        }
        print();
    }

    //This method simply increases the head and tail size based
    //on the maxheight
    public void increaseHeadTailSize() {
        int prevHeight = head.size;     //store old size
        while (head.size < maxheight) { //increase the size of the head
            head.size = 2 * head.size;
        }
        Terminal.println("Expanding Head and Tail to height of: "+head.size);
        SkipNode[] hPrevious = head.prev;
        SkipNode[] hNext = head.next;
        SkipNode[] tPrevious = tail.prev;
        SkipNode[] tNext = tail.next;

        //Create new previous and next references of appropriate size
        head.prev = new SkipNode[head.size];
        head.next = new SkipNode[head.size];
        tail.prev = new SkipNode[head.size];
        tail.next = new SkipNode[head.size];
        int p = 0;
        //set the appropriate references
        while(p <= prevHeight-1) {
            head.prev[p] = hPrevious[p];
            head.next[p] = hNext[p];
            tail.prev[p] = tPrevious[p];
            tail.next[p] = tNext[p];
            p++;
        }
        p = prevHeight;
        while(p <= head.size-1) {
            head.prev[p] = null;
            tail.next[p] = null;
            head.next[p] = tail;
            tail.prev[p] = head;
            p++;
        }
    }

    //Search simply searches the skiplist and sets ptr equal to the SkipNode
    //with the found key, given that the key is found in the first place
    public boolean search(int key) {
        ptr = head;
        Terminal.println("Searching for " + key);
        int l = maxheight-1;
        int k = 0;
        while(l >= 0) {
            if (k == 0) {   //k is a flag indicating level change
                Terminal.print("At level " + l + " compared " + key + " to:");
                k++;
            }
            if (ptr.next[l].key == MAX) {
                Terminal.print(" infinity");
            }else {
                Terminal.print(" " + ptr.next[l].key);
            }
            if (ptr.next[l].key == key) {
                ptr = ptr.next[l];
                Terminal.println("\nFound Key:"+key);
                return true;
            }
            if (ptr.next[l].key < key)
                ptr = ptr.next[l];
            else {
                l--;
                k = 0;
                Terminal.println("");
            }
        }
        Terminal.println("It was not found.");
        print();
        return false;
    }

    //successor returns the next value
    public int successor(int key) {
        Terminal.println("Seeking the successor to: "+key);
        search(key);    // search sets ptr
        if(ptr != null && ptr.next[0].key != MAX) {
            Terminal.println("The successor to "+key+" is "+ptr.next[0].key);
            return ptr.next[0].key;
        }else if(ptr == null) {
            Terminal.println("The key "+key+" was not found in the skiplist.");
            return NOTFOUND;
        }else {
            Terminal.println("There is no successor.");
            return NONE;
        }
    }

    //predecessor returns the previous value
    public int predecessor(int key) {
        Terminal.println("Seeking the predecessor to: "+key);
        search(key);    // search sets ptr
        if(ptr != null && ptr.prev[0].key != MIN) {
            Terminal.println("The predecessor to "+key+" is "+ptr.prev[0].key);
            return ptr.prev[0].key;
        }else if(ptr == null) {
            Terminal.println("The key "+key+" was not found in the skiplist.");
            return NOTFOUND;
        }else {
            Terminal.println("There is no predecessor.");
            return NONE;
        }
    }

    //Find the largest value in the skiplist
    public int maximum() {
        int NONE = MIN;
        if (tail.prev[0].key == MIN){
            Terminal.println("skiplist is empty.");
            return NONE;
        }else {
            Terminal.println("Maximum value in skiplist: " + tail.prev[0].key);
            return tail.prev[0].key;
        }
    }

    //Find the smallest value in the skiplist
    public int minimum() {
        int NONE = MAX;
        if (head.next[0].key == MAX){
            Terminal.println("skiplist is empty.");
            return NONE;
        }else {
            Terminal.println("Minimum value in skiplist: " + head.next[0].key);
            return head.next[0].key;
        }
    }

    //Remove returns true if the key was found and deleted
    //
    public boolean remove(int key) {
        ptr = head;
        Terminal.println("Attempting to remove key: "+key);
        if(search(key)) {
            int j = ptr.size - 1;
            for (int i=0; i <= j; i++) {
                ptr.prev[i].next[i] = ptr.next[i];
                ptr.next[i].prev[i] = ptr.prev[i];
                ptr.next[i] = null;
                ptr.prev[i] = null;
            }
            print();
            Terminal.println("Successfully deleted key "+key+".");
            return true;
        } else {
        Terminal.println("It was not found, so key was not deleted.");
        return false;
        }
    }
}

//A SkipNode which has 2 array pointers and a key
class SkipNode {

    public int key;
    public SkipNode[] next;
    public SkipNode[] prev;
    //NOTE: randseq is static and final thus it is not reinstantiated
    //after every creation of a skipNode
    public static final java.util.Random randseq = new Random();
    public int size;

    SkipNode(int key, int arrayLength ) {
        this.key = key;
        size = arrayLength;
        next = new SkipNode[arrayLength];
        prev = new SkipNode[arrayLength];
    }

    SkipNode(int key) {
        int t = 1;
        while(coinFlip()) {
            t++;
        }
        this.key = key;
        size = t;
        next = new SkipNode[t];
        prev = new SkipNode[t];


    }

    boolean coinFlip() {
        return (randseq.nextInt() < 0);
    }
}