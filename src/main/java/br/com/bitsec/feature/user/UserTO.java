package br.com.bitsec.feature.user;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um usuário do sistema.
 *
 * <p>
 * Esta classe mapeia a tabela {@code BWS_USER} no banco de dados e contém os dados principais de autenticação e
 * autorização do usuário, como nome de usuário, senha e permissões.
 * </p>
 *
 * <p>
 * A propriedade {@code permissions} utiliza uma máscara de bits (bitmask) para representar as permissões atribuídas ao
 * usuário, possibilitando operações eficientes de verificação e modificação via operações bitwise.
 * </p>
 *
 * <p>
 * A entidade possui restrição de unicidade e índice para o campo {@code username}, garantindo performance e
 * consistência.
 * </p>
 */
@SuppressWarnings("serial")
@Entity
@Table( //
        name = "BWS_USER", uniqueConstraints = { //
                @UniqueConstraint( //
                        name = "uk_user_username", //
                        columnNames = { "username" })//
        }, //
        indexes = { //
                @Index( //
                        name = "idx_user_username", //
                        columnList = "username") } //
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTO implements Serializable {

    /**
     * Identificador único do usuário.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;

    /**
     * Nome de usuário utilizado para login no sistema.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Senha do usuário.
     */
    @Column(name = "password", nullable = false)
    private String password;

}