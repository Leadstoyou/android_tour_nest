package com.example.tour_nest.util;

import android.annotation.SuppressLint;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.example.tour_nest.model.Order;

public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "trinhtiendat2510@gmail.com";
    private static final String APP_PASSWORD = "qxok oqkl veao ewom";
    private static final String APP_NAME = "TourNest";

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

    // Gửi email đặt lại mật khẩu
    public static void sendResetPasswordEmail(String recipientEmail) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            String temporaryPassword = "123@123a";
            String emailContent = String.format(
                    "Hello,\n\n" +
                            "Follow this link to reset your %s password for your %s account.\n\n" +
                            "Your password has been changed to: %s\n\n" +
                            "If you didn’t ask to reset your password, you can ignore this email.\n\n" +
                            "Thanks,\n" +
                            "The %s Team",
                    APP_NAME, recipientEmail, temporaryPassword, APP_NAME
            );

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Reset Your " + APP_NAME + " Password");
            message.setText(emailContent);

            Transport.send(message);
            LogUtil.logMessage("Email đặt lại mật khẩu đã được gửi tới: " + recipientEmail);
        } catch (MessagingException e) {
            LogUtil.logMessage("Gửi email thất bại: " + e.getMessage());
        }
    }

    // Gửi email xác nhận order
    public static void sendOrderConfirmationEmail(String recipientEmail, Order order) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            @SuppressLint("DefaultLocale") String emailContent = String.format(
                    "Xin chào,\n\n" +
                            "Cảm ơn bạn đã đặt tour tại %s! Dưới đây là thông tin chi tiết đơn hàng của bạn:\n\n" +
                            "Mã đơn hàng: %s\n" +
                            "Tên tour: %s\n" +
                            "Số người lớn: %d\n" +
                            "Số trẻ em: %d\n" +
                            "Ngày khởi hành: %s\n" +
                            "Giá cơ bản: %s\n" +
                            "Tổng giá người lớn: %s\n" +
                            "Tổng giá trẻ em: %s\n" +
                            "Tổng phụ: %s\n" +
                            "Tổng cộng: %s\n" +
                            "Trạng thái: %s\n" +
                            "Ngày đặt hàng: %s\n\n" +
                            "Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua email này.\n\n" +
                            "Trân trọng,\n" +
                            "Đội ngũ %s",
                    APP_NAME,
                    order.getOrderId() != null ? order.getOrderId() : "Chưa có ID",
                    order.getTourName(),
                    order.getAdultCount(),
                    order.getChildCount(),
                    order.getDepartureDate(),
                    currencyFormat.format(order.getBasePrice()),
                    currencyFormat.format(order.getAdultPrice()),
                    currencyFormat.format(order.getChildPrice()),
                    currencyFormat.format(order.getSubtotal()),
                    currencyFormat.format(order.getTotal()),
                    order.getStatus(),
                    order.getOrderDate() != null ? dateFormat.format(order.getOrderDate()) : "Chưa xác định",
                    APP_NAME
            );

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Xác nhận đơn hàng từ " + APP_NAME);
            message.setText(emailContent);

            Transport.send(message);
            LogUtil.logMessage("Email xác nhận đơn hàng đã được gửi tới: " + recipientEmail);
        } catch (MessagingException e) {
            LogUtil.logMessage("Gửi email xác nhận đơn hàng thất bại: " + e.getMessage());
        }
    }

    // Gửi email chào mừng
    public static void sendWelcomeEmail(String recipientEmail, String fullName) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            String emailContent = String.format(
                    "Xin chào %s,\n\n" +
                            "Chào mừng bạn đến với %s!\n\n" +
                            "Chúng tôi rất vui khi bạn đã gia nhập cộng đồng của chúng tôi. Với %s, bạn có thể khám phá và đặt những chuyến du lịch tuyệt vời một cách dễ dàng.\n\n" +
                            "Hãy bắt đầu hành trình của bạn ngay hôm nay bằng cách đăng nhập và khám phá các tour du lịch hấp dẫn!\n\n" +
                            "Nếu bạn có bất kỳ câu hỏi nào, đừng ngần ngại liên hệ với chúng tôi qua email này.\n\n" +
                            "Trân trọng,\n" +
                            "Đội ngũ %s",
                    fullName, APP_NAME, APP_NAME, APP_NAME
            );

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Chào mừng đến với " + APP_NAME);
            message.setText(emailContent);

            Transport.send(message);
            LogUtil.logMessage("Email chào mừng đã được gửi tới: " + recipientEmail);
        } catch (MessagingException e) {
            LogUtil.logMessage("Gửi email chào mừng thất bại: " + e.getMessage());
        }
    }
}