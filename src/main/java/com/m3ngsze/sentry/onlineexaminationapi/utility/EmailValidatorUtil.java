package com.m3ngsze.sentry.onlineexaminationapi.utility;

import java.util.regex.Pattern;

public class EmailValidatorUtil {

    // Regex taken from Spring's @Email (simplified, real Spring uses RFC 5322 regex)
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValid(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        email = email.trim();

        // check pattern
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }

        // optional: ensure domain has a dot
        String domain = email.substring(email.indexOf("@") + 1);
        return domain.contains(".");
    }

}
