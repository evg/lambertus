package nl.evg.business;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class PdfWriter
{
	public void write(int nofDoc, String text, String outputDir) throws IOException
	{
		PrintWriter writer = new PrintWriter(new FileWriter(outputDir+File.separator+"lambertus.txt"));
		for(int i=0; i<nofDoc; i++)
			writer.println(text);
		writer.flush();
		writer.close();
	}
}
