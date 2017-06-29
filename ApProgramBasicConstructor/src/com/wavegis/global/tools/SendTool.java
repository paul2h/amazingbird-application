package com.wavegis.global.tools;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendTool {
	
	/**
	 * @param userEmail
	 *            用來寄信個mail
	 * @param password
	 *            用來寄信個mail密碼
	 * @param targetEmails
	 *            目標要寄的mail 以逗號分隔
	 * @param subject
	 *            信件主題
	 * @param mailTextContent
	 *            信件文字內容
	 * @param attachFilePath
	 *            附加檔案位置 可null
	 */
	public static void sendGmail(final String userEmail, final String password, String targetEmails, String subject, String mailTextContent, String attachFilePath) throws AddressException, MessagingException, IOException {
		// #[[ 產生 properties
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		// ]]
		// #[[ 產生認證
		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userEmail, password);
			}
		};
		// ]]
		// #[[ 產生session & message
		Session session = Session.getInstance(props, authenticator);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userEmail));
		message.setRecipients(Message.RecipientType.TO, InternetAddress
				.parse(targetEmails));
		message.setSubject(subject);
		System.out.println(subject);

		MimeMultipart multipart = new MimeMultipart();
		// 信件的第一段body - 文字部分
		MimeBodyPart bodyPart1 = new MimeBodyPart();
		bodyPart1.setContent(mailTextContent, "text/html;charset=UTF-8");
		bodyPart1.setHeader("Content-Transfer-Encoding", "base64");
		multipart.addBodyPart(bodyPart1);
		// 信件的第二段body - 附加檔案
		if(attachFilePath != null){
			MimeBodyPart bodyPart2 = new MimeBodyPart();
			bodyPart2.attachFile(attachFilePath);
			multipart.addBodyPart(bodyPart2);			
		}
		// 把所有body放入信件中
		message.setContent(multipart);
		// ]]
		// #[[ 寄信
		Transport.send(message);
		// ]]
	}
}
