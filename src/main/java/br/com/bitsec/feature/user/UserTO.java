/*
 * MIT License
 *
 * Copyright (c) 2025.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

    /**
     * Permissões do usuário representadas como uma máscara de bits.
     */
    @Column(name = "permissions", nullable = false)
    private Long permissions;
}