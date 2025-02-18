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
package br.com.bitsec.feature.order;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável pelas operações relacionadas a pedidos de entrega.
 *
 * <p>
 * <strong>AVISO IMPORTANTE:</strong>
 * </p>
 * <p>
 * Este controlador foi desenvolvido exclusivamente para a prova de conceito (POC) envolvendo o Spring Security e
 * operações bitwise. Seu foco é validar aspectos específicos de segurança e manipulação de bits, não seguindo
 * necessariamente as melhores práticas de arquitetura de APIs RESTful ou princípios sólidos de design de software.
 * </p>
 *
 * <p>
 * <strong>Nota sobre a estrutura do código:</strong>
 * </p>
 * <p>
 * A estrutura adotada para a classe foi propositalmente simplificada, visando facilitar a realização de testes rápidos
 * e objetivos, sem priorizar a flexibilidade, a manutenibilidade ou a escalabilidade a longo prazo.
 * </p>
 */
@RestController
@RequestMapping("orders")
class OrderController {

    /**
     * Cria um novo pedido de entrega.
     *
     * @param description Descrição do pedido.
     * @param customerId ID do cliente.
     * @return Mensagem de sucesso.
     */
    @PostMapping
    String createOrder(
            @RequestParam("description") final String description,
            @RequestParam("customerId") final Long customerId) {
        return "Pedido de entrega criado com sucesso para o cliente ID " + customerId + " com a descrição: "
                + description;
    }

    /**
     * Lista todos os pedidos.
     *
     * @return Mensagem de sucesso.
     */
    @GetMapping
    String listOrders() {
        return "Lista de pedidos retornada com sucesso!";
    }

    /**
     * Busca um pedido por ID.
     *
     * @param orderId ID do pedido.
     * @return Mensagem de sucesso.
     */
    @GetMapping("/{orderId:\\d+}")
    String getOrder(@PathVariable("orderId") final Long orderId) {
        return "Pedido de entrega com ID " + orderId + " obtido com sucesso!";
    }

    /**
     * Atualiza o status de um pedido.
     *
     * @param orderId ID do pedido.
     * @param status Novo status.
     * @return Mensagem de sucesso.
     */
    @PutMapping("/{orderId:\\d+}")
    String updateOrder(@PathVariable("orderId") final Long orderId,
            @RequestParam("status") final String status) {
        return "Status do pedido " + orderId + " atualizado para: " + status;
    }

    /**
     * Cancela um pedido.
     *
     * @param orderId ID do pedido.
     * @return Mensagem de sucesso.
     */
    @DeleteMapping("/{orderId:\\d+}")
    String cancelOrder(@PathVariable("orderId") final Long orderId) {
        return "Pedido de ID " + orderId + " cancelado com sucesso!";
    }

    /**
     * Aplica desconto a um pedido.
     *
     * @param orderId ID do pedido.
     * @param discount Percentual de desconto.
     * @return Mensagem de sucesso.
     */
    @PutMapping("/discount/{orderId:\\d+}")
    String applyDiscountToOrder(@PathVariable("orderId") final Long orderId,
            @RequestParam("discount") final double discount) {
        return "Desconto de " + discount + "% aplicado ao pedido de ID " + orderId + ".";
    }
}
