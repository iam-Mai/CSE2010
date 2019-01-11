import java.util.ArrayList;
import java.util.List;

/*
  Group name: 3C
  Course: CSE 2010
  Section: 3

  Description of the Trie algorithm:
        Trie class construct a Trie structure which each node is a TrieNode. This Trie can do: 
        addWord: add new word to itself
        guessWords: travel inside itself to get 3 most frequent words that contain the given prefix
        reset: clear useless data
        Root: return root
*/

public class Trie {
   private final TrieNode root;
   List<TrieNode> guessList = new ArrayList<>();        //Store all words that already was guessed
   //Constructor
   public Trie() {
      root = new TrieNode();
   }
   
   //addWord adds a word to the Trie
   public void addWord(String word) {
      root.addWord(word.toLowerCase());
   }
   
   //guessWords guess 3 words in the Trie with the given prefix
   public String[] guessWords(String prefix) {
      TrieNode node = root;
      
      //Find the postion of node that contain the given prefix inside the Trie
      for (int i=0; i<prefix.length(); i++) {
        node = node.getNode(prefix.charAt(i));
        //return empty string if the wanted node doesn't exist in the Trie
        if (node == null) return new String[3];
      } 
      
      int[] mostFrequences = new int[3];                    //Store index of 3 most frequent words
      List<TrieNode> list = node.guessWords();              //Store all words begin with the givin prefix
      List<TrieNode> guessNode = new ArrayList<>();         //Store 3 node that contains 3 guesses words
      String[] guesses = new String[3];                     //Store 3 guesses words
      
      //initialize size of guessNode
      for (int i = 0; i < 3; i++) {
          TrieNode n = new TrieNode();
          guessNode.add(n);
      }
      
      //Find the 3 most frequent words
      for(int i=0; i<list.size();i++) {
          int f = list.get(i).frequence;
          boolean isGuess = list.get(i).isGuess;
          if(f > mostFrequences[0] && !isGuess) {
              mostFrequences[0] = f;
              guesses[0] = list.get(i).toString();
              guessNode.set(0, list.get(i));
          } else if(f > mostFrequences[1] && !isGuess) {
              mostFrequences[1] = f;
              guesses[1] = list.get(i).toString();
              guessNode.set(1, list.get(i));
          }  else if(f > mostFrequences[2] && !isGuess) {
              mostFrequences[2] = f;
              guesses[2] = list.get(i).toString();
              guessNode.set(2, list.get(i));
          } 
      }
      
      //Marks each node that contain the guess words as "already guess"
      for (int i = 0; i < 3; i++) {
          guessNode.get(i).isGuess = true;
          guessList.add(guessNode.get(i));
      }
      return guesses;
   }
   
   //reset runs when user finish typing each word. It will reset all words in guessList
   // as unguess (to use with new input) and clear the guessList
   public void reset() {
       for (int i = 0; i < guessList.size(); i++) {
          guessList.get(i).isGuess = false;
       }
       guessList.clear();
   }
   
   //getRoot returns root of the Trie
   public TrieNode getRoot() {
       return this.root;
   }
}
