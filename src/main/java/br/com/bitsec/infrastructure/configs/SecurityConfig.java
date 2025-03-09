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
package br.com.bitsec.infrastructure.configs;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import br.com.bitsec.feature.permission.Permission;
import br.com.bitsec.feature.permission.PermissionService;
import lombok.RequiredArgsConstructor;

/**
 * Configuração de segurança da aplicação utilizando Spring Security.
 *
 * <p>
 * Define as permissões de acesso para diferentes endpoints e configura a autenticação via JWT.
 * </p>
 * <p>
 * Esta classe configura o controle de acesso aos endpoints da aplicação com base nas permissões definidas na enumeração
 * {@link Permission}. Além disso, habilita a autenticação utilizando tokens JWT, garantindo que apenas usuários com as
 * permissões adequadas possam acessar certos recursos da API. A configuração também desativa a proteção CSRF, pois a
 * aplicação é baseada em uma API REST sem sessões.
 * </p>
 * <p>
 * A autenticação e autorização são configuradas com base em JWT, onde as permissões do usuário são extraídas do token
 * JWT e usadas para autorizar o acesso aos endpoints correspondentes.
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
class SecurityConfig {

    final PermissionService permissionService;

    @Value("${bws.jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${bws.jwt.private.key}")
    private RSAPrivateKey privateKey;

    /**
     * Define os endpoints públicos que podem ser acessados sem autenticação.
     *
     * <p>
     * A configuração atual permite que qualquer um acesse as rotas "/h2/**" (para testes do banco H2) e "/auth/**"
     * (para autenticação de usuários).
     * </p>
     *
     * @return {@link RequestMatcher} com os endpoints públicos configurados.
     */
    @Bean
    RequestMatcher publicEndpoints() {
        return new OrRequestMatcher(new AntPathRequestMatcher("/h2/**"), new AntPathRequestMatcher("/auth/**"));
    }

    /**
     * Configura a cadeia de filtros de segurança para a aplicação.
     *
     * <p>
     * Este método define as regras de autorização para os endpoints da aplicação, permitindo o acesso a endpoints
     * públicos sem autenticação e exigindo autenticação e autorização por permissões específicas para os demais
     * endpoints. Além disso, configura a utilização do JWT para autenticação e define a política de sessão como sem
     * estado (stateless).
     * </p>
     *
     * @param http O objeto de configuração do Spring Security.
     * @return A cadeia de filtros configurada.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            // Endpoints públicos
            authorize.requestMatchers(publicEndpoints()).permitAll();

            // Endpoints protegidos dinamicamente com base no enum
            Arrays.stream(Permission.values()).forEach(permission -> {
                var matcher = new AntPathRequestMatcher( //
                        permission.getPath(), //
                        permission.getMethod().name() //
                );

                authorize.requestMatchers(matcher) //
                        .hasAuthority(permission.name());
            });

            // Qualquer outro endpoint exige autenticação
            authorize.anyRequest().authenticated();
        }).csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    /**
     * Converte o token JWT para autoridades (permissões) que o usuário possui.
     *
     * <p>
     * Este conversor extrai as permissões do token JWT e as mapeia para autoridades do Spring Security. As permissões
     * são extraídas a partir do campo "permissions" do token JWT, onde as permissões são armazenadas como uma máscara
     * de bits.
     * </p>
     *
     * @return O conversor de autenticação JWT configurado.
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var permissions = (Long) jwt.getClaim("permissions");
            var authorities = permissionService.authorities(permissions);
            return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        });

        return converter;
    }

    /**
     * Configura o codificador de senha para o Spring Security.
     *
     * <p>
     * O codificador de senha utilizado é o {@link BCryptPasswordEncoder}, que oferece um alto nível de segurança para
     * armazenamento e verificação de senhas de usuários.
     * </p>
     *
     * @return O codificador de senha configurado.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura o decodificador de JWT, utilizando a chave pública para validar a assinatura do token.
     *
     * <p>
     * A chave pública é utilizada para garantir que os tokens JWT sejam válidos e não tenham sido alterados.
     * </p>
     *
     * @return O decodificador JWT configurado.
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    /**
     * Configura o codificador de JWT, utilizando a chave pública e privada para assinar e validar tokens JWT.
     *
     * <p>
     * A chave privada é usada para assinar os tokens JWT, enquanto a chave pública é usada para validá-los.
     * </p>
     *
     * @return O codificador JWT configurado.
     */
    @Bean
    JwtEncoder jwtEncoder() {
        var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();

        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
