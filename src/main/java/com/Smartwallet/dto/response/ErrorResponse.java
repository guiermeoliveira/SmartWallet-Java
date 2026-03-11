package com.Smartwallet.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

// 💡 ErrorResponse padroniza TODOS os erros da API no mesmo formato.
// Sem isso, cada erro retorna um JSON diferente — péssima experiência para o frontend.
//
// Com esse padrão, o frontend sempre sabe o que esperar:
// {
//   "status": 400,
//   "error": "Dados inválidos",
//   "message": "O e-mail já está cadastrado.",
//   "timestamp": "2026-03-10T14:30:00",
//   "fieldErrors": [...]  ← só aparece em erros de validação
// }

// 💡 @JsonInclude(NON_NULL) — campos null não aparecem no JSON.
// Ex: 'fieldErrors' só aparece quando há erros de validação de campos.
// Resposta mais limpa, sem "fieldErrors": null poluindo o JSON.
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

        int status,
        String error,
        String message,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        // 💡 Lista de erros por campo — só preenchida em erros de validação (400).
        // Ex: [{"field": "email", "message": "Formato de e-mail inválido."}]
        List<FieldError> fieldErrors

) {

    // 💡 Construtor simplificado para erros sem detalhe de campos (500, 401, 404 etc.)
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, LocalDateTime.now(), null);
    }

    // 💡 Construtor para erros de validação — inclui a lista de campos com problema
    public static ErrorResponse ofValidation(List<FieldError> fieldErrors) {
        return new ErrorResponse(
                400,
                "Dados inválidos",
                "Verifique os campos e tente novamente.",
                LocalDateTime.now(),
                fieldErrors
        );
    }

    // 💡 Record interno — representa o erro de um campo específico.
    // 'field' = nome do campo (ex: "email"), 'message' = mensagem de erro.
    public record FieldError(String field, String message) {}
}   