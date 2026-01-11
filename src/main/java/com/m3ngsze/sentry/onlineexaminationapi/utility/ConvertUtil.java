package com.m3ngsze.sentry.onlineexaminationapi.utility;

public class ConvertUtil {

    public static String toPascalCase(String input) {
        if (input == null || input.isBlank()) return input;

        StringBuilder result = new StringBuilder();
        // Split by spaces or hyphens to handle names like "sok-dara" or "sok dara"
        String[] words = input.trim().split("[\\s-]+");

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
                // result.append(" "); // Optional: Add space back if you want "Sok Dara"
            }
        }
        return result.toString().trim();
    }
}
