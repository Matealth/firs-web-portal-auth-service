package com.firs.risk.mgt.auth.svc.messaging.mailer;

import com.firs.risk.mgt.auth.svc.model.Message;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HtmlMailer {

    @Value("${app.name}")
    private String appName;
    @Value("${app.no-reply-email}")
    private String noReplyEmail;
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${sendgrid.demo-email-template-id}")
    private String emailTemplateID;

    public void sendDemoEmail(final Message message){
        Email from = new Email(noReplyEmail);
        from.setName(appName);
        String subject = "New Bank Demo Request";
        Email to = new Email(noReplyEmail);
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addDynamicTemplateData("bank_name", message.getName());
        personalization.addDynamicTemplateData("email_address", message.getEmail());
        personalization.addDynamicTemplateData("country", message.getCountry());
        personalization.addDynamicTemplateData("demo_date", message.getDate());
        mail.addPersonalization(personalization);
        mail.setTemplateId(emailTemplateID);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            log.error("Failed to send email: {}", ex.getMessage());
        }
    }
}