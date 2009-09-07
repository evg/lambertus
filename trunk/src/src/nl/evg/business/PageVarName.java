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
	OVERLEDENE(11),
	OVERLDATUM(12),
	OVERL_PLAATS(13),
	GEBDATUM(14),
	GEB_PLAATS(15),
	FACTUURNR(16);
	
	PageVarName(int index)
	{
		this.index = index;
	}
	
	public final int index;
}
