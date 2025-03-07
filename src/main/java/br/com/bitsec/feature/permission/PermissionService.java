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
package br.com.bitsec.feature.permission;

import java.util.List;

import br.com.bitsec.feature.user.UserTO;

/**
 * Serviço responsável pela manipulação das permissões dos usuários no sistema.
 *
 * <p>
 * Este serviço fornece métodos para conceder, revogar e verificar permissões, além de obter as autoridades associadas.
 * </p>
 */
public interface PermissionService {

    /**
     * Concede permissões a um usuário.
     *
     * <p>
     * Permite adicionar permissões específicas a um usuário, utilizando a máscara de bits associada a cada permissão.
     * </p>
     *
     * @param user O usuário ao qual as permissões serão concedidas.
     * @param permission As permissões a serem concedidas ao usuário.
     */
    void grant(final UserTO user, final Permission... permission);

    /**
     * Revoga permissões de um usuário.
     *
     * <p>
     * Permite remover permissões específicas de um usuário, utilizando a máscara de bits para identificá-las.
     * </p>
     *
     * @param user O usuário do qual as permissões serão revogadas.
     * @param permission As permissões a serem revogadas do usuário.
     */
    void revoke(final UserTO user, final Permission... permission);

    /**
     * Verifica se o usuário possui uma permissão específica.
     *
     * <p>
     * Compara a máscara de permissões do usuário com a máscara de bits correspondente à permissão desejada.
     * </p>
     *
     * @param permissions A máscara de permissões associada ao usuário.
     * @param bitMask A máscara de bits que representa a permissão a ser verificada.
     * @return {@code true} se o usuário possuir a permissão, {@code false} caso contrário.
     */
    boolean has(final Long permissions, final long bitMask);

    /**
     * Obtém as autoridades associadas a um conjunto de permissões.
     *
     * <p>
     * Converte a máscara de permissões do usuário em uma lista de autoridades legíveis (strings).
     * </p>
     *
     * @param permissions A máscara de permissões associada ao usuário.
     * @return Uma lista de autoridades representando as permissões do usuário.
     */
    List<String> authorities(final Long permissions);
}
