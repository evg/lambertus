package nl.evg.business;

public class Page
{
	public String getText()
	{
		return text;
	}
	
	public void add(String text)
	{
		this.text = text;
	}
	
	private String text;
}
