# Agenda Accesible – Java puro (Swing + JDBC/H2)

Proyecto base para una agenda accesible con recordatorios para pacientes con dificultades de memoria.
Stack: **Java SE**, **Swing**, **JDBC**, **H2 embebido**. Patrones: **MVC**, **DAO**, **Observer** (+ Strategy/Factory opcional).

## Estructura
```
/src
  /app
  /ui
  /controller
  /domain
  /infra/db
  /infra/dao
    /impl
  /shared
/db
  schema.sql
Prompts.md
```

## Requisitos rápidos
- Java 17/21
- h2-*.jar en classpath (o usar Gradle/Maven si querés)
- IDE: IntelliJ/VS Code con GitHub Copilot (+ Copilot Chat)

## Cómo correr
1) Cargar el driver H2 y crear db local al iniciar (`ConexionDB.initSchemaIfAbsent()`).
2) Ejecutar `app.Main` para abrir la UI.
