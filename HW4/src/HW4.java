
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.Math.abs;

/*

  Author: Sasithorn Hannarong
  Email: shannarong2015@my.fit.deu
  Course: CSE2010
  Section: 03
  Description: HW4.java 

  Input :   args[0] and args[1] form command-line arguments
            args[0] is InputRatings file. This file contains initial song ratings—each line has the name of
                a customer and 10 song ratings. Each rating is between 1 (poor) and 5 (excellent), or 0 (not rated). 
                The target customer is on the first line.
            args[1] is InputActions file. This file contains lines of actions:
                • AddCustomer customer
                • RemoveCustomer customer
                • AddRating customer song rating
                • EditRating customer song rating
                • RemoveRating customer song
                • GetClosestCustomer
                • RecommendSongs
                • PrintCustomerDistanceRatings
            
  Output : Output goes to the standard output (screen), each line corresponds to an action:
                • AddCustomer customer [existingCustomerError]
                • RemoveCustomer customer [noCustomerError]
                • AddRating customer song rating [existingRatingError]
                • EditRating customer song rating [noRatingError]
                • RemoveRating customer song rating [noRatingError]
                • GetClosestCustomer closestCustomer/none
                • RecommendSongs closestCustomer1/none song1 rating1 song2 rating2 ..., closestCustomer2/none song1 rating1 song2 rating2
                • PrintCustomerDistanceRatings
                                targetCust  rating0 ... rating9
                   distance1    customer1   rating0 ... rating9

    1. discuss how *and* why you could update the distance between x and y using O(1) operations when one rating
is changed, and 

    I reuse s and zigma to recalculate new distance with out using a loop. This method works with all customers except the target.
lets say,   s is a number of songs that both customers have rate
            zigma is sum of |rating x(s) − rating y(s)|
Since we get distance from distance(x, y) = 1/|S| + (1/|S| * sum of |rating x(s) − rating y(s)|), if the rate was changed,
we have to update new sum and if the song that we add rate or romove rate is tha song in S (songs that both customers have rate),
we also have to change value of s. There are 3 cases to update the distance
        1. adding a rate of song 
          - If the adding song is in S then we calculate -> s = s+1  and increase zigma by zigma +=  abs(tarRate[song]-addRate);
          - If the adding song isn't in S, just add new rate to rate[] in the value and don't recalculate anything 
            because this adding doesn't effect s or zigma so distance still be the same
        
        2. editing a rate of song 
          - If the editing song is a song that both target and customer rate -> s = s    and calculate new zigma by
            zigma =  zigma - abs(tarRate[song]-prevRate) + abs(tarRate[song]-addRate) because we replace the old rate with a
            new rate so we have to eliminate the old abs(tarRate[song]-prevRate) first then add new abs(tarRate[song]-addRate)
            to the zigma
          - If the editing song isn't in S, just edit rate[song] in the value and don't recalculate anything 
                because this adding doesn't effect s or zigma so distance still be the same
            
        3. remove a rate of song 
          - If the removing song is a song that both target and customer rate -> s = s-1  and decrease zigma  by
            zigma -= abs(tarRate[song]-prevRate). We decrease s by 1 since now the rate is 0 so this song doesn't in S
            anymore. Finally we also have to set zigma = abs(zigma) because we subtract zigma so it might turn to negative 
            but zigma always be positive (sum of absolute value)
          -  If the removing song isn't in S, just change rate[song]=0 in the value and don't recalculate anything 
        All of this calculation is just a mathemetic method so it's O(c) = O(1)

    However, if we change rate of the target, we have to recalculate all other customer's distances again because this might effect
S and zigma of all customer so the runtime will not be O(1) but O(number of customers)

2. state the method(s) in which program file(s) that implement the distance update in O(1) operations and put the
method(s) near the top of the program file(s).
    The method names distanceUpdate which located in line 93 of this program


*/
public class HW4 {
    private static final int NUM_SONG = 10;
    private static final double NOTCLOSE = 1000000.0;
    
    //update Distance O(1)
    /* caseCal
        1 = adding a rate of song that result in both target and customer rate  -> s = s+1  and increase zigma
        2 = editing a rate of song that both target and customer rate           -> s = s    and change zigma
        3 = remove  a rate of song that both target and customer rate           -> s = s-1  and decrease zigma 
    */
    public static void distanceUpdate(Scanner input, HeapAdaptablePriorityQueue heap, int[] tarRate, int caseCal, String opr) {
        int addRate = 0;
        String name = input.next();
        int song = Integer.parseInt(input.next());
        if(!opr.equals("RemoveRating")) {
            addRate = Integer.parseInt(input.next());
        }
        //if the Customer doesn't exist in the heap
        if(!heap.hasEntry(name, 0)) {
            System.out.println(opr + " " + name + " " + song + " " + addRate + " existingCustomerError");
        } else {
                
        //remove that customer in a heap and assign to c
            Entry c = heap.remove(name);
            List cust = (List) c.getValue();
            double distance = (double) c.getKey();
        //get rate information in cust and assign to rate
            int[] rate = (int[]) cust.get(1);
            //if that song is alreay rated
            if(caseCal == 1 && rate[song] != 0) {
                System.out.println(opr + " " + name + " " + song + " " + addRate + " existingRatingError");
                heap.insert(distance, cust);
            } else {
                //add new rate or edit it and store in cust
                int prevRate = rate[song];
                rate[song] = addRate;
                cust.set(1, rate);
                //Update new distance
                double s = (double) cust.get(2);
                double zigma = (double) cust.get(3);
                if(rate[song] != 0 && tarRate[song]!= 0) { 
                // recalculate new distance because the number of songs that both target and customer rated is increased

                    //case 1
                    if(caseCal == 1) {
                    //increase s by 1 because customer rated a song that target also rate so the number of the songs that both rated is increased
                        s++;
                            
                    //zigma has changed because we have to add abs(x[s] - y[s])
                        zigma +=  abs(tarRate[song]-addRate);
                        distance = (1.0/s)+((1.0/s)*(zigma*1.0));
                    }
                    //case 2
                    else if(caseCal == 2) {
                    /*
                        zigma has changed because we have to eliminate the old abs(x[s] - y[s])
                        and add new abs(x[s] - new rate)
                    */
                        zigma =  zigma - abs(tarRate[song]-prevRate) + abs(tarRate[song]-addRate);
                        distance = (1.0/s)+((1.0/s)*(zigma*1.0));
                    }
                    System.out.println(opr + " " + name + " " + song + " " + addRate);
                } 
                //case 3
                else if (caseCal == 3) {
                    //Check if the song that was removed is in S or not
                    if(prevRate != 0 && tarRate[song]!= 0) {
                    //If we remove the rate of a sogn that both target and customer had rated, s must decrease by 1
                        s--;
                    /*
                        If we remove the song in S, zigma has changed because we remove the rate so we have to 
                        remove the old abs(x[s] - y[s]) too
                    */
                        zigma -= abs(tarRate[song]-prevRate);
                        zigma = abs(zigma);
                        distance = (1.0/s)+((1.0/s)*(zigma*1.0));
                    }
                    System.out.println(opr + " " + name + " " + song + " " + prevRate);
                } else {
                    //Print out put in case just change value inside rate[] but we don't recalculate anything 
                    System.out.println(opr + " " + name + " " + song + " " + addRate);
                }
                //Assign new s and zigma to cust
                cust.set(2, s);
                cust.set(3, zigma);
                distance = abs(distance);
                //store new distance with the update rate in a heap
                heap.insert(distance, cust);
            }
        }
    }
     
    //Calculate distance between target customer(x) and anoother customer(y)
    public static Customer distanceCal (int[] x, int[] y) {
        Customer result = new Customer();
        double s = 0;
        double zigma = 0;
        double distance = NOTCLOSE;
        for(int i=0; i<NUM_SONG; i++) {
            //Songs that both customers x and y have rated and equal to s
            if(x[i] != 0 && y[i] != 0) {
                s++;                                    //increas s
                zigma += abs(x[i]-y[i]);              //Add the absolute difference in ratings to sigma 
            }
        }
        if(s != 0) distance = (1/s)+((1/s)*(zigma));    //avoid /0
        distance = (double) Math.round(distance * 1000) / 1000; //round distance to 3 decimal places 
        distance = abs(distance);
        result.setD(distance);
        result.setS(s);
        result.setZ(zigma);
        return result;
    }
    
    /*  Sort name alphabetically. sortCase determind whether the heap was sent to sort and print or just sort and send back
        If sortCase = 1, sort a heap and print Distance name and rate. Print name alphabetically
        If sortCase = 0, jus sort a heap and send it back, don't print anything
    */
    public static HeapAdaptablePriorityQueue sortHeap (String target, int[] tRate, HeapAdaptablePriorityQueue heap) {
        HeapAdaptablePriorityQueue copyHeap = new HeapAdaptablePriorityQueue();          //copy of the heap. This heap uses distance as a key
        HeapAdaptablePriorityQueue sortHeap = new HeapAdaptablePriorityQueue();         //new heap for sort name. This heap uses name as a key
        //Sort heap that has name as a key
        while(heap.size() > 0) {                                        
            Entry c = heap.removeMin();
                
            //Store key and value
            double distance = (double) c.getKey();                      //Store heap key in name
            List cust = (List) c.getValue();                            //Store value in cust
            copyHeap.insert(distance, cust);                            //construct a copy heap
            
            //subtract information in cust and use for sortCust
            String name = (String) cust.get(0);                     
            int[] rate = (int[]) cust.get(1);

            /*  heap has key = name and value = sortCust
                sortCust components are: 
                sortCust[0] = distance between target and that customer
                cust[1] = rate of 10 songs
            */
            List sortCust = new ArrayList<>();
            sortCust.add(distance);
            sortCust.add(rate);
            sortHeap.insert(name, sortCust);
        }
        printDistance(target, tRate, sortHeap);
        return copyHeap;
    }
    
    ////print Customer Distance Ratings and call printRate to print the rest
    public static void printDistance (String target, int[] tRate, HeapAdaptablePriorityQueue sortHeap) {
    //First, print the target costumer detail
        System.out.print(String.format("%" + 6 + "s", "")); 
        printRate(tRate, target);
    //Then print other costumer details
        while(sortHeap.size() > 0) {
            Entry c = sortHeap.removeMin();
         //Store key and value
            String name = (String) c.getKey();
            List cust = (List) c.getValue();
            
        //subtract information in cust
            double distance = (double) cust.get(0);                     
            int[] rate = (int[]) cust.get(1);
            
            if(distance == NOTCLOSE) System.out.print("n/a   ");
            else System.out.printf("%.3f ",distance);
            printRate(rate, name);
        }
    }
    
    //Print name and rate information. This method used with printDistance
    public static void printRate (int[] rate, String name) {
        String padded = String.format("%-10s", name);                    // Assign additional space after name to complete 10 characters 
        System.out.print(padded);
        for(int i=0; i<NUM_SONG; i++) {
            System.out.print(rate[i]+" ");
        }
        System.out.println();
        
        
    }
    
    //Scann and get rate information of each customer
    public static int[] getRate (Scanner input) {
        int[] rate = new int[NUM_SONG];
        for(int i=0; i<NUM_SONG; i++) {
                int r = input.nextInt();
                rate[i] = r;
        }
        return rate;
    }
    
    
    /*
    
    Each node in heap has key = distance and value = cust (this list)
        cust components are: 
        cust[0] = customer name
        cust[1] = rate of 10 songs
        cust[2] = s = number of songs that both customer and targer rate
        cust[3] = z = zigma of |song that target rate - song that customer rate|
    
    */
    public static List makeList (String n, int[] r, double s, double z) {
        List cust = new ArrayList();
        cust.add(n);
        cust.add(r);
        cust.add(s);
        cust.add(z);
        return cust;
     }
    
    //Use to sort closet customers alphabetically. This method used with action GetClosestCustomer and RecommendSongs
    public static HeapAdaptablePriorityQueue sortClosetCust (HeapAdaptablePriorityQueue heap) {
        HeapAdaptablePriorityQueue storeHeap = new HeapAdaptablePriorityQueue();
        HeapAdaptablePriorityQueue sortName = new HeapAdaptablePriorityQueue();
        /*  
            get minDistance from a heap and use it to compare with another distance incase there is more than
            1 closet customer
        */
        double minDistance = (double) heap.min().getKey();
        if (minDistance == NOTCLOSE) sortName.insert("none", 0);                //No closet customer in a heap
        else {
            double distance = (double) heap.min().getKey();
            while( distance == minDistance) {                                   //Find more closet customer in a heap
                Entry newCust = heap.removeMin();
                List closeCust = (List) newCust.getValue();
                String name = (String)closeCust.get(0);
                int[] rate = (int[])closeCust.get(1);
                distance = (double) newCust.getKey();
                
            /*  Since we removeMin from heap, we have to insert that Entry back the heap after finish exicution
                so I store it temporary in storeHeap and will use this heap to do a re-insert after.
            */
                storeHeap.insert(distance, closeCust);
                
            //Store name and rate of a closet customer in sortName
                sortName.insert(name, rate);
                        
                distance = (double) heap.min().getKey();
            }
            while(storeHeap.size() > 0) {
                Entry newCust = storeHeap.removeMin();
                distance = (double) newCust.getKey();
                List closeCust = (List) newCust.getValue();
                
            //re-insert Entry back to the heap
                heap.insert(distance, closeCust);
            }
        } 
        return sortName;
    }
    public static void main (String args[]) throws IOException{
        File file = new File(args[0]);
        Scanner input = new Scanner(file);
        HeapAdaptablePriorityQueue heap = new HeapAdaptablePriorityQueue();             //use key as a distace                     //heap
    //Read InputRatings file
    //Get information of target customer first
        String tarName = input.next();
        int[] tarRate = getRate(input);
    //Get other customers information
        while (input.hasNext()) {
            //get Input
            String name = input.next();
            int[] rate = getRate(input);
            
            //Assign each important data in List cust and use cust as an value for heap
            Customer c = distanceCal(tarRate, rate);
            Double d = c.getD();
            Double z = c.getZ();
            
            List cust = makeList(name, rate, c.getS(), c.getZ());
            heap.insert(d, cust);
            
        } //End InputRatings file
        
        
        //Read InputActions file
        file = new File(args[1]);
        input = new Scanner(file);
        while (input.hasNext()) {
            String action = input.next();
            //action: AddCustomer customer [existingCustomerError]
            if(action.equals("AddCustomer")) {
                String name = input.next();
                //if the Customer alreay exist in the heap
                if(heap.hasEntry(name, 0)) {
                    System.out.println("AddCustomer " + name + " existingCustomerError");
                } 
                //if not, add the Customer
                else {
                    int[] rate = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    
                    //insert new Customer in heap that has distance as a key
                    List cust = makeList(name, rate, 0.0, 0.0);
                    heap.insert(NOTCLOSE, cust);
                    
                    System.out.println("AddCustomer " + name);
                }
            }
            //action: RemoveCustomer customer [noCustomerError]
            else if(action.equals("RemoveCustomer")) {
                String name = input.next();
                
                //remove custimer form heap that has distance as a key
                List rc = (List) heap.remove(name).getValue();
                
                System.out.println("RemoveCustomer " + rc.get(0));
            }  
            //action: AddRating customer song rating [existingCustomerError]
            else if(action.equals("AddRating")) {
                distanceUpdate(input, heap, tarRate, 1, "AddRating");
    
            }
            //action: EditRating customer song rating [noRatingError]
            else if(action.equals("EditRating")) {
                distanceUpdate(input, heap, tarRate, 2, "EditRating");
                
            }
            //action: RemoveRating customer song rating [noRatingError]
            else if(action.equals("RemoveRating")) {
                distanceUpdate(input, heap, tarRate, 3, "RemoveRating");
            }
            //action: GetClosestCustomer
            else if(action.equals("GetClosestCustomer")) {
                HeapAdaptablePriorityQueue sortName = sortClosetCust(heap);
                HeapAdaptablePriorityQueue copySortName = new HeapAdaptablePriorityQueue();
                System.out.print("GetClosestCustomer ");
                while( sortName.size() > 0 ) {
                    Entry nameCust = sortName.removeMin();
                    String name = (String)nameCust.getKey();
                    int[] rate = new int[10];
                    if(!name.equals("none")) rate = (int[]) nameCust.getValue();
                    System.out.print(name + " ");
                    copySortName.insert(name, rate);
                }
                if(sortName.size() == 0 && copySortName.size()!= 0) sortName = copySortName;
                System.out.println();
            }
            //action: RecommendSongs
            else if(action.equals("RecommendSongs")) {
                HeapAdaptablePriorityQueue sortName = sortClosetCust(heap);
                System.out.print("RecommendSongs");
                if( sortName.size() > 0) {
                    Entry nameCust = sortName.min();
                    String name = (String)nameCust.getKey();
                    if(name.equals("none")) {
                        System.out.print(" none");
                    } else {
                        while( sortName.size() > 0) {
                            //find all songs that has rate >= 4
                            nameCust = sortName.removeMin();
                            name = (String)nameCust.getKey();
                            int[] rate = (int[]) nameCust.getValue();
                            int maxRate = 0;
                            List recommendList = new ArrayList<>();
                            for(int i=0; i<NUM_SONG; i++) {
                                if(tarRate[i] == 0 && rate[i] >3) {
                                    int[] song = new int[2];
                                    if(rate[i] > maxRate) maxRate = rate[i];
                                    song[0] = i;
                                    song[1] = rate[i];
                                    recommendList.add(song);
                                }
                            }
                            if(maxRate >= 4 && maxRate <= 5 ) {
                                System.out.print(" "+name);
                                for(int i=0; i<recommendList.size(); i++) {
                                    int[] song = (int[]) recommendList.get(i);
                                    if(song[1] == maxRate) {
                                        System.out.print(" "+song[0]+" "+song[1]);
                                    }
                                }
                            } else {
                                System.out.print(" none");
                            }
                            if(sortName.size() > 0) System.out.print(",");
                        }
                    }
                } else {
                    System.out.print(" none");
                }
                System.out.println();
            }
            //action: PrintCustomerDistanceRatings
            else if(action.equals("PrintCustomerDistanceRatings")) {
                /*  heap will be delete since it was passed by value to printDistance,
                    so I return a copy of heap to restore in the heap. 
                    Finally heap still be the same.
                */
                System.out.println("PrintCustomerDistanceRatings");
                heap = sortHeap(tarName, tarRate, heap);
            }
        } //End InputActions file
    }
}
