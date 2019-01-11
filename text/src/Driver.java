import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Driver
{

	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner fileDictionary = new Scanner(new File(args[0]));
		Scanner fileOldWords = new Scanner(new File(args[1]));
		//Scanner fileNewWords = new Scanner(new File(args[2]));
		
		ArrayList<Word> listWords = new ArrayList<Word>();
		
		// IMPORT DICTIONARY
		while(fileDictionary.hasNext())
		{       
			Word word = new Word(fileDictionary.next());
			System.out.println("Read " + word);
			if(listWords.contains(word))
				listWords.get(listWords.indexOf(word)).FREQUENCY++;
			else
				listWords.add(word);
		}
		
		fileDictionary.close();
		
		// IMPORT PREVIOUSLY USED WORDS
		while(fileOldWords.hasNext())
		{
			Word word = new Word(fileOldWords.next());
			
			if(listWords.contains(word))
				listWords.get(listWords.indexOf(word)).FREQUENCY++;
			else
				listWords.add(word);
		}
		
		fileOldWords.close();
		
		Collections.sort(listWords);
		
		// MAKE TRIE
		Trie t = new Trie(listWords);
		// COMPRESSION REPEATS LAST LETTER
		//t.compress();
		
		// STRING TO SEARCH FOR
		String partialSearch = "b";
		
		// PRINT TOP THREE RESULTS
		System.out.println(t.findPartial(partialSearch));
		for(Word w : listWords)
		{
			if(w.WORD.startsWith(partialSearch))
				System.out.println(w);
		}
	}
}