package nl.evg.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
			PrintWriter writer = new PrintWriter(new FileWriter("lambertus.pdf"));
			for(int i=0; i<nofDoc; i++)
				writer.println(text);
			writer.flush();
			writer.close();
		}

	}

}

//private void writeTxtDocument(int nofDoc, String text, String outputDir)
//{
//	Cursor oldCursor = getCursor();
//	try
//	{
//		setCursor(new Cursor(Cursor.WAIT_CURSOR));
//		String result = "";
//		for(int i=0; i<nofDoc; i++)
//			result += (text+"\n");
//		InputStream stream = new ByteArrayInputStream(result.getBytes());
//		try
//		{
//			FileSaveService saveService = (FileSaveService)ServiceManager.lookup("javax.jnlp.FileSaveService");
//			FileContents fileContents = saveService.saveFileDialog("c:\\temp", new String[]{"txt"}, stream, "lambertus");
//		}
//		catch(UnavailableServiceException e)
//		{
//			PrintWriter writer = new PrintWriter(new FileWriter(outputDir+File.separator+"lambertus.txt"));
//			for(int i=0; i<nofDoc; i++)
//				writer.println(text);
//			writer.flush();
//			writer.close();
//		}
//	} 
//	catch (Exception ioe)
//	{
//		textArea.setText(getText(ioe));
//	}
//	finally
//	{
//		setCursor(oldCursor);
//	}
//}