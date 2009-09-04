package nl.evg.business;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class PageCreator
{
	public String[] createPagesFrom(InputStream contentStream)
	{
		return new String[] {getFirst2009correspondent(contentStream)};
	}
	
	private String getFirst2009correspondent(InputStream contentStream) //throws IOException
	{
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		try
		{
			fs = new POIFSFileSystem(contentStream);
			wb = new HSSFWorkbook(fs);
		}
		catch (Exception e) {
			return e.getMessage();
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
				if (is2009(row))
					return getCorrespondent(row);
//				for (int c = 0; c < cols; c++) {
//					cell = row.getCell(c);
//					if (cell != null) {
//						// Your code here
//						System.out.println("row:"+r+" col:"+c+":"+cell);
//					}
//				}
			}
		}
		return "No corr found for 2009";
	} 
	
	private boolean is2009(HSSFRow row)
	{
		HSSFCell cell = row.getCell(3);
		return cell!=null && cell.toString().equals("2009.0");
	}

	private String getCorrespondent(HSSFRow row)
	{
		HSSFCell cell = row.getCell(4);
		return cell.toString();
	}
}
