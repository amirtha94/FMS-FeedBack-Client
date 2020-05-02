package com.fms.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fms.model.ParticipatedDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Value("${email.bu.subject}")
	private String subjectBu;

	public void sendEmailTemplate(ParticipatedDetails participant) {
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("eventname", participant.getEventName());
			model.put("eventdate", participant.getEventDate());
			model.put("feedback", participant.getFeedback());
			model.put("participant", participant);

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(model);
			context.setVariable("url", "http://localhost:8083/addrating");
			String html = templateEngine.process("MailTemplate", context);
			helper.setTo(participant.getEmailId());
			helper.setText(html, true);
			helper.setSubject(participant.getEmployeeId() + ": FeedBack Request for " + participant.getEventId());
			emailSender.send(message);

		} catch (Exception e) {
			log.error("Exception occured in sendEmailTemplate():: {} :: emailId:{}", e.getMessage(),
					participant.getEmailId());
		}
	}

	public String sendEmail(String mailAdd, String fileName) throws Exception {
		String success = "Mail Sent Successfully!";
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject(subjectBu);
			helper.setTo(new InternetAddress(mailAdd));
			helper.setText(subjectBu);
			File fileLoc = new File(fileName);
			if (fileLoc.exists()) {
				FileSystemResource file = new FileSystemResource(new File(fileName));
				helper.addAttachment("EventExcel.xlsx", file);
			} else {
				log.error("File Doesnt exists at {}", fileName);
			}
			mailSender.send(message);

		} catch (Exception e) {
			log.error("Exception occured in sendEmail():: {}", e.getMessage());
			success = "Mail Sent Failed!";
			throw new Exception(success);
		}
		return success;
	}

	@Bean
	public JavaMailSenderImpl getJavaMail() {
		JavaMailSenderImpl mail = new JavaMailSenderImpl();
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		mail.setJavaMailProperties(props);
		mail.setUsername("capstonefms@gmail.com");
		mail.setPassword("capstone123");

		return mail;
	}
}
