package nl.evg.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.jnlp.FileContents;
import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PdfDocWriter
{
	public void write(int nofDoc, String text) throws DocumentException, MalformedURLException, IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		Paragraph para = new Paragraph(text);
		para.setAlignment(Element.ALIGN_LEFT);
		for (int i = 0; i < nofDoc; i++)
		{
			document.add(para);
			document.add(Chunk.NEXTPAGE);
		}
		document.close();
		
		byte[] bytes = outputStream.toByteArray();
		InputStream stream = new ByteArrayInputStream(bytes);
		try
		{
			FileSaveService saveService = (FileSaveService)ServiceManager.lookup("javax.jnlp.FileSaveService");
			FileContents fileContents = saveService.saveFileDialog("c:\\temp", new String[]{"pdf"}, stream, "lambertus");
		}
		catch(UnavailableServiceException e)
		{
			FileOutputStream fileOutputStream = new FileOutputStream("/home/edwin/lambertus.pdf");
			fileOutputStream.write(bytes);
			fileOutputStream.flush();
			fileOutputStream.close();
		}
	}
}
