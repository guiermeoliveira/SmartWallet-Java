package com.Smartwallet.domain.service;

import com.Smartwallet.domain.model.User;
import com.Smartwallet.domain.repository.UserRepository;
import com.Smartwallet.dto.request.UserRegistrationRequest;
import com.Smartwallet.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// 💡 @Slf4j (Lombok) injeta automaticamente um logger na classe.
// Em vez de System.out.println() (nunca use em produção!), usamos:
// log.info(), log.warn(), log.error(), log.debug()
// Logs estruturados são essenciais para monitorar a aplicação em produção.

@Slf4j

// 💡 @Service marca essa classe como componente de lógica de negócio.
// É um @Component especializado — semânticamente diz "aqui vivem as regras do negócio".
// O Spring cria uma instância única (singleton) e a injeta onde for necessário.
@Service

// 💡 @RequiredArgsConstructor (Lombok) gera o construtor com todos os campos 'final'.
// É a forma recomendada de fazer injeção de dependência no Spring —
// mais testável que @Autowired direto no campo, e o compilador garante
// que as dependências nunca são nulas.
@RequiredArgsConstructor

public class RegistrationService {
    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private final UserRepository userRepository;

    // 💡 PasswordEncoder é uma interface do Spring Security.
    // A implementação (BCryptPasswordEncoder) será configurada no SecurityConfig.
    // Usamos a interface aqui — não a implementação concreta — para facilitar testes
    // e respeitar o princípio de inversão de dependência (SOLID).
    private final PasswordEncoder passwordEncoder;

    public RegistrationService() {
        this.userRepository = null;
        this.passwordEncoder = null;
    }

    // 💡 @Transactional garante atomicidade: se qualquer passo falhar,
    // o banco reverte tudo automaticamente (rollback).
    // Ex: se salvar o usuário mas o envio de email falhar,
    // o usuário não fica cadastrado pela metade.
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {

        logger.info("Iniciando cadastro para email: {}", request.email());
    
        // ===== VALIDAÇÕES DE UNICIDADE =====

        // 💡 Verificamos TODOS os campos únicos antes de tentar salvar.
        // Se verificássemos um por um e lançássemos exceção no primeiro,
        // o usuário teria que corrigir e reenviar múltiplas vezes.
        // Assim coletamos todos os problemas de uma vez — melhor UX.
        validateUniqueFields(request);

        // ===== CONSTRUÇÃO DA ENTIDADE =====

        // 💡 Usamos o padrão Builder (gerado pelo Lombok @Builder na entidade User).
        // Mais legível que um construtor com 10 parâmetros na mesma ordem.
        // Se esquecer um campo, o compilador avisa — diferente de setters espalhados.
        User user = User.builder()
                .name(sanitizeName(request.name()))
                .username(request.username().toLowerCase().trim())
                .email(request.email().toLowerCase().trim())
                // 💡 CPF sempre armazenado sem formatação — só os 11 dígitos.
                // replaceAll("\\D", "") remove qualquer caractere não-numérico.
                .cpf(request.cpf().replaceAll("\\D", ""))
                // 💡 NUNCA armazene senha em texto puro.
                // passwordEncoder.encode() aplica BCrypt com salt aleatório.
                // O resultado é algo como: "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
                .password(passwordEncoder.encode(request.password()))
                // Valores padrão já definidos no @Builder.Default da entidade:
                // enabled = false (aguardando confirmação de email)
                // plan = FREE
                // role = USER
                // knowledgeLevel = BEGINNER
                .build();

        // ===== PERSISTÊNCIA =====

        User savedUser = userRepository.save(user);

        log.info("Usuário cadastrado com sucesso. ID: {}, email: {}",
                savedUser.getId(), savedUser.getEmail());

        // 💡 TODO: Sprint 2 — disparar email de confirmação de conta
        // emailService.sendVerificationEmail(savedUser);

        // 💡 Retornamos o DTO de resposta, nunca a entidade direta.
        // UserResponse.from() faz o mapeamento User → UserResponse.
        return UserResponse.from(savedUser);
    }

    // ===== MÉTODOS PRIVADOS =====

    // 💡 Métodos privados auxiliares deixam o método principal (register) limpo e legível.
    // Cada método tem UMA responsabilidade clara — princípio SRP do SOLID.
    private void validateUniqueFields(UserRegistrationRequest request) {

        // 💡 Coletamos todos os erros antes de lançar a exceção.
        StringBuilder errors = new StringBuilder();

        if (userRepository.existsByEmail(request.email().toLowerCase().trim())) {
            errors.append("E-mail já cadastrado. ");
        }

        if (userRepository.existsByUsername(request.username().toLowerCase().trim())) {
            errors.append("Nome de usuário já está em uso. ");
        }

        if (userRepository.existsByCpf(request.cpf().replaceAll("\\D", ""))) {
            errors.append("CPF já cadastrado. ");
        }

        if (!errors.isEmpty()) {
            // 💡 IllegalArgumentException é capturada pelo GlobalExceptionHandler
            // e retornada como HTTP 400 com mensagem padronizada.
            throw new IllegalArgumentException(errors.toString().trim());
        }
    }

    // 💡 Sanitização do nome: remove espaços extras entre palavras e nas bordas.
    // Ex: "  João   Silva  " → "João Silva"
    // Pequenos detalhes assim evitam dados sujos no banco.
    private String sanitizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }
}