package nl.evg.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PdfDocWriter
{
	public void write(int nofDoc, String text, String outputDir) throws DocumentException, MalformedURLException, IOException
	{
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, new FileOutputStream(outputDir + File.separator + "lambertus.pdf"));
		document.open();
		Paragraph para = new Paragraph(text);
		para.setAlignment(Element.ALIGN_LEFT);
		for (int i = 0; i < nofDoc; i++)
		{
			document.add(para);
			document.add(Chunk.NEXTPAGE);
		}
		document.close();
	}

}
