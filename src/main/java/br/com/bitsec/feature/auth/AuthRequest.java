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

import java.io.Serializable;

/**
 * DTO que representa a requisição de autenticação de um usuário.
 *
 * <p>
 * Esta classe é utilizada para transportar as credenciais de autenticação — nome de usuário e senha — fornecidas pelo
 * cliente no momento do login. Serve como estrutura de dados para endpoints de autenticação, garantindo a transferência
 * segura e estruturada das informações necessárias.
 * </p>
 *
 * <p>
 * Esta classe é imutável e serializável, adequada para utilização em ambientes distribuídos.
 * </p>
 *
 * @param username Nome de usuário utilizado para autenticação.
 * @param password Senha correspondente ao nome de usuário informado.
 */
record AuthRequest(

        String username,

        String password

) implements Serializable {
}
