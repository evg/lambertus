package nl.evg.business;

public enum PageVarName
{
	CORRNAME("CORRNAAM"),
	STREET("STRAAT");
	
	PageVarName(String key)
	{
		this.key = key;
	}
	
	public final String key;
	
}
