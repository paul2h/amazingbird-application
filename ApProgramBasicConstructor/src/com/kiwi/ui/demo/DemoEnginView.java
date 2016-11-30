package com.kiwi.ui.demo;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.kiwi.ui.EnginView;

@SuppressWarnings("serial")
public class DemoEnginView extends JPanel implements EnginView {

	public static DemoEnginView instance;
	private JTextArea messageArea = new JTextArea();

	private static final String enginID = "demo";
	private int messageCount = 0;

	public DemoEnginView() {
		super();
		instance = this;
		this.setLayout(new BorderLayout());
		messageArea = new JTextArea();
		this.add(new JScrollPane(messageArea), BorderLayout.CENTER);
	}

	public static DemoEnginView getInstance() {
		if (instance == null) {
			System.err.println("DemoEnginView物件尚未建置!!!");
		}
		return instance;
	}

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public void showMessage(String message) {
		messageCount++;
		if (messageCount > 100) {
			messageArea.setText(null);
		}
		messageArea.append(message + "\n");
		messageArea.setCaretPosition(messageArea.getText().length());
	}

}
