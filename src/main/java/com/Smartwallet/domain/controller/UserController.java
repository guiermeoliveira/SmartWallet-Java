package com.Smartwallet.domain.controller;

import com.Smartwallet.domain.service.RegistrationService;
import com.Smartwallet.dto.request.UserRegistrationRequest;
import com.Smartwallet.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.logger.Logger;
import org.logger.LoggerFactory;

// @RestController = @Controller + @ResponseBody
// Significa que todos os métodos retornam dados (JSON) diretamente,
// não templates HTML. É o padrão para APIs REST.
@Slf4j
@RestController

// 💡 @RequestMapping define o prefixo de todos os endpoints dessa classe.
// Combinado com o context-path '/api/v1' do application.yml,
// o endpoint completo de registro será:
// POST http://localhost:8080/api/v1/auth/register
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final RegistrationService registrationService;

    // 💡 @PostMapping("/register") mapeia requisições POST para /auth/register
    // @RequestBody deserializa o JSON da requisição para o record UserRegistrationRequest
    // @Valid dispara todas as validações (@NotBlank, @Email, @ValidCPF etc.)
    //         Se alguma falhar, o GlobalExceptionHandler captura e retorna 400.
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody @Valid UserRegistrationRequest request) {

        logger.info("Requisição de cadastro recebida para: {}", request.email());

        UserResponse response = registrationService.register(request);

        // 💡 HTTP 201 Created é o status correto para criação de recursos.
        // Diferente de 200 OK — 201 indica explicitamente que algo foi criado.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 💡 Endpoint de health check — útil para verificar se a API está no ar.
    // Não precisa de autenticação (liberado no SecurityConfig).
    // Em produção, ferramentas de monitoramento chamam esse endpoint periodicamente.
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("SmartWallet API está no ar! 🚀");
    }
}