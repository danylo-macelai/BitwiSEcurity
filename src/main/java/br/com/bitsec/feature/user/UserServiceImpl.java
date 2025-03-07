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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementação do serviço de usuário.
 *
 * <p>
 * Esta classe fornece a lógica de negócios para operações relacionadas a usuários, como busca e atualização de
 * permissões.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserTO findUser(final String username) {
        if (username == null || username.isBlank()) {
            log.warn("Tentativa de busca de usuário com username nulo ou vazio.");
            throw new IllegalArgumentException("Username não pode ser nulo ou vazio.");
        }

        log.info("Buscando usuário com username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado: {}", username);
                    return new RuntimeException("Usuário não encontrado: " + username);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserTO updatePermissions(final UserTO user, final Long permissions) {
        if (user == null) {
            log.warn("Tentativa de atualização de permissões em usuário nulo.");
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        if (permissions == null) {
            log.warn("Tentativa de atualização de permissões com valor nulo para usuário: {}", user.getUsername());
            throw new IllegalArgumentException("Permissões não podem ser nulas.");
        }

        log.info("Atualizando permissões do usuário: {} para: {}", user.getUsername(), permissions);

        user.setPermissions(permissions);
        return userRepository.save(user);
    }
}