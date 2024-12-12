# LunarDocs 🚀
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![Version](https://img.shields.io/github/v/tag/gabrielsizilio/signdocs)
![Status](https://img.shields.io/badge/client--web-Em%20Desenvolvimento-yellow)
![Status](https://img.shields.io/badge/api-Em%20Desenvolvimento-yellow)


O LunarDocs é uma aplicação web construída em Java, utilizando o framework Spring, e por enquanto, React para clients web,  que gerencia a assinatura digital de documentos. Ele permite que os usuários façam login, upload de documentos, definam signatários e gerenciem o fluxo de assinaturas com segurança e eficiência.

## Funcionalidades 🛠️
- 🔐 Autenticação JWT: Login seguro e autenticação stateless com JSON Web Tokens.
- 📎 Upload de Documentos: Permite que os usuários carreguem documentos e se tornem proprietários deles.
- 📜 Gestão de Signatários: Os proprietários podem atribuir signatários para documentos específicos.
- ✍️ Assinatura Digital: Usuários autenticados podem assinar documentos de forma segura.
- ⚜️ Controle de Acesso Baseado em Papéis: Diferenciação de permissões para administradores e usuários comuns.

## Tecnologias Utilizadas ⚜️
- ☕ Linguagem: Java 17
  - Framework: Spring Boot

- Spring Security: Controle de autenticação e autorização.
- Autenticação: JWT (JSON Web Tokens)
- Senha: BCrypt para encriptação de senhas

- Spring Data JPA: Gerenciamento de persistência de dados.
- Banco de Dados: PostgreSQL
