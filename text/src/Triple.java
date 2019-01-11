
public class Triple {
	public Node FIRST;
	public Node SECOND;
	public Node THIRD;
	
	public Triple()
	{
		FIRST = SECOND = THIRD = null;
	}
	
	public void addNode(Node n)
	{
		if(FIRST == null || n.partial.FREQUENCY > FIRST.partial.FREQUENCY)
		{
			THIRD = SECOND;
			SECOND = FIRST;
			FIRST = n;
		}
		else if(SECOND == null || n.partial.FREQUENCY > SECOND.partial.FREQUENCY)
		{
			THIRD = SECOND;
			SECOND = n;
		}
		else if(THIRD == null || n.partial.FREQUENCY > THIRD.partial.FREQUENCY)
		{
			THIRD = n;
		}
	}
	
	public String verbose()
	{
		return	String.format("First: \t%-16sFrequency: %d\n", FIRST, FIRST.partial.FREQUENCY) +
				String.format("Second:\t%-16sFrequency: %d\n", SECOND, SECOND.partial.FREQUENCY) +
				String.format("Third: \t%-16sFrequency: %d\n", THIRD, THIRD.partial.FREQUENCY);
	}
	
	public String toString()
	{
		return String.format("First: \t%-16s\nSecond:\t%-16s\nThird: \t%-16s", FIRST, SECOND, THIRD);
	}
}
