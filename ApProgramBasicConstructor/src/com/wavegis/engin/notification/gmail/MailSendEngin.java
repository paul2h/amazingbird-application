package com.wavegis.engin.notification.gmail;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.apache.logging.log4j.Logger;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.global.tools.SendTool;
import com.wavegis.model.MailData;

public class MailSendEngin extends TimerEngin {

	public static final String enginID = "MailSendEngin";
	private static final String enginName = "郵件發送1.0";
	private static final MailSendEnginView enginView = new MailSendEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	private static final String mailAccount = GlobalConfig.XML_CONFIG.getProperty("MAIL_ACCOUNT" , "");
	private static final String mailAccountPassword = GlobalConfig.XML_CONFIG.getProperty("MAIL_ACCOUNT_PASSWORD" , "");
	
	private Set<String> hasSendSet = new HashSet<String>();

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
		MailData mailData;
		while((mailData = ProxyData.MAIL_SEND_QUEUE.poll()) != null){
			String mailSubject = mailData.getMail_subject();
			if(todayHasSend(mailSubject)){
				showMessage("本日已發送過:  " + mailSubject);
			}else{
				sendMail(mailData);
				noteHasSend(mailSubject);
			}
		}
	}
	
	private boolean todayHasSend(String mailSubject){
		return hasSendSet.contains(mailSubject);
	}
	
	private void sendMail(MailData mailData){
		try {
			showMessage("發送郵件 : " + mailData.getMail_subject());
			SendTool.sendGmail(mailAccount, mailAccountPassword, mailData.getTargetMails(), mailData.getMail_subject(), mailData.getText_content(), mailData.getAttach_file_path());
			showMessage("郵件發送完成.");
		} catch (AddressException e) {
			showMessage("郵件發送失敗 : "+ e.getMessage());
		} catch (MessagingException e) {
			showMessage("郵件發送失敗 : "+ e.getMessage());
		} catch (IOException e) {
			showMessage("郵件發送失敗 : "+ e.getMessage());
		}
	}
	
	private Set<String> noteHasSend(String mailSubject){
		hasSendSet.add(mailSubject);
		return hasSendSet;
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
