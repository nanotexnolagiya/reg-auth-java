package model;

import com.sun.mail.smtp.SMTPTransport;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Users {
    private static MimeMessage connSmtpMail() throws MessagingException {
        Properties mailProps = new Properties();

        mailProps.put("mail.transport.protocol","smtp");
        mailProps.put("mail.smtp.host","mail.eduris.uz");
        mailProps.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(mailProps,new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return(new PasswordAuthentication("subscribe@eduris.uz","1^$QqR1)_f!9"));
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("subscribe@eduris.uz"));
        return message;
    }
    public String registration(String login, String email, String password) throws MessagingException {
        String token = TokenGenerator.md5Custom(TokenGenerator.generateToken());
        String rtoken = TokenGenerator.md5Custom(token);
        password = TokenGenerator.md5Custom(password);
        try (Connection conn = DB.getConnection();
             PreparedStatement sql = conn.prepareStatement("INSERT INTO users (login, email, password, token, reset_token) VALUES (?, ?, ?, ?, ?);");
        ){
            sql.setString(1, login);
            sql.setString(2, email);
            sql.setString(3, password);
            sql.setString(4, token);
            sql.setString(5, rtoken);
            sql.executeUpdate();
            sendActivateToken(rtoken, email);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            token = "";
        }
        return token;
    }
    private static void sendActivateToken(String token, String email) throws MessagingException {

        MimeMessage message = connSmtpMail();
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Активация","utf-8");
        String text = "Чтобы активировать профил перейдите по ссылке\r\n";
        text += "loaclhost:8080/activate?token="+token+"\r\n";
        message.setText(text,"utf-8");
        Transport.send(message);
    }
    public static Boolean issetEmail(String email){
        try (Connection conn = DB.getConnection();
        ){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT email FROM users");
            while (rs.next()) {
                if (rs.getString(1).equals(email)){
                    return true;
                }
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void sendResetPassword(String email) throws MessagingException {
        String rtoken = TokenGenerator.md5Custom(TokenGenerator.generateToken());
        MimeMessage message = connSmtpMail();
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Сбрось пароля","utf-8");
        String text = "Чтобы сбросить пароль перейдите по ссылке\r\n";
        text += "loaclhost:8080/new_password?m="+email+"&token="+rtoken+"\r\n";
        message.setText(text,"utf-8");
        Transport.send(message);
    }

    private static Boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static List<String> validationRegistration(String login, String email, String password, String confirm_pass){
        List<String> errors = new ArrayList<String>();
        if (login.length() < 5){
            errors.add("Логин должен быть больше 5 символов");
        }

            if (isValidEmailAddress(email)){
                if (issetEmail(email)){
                    errors.add("Такой E-mail адрес существует");
                }
            }else{
                errors.add("E-mail неправильно введен");
            }
        if(password.equals(confirm_pass)){
            if(password.length() < 5){
                errors.add("Парол должен быть больше 5 символов");
            }
        }else{
            errors.add("Пароли не совпадает");
        }
        return errors;
    }
    public static List<String> validationAuthorization(String email, String password){
        List<String> errors = new ArrayList<String>();

            if (!isValidEmailAddress(email)){
                errors.add("E-mail неправильно введен");
            }
        if(password.length() < 5){
            errors.add("Парол должен быть больше 5 символов");
        }
        return errors;
    }
    private static String updateToken(int id) throws SQLException, ClassNotFoundException {
        String token = "error";
        try (Connection conn = DB.getConnection();
             PreparedStatement sql = conn.prepareStatement("UPDATE users SET token=? WHERE id=?");
        ){
            token = TokenGenerator.md5Custom(TokenGenerator.generateToken());
            sql.setString(1, token);
            sql.setInt(2, id);
            sql.executeUpdate();
        }
        return token;
    }
    public String authorization(String email, String password){
        String result = "";
        try (Connection conn = DB.getConnection();
        ){
            result = "error";
            int user_id = 0;
            password = TokenGenerator.md5Custom(password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                if (rs.getString("password").equals(password) && rs.getString("email").equals(email)){
                    user_id = rs.getInt("id");
                }
            }
            if (user_id != 0){
                result = updateToken(user_id);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
    public String authentication(String sessionToken){
        String result = "";
        try (Connection conn = DB.getConnection();
        ){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                if (rs.getString("token").equals(sessionToken)){
                    result = rs.getString("login");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Boolean activateUser(String token){
        Boolean result = false;
        try (Connection conn = DB.getConnection();
        ){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                if (rs.getString("reset_token").equals(token)){
                    int id = rs.getInt("id");
                    PreparedStatement sql = conn.prepareStatement("UPDATE users SET reset_token=NULL, status=1 WHERE id="+id);
                    sql.executeUpdate();
                    result = true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }


}
