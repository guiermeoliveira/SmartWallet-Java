package com.Smartwallet.domain.repository;

import com.Smartwallet.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// 💡 @Repository marca essa interface como um componente de acesso a dados.
// O Spring cria a implementação automaticamente em tempo de execução —
// você não precisa escrever nenhum SQL para os métodos básicos.
@Repository

// 💡 JpaRepository<Entidade, TipoDoId> nos dá de graça:
// save(), findById(), findAll(), delete(), count(), existsById() e muito mais.
// Só de estender essa interface, temos um CRUD completo sem escrever uma linha de SQL.
public interface UserRepository extends JpaRepository<User, UUID> {

    // 💡 Spring Data JPA interpreta o nome do método e gera o SQL automaticamente.
    // 'findBy' + 'Email' → SELECT * FROM users WHERE email = ?
    // Optional<> é a forma moderna de lidar com valores que podem ser nulos —
    // evita NullPointerException e força o chamador a tratar o caso "não encontrado".
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByCpf(String cpf);

    // 💡 'existsBy' gera um SELECT COUNT > 0 — mais eficiente que buscar o objeto inteiro
    // quando só precisamos saber se existe. Usamos no cadastro para checar duplicatas.
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByCpf(String cpf);

    // 💡 @Query permite escrever JPQL (Java Persistence Query Language) quando
    // o nome do método ficaria complexo demais ou precisamos de algo customizado.
    // JPQL usa nomes das CLASSES e CAMPOS Java, não nomes de tabelas/colunas SQL.
    // Aqui buscamos por email OU username — útil para o login aceitar os dois.
    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.username = :identifier")
    Optional<User> findByEmailOrUsername(String identifier);
}