package com.wavegis.engin.prototype;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * 最基本的EnginView,只有文字訊息框
 * 
 * @author Kiwi
 *
 */
@SuppressWarnings("serial")
public abstract class SimpleEnginView extends JPanel implements EnginView {

	private JTextArea messageArea = new JTextArea();
	private int messageCount = 0;
	private static final int MessageMaxLine = 100;

	public SimpleEnginView() {
		super();
		this.setLayout(new BorderLayout());
		messageArea = new JTextArea();
		this.add(new JScrollPane(messageArea), BorderLayout.CENTER);
	}

	@Override
	public void showMessage(String message) {
		messageCount++;
		if (messageCount > MessageMaxLine) {
			messageArea.setText(null);
			messageCount = 0;
		}
		messageArea.append(message + "\n");
		messageArea.setCaretPosition(messageArea.getText().length());
	}

}
