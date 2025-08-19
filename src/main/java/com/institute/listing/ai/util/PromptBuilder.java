package com.institute.listing.ai.util;

public class PromptBuilder {

    private PromptBuilder() {
    }

    public static String buildSentimentPrompt(String reviewText) {
        return """
                Analyze the following text in any language (including Nepali and Hindi) and respond ONLY with one of these:
                POSITIVE, NEGATIVE, or INVALID_FEEDBACK if it contains Nepali slang.

                Text: "%s"
                """.formatted(reviewText);
    }
}
