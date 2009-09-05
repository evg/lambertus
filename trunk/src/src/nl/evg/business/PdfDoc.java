package nl.evg.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

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

public class PdfDoc
{
	public PdfDoc() throws DocumentException
	{
		outputStream = new ByteArrayOutputStream();
		document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, outputStream);
		document.open();
	}
	
	public void add(Page page) throws DocumentException
	{
		Paragraph para = new Paragraph(page.getText());
		para.setAlignment(Element.ALIGN_LEFT);
		document.add(para);
		document.add(Chunk.NEXTPAGE);
	}
	
//	public void close()
//	{
//		document.close();
//		isClosed = true;
//	}
	
	public byte[] asBytes()
	{
		document.close();
		return outputStream.toByteArray();
	}
//	public void write() throws DocumentException, MalformedURLException, IOException
//	{
//		if (!isClosed)
//			throw new IllegalStateException("PdfDoc should be closed before it is written");
//		
//		byte[] bytes = outputStream.toByteArray();
//		InputStream stream = new ByteArrayInputStream(bytes);
//		try
//		{
//			FileSaveService saveService = (FileSaveService)ServiceManager.lookup("javax.jnlp.FileSaveService");
//			saveService.saveFileDialog("c:\\temp", new String[]{"pdf"}, stream, "lambertus");
//		}
//		catch(UnavailableServiceException e)
//		{
//			FileOutputStream fileOutputStream = new FileOutputStream("/home/edwin/lambertus.pdf");
//			fileOutputStream.write(bytes);
//			fileOutputStream.flush();
//			fileOutputStream.close();
//		}
//	}

	private boolean isClosed = false;
	private Document document;
	private ByteArrayOutputStream outputStream;
}
