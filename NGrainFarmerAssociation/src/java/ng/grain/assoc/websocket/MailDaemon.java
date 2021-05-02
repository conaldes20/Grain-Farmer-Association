/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.websocket;

/**
 *
 * @author CONALDES
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.activation.MailcapCommandMap;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * the mail daemon in the server in charge of sending e-mails
 * 
 * @author <a href="https://openlowcode.com/" rel="nofollow">Open Lowcode
 *         SAS</a>
 *
 */
public class MailDaemon {
	private static SimpleDateFormat iCalendarDateFormat;
	private static MailDaemon mInstance = null;
	private static Logger logger = Logger.getLogger(MailDaemon.class.getName());
	private String smtpserver;
	private int port;
	private String user = null;
	private String password;
	
	private MailDaemon(String user, String password, String smtpserver, int port){
            this.user = user;
            this.password = password;
            this.smtpserver = smtpserver;
            this.port = port;
        }
	
	public static MailDaemon getInstance(String user, String password, String smtpserver, int port){
            if(mInstance == null){
                mInstance = new MailDaemon(user, password, smtpserver, port);
                iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00Z'");
                    iCalendarDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    logger.severe("Mail deamon created for smtpserver = " + smtpserver + ", port=" + port);
            }
            return mInstance;
        }

	
	/**
	 * gets the best possible connection for the SMTP server (authenticated if
	 * possible)
	 * 
	 * @return the session
	 */	
	private Session connectServer() {
		if (user != null)
			return connectAuthenticatedToServer();
		if (port > 0)
			return connectUnauthenticatedServer();
		return connectUnauthenticatedDefaultPort();
	}

	/**
	 * performs an unauthenticated default port connection to the SMTP server
	 * 
	 * @return the session
	 */
	private Session connectUnauthenticatedDefaultPort() {
		logger.warning("Building unauthenticated e-mail session to server '" + smtpserver + "' with default port");
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpserver); // SMTP Host
		Session session = Session.getInstance(props, null);
		return session;
	}

	/**
	 * perfoms an unauthenticated connection to the SMTP server on the specified
	 * port
	 * 
	 * @return the session
	 */
	private Session connectUnauthenticatedServer() {
		logger.warning("Building unauthenticated e-mail session to server '" + smtpserver + "' with port '"
				+ port + "'");
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpserver); // SMTP Host
		props.put("mail.smtp.socketFactory.port", port); // SSL Port
		Session session = Session.getInstance(props, null);
		return session;
	}

	/**
	 * connects securely to the SMTP server (with authentication)
	 * 
	 * @return the SMTP session
	 */
	private Session connectAuthenticatedToServer() {
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpserver); // SMTP Host
		props.put("mail.smtp.socketFactory.port", port); // SSL Port
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
		props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
		props.put("mail.smtp.port", port); // SMTP Port

		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
                        @Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		};

		Session session = Session.getDefaultInstance(props, auth);
		return session;
	}
	
	/**
	 * sends invitation
	 * 
	 * @param session     session connection to the SMTP server
	 * @param toemails    list of recipient e-mails
	 * @param subject     invitation subject
	 * @param body        invitation start
	 * @param fromemail   user sending the invitation
	 * @param startdate   start date of the invitation
	 * @param enddate     end date of the invitation
	 * @param location    location of the invitation
	 * @param uid         unique id
	 * @param cancelation true if this is a cancelation
	 */
	public void sendInvitation(String[] toemails, String subject, String body, String fromemail,
			Date startdate, Date enddate, String location, String uid, boolean cancelation) {
		Session session = connectServer();
		try {
			// prepare mail mime message
			MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap) MimetypesFileTypeMap.getDefaultFileTypeMap();
			mimetypes.addMimeTypes("text/calendar ics ICS");
			// register the handling of text/calendar mime type
			MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
			mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");

			MimeMessage msg = new MimeMessage(session);
			// set message headers

			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			InternetAddress fromemailaddress = new InternetAddress(fromemail);
			msg.setFrom(fromemailaddress);
			msg.setReplyTo(InternetAddress.parse(fromemail, false));
			msg.setSubject(subject, "UTF-8");
			msg.setSentDate(new Date());

			// set recipient

			InternetAddress[] recipients = new InternetAddress[toemails.length + 1];

			String attendeesinvcalendar = "";
			for (int i = 0; i < toemails.length; i++) {
				recipients[i] = new InternetAddress(toemails[i]);
				attendeesinvcalendar += "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
						+ toemails[i] + "\n";
			}

			recipients[toemails.length] = fromemailaddress;
			msg.setRecipients(Message.RecipientType.TO, recipients);

			Multipart multipart = new MimeMultipart("alternative");
			// set body
			MimeBodyPart descriptionPart = new MimeBodyPart();
			descriptionPart.setContent(body, "text/html; charset=utf-8");
			multipart.addBodyPart(descriptionPart);

			// set invitation
			BodyPart calendarPart = new MimeBodyPart();

			String method = "METHOD:REQUEST\n";
			if (cancelation)
				method = "METHOD:CANCEL\n";

			String calendarContent = "BEGIN:VCALENDAR\n" + method + "PRODID: BCP - Meeting\n" + "VERSION:2.0\n"
					+ "BEGIN:VEVENT\n" + "DTSTAMP:" + iCalendarDateFormat.format(new Date()) + "\n" + "DTSTART:"
					+ iCalendarDateFormat.format(startdate) + "\n" + "DTEND:" + iCalendarDateFormat.format(enddate)
					+ "\n" + "SUMMARY:" + subject + "\n" + "UID:" + uid + "\n" + attendeesinvcalendar
					+ "ORGANIZER:MAILTO:" + fromemail + "\n" + "LOCATION:" + location + "\n" + "DESCRIPTION:" + subject
					+ "\n" + "SEQUENCE:0\n" + "PRIORITY:5\n" + "CLASS:PUBLIC\n" + "STATUS:CONFIRMED\n"
					+ "TRANSP:OPAQUE\n" + "BEGIN:VALARM\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:REMINDER\n"
					+ "TRIGGER;RELATED=START:-PT00H15M00S\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR";

			calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
			calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
			multipart.addBodyPart(calendarPart);
			msg.setContent(multipart);
			logger.severe("Invitation is ready");
			Transport.send(msg);

			logger.severe("EMail Invitation Sent Successfully!! to " + attendeesinvcalendar);
		} catch (Exception e) {
			logger.severe(
					"--- Exception in sending invitation --- " + e.getClass().toString() + " - " + e.getMessage());
			if (e.getCause() != null)
				logger.severe(" cause  " + e.getCause().getClass().toString() + " - " + e.getCause().getMessage());
			throw new RuntimeException("email sending error " + e.getMessage() + " for server = server:"
					+ smtpserver + " - port:" + port + " - user:" + user);
		}

	}

	/**
	 * sends an e-mail
	 * 
	 * @param session   session connection to the SMTP server
	 * @param toemails  list of recipient e-mails
	 * @param subject   subject (title) of the e-mail
	 * @param body      body of the e-mail
	 * @param fromemail origin of the e-mail
	 */
	public String sendEmail(String[] toemails, String subject, String body, String fromemail) {
		Session session = connectServer();
		String respmsg = "";
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress(fromemail));

			InternetAddress[] recipients = new InternetAddress[toemails.length];
			for (int i = 0; i < toemails.length; i++)
				recipients[i] = new InternetAddress(toemails[i]);

			msg.setReplyTo(InternetAddress.parse(fromemail, false));

			msg.setSubject(subject, "UTF-8");

			msg.setContent(body, "text/html; charset=utf-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, recipients);
			logger.severe("Message is ready");
			Transport.send(msg);

			logger.severe("EMail Sent Successfully!!");
			respmsg = "Email Sent Successfully!!";
		} catch (Exception e) {
			respmsg = "email sending error " + e.getMessage() + " for server = server: "
					+ smtpserver + " - port: " + port + " - user: " + user;
		}
		return respmsg;
	}
}
