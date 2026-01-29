package com.tritva.percel_delivery.services.impl;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.tritva.percel_delivery.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Value("${resend.from-name}")
    private String fromName;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(String to, String token) {
        try {
            log.info("=== PREPARING VERIFICATION EMAIL ===");
            log.info("To: {}", to);
            log.info("Code: {}", token);

            String subject = "Verify Your Email - Parcel Delivery App";
            String html = buildVerificationEmailHtml(token);
            sendEmail(to, subject, html);

            log.info("=== VERIFICATION EMAIL SENT SUCCESSFULLY ===");
        } catch (Exception e) {
            log.error("=== EMAIL SENDING FAILED ===");
            log.error("Error: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            log.info("=== PREPARING PASSWORD RESET EMAIL ===");
            log.info("To: {}", to);
            log.info("Reset Code: {}", token);

            String subject = "Password Reset - Parcel Delivery App";
            String html = buildPasswordResetEmailHtml(token);
            sendEmail(to, subject, html);

            log.info("=== PASSWORD RESET EMAIL SENT SUCCESSFULLY ===");
        } catch (Exception e) {
            log.error("=== EMAIL SENDING FAILED ===");
            log.error("Error: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage());
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private void sendEmail(String to, String subject, String html) {
        try {
            Resend resend = new Resend(apiKey);

            SendEmailRequest params = SendEmailRequest.builder()
                    .from(fromName + " <" + fromEmail + ">")
                    .to(to)
                    .subject(subject)
                    .html(html)
                    .build();

            resend.emails().send(params);
            log.info("Email sent to {}", to);
        } catch (ResendException e) {
            log.error("Failed to send email to {}", to, e);
            throw new RuntimeException("Resend API error", e);
        } catch (Exception e) {
            log.error("Unexpected error sending email: {}", e.getMessage());
            throw new RuntimeException("Unexpected error sending email", e);
        }
    }

    private String buildVerificationEmailHtml(String verificationCode) {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Verify Your Email</title>" +
                "</head>" +
                "<body style=\"margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; background-color: #f3f4f6;\">"
                +
                "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #f3f4f6; padding: 40px 20px;\">"
                +
                "        <tr>" +
                "            <td align=\"center\">" +
                "                <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); overflow: hidden;\">"
                +
                "                    <!-- Header -->" +
                "                    <tr>" +
                "                        <td style=\"background: linear-gradient(135deg, #2563eb 0%, #4338ca 100%); padding: 40px 30px; text-align: center;\">"
                +
                "                            <img src=\"https://mflowpos.com/mflow-white/mflow-white-icon.png\" alt=\"Parcel Delivery App\" height=\"50\" style=\"display: block; margin: 0 auto 20px; border: 0;\">"
                +
                "                            <h1 style=\"margin: 0; color: #ffffff; font-size: 28px; font-weight: 700;\">Welcome to Parcel Delivery App!</h1>"
                +
                "                        </td>" +
                "                    </tr>" +
                "                    <!-- Content -->" +
                "                    <tr>" +
                "                        <td style=\"padding: 40px 30px;\">" +
                "                            <p style=\"margin: 0 0 20px; color: #374151; font-size: 16px; line-height: 1.6;\">Thank you for registering with Parcel Delivery App. To complete your registration, please verify your email address using the code below:</p>"
                +
                "                            " +
                "                            <!-- Verification Code Box -->" +
                "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: 30px 0;\">"
                +
                "                                <tr>" +
                "                                    <td align=\"center\" style=\"background-color: #f9fafb; border: 2px dashed #e5e7eb; border-radius: 8px; padding: 30px;\">"
                +
                "                                        <div style=\"font-size: 36px; font-weight: 700; letter-spacing: 8px; color: #2563eb; font-family: 'Courier New', monospace;\">"
                +
                verificationCode +
                "                                        </div>" +
                "                                    </td>" +
                "                                </tr>" +
                "                            </table>" +
                "                            " +
                "                            <p style=\"margin: 20px 0; color: #6b7280; font-size: 14px; line-height: 1.6;\">This code will expire in <strong>15 minutes</strong>. If you didn't create an account with Parcel Delivery App, please ignore this email.</p>"
                +
                "                            " +
                "                            <p style=\"margin: 30px 0 0; color: #374151; font-size: 16px; line-height: 1.6;\">Best regards,<br><strong>The Parcel Delivery App Team</strong></p>"
                +
                "                        </td>" +
                "                    </tr>" +
                "                    <!-- Footer -->" +
                "                    <tr>" +
                "                        <td style=\"background-color: #f9fafb; padding: 30px; text-align: center; border-top: 1px solid #e5e7eb;\">"
                +
                "                            <p style=\"margin: 0 0 10px; color: #6b7280; font-size: 14px;\">© 2026 Parcel Delivery App. All rights reserved.</p>"
                +
                "                            <p style=\"margin: 0; color: #9ca3af; font-size: 12px;\">" +
                "                                <a href=\"" + frontendUrl
                + "\" style=\"color: #2563eb; text-decoration: none;\">Visit our website</a>" +
                "                            </p>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";
    }

    private String buildPasswordResetEmailHtml(String resetCode) {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Password Reset</title>" +
                "</head>" +
                "<body style=\"margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; background-color: #f3f4f6;\">"
                +
                "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #f3f4f6; padding: 40px 20px;\">"
                +
                "        <tr>" +
                "            <td align=\"center\">" +
                "                <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); overflow: hidden;\">"
                +
                "                    <!-- Header -->" +
                "                    <tr>" +
                "                        <td style=\"background: linear-gradient(135deg, #2563eb 0%, #4338ca 100%); padding: 40px 30px; text-align: center;\">"
                +
                "                            <img src=\"https://mflowpos.com/mflow-white/mflow-white-icon.png\" alt=\"Parcel Delivery App\" height=\"50\" style=\"display: block; margin: 0 auto 20px; border: 0;\">"
                +
                "                            <h1 style=\"margin: 0; color: #ffffff; font-size: 28px; font-weight: 700;\">Password Reset Request</h1>"
                +
                "                        </td>" +
                "                    </tr>" +
                "                    <!-- Content -->" +
                "                    <tr>" +
                "                        <td style=\"padding: 40px 30px;\">" +
                "                            <p style=\"margin: 0 0 20px; color: #374151; font-size: 16px; line-height: 1.6;\">We received a request to reset your password for your Parcel Delivery App account. Use the code below to reset your password:</p>"
                +
                "                            " +
                "                            <!-- Reset Code Box -->" +
                "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: 30px 0;\">"
                +
                "                                <tr>" +
                "                                    <td align=\"center\" style=\"background-color: #f9fafb; border: 2px dashed #e5e7eb; border-radius: 8px; padding: 30px;\">"
                +
                "                                        <div style=\"font-size: 36px; font-weight: 700; letter-spacing: 8px; color: #2563eb; font-family: 'Courier New', monospace;\">"
                +
                resetCode +
                "                                        </div>" +
                "                                    </td>" +
                "                                </tr>" +
                "                            </table>" +
                "                            " +
                "                            <p style=\"margin: 20px 0; color: #6b7280; font-size: 14px; line-height: 1.6;\">This code will expire in <strong>15 minutes</strong>. If you didn't request a password reset, please ignore this email or contact support if you have concerns.</p>"
                +
                "                            " +
                "                            <p style=\"margin: 30px 0 0; color: #374151; font-size: 16px; line-height: 1.6;\">Best regards,<br><strong>The Parcel Delivery App Team</strong></p>"
                +
                "                        </td>" +
                "                    </tr>" +
                "                    <!-- Footer -->" +
                "                    <tr>" +
                "                        <td style=\"background-color: #f9fafb; padding: 30px; text-align: center; border-top: 1px solid #e5e7eb;\">"
                +
                "                            <p style=\"margin: 0 0 10px; color: #6b7280; font-size: 14px;\">© 2026 Parcel Delivery App. All rights reserved.</p>"
                +
                "                            <p style=\"margin: 0; color: #9ca3af; font-size: 12px;\">" +
                "                                <a href=\"" + frontendUrl
                + "\" style=\"color: #2563eb; text-decoration: none;\">Visit our website</a>" +
                "                            </p>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";
    }
}
