package nl.evg.business;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfDoc
{
	public PdfDoc(PageTemplate pageTemplate) throws DocumentException
	{
		outputStream = new ByteArrayOutputStream();
		document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		this.pageTemplate = pageTemplate;
	}
	
	public void addPageFor(PageVars pageVars) throws DocumentException
	{
		addTitle();
		addHeader();
		addBody(pageVars);
	}

	public byte[] asBytes()
	{
		document.close();
		return outputStream.toByteArray();
	}

	private void addTitle() throws DocumentException
	{
		Paragraph para = new Paragraph(pageTemplate.title, titleFont);
		para.setAlignment(Element.ALIGN_CENTER);
		document.add(para);
	}
	
	private void addHeader() throws DocumentException
	{
        PdfPTable table = new PdfPTable(2);
		table.addCell(createHeaderCellWith(pageTemplate.headerLeft));
		table.addCell(createHeaderCellWith(pageTemplate.headerRight));
		table.setWidths(new float[]{ 1f, 3f });
		table.setWidthPercentage(100f);
		document.add(table);
	}
	
	private PdfPCell createHeaderCellWith(String text)
	{
		PdfPCell cell = new PdfPCell(new Paragraph(text, headerFont));
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}
	
	private void addBody(PageVars pageVars) throws DocumentException
	{
		String mergedText = merge(pageTemplate.body, pageVars);
		Paragraph para = new Paragraph(mergedText, bodyFont);
		para.setAlignment(Element.ALIGN_LEFT);
		document.add(para);
		
		document.add(Chunk.NEXTPAGE);
	}
	
	private String merge(String text, PageVars pageVars)
	{
		String result = new String(text);
		for(PageVarName name: PageVarName.values())
		{
			String key = name.key;
			String value = pageVars.get(name);
			if (value==null)
				continue;
			result = result.replaceAll(key, value);
		}
		return result;
	}
	
	private Font titleFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, Color.BLACK); 
	private Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, Color.BLACK); 
	private Font bodyFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, Color.BLACK); 
	

	private PageTemplate pageTemplate;
	private Document document;
	private ByteArrayOutputStream outputStream;
}
