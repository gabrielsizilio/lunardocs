# LunarDocs 🚀
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![Status](https://img.shields.io/badge/api-Em%20Desenvolvimento-yellow)

![JWT](http://jwt.io/img/badge-compatible.svg)




O LunarDocs é uma API de **gerenciamento de documentos digitais** permitindo que usuários façam login, upload de documentos, definam signatários e gerenciem o fluxo de assinaturas com segurança e eficiência de arquivos digitais. Está sendo desenvolvida em linguagem Java, utilizando o framework Spring.

## 🛠️ Funcionalidades 
- 🔐 **Autenticação JWT**: Login seguro e autenticação stateless com JSON Web Tokens.
- 📎 **Upload de Documentos**: Permite que os usuários carreguem documentos e se tornem proprietários deles.
- 📜 **Gestão de Signatários**: Os proprietários podem atribuir signatários para documentos específicos.
- ⚜️ **Controle de Acesso Baseado em Papéis**: Diferenciação de permissões para administradores e usuários comuns.
- ✍️ **Assinatura Digital**: Usuários autenticados podem assinar documentos de forma segura. _(🚧 em desenvolvimento)_
- 📑 **Histórico de Ações**: Permite registrar e visualizar alterações feitas nos documentos. _(🔜 em breve)_

## 📡 Endpoints principais 
``` Disponibilizei o arquivo lunarDocs_endpoint.json, que serve para importação de um ambiente no insomnia para testar```

#### 🔑 Autenticação
Estou atualizando os endpoints para seguirem o padrão **RESTful**.

| Método | Endpoint         | Descrição |
|--------|----------------|-----------|
| POST   | `/auth/register` | Cria um novo usuário |
| POST   | `/auth/login`    | Realiza login e retorna o token JWT |

---

#### 📂 Gerenciamento de Documentos
| Método | Endpoint          | Descrição |
|--------|-----------------|------------|
| POST   | `/document/upload` | Faz upload de um novo documento |
| GET    | `/document/findAll` | Lista todos os documentos |
| GET    | `/document/myDocuments` | Lista todos os documentos *upados* do usuário autenticado |
| PUT    | `/document/update/{documentId}` | Atualiza informações de um documento |
| DELET  | `/document/delete/{documentId}` | Remove um documento |

---

#### 📝 Gestão de Signatários
| Método | Endpoint               | Descrição |
|--------|------------------------|-----------|
| POST   | `/document/{documentId}/signers` | Adiciona um signatário ao documento |
| GET    | `/document/{documentId}/signers` | Lista os signatários de um documento |
| DELET  | `/document/{documentId}/signers/{userId}` | Remove um signatário de um documento |

---

#### 👤 Gestão de Users
| Método | Endpoint               | Descrição |
|--------|------------------------|-----------|
| GET    | `/users/findAll` | Lista todos os usuários do sistema |
| GET    | `/users/findByEmail` | Busca um usuário pelo email |
| PUT    | `/users/update/{userId}` | Atualiza informações de um usuário |
| DELET  | `/users/delete/{userId}` | Remove um usuário do sistema |

---

#### ✍️ Assinatura Digital _(🚧 em desenvolvimento)_
| Método | Endpoint                | Descrição |
|--------|-------------------------|-----------|
| POST   | | Assina um documento |

---

#### 📑 Histórico de Ações _(🔜 em breve)_
| Método | Endpoint               | Descrição |
|--------|------------------------|-----------|
| GET    | | Obtém o histórico de alterações e assinaturas de um documento |

---
## 🛡️ Segurança
![JWT](http://jwt.io/img/badge-compatible.svg)

- Utiliza JWT para autenticação
- Permissões baseadas em roles (USER, ADMIN)
- Proteção contra ataques CSRF e CORS
- Proteção de assinatura de documentos usando hash _(🔜 em breve)_

## ⚜️ Tecnologias Utilizadas 
- ☕ Linguagem: Java 17
  - Framework: Spring Boot

- Spring Security: Controle de autenticação e autorização.
- Autenticação: JWT (JSON Web Tokens)
- Senha: BCrypt para encriptação de senhas

- Spring Data JPA: Gerenciamento de persistência de dados.
- Banco de Dados: PostgreSQL
