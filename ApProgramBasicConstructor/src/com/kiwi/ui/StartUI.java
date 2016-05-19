package com.kiwi.ui;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kiwi.controller.Controller;
import com.kiwi.dao.Dao;
import com.kiwi.dao.DaoManager;
import com.kiwi.model.DataModel;

@SuppressWarnings("serial")
public class StartUI extends JFrame implements ActionListener {

	private JTextArea messageArea;
	private Controller controller;
	private JButton button;

	public static void main(String[] args) {
		StartUI s = new StartUI();
	}

	public StartUI() {
		super("Spring+Mybatis程式基本版");
		initUI();
		controller = initSpringConstruct();
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

	/**
	 * 初始化整個程式架構(使用Spring Framework)
	 * 
	 * @return controller 初始化完後回傳UI用的controller
	 * */
	@SuppressWarnings("resource")
	private Controller initSpringConstruct() {
		showMessage("初始化Spring架構...");
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"./spring-config.xml");
		controller = (Controller) context.getBean("Controller");

		controller.testProcess();// 測試用
		showMessage("初始化Spring架構完成.");
		return controller;
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
