package com.book.collection.book_collection_manager.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISBNValidator implements ConstraintValidator<ISBN, String>{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        String digits = value.replaceAll("[\\s-]", "");
        if (digits.length() == 10) return isValidIsbn10(digits);
        if (digits.length() == 13) return isValidIsbn13(digits);
        return false;
    }

    private boolean isValidIsbn10(String s) {
        if (!s.matches("^[0-9]{9}[0-9Xx]$")) return false;
        int sum = 0;
        for (int i = 0; i < 9; i++) sum += (s.charAt(i) - '0') * (10 - i);
        char last = s.charAt(9);
        int check = (last == 'X' || last == 'x') ? 10 : (last - '0');
        sum += check;
        return sum % 11 == 0;
    }

    private boolean isValidIsbn13(String s) {
        if (!s.matches("^[0-9]{13}$")) return false;
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = s.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        return sum % 10 == 0;
    }

}
