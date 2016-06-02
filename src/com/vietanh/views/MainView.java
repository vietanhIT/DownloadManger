/**
 * 
 */
package com.vietanh.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Nguyen Viet Anh
 * @version 1
 * @since Jun 1, 2016
 *
 */
public class MainView {
	JFrame jframe;
	JMenu menu;
	JMenuBar menuBar;
	JMenuItem downloadMenu, historyMenu, aboutMenu;
	DownloadPanel downloadPanel;
	ActionMenuBar action;
	
	public void createView() {
		jframe = new JFrame();
		jframe.setTitle("Download Manager");
		createMenuBar();
		initPanel();
		addAcction();
		jframe.setContentPane(downloadPanel);
		jframe.setSize(550, 620); // thiet lap kich thuoc cho doi tuong JFrame
		// thiet lap vi tri ban dau tren man hinh window
		jframe.setLocation(500, 100);
		jframe.setVisible(true); // thiet lap JFrame nay duoc hien ra
		// thiet lap de tat duoc bang nut x tren JFrame
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initPanel() {
		downloadPanel = new DownloadPanel(jframe);
	}

	public void createMenuBar() {
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		menuBar.add(menu);
		downloadMenu = new JMenuItem("Download");
		historyMenu = new JMenuItem("History");
		aboutMenu = new JMenuItem("About");
		menu.add(downloadMenu);
		menu.add(historyMenu);
		menu.add(aboutMenu);
		jframe.setJMenuBar(menuBar);

	}
	
	public void addAcction(){
		action = this.new ActionMenuBar();
		downloadMenu.addActionListener(action);
		historyMenu.addActionListener(action);
		aboutMenu.addActionListener(action);
	}

	public JPanel createDownloadPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		return panel;
	}

	public JPanel createHistoryPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.RED);
		return panel;
	}

	public JPanel createAboutPanel() {
		JPanel panel = new JPanel();
		JLabel label1 = new JLabel(
				"<html>Design and code by Nguyen Viet Anh<br>Contact: vietanknb@gmail.com<html>");
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(label1);
		return panel;
	}

	class ActionMenuBar implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			switch (e.getActionCommand()) {
			case "Download":
				jframe.setContentPane(downloadPanel);
				jframe.validate();
				break;
			case "History":
				//jframe.setContentPane(createHistoryPanel());
				//jframe.validate();
				break;
			case "About":
				jframe.setContentPane(createAboutPanel());
				jframe.validate();
				break;
			default:
				break;
			}
		}

	}
	
	

	public static void main(String[] args) {
		MainView mainView = new MainView();
		mainView.createView();

	}
}
