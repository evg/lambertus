package nl.evg.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

import nl.evg.business.Page;
import nl.evg.business.PageCreator;
import nl.evg.business.PdfDoc;

public class MainFrame extends JFrame
{

	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				new MainFrame("Lambertus grafrechten verlenging");
			}
		});
	}

	public MainFrame(String title)
	{
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane scrollPane = new JScrollPane();
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(400, 300));
		scrollPane.add(textArea);
		scrollPane.setPreferredSize(new Dimension(400, 300));
		textArea.setText("Deze tekst komt op iedere brief. Je kunt hem aanpassen.");
		startButton = new JButton("Maak 3 brieven");
		startButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0)
			{
				String text = textArea.getText();
//				writePdfDocument(3, text);
			}

		});
		openExcelButton = new JButton("Lees Excel bestand");
		openExcelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0)
			{
				InputStream inputStream = openExcel();
				Page[] pages = new PageCreator().createPagesFrom(inputStream);
				try
				{
					PdfDoc doc = new PdfDoc();
					for(Page page: pages)
						doc.add(page);
					doc.close();
					doc.write();
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

		});
		getContentPane().add(textArea, BorderLayout.CENTER);
		getContentPane().add(startButton, BorderLayout.SOUTH);
		getContentPane().add(openExcelButton, BorderLayout.NORTH);
		pack();
		setVisible(true);
	}


	private void writePdfDocument()
	{
		Cursor oldCursor = getCursor();
		try
		{
			PdfDoc pdfDoc = new PdfDoc();
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			pdfDoc.write();
		} 
		catch (Exception ioe)
		{
		}
		finally
		{
			setCursor(oldCursor);
		}
	}

	private InputStream openExcel()
	{
		try
		{
			FileOpenService openService = (FileOpenService)ServiceManager.lookup("javax.jnlp.FileOpenService");
			FileContents contents = openService.openFileDialog("c:\\", new String[]{"xls"});
			return contents.getInputStream();
		}
		catch(Exception e)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.showOpenDialog(this);
			File file = fileChooser.getSelectedFile();
			try
			{
				return new FileInputStream(file);
			}
			catch(FileNotFoundException fnfe)
			{
				fnfe.printStackTrace();
			}
		}
		return null;
	}
	private JTextArea textArea;
	private JButton startButton;
	private JButton openExcelButton;
}
