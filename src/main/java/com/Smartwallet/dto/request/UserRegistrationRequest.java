package com.Smartwallet.dto.request;

import javax.validation.constraints.*;
import com.Smartwallet.domain.model.Email;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegistrationRequest(
    
    @NotNull(message = "O nome é obrigatório.")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "O nome deve conter apenas letras e espaços.")
    @Size(max = 150, message = "O nome deve conter no máximo 150 caracteres.")
    String name,

    @NotNull(message = "O nome de usuário é obrigatório.")
    @Size(max = 100, message = "O nome de usuário deve conter no máximo 100 caracteres.")
    @Pattern(regexp = "^[\\p{L}0-9._%+-]+$", message = "O nome de usuário deve conter apenas letras, números e caracteres especiais . _ % + -")
    String username,
    
    @NotNull(message = "O e-mail é obrigatório.")
    @Size(max = 180, message = "O e-mail é muito longo")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,24}$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Formato de e-mail inválido.")
    String email,
    
    @NotNull(message = "A senha é obrigatória.")
    @Size(min = 8, max = 72, message = "A senha deve conter entre 8 e 72 caracteres.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password,
    
    @NotNull(message = "O CPF é obrigatório.")
    @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    String cpf
) {
    
}