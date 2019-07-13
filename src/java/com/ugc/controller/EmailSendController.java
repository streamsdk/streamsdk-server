package com.ugc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.ModelMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ugc.helper.FileCreationHelper;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.auth.BasicAWSCredentials;

import javax.servlet.http.HttpServletResponse;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.nio.ByteBuffer;

@Controller
public class EmailSendController
{

    private FileCreationHelper fch;
    static final String FROM = "support@streamsdk.com";  // Replace with your "From" address. This address must be verified.

    private static final String ACCESS_KEY = "AKIAJT2CA7N7TG7QSKQA";
    private static final String SECRET_KEY = "GCf0a00yG+biuXCmrX9CrGElacGqu8+6gFUuw3u7";
    private static Log log = LogFactory.getLog(EmailSendController.class);

    @Autowired
    public EmailSendController(FileCreationHelper fch) {
        this.fch = fch;
    }

    @RequestMapping(value = "/send/mail", method = RequestMethod.POST)
    public void getObject(
            @RequestParam("fileId")String fileId, @RequestParam("to")String to,
            @RequestParam("fullName")String fullName, @RequestParam("fileName")String fileName,
            ModelMap map, HttpServletResponse res) {

        try {
            File fileToSend = fch.createFileWithFileId(fileId);
            String subject = fullName + " shared a file with you";
            String href  = "https://play.google.com/store/apps/details?id=com.facebook.album&hl=en";
            String bodyText = "The attached file is shared through an android app called drag drop share. To receive more sharing from your friends, you can download the application from " + href;
            
            sendMultiPart(bodyText, subject, fileToSend.getAbsolutePath(), to, fileName);
            log.info("Mail Sent To: " + to);

        } catch (Throwable t) {
            log.info("mail send failed: " + t.getMessage());
        }
    }

    private static void sendMultiPart(String bodyText, String subject, String fileName, String to, String realfileName)  {

        try {
            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(bodyText);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(fileName)
            {
                @Override
                public String getContentType() {
                    return "application/octet-stream";
                }
            };
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(realfileName);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart);


            Session s = Session.getInstance(new Properties(), null);
            MimeMessage msg = new MimeMessage(s);

            msg.setFrom(new InternetAddress(FROM));
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setContent(multipart);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                msg.writeTo(out);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            RawMessage rm = new RawMessage();
            rm.setData(ByteBuffer.wrap(out.toByteArray()));
            BasicAWSCredentials basic = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(basic);
            client.sendRawEmail(new SendRawEmailRequest().withSource(FROM).withDestinations(to).withRawMessage(rm));
        } catch (Throwable t) {
            log.info("mail send failed: " + t.getMessage());
        }

    }


}
