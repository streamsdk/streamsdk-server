package com.ugc.test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.ugc.utils.ConversionUtils;
import com.ugc.utils.StreamUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


public class TestAmzon
{
    static final String FROM = "support@streamsdk.com";  // Replace with your "From" address. This address must be verified.
    static final String TO = "edwardyangey@gmail.com"; // Replace with a "To" address. If you have not yet requested
    // production access, this address must be verified.
    static final String BODY = "This email was sent through Amazon SES by using the AWS SDK for Java.";
    static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";
    // private static final String ACCESS_KEY = "AKIAJT2CA7N7TG7QSKQA";
    // private static final String SECRET_KEY = "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";


    private static final String ACCESS_KEY = "AKIAJT2CA7N7TG7QSKQA";
    private static final String SECRET_KEY = "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";

    public static void main(String[] args) {

        String subject = "wang" + " shared with you a file";
        String href = "https://play.google.com/store/apps/details?id=com.facebook.album&hl=en";
        String bodyText = "The file is shared through an android app called drag drop share. To receive more sharing from your friends, you can download the file from " + href;

        sendMultiPart(bodyText, subject, "c:\\issues\\send.txt");
    }

    private static void facebookPasrse() {

        try {
            AWSCredentials myCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
            String res = "{\"data\":[{\"name\":\"Sven Siorpaes\",\"id\":\"507554463\"},{\"name\":\"Nagappan Arunachalam\",\"id\":\"509741059\"},{\"name\":\"Nilesh Joshi\",\"id\":\"510584401\"},{\"name\":\"Robert Sinnott\",\"id\":\"523808151\"},{\"name\":\"Judy Niuniu\",\"id\":\"537196370\"},{\"name\":\"Ningning Shi\",\"id\":\"553322001\"},{\"name\":\"Ray Gallagher\",\"id\":\"556964973\"},{\"name\":\"Victor Issel Olvera-Smith\",\"id\":\"568753059\"},{\"name\":\"Yang Song\",\"id\":\"592797708\"},{\"name\":\"Jinesh Krishnan\",\"id\":\"612163266\"},{\"name\":\"Declan Meaney\",\"id\":\"614406920\"},{\"name\":\"Guy Granger\",\"id\":\"617340057\"},{\"name\":\"Yaou Li\",\"id\":\"625989399\"},{\"name\":\"John Stanley\",\"id\":\"626434424\"},{\"name\":\"Alex Waskiewicz\",\"id\":\"629791184\"},{\"name\":\"Tongkun Cui\",\"id\":\"631328218\"},{\"name\":\"John Fitzgerald\",\"id\":\"632117896\"},{\"name\":\"Salvatore Fanara\",\"id\":\"684080126\"},{\"name\":\"Ray Gallagher\",\"id\":\"702426655\"},{\"name\":\"Mick Morrissey\",\"id\":\"759133535\"},{\"name\":\"Darren Redmond\",\"id\":\"771968845\"},{\"name\":\"Han Zhe\",\"id\":\"805089043\"},{\"name\":\"Hua Guo\",\"id\":\"1018223421\"},{\"name\":\"Karl Tiller\",\"id\":\"1140917794\"},{\"name\":\"Bob Testtest\",\"id\":\"1297489353\"},{\"name\":\"Heyuan Li\",\"id\":\"1315795231\"},{\"name\":\"Prasad Prabha\",\"id\":\"1315798293\"},{\"name\":\"Hao Gao\",\"id\":\"1321914968\"},{\"name\":\"Rob Sin\",\"id\":\"1356276285\"},{\"name\":\"Trevor-anderson Felicity Candice\",\"id\":\"1385867307\"},{\"name\":\"Sebastian G\\u0142owacki\",\"id\":\"1390090116\"},{\"name\":\"Guo Du\",\"id\":\"1628168014\"}]}";
            ByteArrayInputStream in = new ByteArrayInputStream(res.getBytes());
            //String key = getHashValue();
            String key = "B11C63E3F4CA70675AB3351708AE1912d";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(res.length());
            PutObjectResult objectResult = client.putObject("com.streamsdk.counter", key, in, objectMetadata);
            System.out.println(objectResult.getVersionId());
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }


    }

    private static void sendMultiPart(String bodyText, String subject, String fileName) {

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
            attachmentPart.setFileName(fileDataSource.getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart);


            Session s = Session.getInstance(new Properties(), null);
            MimeMessage msg = new MimeMessage(s);

            msg.setFrom(new InternetAddress(FROM));
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(TO));
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
            client.sendRawEmail(new SendRawEmailRequest().withSource(FROM).withDestinations(TO).withRawMessage(rm));
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }

    }

    private static void sendMail() {

        BasicAWSCredentials basic = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        Destination destination = new Destination().withToAddresses(new String[]{TO});

        // Create the subject and body of the message.
        Content subject = new Content().withData(SUBJECT);
        Content textBody = new Content().withData(BODY);
        Body body = new Body().withText(textBody);

        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);

        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);

        try {
            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            // Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(basic);

            // Send the email.
            client.sendEmail(request);
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }

    }

    private static void deleteAll() {

        AWSCredentials myCredentials = new BasicAWSCredentials("AKIAI2LYYTHPW4PMUQDA", "hpRKb9jgNgm8qugxCCaHG7kE4m4LtR5ECZClAyus");
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        DeleteObjectsRequest o = new DeleteObjectsRequest("com.company.object");
        DeleteObjectsResult result = client.deleteObjects(o);
        List<DeleteObjectsResult.DeletedObject> deletedObjects = result.getDeletedObjects();

    }

    private static void testObjectListing() {

        AWSCredentials myCredentials = new BasicAWSCredentials("AKIAI2LYYTHPW4PMUQDA", "hpRKb9jgNgm8qugxCCaHG7kE4m4LtR5ECZClAyus");
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        ObjectListing ol = client.listObjects("com.company.object", "test2");
        List<S3ObjectSummary> summaryList = ol.getObjectSummaries();
        for (S3ObjectSummary s3 : summaryList) {

            System.out.println(s3.getKey());
            System.out.println(s3.getSize());
            System.out.println(s3.getLastModified().getTime());
        }

    }


    private static void testUrl() {
        AWSCredentials myCredentials = new BasicAWSCredentials("AKIAI2LYYTHPW4PMUQDA", "hpRKb9jgNgm8qugxCCaHG7kE4m4LtR5ECZClAyus");
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        GeneratePresignedUrlRequest gen = new GeneratePresignedUrlRequest("com.company.test1", "5095B5B8145ACAAFE6400FB624DA615C");
        URL url = client.generatePresignedUrl(gen);

        System.out.println(url.getPath());
    }

    private static void updateJson() {

        AWSCredentials myCredentials = new BasicAWSCredentials("AKIAI2LYYTHPW4PMUQDA", "hpRKb9jgNgm8qugxCCaHG7kE4m4LtR5ECZClAyus");
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        String res = "{\"data\":[{\"name\":\"Sven Siorpaes\",\"id\":\"507554463\"},{\"name\":\"Nagappan Arunachalam\",\"id\":\"509741059\"},{\"name\":\"Nilesh Joshi\",\"id\":\"510584401\"},{\"name\":\"Robert Sinnott\",\"id\":\"523808151\"},{\"name\":\"Judy Niuniu\",\"id\":\"537196370\"},{\"name\":\"Ningning Shi\",\"id\":\"553322001\"},{\"name\":\"Ray Gallagher\",\"id\":\"556964973\"},{\"name\":\"Victor Issel Olvera-Smith\",\"id\":\"568753059\"},{\"name\":\"Yang Song\",\"id\":\"592797708\"},{\"name\":\"Jinesh Krishnan\",\"id\":\"612163266\"},{\"name\":\"Declan Meaney\",\"id\":\"614406920\"},{\"name\":\"Guy Granger\",\"id\":\"617340057\"},{\"name\":\"Yaou Li\",\"id\":\"625989399\"},{\"name\":\"John Stanley\",\"id\":\"626434424\"},{\"name\":\"Alex Waskiewicz\",\"id\":\"629791184\"},{\"name\":\"Tongkun Cui\",\"id\":\"631328218\"},{\"name\":\"John Fitzgerald\",\"id\":\"632117896\"},{\"name\":\"Salvatore Fanara\",\"id\":\"684080126\"},{\"name\":\"Ray Gallagher\",\"id\":\"702426655\"},{\"name\":\"Mick Morrissey\",\"id\":\"759133535\"},{\"name\":\"Darren Redmond\",\"id\":\"771968845\"},{\"name\":\"Han Zhe\",\"id\":\"805089043\"},{\"name\":\"Hua Guo\",\"id\":\"1018223421\"},{\"name\":\"Karl Tiller\",\"id\":\"1140917794\"},{\"name\":\"Bob Testtest\",\"id\":\"1297489353\"},{\"name\":\"Heyuan Li\",\"id\":\"1315795231\"},{\"name\":\"Prasad Prabha\",\"id\":\"1315798293\"},{\"name\":\"Hao Gao\",\"id\":\"1321914968\"},{\"name\":\"Rob Sin\",\"id\":\"1356276285\"},{\"name\":\"Trevor-anderson Felicity Candice\",\"id\":\"1385867307\"},{\"name\":\"Sebastian G\\u0142owacki\",\"id\":\"1390090116\"},{\"name\":\"Guo Du\",\"id\":\"1628168014\"}]}";
        ByteArrayInputStream in = new ByteArrayInputStream(res.getBytes());
        //String key = getHashValue();
        String key = "B11C63E3F4CA70675AB3351708AE1912";
        PutObjectResult objectResult = client.putObject("mobile.print.media", key, in, new ObjectMetadata());
        System.out.println(objectResult.getVersionId());


    }


    private static void deleteJson() {
        AWSCredentials myCredentials = new BasicAWSCredentials("AKIAI2LYYTHPW4PMUQDA", "hpRKb9jgNgm8qugxCCaHG7kE4m4LtR5ECZClAyus");
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        client.deleteObject("mobile.print.media", "B11C63E3F4CA70675AB3351708AE1912");


    }


    private static void getJson() {

        AWSCredentials myCredentials = new BasicAWSCredentials("AKIAI2LYYTHPW4PMUQDA", "hpRKb9jgNgm8qugxCCaHG7kE4m4LtR5ECZClAyus");
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        S3Object object = client.getObject("mobile.print.media", "B11C63E3F4CA70675AB3351708AE1912");
        InputStream in = object.getObjectContent();
        byte b[] = StreamUtils.readByteArray(in);
        String str = new String(b);
        System.out.println(str);

    }


    private static void putJson() {
        AWSCredentials myCredentials = new BasicAWSCredentials("AKIAI2LYYTHPW4PMUQDA", "hpRKb9jgNgm8qugxCCaHG7kE4m4LtR5ECZClAyus");
        AmazonS3 client = new com.amazonaws.services.s3.AmazonS3Client(myCredentials);
        String res = "{\"data\":[{\"name\":\"Sven Siorpaes\",\"id\":\"507554463\"},{\"name\":\"Nagappan Arunachalam\",\"id\":\"509741059\"},{\"name\":\"Nilesh Joshi\",\"id\":\"510584401\"},{\"name\":\"Robert Sinnott\",\"id\":\"523808151\"},{\"name\":\"Judy Niuniu\",\"id\":\"537196370\"},{\"name\":\"Ningning Shi\",\"id\":\"553322001\"},{\"name\":\"Ray Gallagher\",\"id\":\"556964973\"},{\"name\":\"Victor Issel Olvera-Smith\",\"id\":\"568753059\"},{\"name\":\"Yang Song\",\"id\":\"592797708\"},{\"name\":\"Jinesh Krishnan\",\"id\":\"612163266\"},{\"name\":\"Declan Meaney\",\"id\":\"614406920\"},{\"name\":\"Guy Granger\",\"id\":\"617340057\"},{\"name\":\"Yaou Li\",\"id\":\"625989399\"},{\"name\":\"John Stanley\",\"id\":\"626434424\"},{\"name\":\"Alex Waskiewicz\",\"id\":\"629791184\"},{\"name\":\"Tongkun Cui\",\"id\":\"631328218\"},{\"name\":\"John Fitzgerald\",\"id\":\"632117896\"},{\"name\":\"Salvatore Fanara\",\"id\":\"684080126\"},{\"name\":\"Ray Gallagher\",\"id\":\"702426655\"},{\"name\":\"Mick Morrissey\",\"id\":\"759133535\"},{\"name\":\"Darren Redmond\",\"id\":\"771968845\"},{\"name\":\"Han Zhe\",\"id\":\"805089043\"},{\"name\":\"Hua Guo\",\"id\":\"1018223421\"},{\"name\":\"Karl Tiller\",\"id\":\"1140917794\"},{\"name\":\"Bob Testtest\",\"id\":\"1297489353\"},{\"name\":\"Heyuan Li\",\"id\":\"1315795231\"},{\"name\":\"Prasad Prabha\",\"id\":\"1315798293\"},{\"name\":\"Hao Gao\",\"id\":\"1321914968\"},{\"name\":\"Rob Sin\",\"id\":\"1356276285\"},{\"name\":\"Trevor-anderson Felicity Candice\",\"id\":\"1385867307\"},{\"name\":\"Sebastian G\\u0142owacki\",\"id\":\"1390090116\"},{\"name\":\"Guo Du\",\"id\":\"1628168014\"}]}";
        ByteArrayInputStream in = new ByteArrayInputStream(res.getBytes());
        //String key = getHashValue();
        String key = "B11C63E3F4CA70675AB3351708AE1912";
        PutObjectResult objectResult = client.putObject("mobile.print.media", key, in, new ObjectMetadata());
        System.out.println(objectResult.getVersionId());

    }

    private static String getHashValue() {
        String uid = UUID.randomUUID().toString().replace("-", "");
        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }
}
