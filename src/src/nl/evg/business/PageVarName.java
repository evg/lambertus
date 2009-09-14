package nl.evg.business;

public enum PageVarName
{
	ID(0),
	VAK(1),
	VERLENGEN(2),
	JAAR(3),
	CORRNAAM(4),
	STRAAT(5),
	POSTCODE(6),
	WOONPLAATS(7),
	
	OVERLEDENE1(11),
	OVERLDATUM1(12),
	OVERL_PLAATS1(13),
	
	OVERLEDENE2(11),
	OVERLDATUM2(12),
	OVERL_PLAATS2(13),
	
	OVERLEDENE3(11),
	OVERLDATUM3(12),
	OVERL_PLAATS3(13),
	
	OVERLEDENE4(11),
	OVERLDATUM4(12),
	OVERL_PLAATS4(13),
	
	GEBDATUM(14),
	GEB_PLAATS(15),
	FACTUURNR(16);
	
	PageVarName(int index)
	{
		this.index = index;
	}
	
	public final int index;
}
