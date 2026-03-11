package com.Smartwallet.domain.model;
import java.util.regex.Pattern;

public record Email(String value) {

    // Expressão regular para validar o formato do e-mail
    private static final Pattern PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,24}$", Pattern.CASE_INSENSITIVE);

    // Valida o formato do e-mail no construtor
    public Email {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
    }
}