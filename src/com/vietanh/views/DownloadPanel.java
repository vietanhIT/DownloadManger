/**
 * 
 */
package com.vietanh.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.vietanh.Constant.Constant;
import com.vietanh.model.HttpDownload;

/**
 * @author Nguyen Viet Anh
 * @version 1
 * @since Jun 1, 2016
 *
 */
public class DownloadPanel extends JPanel {
	String[][] data = { { "URL", "Size", "Progress", "Status" } };
	JTextField inputText;
	JButton button;
	DownloadTableModel model;
	JLabel helpLabel, statusLabel;
	JButton pauseButton, resumeButton, cancelButton;
	JFrame frame;
	ArrayList<JButton> listButton=new ArrayList<JButton>();

	public DownloadPanel(JFrame frame) {
		this.frame = frame;
		initView();
		addAction();
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.add(helpLabel);
		this.add(inputText);
		this.add(button);
		this.add(statusLabel);
		this.add(createJTable());
		this.add(pauseButton);
		this.add(resumeButton);
		this.add(cancelButton);
	}

	public void initView() {
		inputText = new JTextField(40);
		button = new JButton("Download");
		pauseButton = new JButton("Pause");
		resumeButton = new JButton("Resume");
		cancelButton = new JButton("Cancel");
		listButton.add(button);
		listButton.add(pauseButton);
		listButton.add(resumeButton);
		listButton.add(cancelButton);
		helpLabel = new JLabel("Enter download url :");
		statusLabel = new JLabel("Status: ");
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(false);
		cancelButton.setEnabled(false);
	}

	public void addAction() {
		ActionButton action = this.new ActionButton();
		button.addActionListener(action);
		pauseButton.addActionListener(action);
		resumeButton.addActionListener(action);
		cancelButton.addActionListener(action);
	
	}

	public boolean checkInput(String fileURL) {
		if (fileURL.equals("")) {
			System.out.println(fileURL);
			JOptionPane.showMessageDialog(frame, "TextField is Empty",
					"Please enter URL", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	class ActionButton implements ActionListener{
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			switch (e.getActionCommand()) {
			case "Download":
				String fileURL = inputText.getText();
				if(checkInput(fileURL)){
					setState();
					HttpDownload http = new HttpDownload();
					model.setList();
					http.downloadThread(fileURL, model,statusLabel,listButton);
					statusLabel.setText("Status: Downloading");
					pauseButton.setEnabled(true);
					cancelButton.setEnabled(true);
					button.setEnabled(false);
				}
				break;
			case "Pause":
				Constant.isResume=false;
				Constant.isPause = true;
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				statusLabel.setText("Status: Pause");
				System.out.println("Pause");
				break;
			case "Resume":
				Constant.isPause =false;
				Constant.isResume = true;
				pauseButton.setEnabled(true);
				resumeButton.setEnabled(false);
				statusLabel.setText("Status: Downloading");
				break;
			case "Cancel":
				Constant.isPause =false;
				Constant.isResume = false;			
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(false);
				cancelButton.setEnabled(false);
				button.setEnabled(true);
				statusLabel.setText("Status: Error");
				Constant.isCancel=true;
				break;
			default:
				break;
			}
			
		}
	}
	
	public void setState(){
		Constant.downloaded=0;
		Constant.isPause=false;
		Constant.isResume=false;
		Constant.isComplete=false;
		Constant.isCancel=false;
	}
	
	public JPanel createJTable() {
		model = new DownloadTableModel();
		// create table with data
		JTable table = new JTable();
		table.setModel(model);
		JPanel downloadsPanel = new JPanel();
		downloadsPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
		downloadsPanel.setLayout(new BorderLayout());
		downloadsPanel.add(new JScrollPane(table), BorderLayout.CENTER);

		ProgressRender renderer = new ProgressRender(0, 100);
		renderer.setStringPainted(true); // show progress text
		table.setDefaultRenderer(JProgressBar.class, renderer);

		return downloadsPanel;
	}

}
