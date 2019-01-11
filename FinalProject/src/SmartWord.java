
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*

  Authors (group members): Sasithorn Hannarong(leader), Shea Ackerman, George Nelson, Maya Prempin
  Email addresses of group members: shannarong2015@my.fit.edu
                                    sakerman2014@my.fit.edu
                                    gnelson2015@my.fit.edu 
                                    mprempin215@my.fit.edu
                                    
  Group name: 3C
  Course: CSE 2010
  Section: 3

  Description of the overall algorithm:
        SwartWord.java uses Trie.java class to implement Trie structure. 
        input is 2 files:
        args[0] is words.txt which store words in English
        args[1] is oldMessageFile which store English article
        
        output: 3 string store in guesses which guess function will return guesses to the method that calling it
*/


public class SmartWord {
    String prev = "";
    String[] guesses = new String[3];   // 3 guesses words from SmartWord
    String userWord = new String();     // store sequence of char that use to guess the word
    int prevWord = 0;
    static Trie t;
    static TrieNode root;
     
    // initialize SmartWord with a file of English words
    public SmartWord(String wordFile) throws FileNotFoundException {
        File file = new File(wordFile);
        Scanner input = new Scanner(file);
        t = new Trie();
        root = t.getRoot();
        while (input.hasNext()) {
            String word = input.next();
            t.addWord(word);
        }
    }

    // process old messages from oldMessageFile
    public void processOldMessages(String oldMessageFile) throws FileNotFoundException {   
        System.out.println("processOldMessages ");
        File file = new File(oldMessageFile);
        Scanner input = new Scanner(file);
        //Perform operations for each line in the file
        while(input.hasNextLine()){
            //Each line is split into words
            String in =  input.nextLine().replaceAll("\\s+", " ");
            
            String[]words = in.split(" ");
            //This for loop will go through each and every word in the file
            for(int indexWord=0; indexWord < words.length; indexWord++){
                words[indexWord] = words[indexWord].replaceAll("[^a-zA-Z]", "").toLowerCase();
                 
                //Remove punctuation from each word and add it to the Trie
                if(!words[indexWord].matches("^.*[^a-zA-Z0-9 ].*$") && !words[indexWord].isEmpty()) {
                    t.addWord(words[indexWord]);
                }
            }
        }
        //constuct hashmap for each level in trie
        for(char c = 'a'; c <= 'z' ; c++) {
            t.mapT(c);
        }
    }

    // based on a letter typed in by the user, return 3 word guesses in an array
    // letter: letter typed in by the user
    // letterPosition:  position of the letter in the word, starts from 0
    // wordPosition: position of the word in a message, starts from 0
    public String[] guess(char letter,  int letterPosition, int wordPosition)
    {   //If the user begin new word, call resetWord() and update new word position
        if(wordPosition != prevWord) {
            prevWord = wordPosition;
            prev = userWord;
            resetWord();
        }
        
        //Adding new char to the sequence inorder to get all characters that user already type.
        userWord += letter;
	guesses = t.guessWords(userWord, prev);
        return guesses;
    }
    
    //resetWord() resets the userWord to an empty string and reset all guess words
    public void resetWord(){
        userWord = "";
        t.reset();
    }
    // feedback on the 3 guesses from the user
    // isCorrectGuess: true if one of the guesses is correct
    // correctWord: 3 cases:
    // a.  correct word if one of the guesses is correct
    // b.  null if none of the guesses is correct, before the user has typed in 
    //            the last letter
    // c.  correct word if none of the guesses is correct, and the user has 
    //            typed in the last letter
    // That is:
    // Case       isCorrectGuess      correctWord   
    // a.         true                correct word
    // b.         false               null
    // c.         false               correct word
    public void feedback(boolean isCorrectGuess, String correctWord)        
    {   
        if(isCorrectGuess) {
            //case a
            resetWord();
        }
        else if(!isCorrectGuess && correctWord == null) {
            //case b
        }
        else {
            //case c
        } 
    }
}