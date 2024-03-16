package com.ivanhorlov.moviesbackend.services;

import com.ivanhorlov.moviesbackend.dtos.RegistrationUserDto;
import com.ivanhorlov.moviesbackend.dtos.ResetPasswordDto;
import com.ivanhorlov.moviesbackend.entities.User;
import freemarker.template.Configuration;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final Configuration configuration;
//    private final SpringTemplateEngine templateEngine;
    @Value("${client-address.url}")
    private String clientAddress;

    @Value("${server-address.url}")
    private String serverAddress;

    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    public void sendMail(String receiver, String text){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(receiver);
        message.setSubject("Текстовое письмо");
        message.setText(text);

        javaMailSender.send(message);
    }



    @Override
    public boolean sendActivationCode(RegistrationUserDto registrationUserDto, String activationCode) throws FileNotFoundException, MessagingException {
//        String text = String.format("Hello, %s \n" +
//                                        "Welcome to Movies-rezka. Please visit the link to activate your account: " +
//                "http://localhost:8080/account_activation/%s",registrationUserDto.getUsername(), activationCode);
//        message.setText(text);
//
//        javaMailSender.send(message);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
//        Context context = new Context();
//        context.setVariables(email.getContext());
//        String emailContent = templateEngine.process(email.getTemplateLocation(), context);

        mimeMessageHelper.setTo(registrationUserDto.getEmail());
        mimeMessageHelper.setSubject("Confirm page");
        mimeMessageHelper.setFrom(senderEmail);
        mimeMessageHelper.setText(getActivationEmailContent(registrationUserDto.getUsername(), String.format("%s/account_activation/%s",clientAddress, activationCode), String.format("%s/account_activation/%s",serverAddress, activationCode)), true);

        javaMailSender.send(message);

        return true;
    }


    @SneakyThrows
    private String getActivationEmailContent(String name, String activateLink, String cancelLink) {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("activate_link", activateLink);
        model.put("cancelLink", cancelLink);
        configuration.getTemplate("confirmMail.html")
                .process(model, stringWriter);
        return stringWriter.getBuffer()
                .toString();
    }

    @Override
    public boolean sendEmailToResetPassword(ResetPasswordDto resetPasswordDto){
        User user = userService.getUserByEmail(resetPasswordDto.getEmail());

        if(user == null){
//            throw new NoSuchElementException(String.format("User with email %s doesn't exist", resetPasswordDto.getEmail()));
            return false;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(resetPasswordDto.getEmail());
        message.setSubject("Text message");

        String newPassword = UUID.randomUUID().toString();
        String encodedPassword = encoder.encode(newPassword);
        message.setText("Your new password: " + newPassword);

        user.setPassword(encodedPassword);
        userService.saveUser(user);

        javaMailSender.send(message);

        return true;
    }

}
