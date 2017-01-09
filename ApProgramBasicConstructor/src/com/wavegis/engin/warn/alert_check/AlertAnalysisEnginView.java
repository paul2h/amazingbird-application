package com.wavegis.engin.warn.alert_check;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.wavegis.engin.EnginView;

@SuppressWarnings("serial")
public class AlertAnalysisEnginView extends JPanel implements EnginView {

	private static final String enginID = AlertAnalysisEngin.enginID;
	private JTextArea messageArea = new JTextArea();
	private int messageCount = 0;
	private static AlertAnalysisEnginView instance;

	public AlertAnalysisEnginView() {
		super();
		instance = this;
		this.setLayout(new BorderLayout());
		messageArea = new JTextArea();
		this.add(new JScrollPane(messageArea), BorderLayout.CENTER);
	}

	public static AlertAnalysisEnginView getInstance() {
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
			messageCount = 0;
		}
		messageArea.append(message + "\n");
		messageArea.setCaretPosition(messageArea.getText().length());
	}

}
