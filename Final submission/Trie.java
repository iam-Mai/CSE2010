import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        getRoot: return root
        hash: constucts hashmap at each level
*/

public class Trie {
   private final TrieNode root;
   List<TrieNode> guessList = new ArrayList<>();        //Store all words that already was guessed
   //Constructor
   public Trie() {
      root = new TrieNode();
   }
   
   //addWord adds a word to the Trie
   public TrieNode addWord(String word) {
      return root.addWord(word.toLowerCase());
   }
   
   //hash constucts hashmap at each level
   public boolean hash(char c) {
      TrieNode node = root.getNode(c);;
      //return false if c doesn't exist in the Trie
      if (node == null) return false;
      List<TrieNode> list = node.guessWords();
      while(list.size() > 0) {
          TrieNode n = list.remove(0);
          n.hash();
      }
      return true;
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
      
      List<TrieNode> guessNode = new ArrayList<>();         //Store 3 node that contains 3 guesses words
      String[] guesses = new String[3];                     //Store 3 guesses words
      List<Integer> keys = new ArrayList(node.childrenHash.keySet());
      
      // create comparator for reverse order (max to min)
      Comparator cmp = Collections.reverseOrder();
      
      // sort the list
      Collections.sort(keys, cmp);
      int i = 0;
      while(guessNode.size() < 3 && i < keys.size()) {
          int k = keys.get(i);
          TrieNode n = node.childrenHash.get(k);
          if(n.isGuess == false) {
            guesses[guessNode.size()] = n.toString();
            guessNode.add(n);
            n.isGuess = true;
            guessList.add(n);
          }
          i++;
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
