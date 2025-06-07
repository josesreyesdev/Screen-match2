package com.jsrdev.screen_match.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.jsrdev.screen_match.utils.Configuration;

public class GeminiAIService {
    public static String getTranslation(String text) {
        Client client = new Client.Builder()
                .apiKey(Configuration.API_KEY_GEMINI)
                .build();

        String prompt = "Translate the following text into Spanish: " + text;

        try {
            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-1.5-flash", // used Gemini model
                            prompt,
                            null); // Added more configurations.

            if (response != null && response.text() != null) {
                return response.text();
            } else {
                System.err.println("Gemini API did not return text to translate.");
            }

        } catch (Exception e) {
            System.err.println("Text translation error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
