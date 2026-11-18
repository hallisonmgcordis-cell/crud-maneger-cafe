# crud-manager-public

Projeto da disciplina Tecnologias de Desenvolvimento para Internet II (Servlets e JSP).

## O que foi adicionado

- **Login**: `LoginController` (`/login`, `/logout`), `login.jsp` e `AuthenticationFilter`
  (bloqueia todas as páginas, exceto login e arquivos estáticos). A senha é guardada como hash
  PBKDF2 (`model.PasswordUtil`) na coluna `users.password`.
- **Paginação** (5 registros por página) em todos os cadastros: usuários, posts, empresas,
  lotes e colheitas (`pagination.jsp`, `ControllerUtil.paginate` e `listPage`/`count` nos DAOs).
- **Tema: cafeicultura**
  - **Lotes de Café** (`/lots`): nome (text), variedade (select), área (number),
    data de plantio (date), irrigado (checkbox) e proprietário (select -> `users`).
  - **Colheitas** (`/harvests`): lote (select -> `coffee_lots`), data (date), sacas (number),
    qualidade (radio), observações (textarea) e responsável (select -> `users`).

## Como rodar

1. Execute `script-sql.sql` no MySQL (recria o banco `crud_manager`).
2. Ajuste usuário/senha do banco em `MySQLConnectionFactory`.
3. Publique no Tomcat 10+ com o contexto `/crud-manager` e acesse `/crud-manager`.
4. Login de exemplo: `emerson@mail.com` / `123456` (todos os usuários de exemplo usam `123456`).
