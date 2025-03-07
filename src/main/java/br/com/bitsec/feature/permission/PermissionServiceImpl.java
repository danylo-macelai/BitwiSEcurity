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

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bitsec.feature.user.UserService;
import br.com.bitsec.feature.user.UserTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço responsável pela gestão de permissões de departamentos no sistema.
 *
 * <p>
 * Esta classe implementa a interface {@link PermissionService}, fornecendo a lógica de negócios para concessão,
 * revogação, verificação e exibição de permissões associadas a um {@link UserTO}. As permissões são manipuladas através
 * de operações bit a bit, utilizando máscaras definidas na enumeração {@link Permission}. As operações realizadas por
 * este serviço garantem que os departamentos possuam ou deixem de possuir determinadas permissões com base em regras
 * consistentes e transações seguras.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
class PermissionServiceImpl implements PermissionService {

    final UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void grant(final UserTO user, final Permission... permissions) {
        // Combina os bitmasks das permissões com OR para criar a máscara de permissões
        var permissionMask = Arrays.stream(permissions).mapToLong(Permission::getMask).reduce(0L, (a, b) -> a | b);

        // Adiciona a nova máscara de permissões às permissões existentes
        var updatedPermissions = user.getPermissions() | permissionMask;

        log.debug("Concedendo permissões [{}] para o usuário [{}]", Arrays.toString(permissions), user.getUsername());

        // Atualiza as permissões do departamento com a nova máscara
        userService.updatePermissions(user, updatedPermissions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void revoke(final UserTO user, final Permission... permissions) {
        // Combina os bitmasks das permissões com OR para criar a máscara de permissões
        var permissionMask = Arrays.stream(permissions).mapToLong(Permission::getMask).reduce(0L, (a, b) -> a | b);

        // Remove as permissões, desativando os bits correspondentes
        var updatedPermissions = user.getPermissions() & ~permissionMask;

        log.debug("Revogando permissões [{}] do usuário [{}]", Arrays.toString(permissions), user.getUsername());

        // Atualiza as permissões do departamento com a máscara alterada
        userService.updatePermissions(user, updatedPermissions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final Long permissions, final long bitMask) {
        var hasPermission = (permissions & bitMask) != 0;

        log.trace("Verificando permissão [{}]: {}", bitMask, hasPermission);

        return hasPermission;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> authorities(final Long permissions) {
        var authorities = Arrays.stream(Permission.values())
                .filter(p -> has(permissions, p.getMask()))
                .map(Enum::name)
                .toList();

        log.trace("Autoridades derivadas das permissões [{}]: {}", permissions, authorities);

        return authorities;
    }
}
