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

/**
 * Interface de serviço para operações relacionadas a usuários.
 *
 * <p>
 * Essa interface deve ser implementada por classes que encapsulam a lógica de negócio relacionada à entidade de
 * usuário. A implementação típica desse serviço envolve o uso de um repositório para persistência dos dados e aplicação
 * de validações ou regras de negócio.
 * </p>
 */
public interface UserService {

    /**
     * Busca um usuário pelo seu nome de usuário (username).
     *
     * @param username Nome de usuário a ser buscado.
     * @return {@link UserTO} com os dados do usuário encontrado.
     * @throws RuntimeException se o nome de usuário for nulo ou se o usuário não for encontrado.
     */
    UserTO findUser(final String username);

    /**
     * Atualiza as permissões de um usuário.
     *
     * <p>
     * As permissões são representadas como um valor do tipo {@code long}, onde cada bit indica se o usuário possui (ou
     * não) determinada permissão. Isso permite realizar verificações e atualizações com operações bitwise.
     * </p>
     *
     * @param user Usuário a ter as permissões atualizadas.
     * @param permissions Novo valor de permissões (máscara de bits).
     * @return {@link UserTO} com as permissões atualizadas.
     */
    UserTO updatePermissions(final UserTO user, final Long permissions);
}
