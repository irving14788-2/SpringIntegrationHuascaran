package com.bbva.integration.common;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS  //Class names should always be capitalized
{

private static String host = "smtp.gmail.com";
private static String user = "pruebajavamailcda@gmail.com";
private static String pass = "ppazos12345";

public void main22222()
{
	String fromAddr="pruebajavamailcda@gmail.com";
	String toAddr="percy1409@gmail.com";
	String subject="subject";
	String body="body";
	
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    Session session = Session.getInstance(props, null);

    MimeMessage message = new MimeMessage(session);

    try
    {
        message.setFrom(new InternetAddress(fromAddr));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddr));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message, user, pass);
    }
    catch (AddressException e) {e.printStackTrace();}
    catch (MessagingException e) {e.printStackTrace();}
}

}