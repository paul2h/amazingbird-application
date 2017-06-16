package com.wavegis.engin.notification.gmail;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.LogTool;

public class MailSendEngin extends TimerEngin {

	public static final String enginID = "MailSendEngin";
	private static final String enginName = "郵件發送Engin";
	private static final MailSendEnginView enginView = new MailSendEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return enginName;
	}

	@Override
	public EnginView getEnginView() {
		return enginView;
	}

	@Override
	public void timerAction() {
		
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
