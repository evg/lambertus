package nl.evg;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class CreatePdfFromText {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception 
	{
		new CreatePdfFromText().makeDoc();

	}

	final static String outputFolder = "/tmp/";

	private static Document document;

	public static void makeDoc()
	      throws DocumentException, MalformedURLException, IOException 
	      {
	        document = new Document(PageSize.A4, 50, 50, 50, 50);
	        PdfWriter.getInstance(document, new FileOutputStream(outputFolder + "ITextTest.pdf"));
	        document.open();
	        Paragraph para = new Paragraph("First page of the document.");
	        para.setAlignment(Element.ALIGN_LEFT);
	        document.add(para);
	        para = new Paragraph("Some more text on the first page with different color and font type. " +
	        		"We are now going to drone on and on and on and happens in the paragraph operations.",
	                FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, Color.BLUE));
	        para.setFirstLineIndent(36f);
	        para.setSpacingAfter(14f);
	        para.setAlignment(Element.ALIGN_JUSTIFIED);
	        para.setLeading(14f);
	        document.add(para);
	        para = new Paragraph("Some more text on the first page with different color and font type. " +
	        		"We are now going to drone on and on and on and on with an eye to finding out what" +
	        		" happens in the paragraph operations.",
	                FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, Color.RED));

	        para.setExtraParagraphSpace(20f);
	        para.setAlignment(Element.ALIGN_JUSTIFIED);
	        para.setLeading(14f);
	        document.add(para);

//	        Image jpg = Image.getInstance(outputFolder + "patternImage.jpg");
//	        jpg.scalePercent((float) 12);
//	        jpg.setBorderColor(Color.BLACK);
//	        jpg.setBorder(15);
//	        jpg.setBorderWidth(10.0f);
//	        document.add(jpg);
	        document.add(Chunk.NEXTPAGE);
	        document.add(new Paragraph("Some more text after Chunk.NEXTPAGE."));
	        
			PdfPCell cell1 = new PdfPCell(new Paragraph("Kerkhofadministratie:",
					FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC, Color.BLACK)));
			cell1.setBorder(PdfPCell.NO_BORDER);
			PdfPCell cell2 = new PdfPCell(new Paragraph("Financiële administratie:",
					FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC, Color.BLACK)));
	        PdfPTable table = new PdfPTable(2);
			cell2.setBorder(PdfPCell.NO_BORDER);
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
//			PdfPCell cell = new PdfPCell(new Paragraph("header with colspan 3"));
//			cell.setColspan(3);
//			table.addCell(cell);
			table.addCell(cell1);
			table.addCell("Busonilaan 8  5654 NP Eindhoven tel: 040-2517112");
			table.addCell(cell2);
			table.addCell("Postbank    39 67 917");
			table.addCell("");
			table.addCell("Rabobank  11 37 21 765");
			table.addCell("");
			table.addCell("t.n.v. Stichting St. Lambertusgemeenschap Rubinsteinlaan 25  5654 PC Eindhoven");
			
			float[] widths2 = { 1f, 3f };
			table.setWidths(widths2);

			
			document.add(table);

	        
	        document.close();
	    

	}
}
