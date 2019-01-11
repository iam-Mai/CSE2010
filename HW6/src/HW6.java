import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
  Author: Sasithorn Hannarong
  Email: shannarong2015@my.fit.deu
  Course: CSE2010
  Section: 03
  Description: HW6.java uses with Vertice.java
    Input:  2D grid world which the first line has number of rows and columns of the world
            the following lines have a player, monster, goal and obstacle

    Output: 1. 2D grid world before player move with row numbers on the top and column numbers on the left
            2. Player's move
            3. 2D grid world after player move with row numbers on the top and column numbers on the left
            4. Monster move (u/d/l/r), length of the shortest path from each monster to the player
               and the shortest path starting with the monster current position (before the move) 
               and ends with the player position
*/

public class HW6 {
    
    static ArrayList<String> monster = new ArrayList <String>  ();
    static LinkedList<Vertice>  monsterList = new LinkedList <Vertice>  (); 
    static int distance = 0;
    static Vertice end = new Vertice();
    public static char[][] gridConstruct(String[] strIn, int rows, int column) {
        char[][] grid = new char[rows][column];
        for(int i =0; i< rows ; i++) {
            for(int j =0; j<column ; j++) {
                grid[i][j] = strIn[i].charAt(j);
            }
        }
        return grid;
    }
    
    //bfs use to find the shortest path from (x,y) to goal in grid
    public static LinkedList<Vertice> bfs(int x, int y, char[][] grid, char goal){
        distance = 0;
        Queue <Vertice> queue = new LinkedList <Vertice> ();                    //used to perfrom BFS
        LinkedList<Vertice>  path = new LinkedList <Vertice>  ();               //Used to store the shortest path
        boolean isCorrect = false;
        //Since path is a global variable and reuseable, I clear it everytime before use
        path.clear();
        
        Vertice v = new Vertice(x,y);           //the start vertice
        Vertice u = new Vertice();              //the vertice that dequeue from the queue
        queue.add(v);                           //Enqueue the start position
        while (queue.peek() != null){
            u = queue.remove();
        //Enqueue u childrens in 4 position (up/down/left/right)
        //1.)Child vertice above u
            if(u.x-1 >= 0 && grid[u.x-1][u.y] != '#' && !(grid[u.x-1][u.y] >= 'A' && grid[u.x-1][u.y] <= 'Z')){
                Vertice temp = new Vertice(u.x-1, u.y, u, u.distance+1);
                
                //If the vertice above u is a goal, end search and return a path
                if (grid[u.x-1][u.y] == goal){
                    return tracePath(temp);  
                }

                /*  Else enqueue that vertice and mark it as # mean it was visited and 
                    avoid visit again (same as an obstacle)
                */
                else {
                    queue.add(temp);
                    grid[u.x-1][u.y] = '#';
                    temp.parent = u;
                }
            }
        //2.)Child vertice below u
            if(u.x+1 < grid.length && grid[u.x+1][u.y] != '#' && !(grid[u.x+1][u.y] >= 'A' && grid[u.x+1][u.y] <= 'Z')){
                Vertice temp = new Vertice(u.x+1, u.y, u, u.distance+1);
                //If the vertice below u is a goal, end search and return a path
                if (grid[u.x+1][u.y] == goal){
                    return tracePath(temp); 
                } else {
                    queue.add(temp);
                    grid[u.x+1][u.y] = '#';
                    temp.parent = u;
                }
            }
        //3.)Child vertice next u
            if(u.y+1 < grid.length && grid[u.x][u.y+1] != '#'&& !(grid[u.x][u.y+1] >= 'A' && grid[u.x][u.y+1] <= 'Z')){
                Vertice temp = new Vertice(u.x, u.y+1, u, u.distance+1);
                //If the vertice next u is a goal, end search and return a path
                if (grid[u.x][u.y+1] == goal){
                    return tracePath(temp);  
                } else {
                    queue.add(temp);
                    grid[u.x][u.y+1] = '#';
                    temp.parent = u;
                }
            }
        //4.)Child vertice before u
            if(u.y-1 >= 0 && grid[u.x][u.y-1] != '#'&& !(grid[u.x][u.y-1] >= 'A' && grid[u.x][u.y-1] <= 'Z')){
                Vertice temp = new Vertice(u.x, u.y-1, u, u.distance+1);
                //If the vertice before u is a goal, end search and return a path
                if (grid[u.x][u.y-1] == goal){
                    return tracePath(temp); 
                } else {
                    queue.add(temp);
                    grid[u.x][u.y-1] = '#';
                    temp.parent = u;
                }
            }
            
        }
        return null;        //No path
    }
    
    //tracePath make path from the goal to temp (path from player to monster) which work as a stack
    public static LinkedList<Vertice> tracePath(Vertice temp){
        LinkedList<Vertice>  path = new LinkedList <Vertice>  ();
        path.add(temp);
        while(temp.parent != null) {
            temp = temp.parent;
            path.addLast(temp);
            distance++;
        }
        return path; 
    }
    
    //printPath prints all elements in path which get from tracePath
    public static void printPath(LinkedList<Vertice> pPath) {
        while (pPath.peek() != null){
            //Since path is a stack, we have to push each vertice (removeLast) before print it;
            Vertice u = pPath.removeLast();
            System.out.print("(" + u.x + "," + u.y + ") ");
        }
        System.out.println();
    }
    
    //findPos find player's position and monsters' positions in the grid
    public static int[] findPos(char p, char[][] grid) {
        int[] pos = {-1,-1};
        for(int i =0; i< grid.length ; i++) {
            for(int j =0; j<grid[0].length ; j++) {
                if(grid[i][j] == p) {
                    pos[0] = i;
                    pos[1] = j;
                } else if(grid[i][j] >= 'A' && grid[i][j] <= 'Z') {
                    monster.add(grid[i][j]+"");
                }
            }
        }
        //Sort monster alphabetically
        Collections.sort(monster);
        //Find all monster 
        while(monster.size() > 0) {
            char m  = monster.remove(0).charAt(0);
            for(int i =0; i< grid.length ; i++) {
                for(int j =0; j<grid[0].length ; j++) {
                    if(grid[i][j] == m) {
                        Vertice mons = new Vertice(grid[i][j], i, j);
                        mons.element = grid[i][j];
                        monsterList.add(mons);
                    }
                }
            }
        }
        return pos;
    }
    
    //printGrid prints the world with row numbers on the top and column numbers on the left
    public static void printGrid(char[][] grid) {
        System.out.print(" ");
        for(int j = 0; j<grid[0].length ; j++) {
                System.out.print(j);
        }
        System.out.println();
        for(int i = 0; i< grid.length ; i++) {
            System.out.print(i);
            for(int j = 0; j<grid[0].length ; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
    
    // return 0 = not end keep playing, 1 = player win, -1 = player lost
    public static int outPut(char[][] grid) {
        int status = 0;
        boolean isError = false;
        /****** Output ******/
        //1. Display 2D grid world before player move
        printGrid(grid);
        int[] position = findPos('0', grid);                                    //Find player's position
        int x = position[0], y = position[1];
        char move = ' ';
        do {
            System.out.print("Player 0, please enter your move [u(p), d(own), l(elf), or r(ight)]: ");
            Scanner scanner = new Scanner(System.in);
            String in = scanner.next();
            move = in.charAt(0);
            if(move == 'u' || move == 'd' || move == 'l' || move == 'r') {
                grid[x][y] = ' ';
                isError = false;
            }
            else {
                isError = true;
            }
        
        //2. Display Player's move and 2D grid world after player move
        //Decide which cell will change
        switch (move) {
            case 'u': // x > x-1 & y = y
                if( x==0 || grid[x-1][y] == '#') {
                    System.out.println("You can’t move in this direction, please re-enter");
                    isError = true;
                } else {
                    x = x-1;
                }
                break;
            case 'd': // x > x+1 & y = 1
                if(x==grid.length || grid[x+1][y] == '#') {
                    System.out.println("You can’t move in this direction, please re-enter");
                    isError = true;
                } else {
                    x = x+1;
                }
                break;
            case 'l': // x = x & y = y-1
                if(y==0 || grid[x][y-1] == '#') {
                    System.out.println("You can’t move in this direction, please re-enter");
                    isError = true;
                } else {
                   y = y-1;
                }
                break;
            case 'r': // x = x & y = y+1
                if( y == grid[0].length || grid[x][y+1] == '#') {
                    System.out.println("You can’t move in this direction, please re-enter");
                    isError = true;
                } else {
                    y = y+1;
                }
                break;
            default: 
                System.out.println("Invalid move, please re-enter");
                isError = true;
                break; 
            } 
        } while (isError);
        
        //Move player
        if(grid[x][y] >= 'A' && grid[x][y] <= 'Z') {
            status = -1;
        } else if(grid[x][y] == '*') {
            status = 1;
            end.x = x;
            end.y = y;
        } else {
            grid[x][y] = '0';
            printGrid(grid);
        }
        
        /*  4. Display Monster move */
        char [][] grid2 = new char[grid.length][];
            //Copy grid world before find shortest path
            for(int i = 0; i < grid.length; i++)
                    grid2[i] = grid[i].clone();
            
        while(!monsterList.isEmpty()) {
            Vertice monsV = monsterList.remove();
            char [][] copy = new char[grid2.length][];
            //Copy grid world before find shortest path
            for(int i = 0; i < grid2.length; i++)
                    copy[i] = grid2[i].clone();
            
            LinkedList<Vertice> path = bfs(monsV.x,monsV.y,copy,'0');
            
            if(path == null) {
                System.out.println("Creature " + monsV.element + " : " + "No shortest path");
            }
            else {
                
                Vertice c = path.getLast();                                 //Monster's cuurent position (before move)
                Vertice f = path.get(path.size()-2);                        //First direction that monster will move
                
                char m = '0';
                grid[c.x][c.y] = ' ';
                //If its move is up
                if(c.x > f.x) {
                    m = 'u';
                }
                //If its move is down
                else if(c.x < f.x) {
                    m = 'd';
                }
                //If its move is right
                else if(c.y < f.y) {
                    m = 'r';
                }
                //If its move is left
                else {
                    m = 'l';
                }
                
                if(!(grid2[f.x][f.y] >= 'A' && grid2[f.x][f.y] <= 'Z') && grid2[f.x][f.y] != '#') {
                        if(grid2[f.x][f.y] == '0') {
                            status = -1;
                            end.x = f.x;
                            end.y = f.y;
                            end.element = monsV.element;
                            grid2[c.x][c.y] = ' ';
                        } else {
                            grid[f.x][f.y] = monsV.element;
                            grid2[f.x][f.y] = '#';
                            grid2[c.x][c.y] = ' ';
                        }
                }
                System.out.print("Creature " + monsV.element + ":" + " " + m + " " + distance + " ");
                printPath(path);
            }
        }
        return status;
        /****** End Output ******/   
    }
    
    public static void main(String args[]) throws IOException{
    /****** Get Input ******/
        File file = new File(args[0]);
        Scanner input = new Scanner(file);
        int rows = input.nextInt();
        int column = input.nextInt();
        int line = 0;
        String[] strIn = new String[rows];                                      //Use to get input
        input.nextLine();                                                       //Avoid empty /n from the 1st line
        while (input.hasNext() && line < rows) {
            strIn[line] = input.nextLine();
            line++;
        }
        char[][] grid = gridConstruct(strIn, rows, column);                     //Construct a grid
    /****** End Get Input ******/
    
        int result = outPut(grid);
        if(result == 1) {
            printGrid(grid);
            System.out.println("Player 0 beats the hungry creatures!");
        }
        else if (result == -1) {
            printGrid(grid);
            System.out.println("One creature is not hungry any more!");
        }
    }
}
