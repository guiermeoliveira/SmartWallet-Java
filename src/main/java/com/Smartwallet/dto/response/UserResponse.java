package com.Smartwallet.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import com.Smartwallet.domain.model.Email;
import com.Smartwallet.domain.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

// 💡 UserResponse é o DTO de SAÍDA — controla exatamente o que a API retorna.
// Regra de ouro: NUNCA retorne a entidade User diretamente na resposta.
// Motivo: a entidade tem campos sensíveis (password, cpf) e campos internos
// (accountNonLocked, authorities) que não devem ser expostos publicamente.
//
// Com esse DTO temos controle total sobre o contrato da API — podemos mudar
// a entidade internamente sem quebrar os clientes que consomem a API.
public record UserResponse(

        UUID id,
        String name,
        String username,
        String email,

        // 💡 Retornamos o plano e o nível de conhecimento para o frontend
        // poder adaptar a UI (ex: mostrar badge "Premium", trilhas corretas etc.)
        String plan,
        String knowledgeLevel,

        boolean emailVerified,

        // 💡 @JsonFormat define como datas são serializadas no JSON.
        // Sem isso, o Jackson retorna um array de números: [2026, 3, 10, 14, 30, 0]
        // Com isso, retorna uma string legível: "2026-03-10T14:30:00"
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastLoginAt

        // 💡 Repare no que NÃO está aqui:
        // ✗ password   → jamais exposto
        // ✗ cpf        → dado sensível, só retornado em endpoints específicos e autenticados
        // ✗ accountNonLocked, enabled → detalhes internos de segurança
        // ✗ financialProfileJson → endpoint próprio para isso (GET /profile)
) {

    // 💡 Método estático de fábrica 'from()' — converte User → UserResponse.
    // Centraliza a lógica de mapeamento em um único lugar.
    // Se um campo mudar de nome na entidade, corrigimos só aqui.
    //
    // Alternativa seria usar MapStruct (biblioteca de mapeamento automático),
    // mas para o MVP o método manual é mais simples e mais fácil de entender.
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPlan().name(),
                user.getKnowledgeLevel().name(),
                user.isEmailVerified(),
                user.getCreatedAt(),
                user.getLastLoginAt()
        );
    }
}