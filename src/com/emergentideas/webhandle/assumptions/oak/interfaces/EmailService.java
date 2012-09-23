package com.emergentideas.webhandle.assumptions.oak.interfaces;

public interface EmailService {

	public boolean sendEmail(String[] to, String from, String[] cc, String[] bcc, String subject, String textContent, String htmlContent);
	
}
