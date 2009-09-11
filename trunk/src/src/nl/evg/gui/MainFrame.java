package nl.evg.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

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
				try
				{
					new MainFrame("Lambertus brieven");
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame(String title) throws IOException
	{
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JTabbedPane();
		mainPanel.setPreferredSize(new Dimension(600,400));
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
	
	private JPanel getLetterPanel() throws IOException
	{
		JPanel result = new JPanel(new BorderLayout());
		JPanel headerPanel = new JPanel(new BorderLayout());
		JPanel bodyPanel = new JPanel(new BorderLayout());
		JPanel saveLoadPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		titlePanel.add(titleField = new JTextField());
		fill(titleField,"title.txt");
		leftHeaderTextArea = new JTextArea();
		rightHeaderTextArea = new JTextArea();
		fill(leftHeaderTextArea,"leftHeader.txt");
		fill(rightHeaderTextArea,"rightHeader.txt");
		headerPanel.add(titlePanel, BorderLayout.NORTH);
		headerPanel.add(leftHeaderTextArea, BorderLayout.WEST);
		headerPanel.add(rightHeaderTextArea, BorderLayout.CENTER);
		
		bodyTextArea = new JTextArea();
		bodyTextArea.setLineWrap(true);
		bodyTextArea.setWrapStyleWord(true);
		fill(bodyTextArea,"body.txt");
		JScrollPane scrollPane = new JScrollPane(bodyTextArea);
		bodyPanel.add(scrollPane);
		
		JButton saveButton = new JButton("Opslaan...");
		JButton loadButton = new JButton("Openen...");
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					saveLetterDefinition();
				}
				catch(UnsupportedEncodingException uee)
				{
					//TODO inform user
				}
			}
		});
		loadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				loadLetterDefinition();
			}
		});
		saveLoadPanel.add(saveButton);
		saveLoadPanel.add(loadButton);
		
		result.add(headerPanel, BorderLayout.NORTH);
		result.add(bodyPanel, BorderLayout.CENTER);
		result.add(saveLoadPanel, BorderLayout.SOUTH);
		return result;
	}
	
	private void saveLetterDefinition() throws UnsupportedEncodingException
	{
		save("letterdef.txt",getLetterDef());
	}
	
	private byte[] getLetterDef() throws UnsupportedEncodingException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(stream, "UTF8"));
		writer.println(titleField.getText());
		writer.println(SEPARATOR);
		writer.println(leftHeaderTextArea.getText());
		writer.println(SEPARATOR);
		writer.println(rightHeaderTextArea.getText());
		writer.println(SEPARATOR);
		writer.println(bodyTextArea.getText());
		writer.flush();
		writer.close();
		return stream.toByteArray();
	}
	
	private static final String SEPARATOR = "~ ~ ~";
	
	private void loadLetterDefinition()
	{
//		TODO load
	}
	
	private void fill(JTextArea textArea, String file) throws IOException
	{
		InputStream inputStream = this.getClass().getResourceAsStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
		String line = reader.readLine();
		while (line!=null)
		{
			textArea.append(line);
			textArea.append("\n");
			line = reader.readLine();
		}
		reader.close();
	}

	//TODO refactor overloaded method
	private void fill(JTextField textField, String file) throws IOException
	{
		InputStream inputStream = this.getClass().getResourceAsStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
		String line = reader.readLine();
		if (line!=null)
		{
			textField.setText(line);
		}
		reader.close();
	}
	
	private JPanel getSelectYearPanel()
	{
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));
		result.add(new JLabel("Jaar:"));
		result.add(yearField = new JTextField("2009"));
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
		String year = yearField.getText();
		pdfDoc = new PdfDoc(pageTemplate);
		PageCreator creator = new PageCreator();
		List<PageVars> pageVarList = creator.createPagesFrom(excelInputStream, year);
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
				save("lambertus.pdf",pdfDoc.asBytes());
			}

		});
		result.add(new JLabel("Pdf bestand:"));
		result.add(browseButton);
		return result;
	}
	
	private void save(String defaultName, byte[] bytes)
	{
		InputStream stream = new ByteArrayInputStream(bytes);
		try
		{
			FileSaveService saveService = (FileSaveService)ServiceManager.lookup("javax.jnlp.FileSaveService");
			saveService.saveFileDialog("", new String[]{}, stream, "");
		}
		catch(Exception e)
		{
			try
			{
				FileOutputStream fileOutputStream = new FileOutputStream("/home/edwin/"+defaultName);
				fileOutputStream.write(bytes);
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	private JTextField yearField;
	private JTextField titleField;
	private JTextArea leftHeaderTextArea;
	private JTextArea rightHeaderTextArea;
	private JTextArea bodyTextArea;
	private InputStream excelInputStream;
	private JTabbedPane mainPanel;
	private JButton prevButton;
	private JButton nextButton;
	private PdfDoc pdfDoc;
	
}
