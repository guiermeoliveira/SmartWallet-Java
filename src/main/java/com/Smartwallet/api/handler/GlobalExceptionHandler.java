package com.Smartwallet.api.handler;

import com.Smartwallet.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

// 💡 @RestControllerAdvice intercepta exceções lançadas em qualquer Controller
// e as transforma em respostas HTTP padronizadas com nosso ErrorResponse.
// É o "tratador global de erros" da API — sem ele, erros retornam HTML ou JSON
// despadronizado do Spring, o que é péssimo para quem consome a API.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 💡 Captura erros de validação do @Valid / @Validated nos Controllers.
    // Quando um campo falha em @NotBlank, @Email, @ValidCPF etc.,
    // o Spring lança MethodArgumentNotValidException com todos os erros de campo.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Extrai todos os erros de campo e converte para nosso FieldError
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.FieldError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.ofValidation(fieldErrors));
    }

    // 💡 Captura erros de negócio que lançamos manualmente no Service.
    // Ex: "E-mail já cadastrado", "CPF já existe" etc.
    // IllegalArgumentException é usada para regras de negócio simples no MVP.
    // Nas próximas sprints criaremos exceptions customizadas (BusinessException etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBusinessErrors(
            IllegalArgumentException ex) {

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(400, "Erro de validação", ex.getMessage()));
    }

    // 💡 Credenciais inválidas no login (email/senha errados)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex) {

        // 💡 Mensagem genérica intencional — não dizemos se o email ou a senha
        // estão errados para não dar dicas a atacantes (enumeração de usuários).
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(401, "Não autorizado", "Credenciais inválidas."));
    }

    // 💡 Conta desabilitada (email não confirmado)
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledAccount(
            DisabledException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(403, "Conta inativa", "Confirme seu e-mail para ativar a conta."));
    }

    // 💡 Captura qualquer exceção não tratada pelos handlers acima.
    // Retorna 500 com mensagem genérica — nunca exponha stacktrace em produção!
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericErrors(Exception ex) {

        // 💡 Log do erro real para investigação interna (veremos logging estruturado em breve)
        // Em produção: logar com correlation ID para rastrear o erro nos logs
        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        500,
                        "Erro interno",
                        "Ocorreu um erro inesperado. Tente novamente mais tarde."
                ));
    }
}