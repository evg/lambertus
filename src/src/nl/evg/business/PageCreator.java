package nl.evg.business;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class PageCreator
{
	public List<PageVars> createPagesFrom(InputStream contentStream, String year)
	{
		List<PageVars> result = new ArrayList<PageVars>();
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		try
		{
			fs = new POIFSFileSystem(contentStream);
			wb = new HSSFWorkbook(fs);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;

		int rows; // No of rows
		rows = sheet.getPhysicalNumberOfRows();

		int cols = 0; // No of columns
		int tmp = 0;

		// This trick ensures that we get the data properly even if it
		// doesn't start from first few rows
		for (int i = 0; i < 10 || i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				tmp = sheet.getRow(i).getPhysicalNumberOfCells();
				if (tmp > cols)
					cols = tmp;
			}
		}

		String lastId = "unknown";
		for (int r = 0; r < rows; r++) {
			row = sheet.getRow(r);
			if (row != null) {
				if (isRightYear(row, year))
				{
					PageVars pageVars = new PageVars();
					if (getId(row).equals(lastId))
					{
						pageVars = result.get(result.size()-1);
					}	
					else
					{
						fill(pageVars,row);
						result.add(pageVars);
					}
					pageVars.addOverledene(getOverledeneFrom(row));
					lastId = pageVars.get(PageVarName.ID);
				}
			}
		}
		return result;
	} 
	
	private String[] getOverledeneFrom(HSSFRow row)
	{
		String[] result = new String[3];
		result[0]="";
		result[1]="";
		result[2]="";
		HSSFCell cell = row.getCell(PageVarName.OVERLEDENE1.index);
		if (cell!=null)
			result[0]= cell.toString();
		cell = row.getCell(PageVarName.OVERLDATUM1.index);
		if (cell!=null)
			result[1]= getDateValue(cell);
		cell = row.getCell(PageVarName.OVERL_PLAATS1.index);
		if (cell!=null)
			result[2]= cell.toString();
		return result;
	}
	
	private void fill(PageVars pageVars, HSSFRow row)
	{
		for(PageVarName name: PageVarName.values())
		{
			HSSFCell cell = row.getCell(name.index);
			if (cell==null)
				continue;
			String value = cell.toString();
			if (name.index<PageVarName.OVERLEDENE1.index)
				pageVars.set(name, value);
		}
	}
	
	private String getId(HSSFRow row)
	{
		HSSFCell cell = row.getCell(PageVarName.ID.index);
		return cell.toString();
	}
	
	private String getDateValue(HSSFCell cell)
	{
		Date date = cell.getDateCellValue();
		DateFormat formatter = new SimpleDateFormat("d-M-yyyy");
		return formatter.format(date);
	}
	
	private boolean isRightYear(HSSFRow row, String year)
	{
		HSSFCell cell = row.getCell(3);
		return cell!=null && cell.toString().equals(year+".0");
	}
}
