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

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import lombok.Getter;

/**
 * Enumeração que representa as permissões disponíveis para os usuários no sistema.
 *
 * <p>
 * Cada permissão é representada por um valor de bit único, permitindo a combinação de múltiplas permissões em uma única
 * variável do tipo {@code long}. Essa abordagem facilita a verificação, concessão e revogação de permissões utilizando
 * operações bit a bit.
 * </p>
 *
 * <p>
 * Esta enumeração é utilizada principalmente para controlar o acesso a funcionalidades específicas da aplicação,
 * garantindo que apenas usuários autorizados possam executar determinadas ações.
 * </p>
 */
@Getter
public enum Permission {

    /**
     * Permissão para conceder permissões a um usuário. (Bit 0)
     */
    PERMISSION_GRANT("/permissions/{username:\\w+}/grant", POST, 0x1L),

    /**
     * Permissão para revogar permissões de um usuário. (Bit 1)
     */
    PERMISSION_REVOKE("/permissions/{username:\\w+}/revoke", POST, 0x2L),

    /**
     * Permissão para criar um novo pedido de entrega. (Bit 2)
     */
    ORDER_CREATE_ORDER("/orders", POST, 0x4L),

    /**
     * Permissão para listar todos os pedidos de entrega. (Bit 3)
     */
    ORDER_LIST_ORDERS("/orders", GET, 0x8L),

    /**
     * Permissão para buscar um pedido por ID. (Bit 4)
     */
    ORDER_GET_ORDER("/orders/{orderId:\\d+}", GET, 0x10L),

    /**
     * Permissão para atualizar o status de um pedido. (Bit 5)
     */
    ORDER_UPDATE_ORDER("/orders/{orderId:\\d+}", PUT, 0x20L),

    /**
     * Permissão para cancelar um pedido. (Bit 6)
     */
    ORDER_CANCEL_ORDER("/orders/{orderId:\\d+}", DELETE, 0x40L),

    /**
     * Permissão para aplicar desconto a um pedido. (Bit 7)
     */
    ORDER_APPLY_DISCOUNT("/orders/discount/{orderId:\\d+}", PUT, 0x80L);

    // BIT_RESERVED_8(null, null, 0x100L),
    // BIT_RESERVED_9(null, null, 0x200L),
    // BIT_RESERVED_10(null, null, 0x400L),
    // BIT_RESERVED_11(null, null, 0x800L),
    // BIT_RESERVED_12(null, null, 0x1000L),
    // BIT_RESERVED_13(null, null, 0x2000L),
    // BIT_RESERVED_14(null, null, 0x4000L),
    // BIT_RESERVED_15(null, null, 0x8000L),
    // BIT_RESERVED_16(null, null, 0x10000L),
    // BIT_RESERVED_17(null, null, 0x20000L),
    // BIT_RESERVED_18(null, null, 0x40000L),
    // BIT_RESERVED_19(null, null, 0x80000L),
    // BIT_RESERVED_20(null, null, 0x100000L),
    // BIT_RESERVED_21(null, null, 0x200000L),
    // BIT_RESERVED_22(null, null, 0x400000L),
    // BIT_RESERVED_23(null, null, 0x800000L),
    // BIT_RESERVED_24(null, null, 0x1000000L),
    // BIT_RESERVED_25(null, null, 0x2000000L),
    // BIT_RESERVED_26(null, null, 0x4000000L),
    // BIT_RESERVED_27(null, null, 0x8000000L),
    // BIT_RESERVED_28(null, null, 0x10000000L),
    // BIT_RESERVED_29(null, null, 0x20000000L),
    // BIT_RESERVED_30(null, null, 0x40000000L),
    // BIT_RESERVED_31(null, null, 0x80000000L),
    // BIT_RESERVED_32(null, null, 0x100000000L),
    // BIT_RESERVED_33(null, null, 0x200000000L),
    // BIT_RESERVED_34(null, null, 0x400000000L),
    // BIT_RESERVED_35(null, null, 0x800000000L),
    // BIT_RESERVED_36(null, null, 0x1000000000L),
    // BIT_RESERVED_37(null, null, 0x2000000000L),
    // BIT_RESERVED_38(null, null, 0x4000000000L),
    // BIT_RESERVED_39(null, null, 0x8000000000L),
    // BIT_RESERVED_40(null, null, 0x10000000000L),
    // BIT_RESERVED_41(null, null, 0x20000000000L),
    // BIT_RESERVED_42(null, null, 0x40000000000L),
    // BIT_RESERVED_43(null, null, 0x80000000000L),
    // BIT_RESERVED_44(null, null, 0x100000000000L),
    // BIT_RESERVED_45(null, null, 0x200000000000L),
    // BIT_RESERVED_46(null, null, 0x400000000000L),
    // BIT_RESERVED_47(null, null, 0x800000000000L),
    // BIT_RESERVED_48(null, null, 0x1000000000000L),
    // BIT_RESERVED_49(null, null, 0x2000000000000L),
    // BIT_RESERVED_50(null, null, 0x4000000000000L),
    // BIT_RESERVED_51(null, null, 0x8000000000000L),
    // BIT_RESERVED_52(null, null, 0x10000000000000L),
    // BIT_RESERVED_53(null, null, 0x20000000000000L),
    // BIT_RESERVED_54(null, null, 0x40000000000000L),
    // BIT_RESERVED_55(null, null, 0x80000000000000L),
    // BIT_RESERVED_56(null, null, 0x100000000000000L),
    // BIT_RESERVED_57(null, null, 0x200000000000000L),
    // BIT_RESERVED_58(null, null, 0x400000000000000L),
    // BIT_RESERVED_59(null, null, 0x800000000000000L),
    // BIT_RESERVED_60(null, null, 0x1000000000000000L),
    // BIT_RESERVED_61(null, null, 0x2000000000000000L),
    // BIT_RESERVED_62(null, null, 0x4000000000000000L),
    // BIT_RESERVED_63(null, null, 0x8000000000000000L);

    private static final PathPatternParser            PARSER        = new PathPatternParser();
    private static final Map<Permission, PathPattern> PATTERN_CACHE = new EnumMap<>(Permission.class);

    static {
        for (var permission : values()) {
            PATTERN_CACHE.put(permission, PARSER.parse(permission.path));
        }
    }

    private final String     path;
    private final HttpMethod method;
    private final long       mask;

    Permission(final String path, final HttpMethod method, final long mask) {
        this.path = path;
        this.method = method;
        this.mask = mask;
    }

    /**
     * Verifica se a permissão corresponde ao caminho e método HTTP fornecidos.
     *
     * @param requestPath Caminho da requisição.
     * @param method Método HTTP da requisição.
     * @return {@code true} se houver correspondência, caso contrário {@code false}.
     */
    public boolean matches(final String requestPath, final HttpMethod method) {
        var pattern = PATTERN_CACHE.get(this);
        return this.method == method && pattern.matches(PathContainer.parsePath(requestPath));
    }

    /**
     * Busca uma permissão com base no caminho e método HTTP.
     *
     * @param requestPath Caminho da requisição.
     * @param method Método HTTP da requisição.
     * @return {@link Optional} contendo a permissão correspondente, se encontrada.
     */
    public static Optional<Permission> findByPathAndMethod(final String requestPath, final HttpMethod method) {
        return Arrays.stream(values())
                .filter(p -> p.matches(requestPath, method))
                .findFirst();
    }
}
