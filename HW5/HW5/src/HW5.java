
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/*

  Author: Sasithorn Hannarong
  Email: shannarong2015@my.fit.deu
  Course: CSE2010
  Section: 03
  Description: HW5.java read input and create a skip list with SkipListMap.java, use FakeRandom
    Input:  get filename of actions as args[0] from the command-line arguments
    Output: goes to the standard output (screen), each line has a result for the corresponding action from input

*/

public class HW5 extends DoublyLinkedList{
    public static void main (String args[]) throws IOException{
        SkipListMap skipList = new SkipListMap();
        File file = new File(args[0]);
        Scanner input = new Scanner(file);
        while (input.hasNext()) {
            String action = input.next();
            Double date = 0.0, start = 0.0, end = 0.0;
            String event = "", result = "";
            switch (action) {
            //Output:DisplayEvent date event/none
            case "DisplayEvent":
                date = input.nextDouble();
                Node p = skipList.search(date);
                System.out.print("DisplayEvent " +  String.format("%04.0f", date) + " ");
                if(p.getKey().equals(date)) {
                    System.out.println(p.getElement());
                } else {
                    System.out.println("none");
                }
                break;
                
            //Output:AddEvent date event success/existingDateError
            case "AddEvent":
                date = input.nextDouble();
                event = input.next();
                result = skipList.put(date, event);
                System.out.println("AddEvent " +  String.format("%04.0f", date) + " " + event + " "+ result);
                break;
                
            //Output:DeleteEvent date success/noDateError
            case "DeleteEvent":
                date = input.nextDouble();
                result = skipList.remove(date);
                System.out.println("DeleteEvent " +  String.format("%04.0f", date) + " " + result);
                break;
                
            //Output:DisplayEventsBetweenDates startDate endDate date1:event1 ... or none
            case "DisplayEventsBetweenDates":
                start = input.nextDouble();
                end = input.nextDouble();
                System.out.print("DisplayEventsBetweenDates " +  String.format("%04.0f", start) + " " + String.format("%04.0f", end) + " ");
                result = skipList.subMap(start, end);
                if(result.equals("none")) System.out.print(" none");
                System.out.println();
                break;
                
            //Output:DisplayEventsFromStartDate startDate date1:event1 ... or none
            case "DisplayEventsFromStartDate":
                date = input.nextDouble();
                System.out.print("DisplayEventsFromStartDate " +  String.format("%04.0f", date) + " ");
                result = skipList.ceilingEntry(date);
                if(result.equals("none")) System.out.print(" none");
                System.out.println();
                break;
                
            //Output:DisplayEventsToEndDate endDate date1:event1 ... or none
            case "DisplayEventsToEndDate":
                date = input.nextDouble();
                System.out.print("DisplayEventsToEndDate " +  String.format("%04.0f", date) + " ");
                result = skipList.floorEntry(date);
                if(result.equals("none")) System.out.print(" none");
                System.out.println();
                break;
                
            //Output:DisplayAllEvents date1:event1 ... or none
            case "DisplayAllEvents":
                System.out.print("DisplayAllEvents ");
                result = skipList.printAllKeys();
                if(result.equals("none")) System.out.print("none");
                System.out.println();
                break;
                
            case "PrintSkipList": 
                System.out.println("PrintSkipList");
                skipList.printSkipList();
                break;
                
            default: 
                System.out.println("Invalid action");
                break;
            }
           
        }
    }
}
