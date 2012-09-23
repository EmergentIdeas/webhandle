package com.emergentideas.webhandle.apps.oak.email;


import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.RequestMessages;
import com.emergentideas.webhandle.assumptions.oak.interfaces.EmailService;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.Show;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

public class EmailHandle {
	
	protected String successURL = "composemail";
	protected EmailService emailService;
	protected Logger log = SystemOutLogger.get(EmailHandle.class);
	
	@Handle(value = {"/sendemail", "/composemail"}, method = HttpMethod.GET)
	@Template
	@Wrap("public_page")
	public Object loginGet(Location location, String forward) {
		return "composeEmail";
	}
	
	@Handle(value = "/sendemail", method = HttpMethod.POST)
	@Template
	@Wrap("public_page")
	public Object loginPost(RequestMessages messages, String to, String from, String subject, String msg) {
		try {
			emailService.sendEmail(new String[] { to }, from, null, null, subject, null, msg);
			messages.getSuccessMessages().add("Your email has been sent!");
		}
		catch(Exception e) {
			log.error("There was some problem sending the email.", e);
			messages.getErrorMessages().add("Oops.  We couldn't send the email.");
		}
		
		return new Show(successURL);
	}

	public String getSuccessURL() {
		return successURL;
	}

	public void setSuccessURL(String successURL) {
		this.successURL = successURL;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	@Wire
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}


	
}
