package com.institute.listing.core.service;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SlackService {
    private static final Logger log = LoggerFactory.getLogger(SlackService.class);

    private final Slack slack;
    private final String webhookUrl;

    public SlackService(@Value("${slack.webhook.url}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.slack = Slack.getInstance();
    }

    /**
     * Sends a message to Slack workspace using the webhook URL.
     * @param message the text message
     */
    public void sendMessage(String message) {
        Payload payload = Payload.builder().text(message).build();
        log.debug("Sending message to Slack: {}", message);

        try {
            WebhookResponse response = slack.send(webhookUrl, payload);
            log.debug("Slack response code: {}, message: {}", response.getCode(), response.getMessage());
        } catch (Exception e) {
            log.error("Failed to send message to Slack: {}", e.getMessage(), e);
        }
    }
}
