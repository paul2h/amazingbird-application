package com.kiwi.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.springframework.beans.factory.annotation.Autowired;
import com.kiwi.controller.Controller;

@SuppressWarnings("serial")
public class StartUI extends JFrame implements ActionListener {

	private JTextArea messageArea;
	private JButton button;
	
	@Autowired
	private Controller controller;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		StartUI s = new StartUI();
	}

	public StartUI() {
		super("Spring+Mybatis程式基本版");
	}
	
	public void start(){
		initUI();
	}

	/** 初始化UI介面 */
	private void initUI() {

		// #[[ UI物件設定
		// 文字框
		this.setLayout(new BorderLayout());
		messageArea = new JTextArea();
		this.add(new JScrollPane(messageArea), BorderLayout.CENTER);
		// 按鈕
		button = new JButton("測試寫入&取得資料");
		button.addActionListener(this);
		this.add(button, BorderLayout.SOUTH);
		// ]]

		// #[[ 其他基本設定
		this.setSize(400, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// ]]

		showMessage("初始化UI介面完成.");
	}



	/** 訊息視窗呈現 */
	private void showMessage(String message) {
		messageArea.append(message + "\n");
		messageArea.setCaretPosition(messageArea.getText().length());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			showMessage("測試寫入&讀取DB資料...");
			showMessage(controller.testProcess());
			showMessage("寫入&讀取完成.");
		}
	}
}

