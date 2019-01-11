public class Word implements Comparable<Word>
{
	public final String WORD;
	public int FREQUENCY;
	
	public Word(String w)
	{
		this(w, 1);
	}
	
	public Word(String w, int f)
	{
		this.WORD = w;
		this.FREQUENCY = f;
	}
	
	public boolean equals(Object o)
	{
		Word b = (Word) o;
		
		return this.WORD.equals(b.WORD);
	}

	public int compareTo(Word o)
	{
		return this.WORD.compareTo(o.WORD);
	}
	
	public String toString()
	{
		return WORD + " : " + FREQUENCY;
	}
}