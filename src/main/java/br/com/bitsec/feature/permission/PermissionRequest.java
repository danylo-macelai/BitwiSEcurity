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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO utilizado para representar as permissões associadas a um usuário no sistema.
 *
 * <p>
 * Este DTO contém um array de permissões, onde cada permissão é representada por um valor da enumeração
 * {@link Permission}. A enumeração {@link Permission} define as permissões disponíveis, como a capacidade de criar
 * pedidos, visualizar pedidos, entre outras ações específicas.
 * </p>
 *
 * <p>
 * Este modelo permite a atribuição de múltiplas permissões a um usuário através da especificação de uma lista de
 * valores, possibilitando a combinação de permissões utilizando operações bit a bit. Essa abordagem facilita o controle
 * de acesso e a definição de quais funcionalidades o usuário poderá acessar.
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
record PermissionRequest(
        Permission[] flags //
) implements Serializable {
}
