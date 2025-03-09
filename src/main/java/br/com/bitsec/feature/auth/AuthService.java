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
package br.com.bitsec.feature.auth;

/**
 * Serviço responsável pela autenticação de usuários no sistema.
 *
 * <p>
 * Esta interface define o contrato para autenticação de usuários, recebendo as credenciais de login (e-mail e senha),
 * validando-as e retornando um token de acesso JWT caso as credenciais sejam válidas.
 * </p>
 *
 * <p>
 * O token JWT gerado pode ser utilizado para autenticar o usuário em requisições subsequentes, permitindo o acesso a
 * recursos protegidos da API. A autenticação falha se as credenciais forem inválidas.
 * </p>
 */
public interface AuthService {

    /**
     * Realiza a autenticação de um usuário utilizando o e-mail e a senha fornecidos.
     *
     * <p>
     * Este método valida as credenciais fornecidas pelo usuário. Se as credenciais forem válidas, um token JWT é gerado
     * e retornado. Caso contrário, uma exceção de autenticação será lançada.
     * </p>
     *
     * @param email O endereço de e-mail do usuário, usado como nome de usuário para autenticação.
     * @param password A senha do usuário, usada juntamente com o e-mail para validação.
     * @return Um token JWT gerado para o usuário autenticado, que poderá ser utilizado para acessar recursos
     *         protegidos.
     */
    String authenticate(final String email, final String password);

}
