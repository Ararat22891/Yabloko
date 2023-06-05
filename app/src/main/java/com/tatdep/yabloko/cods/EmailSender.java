package com.tatdep.yabloko.cods;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {
    private static final String TAG = "EmailSender";
    private static final String SMTP_HOST = "smtp.mail.ru";
    private static final int SMTP_PORT = 587;
    private static final String USERNAME = "partiyay@mail.ru"; // Замените на вашу почту отправителя
    private static final String PASSWORD = "2ESPRtf1wY2xK6SVtJxn"; // Замените на пароль от внешних приложений

    public static void sendEmail(String recipientEmail, String subject, String body) {
        new SendEmailTask().execute(recipientEmail, subject, body);
    }

    private static class SendEmailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String recipientEmail = params[0];
            String subject = params[1];
            String body = params[2];

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(USERNAME));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(subject);

                // Создание многочастного сообщения
                Multipart multipart = new MimeMultipart();

                // Добавление текстовой части сообщения
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(body, "text/html; charset=utf-8");
                multipart.addBodyPart(messageBodyPart);

                // Установка содержимого сообщения
                message.setContent(multipart);

                Transport.send(message);

                Log.d(TAG, "Email sent successfully");
            } catch (MessagingException e) {
                Log.e(TAG, "Failed to send email", e);
            }

            return null;
        }
    }
}

