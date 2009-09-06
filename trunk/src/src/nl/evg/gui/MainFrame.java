package nl.evg.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nl.evg.business.PageTemplate;
import nl.evg.business.PageVars;
import nl.evg.business.PageCreator;
import nl.evg.business.PdfDoc;

import com.lowagie.text.DocumentException;

public class MainFrame extends JFrame
{
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				new MainFrame("Lambertus brieven");
			}
		});
	}

	public MainFrame(String title)
	{
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JTabbedPane();
		mainPanel.setPreferredSize(new Dimension(400,300));
		mainPanel.add("Excel bestand",getExcelPanel());
		mainPanel.add("Jaar",getSelectYearPanel());
		mainPanel.add("Brief definitie",getLetterPanel());
		mainPanel.add("Samenvoegen", getMergePanel());
		mainPanel.add("Pdf bestand",getSavePanel());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		getContentPane().add(getWizardButtonsPanel(), BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}
	
	private JPanel getLetterPanel()
	{
		JPanel result = new JPanel(new BorderLayout());
		JPanel headerPanel = new JPanel(new BorderLayout());
		JPanel bodyPanel = new JPanel(new BorderLayout());
		
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		titlePanel.add(titleField = new JTextField("STICHTING ST. LAMBERTUSGEMEENSCHAP"));
		leftHeaderTextArea = new JTextArea("Kerkhofadministratie:\nFinanciëleadministratie:");
		rightHeaderTextArea = new JTextArea("Busonilaan 8  5654 NP Eindhoven tel: 040-2517112\nPostbank    39 67 917\nRabobank  11 37 21 765\nt.n.v. Stichting St. Lambertusgemeenschap Rubinsteinlaan 25  5654 PC Eindhoven");
		headerPanel.add(titlePanel, BorderLayout.NORTH);
		headerPanel.add(leftHeaderTextArea, BorderLayout.WEST);
		headerPanel.add(rightHeaderTextArea, BorderLayout.CENTER);
		
		bodyPanel.add(bodyTextArea = new JTextArea("Eindhoven,\n\n\n\nBetreft: Verlenging grafrechten vak $VAK\n\nZeer geachte mevrouw, mijnheer,"));
		result.add(headerPanel, BorderLayout.NORTH);
		result.add(bodyPanel, BorderLayout.CENTER);
		return result;
	}
	
	private JTextField titleField;
	private JTextArea leftHeaderTextArea;
	private JTextArea rightHeaderTextArea;
	private JTextArea bodyTextArea;
	
	
	private JPanel getSelectYearPanel()
	{
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));
		result.add(new JLabel("Jaar:"));
		result.add(new JTextField("2009"));
		return result;
	}

	private JPanel getWizardButtonsPanel()
	{
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));
		result.add(prevButton = new JButton("Vorige"));
		result.add(nextButton = new JButton("Volgende"));
		result.add(new JButton("Sluiten"));
		
		prevButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) 
			{
				int selIndex = mainPanel.getSelectedIndex(); 
				if (selIndex > 0)
					mainPanel.setSelectedIndex(selIndex-1);
			}
			
		});

		nextButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) 
			{
				int selIndex = mainPanel.getSelectedIndex(); 
				if (selIndex < mainPanel.getComponentCount()-1)
					mainPanel.setSelectedIndex(selIndex+1);
			}
			
		});
	
		return result;
	}
	
	private JPanel getExcelPanel()
	{
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0)
			{
				excelInputStream = openExcel();
			}

		});
		result.add(new JLabel("Excel bestand:"));
		result.add(browseButton);
		return result;
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
	
	private JPanel getMergePanel()
	{
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton mergeButton = new JButton("Start samenvoegen");
		result.add(mergeButton);
		
		mergeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {
			
			try
			{
				showWaitCursor();
				merge();
			} 
			catch (DocumentException e)
			{
				e.printStackTrace();
			}
			finally
			{
				showNormalCursor();
			}
		
		};});
		
		return result;
	}
	
	private void merge() throws DocumentException
	{
		PageTemplate pageTemplate = new PageTemplate();
		pageTemplate.title = titleField.getText();
		pageTemplate.headerLeft = leftHeaderTextArea.getText();
		pageTemplate.headerRight = rightHeaderTextArea.getText();
		pageTemplate.body = bodyTextArea.getText();
		pdfDoc = new PdfDoc(pageTemplate);
		PageCreator creator = new PageCreator();
		List<PageVars> pageVarList = creator.createPagesFrom(excelInputStream);
		for(PageVars pageVars: pageVarList)
			pdfDoc.addPageFor(pageVars);
	}
	
	private void showWaitCursor()
	{
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}
	
	private void showNormalCursor()
	{
		setCursor(Cursor.getDefaultCursor());
	}
	
	private JPanel getSavePanel()
	{
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0)
			{
				save();
			}

		});
		result.add(new JLabel("Pdf bestand:"));
		result.add(browseButton);
		return result;
	}
	
	private void save()
	{
		byte[] bytes = pdfDoc.asBytes();
		InputStream stream = new ByteArrayInputStream(bytes);
		try
		{
			FileSaveService saveService = (FileSaveService)ServiceManager.lookup("javax.jnlp.FileSaveService");
			saveService.saveFileDialog("c:\\temp", new String[]{"pdf"}, stream, "lambertus");
		}
		catch(Exception e)
		{
			try
			{
				FileOutputStream fileOutputStream = new FileOutputStream("/home/edwin/lambertus.pdf");
				fileOutputStream.write(bytes);
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
	
	private InputStream excelInputStream;
	private JTabbedPane mainPanel;
	private JButton prevButton;
	private JButton nextButton;
	private PdfDoc pdfDoc;
	
}
