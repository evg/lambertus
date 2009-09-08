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

		for (int r = 0; r < rows; r++) {
			row = sheet.getRow(r);
			if (row != null) {
				if (isRightYear(row, year))
				{
					PageVars pageVars = new PageVars();
					fill(pageVars,row);
					result.add(pageVars);
				}
			}
		}
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
			if (name.equals(PageVarName.OVERLDATUM))
				value = getDateValue(cell);
			pageVars.set(name, value);
		}
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
