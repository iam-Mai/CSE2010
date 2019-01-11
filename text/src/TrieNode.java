import java.util.ArrayList;
import java.util.List;
/*
  Group name: 3C
  Course: CSE 2010
  Section: 3

  Description of the TrieNode algorithm:
        TrieNode is a node inside Trie structure. Its components are:
        parent, children, character, isLeaf, isWord, isGuess, frequence
*/

public class TrieNode {
   private TrieNode parent;     //parent node of this node
   private TrieNode[] children; //all children node of this node
   private char character;      //Data stores in this node
   private boolean isLeaf;      //isLeaf checks if any children exist
   private boolean isWord;      //isWord checks if the node represent the last character of a word
   public boolean isGuess;      //isGuess checks if this word is already guess or not
   public int frequence = 0;    //Store frequence of this word
  
   
   //TrieNode for initialize root
   public TrieNode() {
      this.children = new TrieNode[26];
      this.isLeaf = true;
      this.isWord = false;
      this.isGuess = false;
      frequence = 0;
   }

   //TrieNode for initialize for child node
   public TrieNode(char character) {
      this();
      this.character = character;
      this.isGuess = false;
      frequence = 0;
   }
   
   //addWord add a word to the Trie and count a frequence of that word
   public TrieNode addWord(String word) {
      isLeaf = false;
      isGuess = false;
      int index =  getIndex(word.charAt(0));
      
      //If this character doesn't exist in a Trie, add the new TrieNode to the trie
      if (children[index] == null) {
          children[index] = new TrieNode(word.charAt(0));
          children[index].parent = this;
      }
      
      if (word.length() > 1) {                  //Run recursive to add other characters in the word
          children[index].addWord(word.substring(1));
      } else {                                  //No more character left, addWord is complete
          children[index].isWord = true;
          children[index].frequence += 1;
      }
      return children[index];
   }
   
   //getNode returns the child TrieNode that contains char c, or null if no node exists.
   public TrieNode getNode(char c) {
      return children[getIndex(c)];
   }
   
   //guessWords returns 3 words that have givien prefix and have most frequences in the Trie.
   public List<TrieNode> guessWords() {
      List<TrieNode> list = new ArrayList<>();              //List store of all words of this node
      //If this node represents a word, add it to the list
      if (isWord) {
        list.add(this);
      }
      
      //If this node isn't a word, keep travelling until reach the leaf (which is word)
        if (!isLeaf) {
          //Add any words belonging to any children
          for (TrieNode children1 : children) {
              if (children1 != null) {
                  list.addAll(children1.guessWords());
              }
          }
        }
        return (List<TrieNode>)list; 
    }
   
    //getIndex returns index of this node
    public int getIndex(char c) {
        //This used to calculate the index begin from a = 0 to z = 26
        return c - 'a';
    }
    
    //toString returns the String that this node represents
   @Override
    public String toString() {
        if (parent == null) {
            return "";
        } else {
            return parent.toString() + new String(new char[] {character});
        }
    } 
}
