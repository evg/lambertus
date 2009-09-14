package nl.evg.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	public void addOverledene(String[] overledene)
	{
		PageVarName[] names = getNames(index);
		for(int i=0; i<overledene.length; i++)
			values.put(names[i],overledene[i]);
		index++;
	}
	
	public int getNofOverledenen()
	{
		return index-1;
	}
	
	private PageVarName[] getNames(int index)
	{
		if (index==1)
			return new PageVarName[]{PageVarName.OVERLEDENE1, PageVarName.OVERLDATUM1, PageVarName.OVERL_PLAATS1};
		if (index==2)
			return new PageVarName[]{PageVarName.OVERLEDENE2, PageVarName.OVERLDATUM2, PageVarName.OVERL_PLAATS2};
		if (index==3)
			return new PageVarName[]{PageVarName.OVERLEDENE3, PageVarName.OVERLDATUM3, PageVarName.OVERL_PLAATS3};
		if (index==4)
			return new PageVarName[]{PageVarName.OVERLEDENE4, PageVarName.OVERLDATUM4, PageVarName.OVERL_PLAATS4};
		throw new IllegalArgumentException("Max. 4 overledenen per correspondent ondersteund");
	}
	
	int index = 1;
	private Map<PageVarName,String> values = new HashMap<PageVarName,String>();
}
