<p align="center">
  <img src="Odin/docs/capturas/odin-logo.png" alt="LOGO ODIN" width="120"/>
</p>

# ODIN — Sistema de Gestión Documental

<p align="center">
  <img src="https://img.shields.io/badge/SENA-%2339A900?style=for-the-badge&logoColor=white" alt="SENA"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/PostgreSQL-Supabase-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
</p>

<p align="center">
  <strong>Organización Documental Institucional</strong><br/>
  Gestor documental para el seguimiento, radicación y trazabilidad de trámites<br/>
  en el marco de las prácticas formativas del <strong>SENA</strong>.
</p>

<p align="center">
  <a href="https://github.com/Dinamo-mrm/Gestor-Documental-Odin"><img src="https://img.shields.io/badge/GitHub-Dinamo--mrm%2FGestor--Documental--Odin-181717?style=flat-square&logo=github" alt="Repo"/></a>
  <img src="https://img.shields.io/badge/estado-candidato%20a%20entrega%20técnica-yellow?style=flat-square" alt="Estado"/>
  <img src="https://img.shields.io/badge/licencia-uso%20institucional%20SENA-lightgrey?style=flat-square" alt="Licencia"/>
</p>

---

## Documentación completa

La documentación detallada del proyecto (módulos, instalación, equipo, capturas, grupos 1–4 y seguridad) está en:

### **[→ Abrir README completo del proyecto (Odin/README.md)](Odin/README.md)**

---
### Equipo base — colaboradores del repositorio

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/norx01">
        <img src="https://avatars.githubusercontent.com/u/19365584?v=4" width="115"/><br/>
        <sub><b>Juan Pablo Pinillos</b></sub>
      </a><br/>
      <sub>Write · ingeniería / proyectos</sub><br/>
    </td>
    <td align="center">
      <a href="https://github.com/Dinamo-mrm">
        <img src="https://avatars.githubusercontent.com/u/179788566?v=4" width="115"/><br/>
        <sub><b>Carlos Edo. Barahona</b></sub>
      </a><br/>
      <sub>Admin · owner del repo</sub><br/>
    </td>
    <td align="center">
      <a href="https://github.com/diealeolagon-sketch">
        <img src="https://avatars.githubusercontent.com/u/244035545?v=4" width="115"/><br/>
        <sub><b>Diego Olaya</b></sub>
      </a><br/>
      <sub> write</sub><br/>
      <sub>~20 commits</sub>
    </td>
    <td align="center">
      <a href="https://github.com/juancamilo343">
        <img src="https://avatars.githubusercontent.com/u/213087527?v=4" width="115"/><br/>
        <sub><b>juancamilo343</b></sub>
      </a><br/>
      <sub>Write · radicación / G1·G3</sub><br/>
    </td>
    <td align="center">
      <a href="https://github.com/JJMA04-code">
        <img src="https://avatars.githubusercontent.com/u/255725275?v=4" width="115"/><br/>
        <sub><b>JJMA04-code</b></sub>
      </a><br/>
      <sub>Write</sub><br/>
    </td>
  </tr>
</table>

---

## Arranque rápido

```bash
git clone https://github.com/Dinamo-mrm/Gestor-Documental-Odin.git
cd Gestor-Documental-Odin/Odin
cp .env.example .env   # configurar Supabase
./mvnw -DskipTests package
./mvnw spring-boot:run
```

Abrir: `http://localhost:8080/login`

---

## Estructura del repositorio

```text
Gestor-Documental-Odin/     ← raíz (este README.md se muestra en GitHub)
├── README.md               ← portada del repositorio
├── Odin/                   ← aplicación Spring Boot
│   ├── README.md           ← documentación completa
│   ├── pom.xml
│   ├── docs/capturas/      ← logos y pantallas
│   └── src/
├── .github/workflows/
└── .gitignore
```

---

## Capturas

<p align="center">
  <img src="Odin/docs/capturas/odin_login.png" alt="Login ODIN" width="640"/>
</p>
<p align="center"><em>Login institucional ODIN</em></p>

<p align="center">
  <img src="Odin/docs/capturas/odin_dashboard.png" alt="Dashboard ODIN" width="640"/>
</p>
<p align="center"><em>Dashboard — KPIs operativos</em></p>

Más capturas y detalle del equipo, seguridad, RF y modelo de datos: **[Odin/README.md](Odin/README.md)**.

---

<p align="center">
  <img src="Odin/docs/capturas/odinti-logo.png" alt="ODIN" width="64"/><br/>
  <sub>ODIN — Organización Documental Institucional · SENA</sub>
</p>
