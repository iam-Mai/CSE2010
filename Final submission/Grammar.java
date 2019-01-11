import java.util.Arrays;

public class Grammar {
	/*
	 * processes to find the most likely word to follow the previous one
	 * 
	 * @params word - word to be type casted
	 *  return - word type as string for now
	 */
	public String simple (String word)
    {
		//words in which a form of "is" will likely follow
		String[] pronouns = {"I","you","he","she","they","we"};
		if (Arrays.asList(pronouns).contains(word)) {
    		return "isPronoun";
    	}
		
    	//words in which "the, a, it" are most common to follow
    	String[] prepositions = {"with", "to", "in",  "will", "not","which",
    							 "and",  "of", "that","only", "on",
    							 "be",   "or", "have","for",  "is",
    							 "as",   "at", "but", "out",  "if",
    							 "about","get","put", "would","what"
    							};
    	//boolean contains = Arrays.asList(commonWords).contains(word);
    	//the word is in prepositions
    	if (Arrays.asList(prepositions).contains(word)) {
    		//String[] mostFreq = {"the", "a","it"};
    		return "isPrepos";
    	}
    	//if word is 4+ letters, should be checked for verb/adjective/noun ending
    	if (word.length() > 3) {
    		/*if (word.substring(word.length()-3, word.length()).equals("ing")) {
    			return "verb";
    		}*/
    		switch (word.substring(word.length()-3, word.length()))//last three letters
    		{
    		//verb endings
    		case "ing":
    		//case "":
    			return "isVerb";
    		//adjective endings
    		case "ary":
    		case "ful":
    		case "ish":
    		case "ous":
    		case "ive":
    			return "isAdj";
    		//noun ending
    		case "ies":
    		case "oes":
    		case "ves":
    			return "isNoun";
    		}
    	} 
    	
    	return "";//placeholder EDIT LATER
    	//same for adjectives otherwise probably a noun
    }
}
