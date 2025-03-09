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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador responsável por gerenciar operações de autenticação no sistema.
 *
 * <p>
 * Este controlador expõe endpoints REST para autenticação de usuários e geração de tokens JWT, permitindo que clientes
 * obtenham tokens válidos mediante fornecimento de credenciais corretas.
 * </p>
 *
 * <p>
 * As operações são delegadas ao serviço {@link AuthService}, que encapsula a lógica de autenticação e emissão de
 * tokens.
 * </p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
class AuthController {

    final AuthService authService;

    /**
     * Autentica um usuário e gera um token JWT.
     *
     * <p>
     * Este endpoint recebe as credenciais de um usuário, valida os dados e, caso estejam corretos, retorna um token JWT
     * que pode ser usado para autenticação em chamadas subsequentes.
     * </p>
     *
     * @param auth DTO contendo nome de usuário e senha.
     * @return {@link ResponseEntity} contendo o token JWT como string no corpo da resposta.
     */
    @PostMapping(value = "/authenticate", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    HttpEntity<String> authenticate(@RequestBody final AuthRequest auth) {
        log.info("Iniciando processo de autenticação para o usuário: {}", auth.username());

        // Realiza a autenticação e gera o token
        var token = authService.authenticate(auth.username(), auth.password());

        log.info("Autenticação bem-sucedida para o usuário: {}", auth.username());

        // Retorna o token JWT
        return ResponseEntity.ok(token);
    }
}
