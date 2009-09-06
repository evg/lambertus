package nl.evg.business;

import java.util.HashMap;
import java.util.Map;

public class PageVars
{
	public String get(PageVarName name)
	{
		return values.get(name);
	}
	
	public void set(PageVarName name, String value)
	{
		values.put(name, value);
	}
	
	private Map<PageVarName,String> values = new HashMap<PageVarName,String>();
}
