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

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bitsec.feature.permission.PermissionService;
import br.com.bitsec.feature.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço responsável pela autenticação de departamentos e geração de tokens JWT.
 *
 * <p>
 * Esta implementação do contrato {@link AuthService} realiza a validação das credenciais fornecidas para o
 * departamento, incluindo verificações de status e integridade da senha. Em caso de sucesso, gera um token JWT contendo
 * as permissões (scopes) do departamento autenticado. Caso as credenciais sejam inválidas ou o departamento esteja
 * desativado ou bloqueado, uma exceção do tipo {@link BuzzerException} será lançada com o código apropriado.
 * </p>
 *
 * <p>
 * O token gerado contém informações como o nome de usuário, tempo de expiração e permissões associadas ao departamento,
 * permitindo o acesso a recursos protegidos da API.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
class AuthServiceImpl implements AuthService {

    final JwtEncoder        jwtEncoder;
    final UserService       userService;
    final PermissionService permissionService;
    final PasswordEncoder   encryptionService;

    @Value("${bws.jwt.expires.in}")
    private long expiresIn;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public String authenticate(final String username, final String password) {
        log.info("Iniciando processo de autenticação para o usuário: {}", username);

        var user = userService.findUser(username);

        // Verifica se a senha fornecida corresponde à senha armazenada
        if (!encryptionService.matches(password, user.getPassword())) {
            log.error("Falha na autenticação: Senha inválida para o usuário {}", username);
            throw new RuntimeException("Senha inválida para o usuário informado.");
        }

        log.info("Autenticação bem-sucedida para o usuário: {}", username);

        var now = Instant.now();

        // Criação do conjunto de declarações (claims) do JWT
        var claims = JwtClaimsSet
                .builder()
                .issuer("bitsec-api")
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("permissions", user.getPermissions())
                .build();

        // Log para indicar que o token foi gerado com sucesso
        log.info("Token JWT gerado com sucesso para o usuário: {}", username);

        // Retorna o token JWT codificado
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
