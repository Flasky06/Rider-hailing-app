package com.tritva.percel_delivery.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class NotificationService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Value("${resend.from-name}")
    private String fromName;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String RESEND_URL = "https://api.resend.com/emails";

    public void sendAssignmentEmail(String toEmail, String riderName) {
        String subject = "Your Parcel Delivery has been Assigned!";
        String htmlBody = "<p>Good news! A rider has been assigned to your request.</p>" +
                "<p><strong>Rider Name:</strong> " + riderName + "</p>" +
                "<p>They are on their way to the pickup location.</p>";

        sendEmail(toEmail, subject, htmlBody);
    }

    public void sendDeliveryEmail(String toEmail, String deliveryId) {
        String subject = "Your Parcel has been Delivered!";
        String htmlBody = "<p>Your parcel (Order ID: " + deliveryId + ") has been successfully delivered.</p>" +
                "<p>Thank you for using our service.</p>";

        sendEmail(toEmail, subject, htmlBody);
    }

    private void sendEmail(String to, String subject, String html) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("from", fromName + " <" + fromEmail + ">");
            body.put("to", to);
            body.put("subject", subject);
            body.put("html", html);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(RESEND_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email sent successfully to {}", to);
            } else {
                log.error("Failed to send email to {}. Status: {}", to, response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error sending email to {}: {}", to, e.getMessage());
        }
    }
}
