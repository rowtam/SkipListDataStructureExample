// Ronnie Tam
// March 21, 1999
// Lab3.java
// Computer Science 241
// Washington University in St. Louis


public class Lab3 {

    public static void main(String args[]){
        Terminal.startTranscript("transcript2.txt");
        SkipList list = new SkipList(true);
        list.insert(15);
        list.insert(25);
        list.insert(87);
        list.insert(102);
        list.insert(241);
        list.insert(333);
        list.insert(441);
        list.insert(630);
        list.insert(727);
        list.insert(827);
        list.remove(12);    //12 is not in list
        list.remove(15);
        list.successor(102);
        list.remove(630);
        list.search(85);  //85 not in list
        list.predecessor(15);
        list.successor(827);
        list.predecessor(102);
        list.successor(102);
        list.predecessor(441); //333
        list.successor(441); //727
        list.maximum(); //827
        list.minimum(); //25
        Terminal.stopTranscript();
    }
}