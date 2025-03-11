<div align="justify">
    <div align="center">
        <picture>
            <img alt="BWS - Bitwise Security" src="./docs/banner.png" />
        </picture>
    </div>
    <hr />
    <p>
        Uma POC enxuta, focada em demonstrar o controle de permissões utilizando operações bitwise + Spring Security. Ideal para quem busca otimizar o armazenamento de permissões e proteger rotas de forma simples, minimalista e performática.
    </p>
    <h2>
        Bitwise
    </h2>
    <p>
        Ao utilizar <strong>bitwise</strong> para controlar permissões, buscamos uma solução simples e eficiente que aproveita cada <strong>bit</strong> de um número para representar uma <strong>permissão</strong>.
    </p>
    <p>
        Em vez de armazenar permissões em listas ou estruturas complexas, podemos representá-las em um único número. Cada bit desse número pode estar ativado (<strong>1</strong>), indicando que o usuário tem uma permissão, ou desativado (<strong>0</strong>), indicando que ele não possui.
    </p>
    <div align="center">

```bash
0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0011 
```
<p>
            <sup><small>Representação binária de um valor <code>long</code> em Java com 64 bits.</small></sup>
        </p>
    </div>
    <p>
        Neste exemplo, os dois últimos bits à direita estão ativados, ou seja, com valor <strong>1</strong>. O que significa que as permissões nas posições <strong>0</strong> e <strong>1</strong> estão ativas, ou seja, o usuário possui duas permissões.
    </p>
    <blockquote>
        <p>💡 <strong>Dica: </strong></p>
        <p>
             Imagine uma fileira de interruptores de luz, onde cada um controla uma função específica. Se o interruptor está ligado (1), a função correspondente está ativa, se está desligado (0), ela está inativa. No nosso exemplo, apenas os dois primeiros interruptores à direita estão ligados, ou seja, apenas duas funções estão habilitadas.
        </p>
    </blockquote>
    <blockquote>
        <p><strong>⚠️ Atenção:</strong></p>
        <p>
            A contagem dos bits ocorre da direita para a esquerda. Começa-se pelo bit 0, depois o bit 1, o bit 2 e assim por diante. A posição de cada bit é importante, pois ela representa uma potência de 2. Ou seja, o bit mais à direita, o bit 0, corresponde a 2⁰, o próximo (bit 1) corresponde a 2¹, e assim por diante.
        </p>
    </blockquote>
    <p>
        As permissões são representadas por um <code>enum</code> com máscaras de bits, onde cada permissão, como <code>PERMISSION_GRANT</code> e <code>PERMISSION_REVOKE</code>, tem um valor associado a um bit específico (ex: 0x1L para conceder acessos e 0x2L para revogar). Usando operações bitwise, é possível combinar essas permissões de maneira eficiente e compacta.
    </p>

```java
@Getter
public enum Permission {

    PERMISSION_GRANT("/permissions/{username:\\w+}/grant", POST, 0x1L),

    PERMISSION_REVOKE("/permissions/{username:\\w+}/revoke", POST, 0x2L);
    
    ... Outros bits reservados para novas funcionalidades 
    
    private final String path;
    private final HttpMethod method;
    private final long mask;
    
    ... Outros Métodos 

}
```
<div align="center">
        <p>
            <sup><small>Representação do mapeamento entre <code>Serviço</code>, <code>Método</code> e <code>Máscara</code> de permissões.</small></sup>
        </p>
    </div>
    <h4>
        Armazenamento Eficiente de Permissões
    </h4>
    <p>
        As permissões ficam representadas pelo número binário <strong>...0011</strong>, que equivale ao valor decimal <strong>3</strong>. Isso indica que o usuário tem duas permissões ativas, de forma simples e eficiente.
    </p>
    <p>
        Assim, evitamos criar várias colunas ou tabelas auxiliares. As permissões ficam centralizadas em um único valor do tipo <code>Long</code>, armazenado na entidade <code>UserTO</code>, o que facilita o controle e a manutenção das permissões.
    </p>

```java
@Table(
    name = "BWS_USER",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_username", columnNames = { "username" })
    },
    indexes = {
        @Index(name = "idx_user_username", columnList = "username")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTO implements Serializable {

    ... Outros Atributos 
   
    @Column(name = "permissions", nullable = false)
    private Long permissions;
   
    ... Outros Atributos 

}
```
<div align="center">
        <p>
            <sup><small>Representação de 64 permissões em um único <code>Long</code>.</small></sup>
        </p>
    </div>
    <h4>
        Operação Bitwise OR para Ativar Permissões
    </h4>
    <p>
        No método de concessão de permissões, usamos a operação <code>OR</code> para combinar os bitmasks das permissões e ativar os bits correspondentes. A operação mantém os bits já ativados e adiciona as permissões novas de forma eficiente.
    </p>

```java
@Override
@Transactional
public void grant(final UserTO user, final Permission... permissions) {
    // Combina os bitmasks das permissões usando OR
    var permissionMask = Arrays.stream(permissions)
        .mapToLong(Permission::getMask)
        .reduce(0L, (a, b) -> a | b);

    // Ativa os bits das permissões no valor atual
    var updatedPermissions = user.getPermissions() | permissionMask;

    // Atualiza o usuário com as novas permissões
    userService.updatePermissions(user, updatedPermissions);
}
```
<div align="center">
        <p>
            <sup><small>Implementação da concessão de permissões na classe <code>PermissionServiceImpl</code>.</small></sup>
        </p>
    </div>
    <h4>
        Operações Bitwise AND e NOT para Revogar Permissões
    </h4>
    <p>
        No método de revogação, usamos a operação <code>AND</code> combinada com <code>NOT</code> para desativar os bits das permissões a serem removidas, mantendo as permissões restantes intactas.
    </p>

```java
@Override
@Transactional
public void revoke(final UserTO user, final Permission... permissions) {
    // Combina os bitmasks das permissões a serem removidas
    var permissionMask = Arrays.stream(permissions)
        .mapToLong(Permission::getMask)
        .reduce(0L, (a, b) -> a | b);

    // Desativa os bits das permissões com AND & ~
    var updatedPermissions = user.getPermissions() & ~permissionMask;

    // Atualiza o usuário com a nova máscara de permissões
    userService.updatePermissions(user, updatedPermissions);
}
```
<div align="center">
        <p>
            <sup><small>Implementação da revogação de permissões na classe <code>PermissionServiceImpl</code>.</small></sup>
        </p>
    </div>
    <h2>
        Spring Security
    </h2>
    <p>
        A integração com o Spring Security protege a API, utilizando JWT para carregar as informações do usuário e as permissões representadas por bits.
    </p>
    <h4>
        Funciona assim:
    </h4>
    <p>
        O <code>enum</code> <code>Permission</code> define as rotas protegidas. Cada valor do enum inclui o caminho, o método HTTP e a permissão associada. A partir disso, é possível registrar cada endpoint com a autorização necessária de forma dinâmica:
    </p>

```java
@Bean
public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> {
        
        ... Endpoints públicos
        
        // Protege endpoints dinamicamente com base no enum Permission
        Arrays.stream(Permission.values()).forEach(permission -> {
            var matcher = new AntPathRequestMatcher( //
                    permission.getPath(), //
                    permission.getMethod().name() //
                    );
            
            authorize.requestMatchers(matcher) //
                        .hasAuthority(permission.name());
        });
        
        ... Qualquer outro endpoint exige autenticação
        
    })

    ... Outras configurações 
    
    return http.build();
}
```
<div align="center">
        <p>
            <sup><small>Configuração de segurança com mapeamento dinâmico de permissões para rotas e métodos HTTP na classe <code>SecurityConfig</code>.</small></sup>
        </p>
    </div>
    <p>
        Quando o usuário se autentica, o backend gera um JWT, incluindo as permissões do usuário no campo <code>permissions</code> como um <code>long</code> com os bits ativados.
    </p>

```java
@Override
@Transactional(readOnly = true)
public String authenticate(final String username, final String password) {
    var user = userService.findUser(username);

    if (!encryptionService.matches(password, user.getPassword())) {
        throw new RuntimeException("Senha inválida para o usuário informado.");
    }

    var now = Instant.now();

    var claims = JwtClaimsSet.builder()
        .issuer("bitsec-api")
        .subject(user.getUsername())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(expiresIn))
        .claim("permissions", user.getPermissions())
        .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
}
```
<div align="center">
        <p>
            <sup><small>Uso de um único claim <code>Long</code> para representar até 64 permissões na classe <code>AuthServiceImpl</code>.</small></sup>
        </p>
    </div>
    <p>
        O JWT é assinado e enviado ao cliente, e nas requisições subsequentes, o token é usado para validar o acesso aos recursos protegidos. O <strong>Spring Security</strong> intercepta a requisição, valida a assinatura do token e verifica se o usuário tem as permissões necessárias.
    </p>

```java
@Bean
public JwtAuthenticationConverter jwtAuthenticationConverter() {
    var converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
        var permissions = (Long) jwt.getClaim("permissions");
        var authorities = permissionService.authorities(permissions);
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    });
    return converter;
}
```
<div align="center">
        <p>
            <sup><small>Conversão de permissões codificadas no claim para autorizações usando <code>Permissions</code> na classe <code>SecurityConfig</code>.</small></sup>
        </p>
    </div>
    <p>
        Com isso, o controle de acesso se torna mais leve, performático e escalável, ideal para APIs modernas que exigem rapidez e segurança. Usando operações bitwise, podemos verificar permissões rapidamente, seja em sistemas pequenos ou grandes.
    </p>
    <h2>
        Quer testar a aplicação?
    </h2>
    <h4>
        Baixar o projeto do GitHub
    </h4>
    <p>
        Faça o download do projeto acessando o link abaixo:
    </p>
    <p>
        👉 <a href="https://github.com/danylo-macelai/BitwiSEcurity/archive/refs/heads/main.zip" target="_blank">BitwiSEcurity</a>
    <p>
    <p>
        Extraia o conteúdo do ZIP para uma pasta, por exemplo: <code>C:\Projetos\BitwiSEcurity</code>
    </p>
    <h4>
        Rodar a aplicação (sem instalar nada!)
    </h4>
    <p>
        Abra o Prompt de Comando (ou PowerShell).
    </p>
    <p>
        Vá até a pasta onde você extraiu o projeto. Exemplo:
    </p>

```bash
cd C:\Projetos\BitwiSEcurity
```
   <p>
        Execute o comando abaixo para iniciar a aplicação:
    </p>

```bash
.\mvnw.cmd spring-boot:run
```
   <blockquote>
        <p><strong>⚠️ Atenção:</strong></p>
        <p>
            Isso pode demorar um pouco na primeira vez, pois o Maven irá baixar todas as dependências e preparar tudo automaticamente.
        </p>
    </blockquote>
   <h4>
        Como parar a aplicação
    </h4>
    <p>
        No terminal onde ela está rodando, pressione:
    </p>

```bash
Ctrl + C
```
   <h4>
        Cenários de Teste com Tokens e Permissões
    </h4>
    <blockquote>
        <p>💡 <strong>Dica: </strong></p>
        <p>
             Você pode importar o arquivo <a href="./docs/BitwaSEcurity.postman_collection.json">BitwaSEcurity.postman_collection.json</a> no Postman para testar todos os endpoints rapidamente. As requisições já estão configuradas com exemplos de autenticação e uso de Bearer Tokens.
        </p>
    </blockquote>
    <p>
        O usuário <code>gyodai</code> atua como autoridade máxima no sistema. Ele é responsável por autenticar-se com login e senha e, após isso, tem a capacidade de conceder (<code>PERMISSION_GRANT</code>) ou revogar (<code>PERMISSION_REVOKE</code>) permissões de acesso para outros usuários. Qualquer operação sensível requer o uso de um Bearer Token.
    </p>
    <table border="1" cellpadding="6" cellspacing="0">
    <thead>
        <tr>
            <th>Método</th>
            <th>Path</th>
            <th>Usuário</th>
            <th>Senha</th>
            <th>Permissão</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST</td>
            <td>/auth/authenticate</td>
            <td>gyodai</td>
            <td>123456</td>
            <td></td>
        </tr>
        <tr>
            <td>POST</td>
            <td>/permissions/{username:\w+}/grant</td>
            <td colspan="2" align="center">Bearer Token</td>
            <td>PERMISSION_GRANT</td>
        </tr>
        <tr>
            <td>POST</td>
            <td>/permissions/{username:\w+}/revoke</td>
            <td colspan="2" align="center">Bearer Token</td>
            <td>PERMISSION_REVOKE</td>
        </tr>
    </tbody>
    </table>
    <p>
        O usuário <code>mummra</code> é o responsável por manter o ritmo das operações. Depois de se autenticar, ele pode criar novos pedidos (<code>ORDER_CREATE_ORDER</code>), visualizar todos os registros existentes, acessar detalhes específicos e até realizar atualizações em pedidos em andamento.
    </p>
    <table border="1" cellpadding="6" cellspacing="0">
    <thead>
        <tr>
            <th>Método</th>
            <th>Path</th>
            <th>Usuário</th>
            <th>Senha</th>
            <th>Permissão</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST</td>
            <td>/auth/authenticate</td>
            <td>mummra</td>
            <td>123456</td>
            <td></td>
        </tr>
        <tr>
            <td>POST</td>
            <td>/orders</td>
            <td colspan="2" align="center">Bearer Token</td>
            <td>ORDER_CREATE_ORDER</td>
        </tr>
        <tr>
            <td>GET</td>
            <td>/orders</td>
            <td colspan="2" align="center">Bearer Token</td>
            <td>ORDER_LIST_ORDERS</td>
        </tr>
        <tr>
            <td>GET</td>
            <td>/orders/{orderId:\d+}</td>
            <td colspan="2" align="center">Bearer Token</td>
            <td>ORDER_GET_ORDER</td>
        </tr>
        <tr>
            <td>PUT</td>
            <td>/orders/{orderId:\d+}</td>
            <td colspan="2" align="center">Bearer Token</td>
            <td>ORDER_UPDATE_ORDER</td>
        </tr>
    </tbody>
    </table>
    <p>
        O usuário <code>skeletor</code> é encarregado de tomar decisões estratégicas sobre os pedidos. Ele tem autoridade para cancelar ordens (<code>ORDER_CANCEL_ORDER</code>) e aplicar descontos (<code>ORDER_APPLY_DISCOUNT</code>) quando necessário.
    </p>
    <table border="1" cellpadding="6" cellspacing="0">
    <thead>
        <tr>
            <th>Método</th>
            <th>Path</th>
            <th>Usuário</th>
            <th>Senha</th>
            <th>Permissão</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST</td>
            <td>/auth/authenticate</td>
            <td>skeletor</td>
            <td>123456</td>
            <td></td>
        </tr>
        <tr>
            <td>DELETE</td>
            <td>/orders/{orderId:\d+}</td>
            <td colspan="2" align="center">Bearer Token</td>
            <td>ORDER_CANCEL_ORDER</td>
        </tr>
        <tr>
            <td>PUT</td>
            <td>/orders/discount/{orderId:\d+}</td>
            <td colspan="2" align="center">Bearer Token</td>
            <td>ORDER_APPLY_DISCOUNT</td>
        </tr>
    </tbody>
    </table>
    <h2>Links Úteis</h2>
    <div align="left">

   [![Spring - oauth2](https://img.shields.io/badge/OAuth%202.0%20Resource%20Server%20JWT-v6.4.5-FF4081)](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
   [![Spring - Security](https://img.shields.io/badge/Spring%20Security-v6.4.5-E91E63)](https://docs.spring.io/spring-security/reference/index.html)
   [![mozilla - Bitwise_AND](https://img.shields.io/badge/Bitwise_AND-Mozilla-4CAF50)](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Bitwise_AND)
   [![mozilla - Bitwise_OR](https://img.shields.io/badge/Bitwise_OR-Mozilla-00BCD4)](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Bitwise_OR)
   [![Oracle - Bitwise Operators](https://img.shields.io/badge/Bitwise%20Operators-Oracle-3F51B5)](https://docs.oracle.com/cd/E19253-01/817-6223/chp-typeopexpr-7/index.html)
   [![Spring - Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Spring-8BC34A)](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
   [![Spring - jpa.java-config](https://img.shields.io/badge/jpa.java-config-FF5722)](https://docs.spring.io/spring-data/jpa/reference/repositories/create-instances.html#jpa.java-config)
   [![H2 - Database](https://img.shields.io/badge/Database-H2-9C27B0)](https://www.h2database.com/html/installation.html)
   [![Hibernate - Domain Model](https://img.shields.io/badge/Domain%20Model-Hibernate-673AB7)](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#domain-model)
   [![Spring - Servlet Web Applications](https://img.shields.io/badge/Web-Spring-2196F3)](https://docs.spring.io/spring-boot/reference/web/servlet.html)
   [![tools - Project Lombok Download](https://img.shields.io/badge/Project%20Lombok-Download-FF9800)](https://projectlombok.org/download)
   [![tools - Project Lombok Setup Eclipse](https://img.shields.io/badge/Project%20Lombok--Setup-Eclipse-FFB300)](https://projectlombok.org/setup/eclipse)
   [![Maven - Project Lombok Setup Maven](https://img.shields.io/badge/Project%20Lombok--Setup-Maven-FFC107)](https://projectlombok.org/setup/maven)
   [![Features - Project Lombok Features](https://img.shields.io/badge/Project%20Lombok-Features-FF5722)](https://projectlombok.org/features/)
   [![Maven - Resources Plugin](https://img.shields.io/badge/Maven%20Resources%20Plugin-V3.3.1-009688)](https://maven.apache.org/plugins/maven-resources-plugin/index.html)
   [![Maven - Dependency Plugin](https://img.shields.io/badge/Dependency%20Plugin-Maven-4CAF50)](https://maven.apache.org/plugins/maven-dependency-plugin/usage.html)
   [![Maven - Surefire Plugin](https://img.shields.io/badge/Surefire%20Plugin-Maven-3F51B5)](https://maven.apache.org/surefire/maven-surefire-plugin/#maven-surefire-plugin)
   [![Maven - Failsafe Plugin](https://img.shields.io/badge/Failsafe%20Plugin-Maven-9C27B0)](https://maven.apache.org/surefire/maven-failsafe-plugin/#maven-failsafe-plugin)
   [![Spring - Testing the Web Layer](https://img.shields.io/badge/Testing%20the%20Web%20Layer-Spring-FF4081)](https://spring.io/guides/gs/testing-web)
   [![Spring - Testing Spring Boot Applications](https://img.shields.io/badge/Testing%20Spring%20Boot%20Applications-Spring-03A9F4)](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html)
   [![Spring - Developer Tools](https://img.shields.io/badge/Developer%20Tools-Spring-00BCD4)](https://docs.spring.io/spring-boot/reference/using/devtools.html)
   [![pom - Maven Compiler)](https://img.shields.io/badge/Maven%20Compiler%20Plugin-V3.9.9-673AB7)](https://maven.apache.org/plugins/maven-compiler-plugin/)
   [![Spring - Initializr)](https://img.shields.io/badge/Initializr-V3.4.4-8BC34A)](https://start.spring.io/)
   [![tools - Eclipse jee 2025-03 R)](https://img.shields.io/badge/Eclipsee%20IDE-2025--03e%20R-FF5722)](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2025-03/R/eclipse-jee-2025-03-R-win32-x86_64.zip)
   [![tools - Maven)](https://img.shields.io/badge/Apache%20Maven-V3.9.9-FF9800)](https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip)
   [![tools - Jdk 17)](https://img.shields.io/badge/Java%20SE%20Development%20Kit-V17.0.12-009688)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
   [![GitHub - Docs](https://img.shields.io/badge/Docs-GitHub-607D8B)](https://docs.github.com/en)

</div>
</div>
