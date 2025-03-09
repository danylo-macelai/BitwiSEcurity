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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bitsec.feature.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador responsável pela gestão de permissões de usuários.
 *
 * <p>
 * Este controlador disponibiliza endpoints para conceder e revogar permissões de usuários, utilizando operações bitwise
 * sobre valores do tipo {@code long}.
 * </p>
 *
 * <p>
 * Ele depende dos serviços {@link UserService} e {@link PermissionService} para localizar usuários e gerenciar suas
 * permissões associadas.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
class PermissionController {

    final UserService       userService;
    final PermissionService permissionService;

    /**
     * Concede permissões a um usuário.
     *
     * @param username Nome do usuário ao qual as permissões serão concedidas.
     * @param permissionRequest Solicitação contendo as permissões a serem concedidas.
     * @return {@link ResponseEntity} com status HTTP 200 OK indicando sucesso.
     */
    @PostMapping(value = "/{username:\\w+}/grant", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    HttpEntity<Void> grant(
            @PathVariable("username") final String username,
            @RequestBody final PermissionRequest permissionRequest) {
        log.info("Concedendo permissões {} para o usuário '{}'", permissionRequest.flags(), username);
        var user = userService.findUser(username);
        permissionService.grant(user, permissionRequest.flags());
        log.debug("Permissões concedidas com sucesso para o usuário '{}'", username);
        return ResponseEntity.ok().build();
    }

    /**
     * Revoga permissões de um usuário.
     *
     * @param username Nome do usuário cujas permissões serão revogadas.
     * @param permissionRequest Solicitação contendo as permissões a serem revogadas.
     * @return {@link ResponseEntity} com status HTTP 200 OK indicando sucesso.
     */
    @PostMapping(value = "/{username:\\w+}/revoke", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    HttpEntity<Void> revoke(
            @PathVariable("username") final String username,
            @RequestBody final PermissionRequest permissionRequest) {
        log.info("Revogando permissões {} do usuário '{}'", permissionRequest.flags(), username);
        var user = userService.findUser(username);
        permissionService.revoke(user, permissionRequest.flags());
        log.debug("Permissões revogadas com sucesso do usuário '{}'", username);
        return ResponseEntity.ok().build();
    }
}