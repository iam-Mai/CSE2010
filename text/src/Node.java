import java.util.ArrayList;

public class Node implements Comparable<Node>
{
	public Word partial;
	public Node parent;
	public ArrayList<Node> children;
	public Triple topThree;
	public boolean isWord;
	public boolean isLeaf;
	public boolean wasGuessed;
	public int depth;
	
	public Node(String w, Node p)
	{
		this(new Word(w, 0), p);
	}
	
	public Node(Word w, Node p)
	{
		this.partial = w;
		this.parent = p;
		this.children = new ArrayList<Node>();
		this.topThree = new Triple();
		this.isWord = false;
		this.isLeaf = false;
		this.wasGuessed = false;
		this.depth = 0;
	}
	
	public boolean hasChild(String c)
	{
		return children.contains(new Node(c, parent));
	}
	
	public boolean equals(Object o)
	{
		Node b = (Node) o;
		
		return this.partial.equals(b.partial);
	}
	
	public int compareTo(Node o)
	{
		return this.partial.compareTo(o.partial);
	}
	
	public Word getWord()
	{
		return new Word(this.toString(), this.partial.FREQUENCY);
	}
	
	public String toString()
	{
		String output = "";
		
		System.out.println("THIS: " + this.partial.WORD);
		
		for(Node n = this; !n.partial.WORD.equals(""); n = n.parent)
		{
			output = n.partial.WORD + output;
			
			System.out.println("PARENT: " + n.parent.partial.WORD);
		}
		
		return output;
	}
}