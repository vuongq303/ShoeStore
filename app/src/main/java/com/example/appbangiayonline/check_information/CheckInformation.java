package com.example.appbangiayonline.check_information;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckInformation {
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNumberValid(String number) {
        if (number.charAt(0) == '0' && number.length() == 10) {
            return true;
        }
        return false;
    }
}
