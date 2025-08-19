package com.institute.listing.ai.service.impl;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.institute.listing.ai.service.GeminiService;
import com.institute.listing.ai.util.PromptBuilder;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeminiServiceImpl implements GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiServiceImpl.class);

    private final Client client;
    private final Map<String, String> sentimentCache = new ConcurrentHashMap<>();

    @Value("${gemini.api.model}")
    private String modelId;

    public GeminiServiceImpl(@Value("${gemini.api.key}") String apiKey) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    /**
     * Analyze the sentiment of a review text.
     * Returns POSITIVE, NEGATIVE, INVALID_FEEDBACK, or UNKNOWN.
     */
    @Override
    public String analyzeReviewSentiment(String reviewText) {
        String cached = sentimentCache.get(reviewText);
        if (cached != null) {
            log.info("Review text matched previous input, using cached sentiment: {}", reviewText);
            return cached;
        }

        try {
            String prompt = PromptBuilder.buildSentimentPrompt(reviewText);
            GenerateContentResponse response = client.models.generateContent(modelId, prompt, null);

            String sentiment = normalizeResponse(response.text(), "POSITIVE", "NEGATIVE", "INVALID_FEEDBACK");

            sentimentCache.put(reviewText, sentiment);
            log.info("New review analyzed and cached: {}", reviewText);

            return sentiment;

        } catch (Exception e) {
            log.error("Failed to analyze review sentiment for text: {}", reviewText, e);
            return "UNKNOWN";
        }
    }

    /**
     * Normalize AI response to match expected valid values
     */
    private String normalizeResponse(String response, String... validResponses) {
        String text = Optional.ofNullable(response)
                .map(String::trim)
                .map(String::toUpperCase)
                .orElse("UNKNOWN");

        for (String valid : validResponses) {
            if (text.equals(valid)) return text;
        }
        return "UNKNOWN";
    }

    @PreDestroy
    public void close() {
        try {
            client.close();
            log.info("Gemini client closed successfully.");
        } catch (Exception e) {
            log.warn("Failed to close Gemini client safely.", e);
        }
    }
}
