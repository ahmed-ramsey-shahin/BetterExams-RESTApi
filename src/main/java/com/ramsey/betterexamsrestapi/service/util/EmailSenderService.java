package com.ramsey.betterexamsrestapi.service.util;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.resource.Emailv31;
import com.ramsey.betterexamsrestapi.error.EmailFormatError;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
	
	private final VerificationCodeService verificationCodeService;
	private final PropertiesReaderService propertiesReaderService;
	private Pattern pattern;
	
	@PostConstruct
	@SuppressWarnings({"RegExpRedundantEscape", "RegExpUnnecessaryNonCapturingGroup"})
	public void init() {
		
		pattern = Pattern.compile(
				"^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
						"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
						"*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2" +
						"(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])" +
						"|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b" +
						"\\x0c\\x0e-\\x7f])+)\\])$"
		);
		
	}
	
	public String sendConfirmationEmail(String receiver) {
		
		if(!pattern.matcher(receiver).matches()) {
			
			throw new EmailFormatError();
			
		}
		
		String code = verificationCodeService.generateCode(8);
		sendEmail(
				receiver,
				"Confirmation email",
				String.format(
						"Your verification code is \"%s\", it is valid for only 1 hour",
						code
				)
		);
		
		return code;
		
	}
	
	public void sendEmail(String receiver, String subject, String body) {
		
		new Thread(() -> {
			
			MailjetClient client;
			MailjetRequest request;
			var fromJsonObject = new JSONObject()
					.put("Email", "ahmed.ramsey.shahin@gmail.com")
					.put("Name", "Better Exams");
			var toJsonArray = new JSONArray().put(new JSONObject()
					.put("Email", receiver)
					.put("Name", receiver));
			client = new MailjetClient(
					propertiesReaderService.get("apiKey", PropertiesReaderService.EMAIL),
					propertiesReaderService.get("apiSecretKey", PropertiesReaderService.EMAIL),
					new ClientOptions("v3.1")
			);
			request = new MailjetRequest(Emailv31.resource)
					.property(
							Emailv31.MESSAGES, new JSONArray()
									.put(
											new JSONObject()
													.put(Emailv31.Message.FROM, fromJsonObject)
													.put(Emailv31.Message.TO, toJsonArray)
													.put(Emailv31.Message.SUBJECT, subject)
													.put(Emailv31.Message.TEXTPART, body)
													.put(Emailv31.Message.HTMLPART, "")
													.put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")
									)
					);
			
			try {
				
				client.post(request);
				
			} catch(Exception ignored) {  }
			
		}).start();
		
	}
	
}
