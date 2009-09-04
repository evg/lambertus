package nl.evg.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nl.evg.business.PdfDocWriter;

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
				writePdfDocument(3, text);
			}

		});
		getContentPane().add(textArea, BorderLayout.CENTER);
		getContentPane().add(startButton, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

	private void writePdfDocument(int nofDoc, String text)
	{
		Cursor oldCursor = getCursor();
		try
		{
			PdfDocWriter writer = new PdfDocWriter();
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			writer.write(nofDoc, text);
		} 
		catch (Exception ioe)
		{
		}
		finally
		{
			setCursor(oldCursor);
		}
	}

	private JTextArea textArea;
	private JButton startButton;
}
