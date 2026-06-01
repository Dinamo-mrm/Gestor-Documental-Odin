-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.44 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.15.0.7171
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para odin
CREATE DATABASE IF NOT EXISTS `odin` /*!40100 DEFAULT CHARACTER SET utf16 COLLATE utf16_spanish_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `odin`;

-- Volcando estructura para tabla odin.anexos
CREATE TABLE IF NOT EXISTS `anexos` (
  `id_anexo` bigint NOT NULL AUTO_INCREMENT,
  `id_radicado` int DEFAULT NULL,
  `descripcion` varchar(255) NOT NULL,
  `archivo` text NOT NULL,
  PRIMARY KEY (`id_anexo`),
  KEY `fk_anexo_radicado` (`id_radicado`),
  CONSTRAINT `fk_anexo_radicado` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 3 - Anexos / Soportes Adicionales** \r\nPropósito: Archivos complementarios (imágenes, soportes, evidencias). \r\nComunicación: N:1 → radicados. \r\nTrazabilidad: Soporte documental referenciado en observaciones y PLANTILLA.';

-- Volcando datos para la tabla odin.anexos: ~1 rows (aproximadamente)
INSERT INTO `anexos` (`id_anexo`, `id_radicado`, `descripcion`, `archivo`) VALUES
	(1, 1, 'descripcion de la persona que recibe el anexo', '<!DOCTYPE html>\n<html lang="es">\n  <head>\n    <meta charset="UTF-8" />\n    <meta name="viewport" content="width=device-width, initial-scale=1.0" />\n    <title>Guía Completa de React para Principiantes en Windows</title>\n    <script src="https://cdn.tailwindcss.com"></script>\n    <style>\n      :root {\n        color-scheme: light;\n        --ink: #14213d;\n        --muted: #52607a;\n        --panel: #ffffff;\n        --soft: #eef4ff;\n        --line: #d8e2f2;\n        --brand: #0f766e;\n        --brand-soft: #dff7f3;\n        --accent: #1d4ed8;\n        --code-bg: #0f172a;\n        --code-text: #e2e8f0;\n        --code-muted: #94a3b8;\n        --code-key: #93c5fd;\n        --code-str: #86efac;\n        --code-num: #fca5a5;\n        --code-tag: #f9a8d4;\n      }\n\n      body {\n        font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;\n        background:\n          radial-gradient(circle at top left, rgba(15, 118, 110, 0.08), transparent 26rem),\n          radial-gradient(circle at top right, rgba(29, 78, 216, 0.08), transparent 22rem),\n          #f8fbff;\n        color: var(--ink);\n      }\n\n      .glass {\n        background: rgba(255, 255, 255, 0.94);\n        border: 1px solid var(--line);\n        box-shadow: 0 20px 60px rgba(15, 23, 42, 0.08);\n        backdrop-filter: blur(10px);\n      }\n\n      .code-block {\n        background: var(--code-bg);\n        color: var(--code-text);\n        border: 1px solid rgba(148, 163, 184, 0.15);\n        border-radius: 18px;\n        padding: 1rem 1.1rem;\n        overflow-x: auto;\n        font-family: "Cascadia Code", Consolas, "Courier New", monospace;\n        font-size: 0.92rem;\n        line-height: 1.7;\n      }\n\n      .code-block .muted { color: var(--code-muted); }\n      .code-block .key { color: var(--code-key); }\n      .code-block .str { color: var(--code-str); }\n      .code-block .num { color: var(--code-num); }\n      .code-block .tag { color: var(--code-tag); }\n\n      .terminal::before {\n        content: "Terminal";\n        display: inline-block;\n        margin-bottom: 0.7rem;\n        padding: 0.25rem 0.55rem;\n        border-radius: 999px;\n        background: rgba(148, 163, 184, 0.14);\n        color: #cbd5e1;\n        font-size: 0.72rem;\n        letter-spacing: 0.08em;\n        text-transform: uppercase;\n      }\n\n      .page-break {\n        break-inside: avoid;\n        page-break-inside: avoid;\n      }\n\n      .check-list li::before {\n        content: "•";\n        color: var(--brand);\n        font-weight: 700;\n        margin-right: 0.6rem;\n      }\n\n      @media print {\n        body {\n          background: white !important;\n          color: #111827 !important;\n        }\n\n        .no-print {\n          display: none !important;\n        }\n\n        .glass {\n          box-shadow: none !important;\n          border: 1px solid #d1d5db !important;\n          background: white !important;\n        }\n\n        .code-block {\n          background: #111827 !important;\n          color: #f9fafb !important;\n          border-color: #d1d5db !important;\n        }\n\n        section {\n          break-inside: avoid;\n          page-break-inside: avoid;\n        }\n\n        h2, h3 {\n          page-break-after: avoid;\n        }\n\n        .print-gap {\n          page-break-before: always;\n        }\n      }\n    </style>\n  </head>\n  <body class="min-h-screen">\n    <main class="mx-auto max-w-6xl px-4 py-6 md:px-6 lg:px-8">\n      <header class="glass rounded-[28px] p-6 md:p-8">\n        <div class="flex flex-col gap-5 lg:flex-row lg:items-start lg:justify-between">\n          <div class="max-w-4xl">\n            <p class="mb-3 inline-flex rounded-full bg-teal-50 px-3 py-1 text-xs font-semibold uppercase tracking-[0.22em] text-teal-700">\n              Guía para principiantes\n            </p>\n            <h1 class="text-3xl font-black leading-tight text-slate-900 md:text-5xl">\n              React desde cero en Windows\n            </h1>\n            <p class="mt-4 max-w-3xl text-base leading-8 text-slate-600">\n              Una guía pensada para personas que nunca han usado React. Vamos paso a paso,\n              con ejemplos pequeños, comandos listos para copiar y una mini‑aplicación final.\n              No te preocupes si al principio no entiendes todo: la idea es que salgas con un\n              mapa claro y con ganas de practicar.\n            </p>\n            <div class="mt-5 rounded-2xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm leading-7 text-amber-900">\n              <strong>Nota importante, verificada el 8 de mayo de 2026:</strong>\n              la documentación oficial de React indica que <strong>Create React App está deprecado</strong>.\n              En esta guía lo menciono porque todavía aparece en muchos tutoriales, pero para empezar hoy\n              te recomiendo usar <strong>Vite</strong>. Aun así, también te muestro el flujo con\n              <code>npx create-react-app</code> para que entiendas material más antiguo.\n            </div>\n          </div>\n\n          <div class="no-print flex flex-col gap-3">\n            <button\n              onclick="window.print()"\n              class="rounded-2xl bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700"\n            >\n              Descargar PDF\n            </button>\n            <a\n              href="#contenido"\n              class="rounded-2xl border border-slate-300 bg-white px-5 py-3 text-center text-sm font-semibold text-slate-700 transition hover:border-slate-400"\n            >\n              Ir a la guía\n            </a>\n          </div>\n        </div>\n      </header>\n\n      <section id="contenido" class="mt-6 grid gap-6 lg:grid-cols-[280px_minmax(0,1fr)]">\n        <aside class="glass page-break rounded-[24px] p-5">\n          <h2 class="text-lg font-bold text-slate-900">Índice rápido</h2>\n          <nav class="mt-4 space-y-3 text-sm leading-6">\n            <a class="block text-slate-700 hover:text-blue-700" href="#que-es-react">1. ¿Qué es React?</a>\n            <a class="block text-slate-700 hover:text-blue-700" href="#instalar-react">2. Instalar React en Windows</a>\n            <a class="block text-slate-700 hover:text-blue-700" href="#estructura-proyecto">3. Estructura de un proyecto</a>\n            <a class="block text-slate-700 hover:text-blue-700" href="#conceptos-basicos">4. Conceptos básicos</a>\n            <a class="block text-slate-700 hover:text-blue-700" href="#mini-app">5. Mini app: lista de tareas</a>\n            <a class="block text-slate-700 hover:text-blue-700" href="#buenas-practicas">6. Buenas prácticas</a>\n            <a class="block text-slate-700 hover:text-blue-700" href="#recursos">7. Recursos adicionales</a>\n          </nav>\n\n          <div class="mt-6 rounded-2xl bg-blue-50 p-4 text-sm leading-7 text-slate-700">\n            <p class="font-semibold text-slate-900">Idea clave</p>\n            <p class="mt-2">\n              React te ayuda a construir pantallas usando piezas pequeñas llamadas\n              <strong>componentes</strong>. Si entiendes esa idea, ya empezaste bien.\n            </p>\n          </div>\n        </aside>\n\n        <div class="space-y-6">\n          <section id="que-es-react" class="glass page-break rounded-[24px] p-6 md:p-7">\n            <h2 class="text-2xl font-black text-slate-900">1. ¿Qué es React?</h2>\n            <p class="mt-4 text-base leading-8 text-slate-700">\n              React es una <strong>biblioteca de JavaScript</strong> para construir interfaces de usuario.\n              Fue creada por Facebook y hoy se usa muchísimo para hacer aplicaciones web modernas.\n            </p>\n            <div class="mt-5 rounded-2xl border border-teal-200 bg-teal-50 p-4">\n              <p class="text-sm font-semibold uppercase tracking-[0.18em] text-teal-700">Metáfora</p>\n              <p class="mt-2 text-base leading-8 text-slate-700">\n                React es como un set de <strong>LEGO</strong>: construyes partes pequeñas\n                llamadas <strong>componentes</strong> y luego las unes para formar una página completa.\n              </p>\n            </div>\n\n            <div class="mt-6 grid gap-4 md:grid-cols-3">\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">Reutilización</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Un botón, una tarjeta o un formulario se pueden usar varias veces sin escribirlos de nuevo.\n                </p>\n              </article>\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">Actualización eficiente</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Cuando cambia un dato, React actualiza la parte necesaria de la pantalla.\n                </p>\n              </article>\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">Ecosistema enorme</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Hay miles de recursos, librerías, cursos y ejemplos para aprender y crecer.\n                </p>\n              </article>\n            </div>\n          </section>\n\n          <section id="instalar-react" class="glass page-break rounded-[24px] p-6 md:p-7">\n            <h2 class="text-2xl font-black text-slate-900">2. ¿Cómo instalar React en Windows?</h2>\n            <p class="mt-4 text-base leading-8 text-slate-700">\n              Antes de crear tu primera app, necesitas tener instalado <strong>Node.js</strong>.\n              Node te permite ejecutar herramientas de JavaScript fuera del navegador, y con él llega\n              <strong>npm</strong>, que sirve para instalar paquetes.\n            </p>\n\n            <div class="mt-5 rounded-2xl border border-slate-200 bg-slate-50 p-4">\n              <h3 class="font-bold text-slate-900">Requisitos previos</h3>\n              <ul class="check-list mt-3 space-y-2 text-sm leading-7 text-slate-700">\n                <li>Windows 10 u 11</li>\n                <li>Node.js en su versión LTS desde la web oficial</li>\n                <li>PowerShell o CMD</li>\n                <li>Conexión a internet para descargar dependencias</li>\n              </ul>\n            </div>\n\n            <div class="mt-6 space-y-6">\n              <div>\n                <h3 class="text-lg font-bold text-slate-900">Paso 1. Abrir PowerShell o CMD</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Puedes abrir <strong>PowerShell</strong> o <strong>CMD</strong>. Si algo falla por permisos,\n                  prueba abrirlo como administrador.\n                </p>\n                <div class="mt-3 rounded-2xl border border-slate-200 bg-slate-50 p-4 text-sm text-slate-700">\n                  🖥️ <strong>Simulación:</strong> Inicio → buscar “PowerShell” → clic derecho → “Ejecutar como administrador”\n                </div>\n              </div>\n\n              <div>\n                <h3 class="text-lg font-bold text-slate-900">Paso 2. Verificar Node y npm</h3>\n                <div class="code-block terminal mt-3">\n                  <div>node -v</div>\n                  <div>npm -v</div>\n                </div>\n                <p class="mt-3 text-sm leading-7 text-slate-700">\n                  Si ambos comandos muestran un número, todo va bien.\n                  Si Windows dice que no reconoce el comando, Node.js no está instalado o no quedó en el PATH.\n                </p>\n              </div>\n\n              <div>\n                <h3 class="text-lg font-bold text-slate-900">Paso 3. Crear tu proyecto React</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  <strong>Recomendado hoy:</strong> Vite. Es más rápido y más liviano.\n                </p>\n                <div class="code-block terminal mt-3">\n                  <div>npm create vite@latest mi-primer-app -- --template react</div>\n                  <div>cd mi-primer-app</div>\n                  <div>npm install</div>\n                  <div>npm run dev</div>\n                </div>\n                <p class="mt-3 text-sm leading-7 text-slate-700">\n                  Normalmente Vite abre la app en una dirección como <code>http://localhost:5173</code>.\n                </p>\n              </div>\n\n              <div class="rounded-2xl border border-amber-200 bg-amber-50 p-5">\n                <h3 class="text-lg font-bold text-amber-900">Flujo con Create React App</h3>\n                <p class="mt-2 text-sm leading-7 text-amber-900">\n                  Lo incluyo porque aparece en muchos cursos. A mayo de 2026, React lo marca como deprecado para apps nuevas.\n                </p>\n                <div class="code-block terminal mt-3">\n                  <div class="muted"># Opcional: instalación global antigua (ya no hace falta)</div>\n                  <div>npm install -g create-react-app</div>\n                  <div class="muted"></div>\n                  <div class="muted"># Forma recomendada si necesitas seguir ese camino</div>\n                  <div>npx create-react-app mi-primer-app</div>\n                  <div>cd mi-primer-app</div>\n                  <div>npm start</div>\n                </div>\n                <p class="mt-3 text-sm leading-7 text-amber-900">\n                  Create React App suele abrir la app en <code>http://localhost:3000</code>.\n                </p>\n              </div>\n\n              <div>\n                <h3 class="text-lg font-bold text-slate-900">Paso 4. Abrir la app en el navegador</h3>\n                <div class="mt-3 rounded-2xl border border-slate-200 bg-slate-50 p-4 text-sm text-slate-700">\n                  ✅ Si todo salió bien, verás una página inicial de React.<br />\n                  📦 Con Vite: normalmente en <code>http://localhost:5173</code><br />\n                  ⚛️ Con CRA: normalmente en <code>http://localhost:3000</code>\n                </div>\n              </div>\n            </div>\n\n            <div class="mt-7 rounded-2xl border border-rose-200 bg-rose-50 p-5">\n              <h3 class="text-lg font-bold text-rose-900">Problemas comunes al instalar</h3>\n              <ul class="check-list mt-3 space-y-2 text-sm leading-7 text-rose-900">\n                <li><strong>“node no se reconoce”:</strong> reinstala Node.js y marca la opción para agregarlo al PATH.</li>\n                <li><strong>Puerto ocupado:</strong> si <code>3000</code> o <code>5173</code> está ocupado, acepta otro puerto o cierra la app que lo usa.</li>\n                <li><strong>PowerShell bloquea scripts:</strong> abre PowerShell como administrador y ejecuta <code>Set-ExecutionPolicy RemoteSigned</code> si realmente lo necesitas.</li>\n                <li><strong>Permisos raros de npm:</strong> cierra la terminal, vuelve a abrirla como administrador y prueba otra vez.</li>\n                <li><strong>Instalación muy lenta:</strong> revisa internet o intenta más tarde; a veces el problema es del registro de paquetes.</li>\n              </ul>\n            </div>\n          </section>\n\n          <section id="estructura-proyecto" class="glass page-break rounded-[24px] p-6 md:p-7">\n            <h2 class="text-2xl font-black text-slate-900">3. Estructura de un proyecto React</h2>\n            <p class="mt-4 text-base leading-8 text-slate-700">\n              No necesitas memorizar todo al principio. Solo ubica las piezas más importantes:\n            </p>\n\n            <div class="code-block mt-4">\n              <div><span class="key">mi-primer-app/</span></div>\n              <div>├── <span class="key">src/</span> <span class="muted"># aquí vive casi todo tu código</span></div>\n              <div>├── <span class="key">public/</span> <span class="muted"># archivos públicos como iconos</span></div>\n              <div>├── <span class="key">package.json</span> <span class="muted"># dependencias y scripts</span></div>\n              <div>├── <span class="key">index.js</span> o <span class="key">main.jsx</span> <span class="muted"># punto de entrada</span></div>\n              <div>└── <span class="key">App.js</span> o <span class="key">App.jsx</span> <span class="muted"># componente principal</span></div>\n            </div>\n\n            <div class="mt-5 grid gap-4 md:grid-cols-2">\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">`src`</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Es la carpeta más importante al empezar. Aquí pones componentes, estilos y lógica.\n                </p>\n              </article>\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">`public`</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Guarda archivos estáticos. Al principio casi no la tocarás.\n                </p>\n              </article>\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">`package.json`</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Aquí están las dependencias y comandos como <code>npm start</code> o <code>npm run dev</code>.\n                </p>\n              </article>\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">`App.js` / `App.jsx`</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Es el componente principal. Piensa en él como el punto de arranque de la pantalla.\n                </p>\n              </article>\n            </div>\n          </section>\n\n          <section id="conceptos-basicos" class="glass page-break rounded-[24px] p-6 md:p-7">\n            <h2 class="text-2xl font-black text-slate-900">4. Conceptos básicos con ejemplos</h2>\n\n            <div class="mt-6 space-y-6">\n              <article>\n                <h3 class="text-lg font-bold text-slate-900">Componentes</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Un componente es una función que devuelve una parte de la interfaz.\n                </p>\n                <div class="code-block mt-3">\n                  <div><span class="key">function</span> <span class="tag">Saludo</span>() {</div>\n                  <div>&nbsp;&nbsp;<span class="key">return</span> <span class="tag">&lt;h1&gt;</span>Hola mundo<span class="tag">&lt;/h1&gt;</span>;</div>\n                  <div>}</div>\n                </div>\n              </article>\n\n              <article>\n                <h3 class="text-lg font-bold text-slate-900">JSX</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  JSX te deja escribir algo parecido a HTML dentro de JavaScript.\n                </p>\n                <ul class="check-list mt-3 space-y-2 text-sm leading-7 text-slate-700">\n                  <li>Debes devolver un solo elemento padre.</li>\n                  <li>Se usa <code>className</code> en vez de <code>class</code>.</li>\n                  <li>Puedes mezclar JavaScript con llaves <code>{ }</code>.</li>\n                </ul>\n                <div class="code-block mt-3">\n                  <div><span class="key">function</span> <span class="tag">Caja</span>() {</div>\n                  <div>&nbsp;&nbsp;<span class="key">return</span> (</div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;div</span> <span class="key">className</span>=<span class="str">"tarjeta"</span><span class="tag">&gt;</span></div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;p&gt;</span>Hola desde JSX<span class="tag">&lt;/p&gt;</span></div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;/div&gt;</span></div>\n                  <div>&nbsp;&nbsp;);</div>\n                  <div>}</div>\n                </div>\n              </article>\n\n              <article>\n                <h3 class="text-lg font-bold text-slate-900">Props</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Las props son datos que un componente padre le pasa a un hijo. Piensa en ellas como argumentos.\n                </p>\n                <div class="code-block mt-3">\n                  <div><span class="key">function</span> <span class="tag">Saludo</span>({ nombre }) {</div>\n                  <div>&nbsp;&nbsp;<span class="key">return</span> <span class="tag">&lt;h2&gt;</span>Hola, {nombre}<span class="tag">&lt;/h2&gt;</span>;</div>\n                  <div>}</div>\n                  <div class="muted"></div>\n                  <div><span class="tag">&lt;Saludo</span> <span class="key">nombre</span>=<span class="str">"Ana"</span> <span class="tag">/&gt;</span></div>\n                </div>\n              </article>\n\n              <article>\n                <h3 class="text-lg font-bold text-slate-900">Estado (`state`)</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  El estado es la información que cambia en un componente. Cuando cambia, React vuelve a dibujar esa parte.\n                </p>\n                <div class="code-block mt-3">\n                  <div><span class="key">import</span> { useState } <span class="key">from</span> <span class="str">"react"</span>;</div>\n                  <div class="muted"></div>\n                  <div><span class="key">function</span> <span class="tag">Contador</span>() {</div>\n                  <div>&nbsp;&nbsp;<span class="key">const</span> [numero, setNumero] = useState(<span class="num">0</span>);</div>\n                  <div class="muted"></div>\n                  <div>&nbsp;&nbsp;<span class="key">return</span> (</div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;div&gt;</span></div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;p&gt;</span>{numero}<span class="tag">&lt;/p&gt;</span></div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;button</span> <span class="key">onClick</span>={() =&gt; setNumero(numero + <span class="num">1</span>)}<span class="tag">&gt;</span></div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sumar</div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;/button&gt;</span></div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;/div&gt;</span></div>\n                  <div>&nbsp;&nbsp;);</div>\n                  <div>}</div>\n                </div>\n              </article>\n\n              <article>\n                <h3 class="text-lg font-bold text-slate-900">Eventos</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  En React los eventos se escriben en camelCase: <code>onClick</code>, <code>onChange</code>, etc.\n                </p>\n                <div class="code-block mt-3">\n                  <div><span class="tag">&lt;button</span> <span class="key">onClick</span>={() =&gt; alert(<span class="str">"Hola"</span>)}<span class="tag">&gt;</span></div>\n                  <div>&nbsp;&nbsp;Haz clic</div>\n                  <div><span class="tag">&lt;/button&gt;</span></div>\n                </div>\n              </article>\n\n              <article>\n                <h3 class="text-lg font-bold text-slate-900">Listas y `key`</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  Para mostrar arreglos usamos <code>map()</code>. Cada elemento necesita una <code>key</code> única.\n                </p>\n                <div class="code-block mt-3">\n                  <div><span class="key">const</span> frutas = [<span class="str">"Manzana"</span>, <span class="str">"Pera"</span>, <span class="str">"Mango"</span>];</div>\n                  <div class="muted"></div>\n                  <div><span class="tag">&lt;ul&gt;</span></div>\n                  <div>&nbsp;&nbsp;{frutas.map((fruta, index) =&gt; (</div>\n                  <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;li</span> <span class="key">key</span>={index}<span class="tag">&gt;</span>{fruta}<span class="tag">&lt;/li&gt;</span></div>\n                  <div>&nbsp;&nbsp;))}</div>\n                  <div><span class="tag">&lt;/ul&gt;</span></div>\n                </div>\n                <p class="mt-3 text-sm leading-7 text-slate-700">\n                  <strong>JS breve:</strong> <code>map()</code> recorre un arreglo y devuelve otro. Te sirve mucho para “pintar listas”.\n                </p>\n              </article>\n            </div>\n          </section>\n\n          <section id="mini-app" class="glass page-break rounded-[24px] p-6 md:p-7">\n            <h2 class="text-2xl font-black text-slate-900">5. Ejemplo completo: mini lista de tareas</h2>\n            <p class="mt-4 text-base leading-8 text-slate-700">\n              Vamos a hacer una app pequeña que:\n            </p>\n            <ul class="check-list mt-3 space-y-2 text-sm leading-7 text-slate-700">\n              <li>Muestra un input</li>\n              <li>Agrega tareas a un arreglo</li>\n              <li>Las enseña en pantalla</li>\n              <li>Permite eliminar cada tarea</li>\n            </ul>\n\n            <div class="mt-5 rounded-2xl border border-slate-200 bg-slate-50 p-4 text-sm leading-7 text-slate-700">\n              <strong>JS que aparece aquí:</strong> función flecha, <code>useState</code>, spread <code>...</code> y <code>filter()</code>.\n              <br />\n              <strong>Spread</strong> sirve para copiar un arreglo y agregarle algo sin modificar el original.\n            </div>\n\n            <div class="code-block mt-5">\n              <div><span class="key">import</span> { useState } <span class="key">from</span> <span class="str">"react"</span>;</div>\n              <div class="muted"></div>\n              <div><span class="key">export default function</span> <span class="tag">App</span>() {</div>\n              <div>&nbsp;&nbsp;<span class="key">const</span> [tarea, setTarea] = useState(<span class="str">""</span>);</div>\n              <div>&nbsp;&nbsp;<span class="key">const</span> [tareas, setTareas] = useState([]);</div>\n              <div class="muted"></div>\n              <div>&nbsp;&nbsp;<span class="key">const</span> agregarTarea = () =&gt; {</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="key">if</span> (tarea.trim() === <span class="str">""</span>) <span class="key">return</span>;</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;setTareas([...tareas, tarea]);</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;setTarea(<span class="str">""</span>);</div>\n              <div>&nbsp;&nbsp;};</div>\n              <div class="muted"></div>\n              <div>&nbsp;&nbsp;<span class="key">const</span> eliminarTarea = (indice) =&gt; {</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="key">const</span> nuevasTareas = tareas.filter((_, i) =&gt; i !== indice);</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;setTareas(nuevasTareas);</div>\n              <div>&nbsp;&nbsp;};</div>\n              <div class="muted"></div>\n              <div>&nbsp;&nbsp;<span class="key">return</span> (</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;div&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;h1&gt;</span>Lista de tareas<span class="tag">&lt;/h1&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;input</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="key">value</span>={tarea}</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="key">onChange</span>={(e) =&gt; setTarea(e.target.value)}</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="key">placeholder</span>=<span class="str">"Escribe una tarea"</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;/&gt;</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;button</span> <span class="key">onClick</span>={agregarTarea}<span class="tag">&gt;</span>Agregar<span class="tag">&lt;/button&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;ul&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{tareas.map((item, index) =&gt; (</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;li</span> <span class="key">key</span>={index}<span class="tag">&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{item}</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;button</span> <span class="key">onClick</span>={() =&gt; eliminarTarea(index)}<span class="tag">&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Eliminar</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;/button&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;/li&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;))}</div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;/ul&gt;</span></div>\n              <div>&nbsp;&nbsp;&nbsp;&nbsp;<span class="tag">&lt;/div&gt;</span></div>\n              <div>&nbsp;&nbsp;);</div>\n              <div>}</div>\n            </div>\n\n            <div class="mt-6 grid gap-4 md:grid-cols-2">\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">Qué pasa al escribir</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  <code>onChange</code> captura lo que escribes y lo guarda en el estado <code>tarea</code>.\n                </p>\n              </article>\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">Qué pasa al agregar</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  <code>setTareas([...tareas, tarea])</code> crea un arreglo nuevo con la tarea al final.\n                </p>\n              </article>\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">Qué pasa al mostrar</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  <code>map()</code> convierte el arreglo en elementos visuales de la lista.\n                </p>\n              </article>\n              <article class="rounded-2xl border border-slate-200 bg-slate-50 p-4">\n                <h3 class="font-bold text-slate-900">Qué pasa al eliminar</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">\n                  <code>filter()</code> construye una lista nueva sin la tarea seleccionada.\n                </p>\n              </article>\n            </div>\n          </section>\n\n          <section id="buenas-practicas" class="glass page-break rounded-[24px] p-6 md:p-7">\n            <h2 class="text-2xl font-black text-slate-900">6. Buenas prácticas para empezar</h2>\n            <ul class="check-list mt-5 space-y-3 text-sm leading-7 text-slate-700">\n              <li>Haz componentes pequeños. Un componente, una responsabilidad principal.</li>\n              <li>Nombra archivos con PascalCase: <code>MiComponente.js</code>.</li>\n              <li>Deja el estado lo más arriba que sea necesario, pero no más.</li>\n              <li>Usa <code>npm start</code> o <code>npm run dev</code> para desarrollo.</li>\n              <li>Usa <code>npm run build</code> cuando quieras preparar la app para producción.</li>\n              <li>No intentes memorizar todo de una vez. Practica ejemplos cortos.</li>\n            </ul>\n          </section>\n\n          <section id="recursos" class="glass page-break rounded-[24px] p-6 md:p-7">\n            <h2 class="text-2xl font-black text-slate-900">7. Recursos adicionales</h2>\n            <div class="mt-5 grid gap-4 md:grid-cols-2">\n              <a class="rounded-2xl border border-slate-200 bg-slate-50 p-4 transition hover:border-blue-300" href="https://react.dev/learn" target="_blank" rel="noreferrer">\n                <h3 class="font-bold text-slate-900">Documentación oficial de React</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">La mejor referencia para aprender conceptos y ejemplos actualizados.</p>\n              </a>\n              <a class="rounded-2xl border border-slate-200 bg-slate-50 p-4 transition hover:border-blue-300" href="https://react.dev/learn/installation" target="_blank" rel="noreferrer">\n                <h3 class="font-bold text-slate-900">Instalación oficial</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">Guía oficial para empezar y nota sobre la deprecación de Create React App.</p>\n              </a>\n              <a class="rounded-2xl border border-slate-200 bg-slate-50 p-4 transition hover:border-blue-300" href="https://nodejs.org/es/download" target="_blank" rel="noreferrer">\n                <h3 class="font-bold text-slate-900">Descarga oficial de Node.js</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">Instala la versión LTS para Windows desde la web oficial.</p>\n              </a>\n              <a class="rounded-2xl border border-slate-200 bg-slate-50 p-4 transition hover:border-blue-300" href="https://www.youtube.com/results?search_query=react+desde+cero+espa%C3%B1ol" target="_blank" rel="noreferrer">\n                <h3 class="font-bold text-slate-900">Videos en español</h3>\n                <p class="mt-2 text-sm leading-7 text-slate-700">Busca cursos “React desde cero” para reforzar con práctica visual.</p>\n              </a>\n            </div>\n\n            <div class="mt-6 rounded-2xl border border-slate-200 bg-slate-50 p-5 text-sm leading-7 text-slate-700">\n              <p class="font-bold text-slate-900">Fuentes verificadas</p>\n              <p class="mt-2">\n                React Installation: <a class="text-blue-700 underline" href="https://react.dev/learn/installation" target="_blank" rel="noreferrer">react.dev/learn/installation</a><br />\n                Sunsetting Create React App: <a class="text-blue-700 underline" href="https://react.dev/blog/2025/02/14/sunsetting-create-react-app" target="_blank" rel="noreferrer">react.dev/blog/2025/02/14/sunsetting-create-react-app</a><br />\n                Node.js Downloads: <a class="text-blue-700 underline" href="https://nodejs.org/es/download" target="_blank" rel="noreferrer">nodejs.org/es/download</a>\n              </p>\n            </div>\n          </section>\n\n          <footer class="glass no-print rounded-[24px] p-6 text-center">\n            <p class="text-sm leading-7 text-slate-600">\n              Si al principio React se siente extraño, es completamente normal.\n              Lo importante no es memorizar, sino construir algo pequeño, romperlo, arreglarlo y repetir.\n            </p>\n            <button\n              onclick="window.print()"\n              class="mt-4 rounded-2xl bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700"\n            >\n              Descargar esta guía como PDF\n            </button>\n          </footer>\n        </div>\n      </section>\n    </main>\n  </body>\n</html>\n');

-- Volcando estructura para tabla odin.auditoria_radicados
CREATE TABLE IF NOT EXISTS `auditoria_radicados` (
  `id_auditoria` bigint NOT NULL AUTO_INCREMENT,
  `id_radicado` int NOT NULL,
  `id_usuario` int NOT NULL,
  `accion` varchar(100) NOT NULL,
  `campo_modificado` varchar(100) DEFAULT NULL,
  `valor_anterior` text,
  `valor_nuevo` text,
  `ip` varchar(45) DEFAULT NULL,
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  `comentario` text,
  PRIMARY KEY (`id_auditoria`),
  KEY `id_radicado` (`id_radicado`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `auditoria_radicados_ibfk_1` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`),
  CONSTRAINT `auditoria_radicados_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 4 - Auditoría Detallada** \r\nPropósito: Registro técnico de cambios (quién, qué, cuándo, valor anterior/nuevo). \r\nComunicación: N:1 → radicados. \r\nTrazabilidad: Cumplimiento normativo y auditoría interna.';

-- Volcando datos para la tabla odin.auditoria_radicados: ~0 rows (aproximadamente)

-- Volcando estructura para tabla odin.ccd_series
CREATE TABLE IF NOT EXISTS `ccd_series` (
  `id_serie` int NOT NULL AUTO_INCREMENT,
  `codigo_serie` varchar(50) NOT NULL,
  `nombre_serie` varchar(300) NOT NULL,
  `codigo_unidad` varchar(30) NOT NULL,
  `codigo_seccion` varchar(30) DEFAULT NULL,
  `codigo_subseccion` varchar(30) DEFAULT NULL,
  `descripcion` text,
  `informacion_publica` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_serie`),
  UNIQUE KEY `codigo_serie` (`codigo_serie`),
  KEY `codigo_unidad` (`codigo_unidad`),
  KEY `idx_codigo_serie` (`codigo_serie`),
  CONSTRAINT `ccd_series_ibfk_1` FOREIGN KEY (`codigo_unidad`) REFERENCES `ccd_unidades` (`codigo_unidad`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 2 - CCD Series** \r\nPropósito: Series documentales del Cuadro de Clasificación Documental. \r\nComunicación: 1:N → ccd_subseries. \r\nTrazabilidad: Clasificación intermedia obligatoria en la tabla radicados.';

-- Volcando datos para la tabla odin.ccd_series: ~11 rows (aproximadamente)
INSERT INTO `ccd_series` (`id_serie`, `codigo_serie`, `nombre_serie`, `codigo_unidad`, `codigo_seccion`, `codigo_subseccion`, `descripcion`, `informacion_publica`) VALUES
	(1, '1.1012-27', 'DERECHOS DE PETICIÓN', '1.1012', NULL, NULL, NULL, NULL),
	(2, '1.0010-27', 'DERECHOS DE PETICIÓN', '1.0010', NULL, NULL, NULL, NULL),
	(3, '1.2020-27', 'DERECHOS DE PETICIÓN', '1.2020', NULL, NULL, NULL, NULL),
	(4, '1.1010-42', 'INFORMES', '1.1010', NULL, NULL, NULL, NULL),
	(9, '1.1013-27', 'DERECHOS DE PETICIÓN', '1.1013', NULL, NULL, NULL, NULL),
	(10, '1.1014-27', 'DERECHOS DE PETICIÓN', '1.1014', NULL, NULL, NULL, NULL),
	(11, '1.1015-27', 'DERECHOS DE PETICIÓN', '1.1015', NULL, NULL, NULL, NULL),
	(12, '1.1012-42', 'INFORMES', '1.1012', NULL, NULL, NULL, NULL),
	(13, '1.1010-22', 'CONTRATOS', '1.1010', NULL, NULL, NULL, NULL),
	(14, '1.1012-22', 'CONTRATOS', '1.1012', NULL, NULL, NULL, NULL),
	(15, '1.1010-25', 'CONVENIOS', '1.1010', NULL, NULL, NULL, NULL);

-- Volcando estructura para tabla odin.ccd_subseries
CREATE TABLE IF NOT EXISTS `ccd_subseries` (
  `id_subserie` int NOT NULL AUTO_INCREMENT,
  `codigo_subserie` varchar(60) NOT NULL,
  `nombre_subserie` varchar(400) NOT NULL,
  `id_serie` int NOT NULL,
  `tipo_pqrsf` text,
  `retencion_anios` int DEFAULT '5' COMMENT 'Años de retención',
  `disposicion_final` enum('CONSERVACION','ELIMINACION','SELECCION','DIGITALIZACION') DEFAULT 'CONSERVACION',
  `nivel_acceso` enum('PUBLICO','RESERVADO','CONFIDENCIAL') DEFAULT 'PUBLICO',
  `valor_documental` enum('TESTIMONIAL','ADMINISTRATIVO','LEGAL') DEFAULT 'ADMINISTRATIVO',
  `observaciones_retencion` text,
  PRIMARY KEY (`id_subserie`),
  UNIQUE KEY `codigo_subserie` (`codigo_subserie`),
  KEY `id_serie` (`id_serie`),
  KEY `idx_codigo_subserie` (`codigo_subserie`),
  CONSTRAINT `ccd_subseries_ibfk_1` FOREIGN KEY (`id_serie`) REFERENCES `ccd_series` (`id_serie`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 2 - CCD Subseries + Retención Documental** \r\nPropósito: Subseries y política de retención documental (tiempo, disposición final, nivel de acceso). \r\nComunicación: N:1 → radicados (obligatorio). \r\nTrazabilidad: Define el valor documental y tiempo de conservación de cada radicado.';

-- Volcando datos para la tabla odin.ccd_subseries: ~6 rows (aproximadamente)
INSERT INTO `ccd_subseries` (`id_subserie`, `codigo_subserie`, `nombre_subserie`, `id_serie`, `tipo_pqrsf`, `retencion_anios`, `disposicion_final`, `nivel_acceso`, `valor_documental`, `observaciones_retencion`) VALUES
	(1, '1.1012-27-01', 'Peticiones Generales', 1, 'peticion', 5, 'CONSERVACION', 'PUBLICO', 'ADMINISTRATIVO', NULL),
	(2, '1.1012-27-02', 'Quejas y Reclamos', 1, 'queja', 5, 'CONSERVACION', 'PUBLICO', 'ADMINISTRATIVO', NULL),
	(3, '1.1012-27-03', 'Tutelas', 1, 'peticion', 5, 'CONSERVACION', 'PUBLICO', 'ADMINISTRATIVO', NULL),
	(4, '1.1013-27-01', 'Peticiones de Información', 2, 'peticion', 5, 'CONSERVACION', 'PUBLICO', 'ADMINISTRATIVO', NULL),
	(5, '1.1014-27-01', 'Peticiones Tecnológicas', 3, 'peticion', 5, 'CONSERVACION', 'PUBLICO', 'ADMINISTRATIVO', NULL),
	(6, '1.1010-25-02', 'Convenios de Asociación', 9, 'general', 5, 'CONSERVACION', 'PUBLICO', 'ADMINISTRATIVO', NULL);

-- Volcando estructura para tabla odin.ccd_unidades
CREATE TABLE IF NOT EXISTS `ccd_unidades` (
  `id_unidad` int NOT NULL AUTO_INCREMENT,
  `codigo_unidad` varchar(30) NOT NULL,
  `nombre_unidad` varchar(255) NOT NULL,
  `codigo_padre` varchar(30) DEFAULT NULL,
  `nivel` tinyint NOT NULL,
  `descripcion` text,
  PRIMARY KEY (`id_unidad`),
  UNIQUE KEY `codigo_unidad` (`codigo_unidad`),
  KEY `idx_codigo_unidad` (`codigo_unidad`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 2 - Estructura Organizacional (CCD)** \r\nPropósito: Representa las unidades administrativas del SENA según Cuadro de Clasificación Documental 2025. \r\nComunicación: 1:N → ccd_series → ccd_subseries → radicados. \r\nTrazabilidad: Base normativa para la clasificación documental de todos los radicados.';

-- Volcando datos para la tabla odin.ccd_unidades: ~8 rows (aproximadamente)
INSERT INTO `ccd_unidades` (`id_unidad`, `codigo_unidad`, `nombre_unidad`, `codigo_padre`, `nivel`, `descripcion`) VALUES
	(1, '1.1010', 'DESPACHO DIRECTOR GENERAL', NULL, 3, NULL),
	(2, '1.1012', 'Oficina de Control Interno', NULL, 4, NULL),
	(3, '1.0010', 'DIRECCIÓN JURÍDICA', NULL, 3, NULL),
	(4, '1.2020', 'SECRETARÍA GENERAL', NULL, 3, NULL),
	(13, '1', 'DIRECCIÓN GENERAL', NULL, 2, NULL),
	(14, '1.1013', 'Oficina de Comunicaciones', '1.1010', 4, NULL),
	(15, '1.1014', 'Oficina de Sistemas', '1.1010', 4, NULL),
	(16, '1.1015', 'Oficina de Control Interno Disciplinario', '1.1010', 4, NULL);

-- Volcando estructura para tabla odin.dependencias
CREATE TABLE IF NOT EXISTS `dependencias` (
  `id_dependencia` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `estado` varchar(255) NOT NULL,
  `dependencia` varchar(255) NOT NULL,
  `codigo` varchar(30) DEFAULT NULL,
  `tipo` enum('principal','apoyo','operativa') DEFAULT 'operativa',
  PRIMARY KEY (`id_dependencia`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 2 - Dependencias** \r\nPropósito: Estructura operativa del SENA (Recepción, Coordinación, Subdirección, etc.). \r\nComunicación: 1:N con radicados y reasignaciones. \r\nTrazabilidad: Permite rastrear a qué dependencia pertenece cada radicado.';

-- Volcando datos para la tabla odin.dependencias: ~3 rows (aproximadamente)
INSERT INTO `dependencias` (`id_dependencia`, `nombre`, `descripcion`, `estado`, `dependencia`, `codigo`, `tipo`) VALUES
	(1, 'coordinacion', 'coordinacion de cordinadores', 'activo', 'cod01', NULL, 'operativa'),
	(2, 'recepcion', 'recepcion', 'activo', 'recep01', NULL, 'operativa'),
	(3, 'subdireccion', 'subdireccion de centro', 'activo', 'subd01', NULL, 'operativa');

-- Volcando estructura para tabla odin.documentos
CREATE TABLE IF NOT EXISTS `documentos` (
  `id_documento` bigint NOT NULL AUTO_INCREMENT,
  `id_radicado` int DEFAULT NULL,
  `tamano` int DEFAULT NULL,
  `nombre_archivo` varchar(255) NOT NULL,
  `ruta_archivo` varchar(255) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `fecha_subida` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id_documento`),
  KEY `fk_documento_radicado` (`id_radicado`),
  CONSTRAINT `fk_documento_radicado` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 3 - Documentos Principales** \r\nPropósito: Gestión de documentos adjuntos principales. \r\nComunicación: N:1 → radicados (Grupo 3). \r\nTrazabilidad: Vinculado al ciclo de vida del radicado en PLANTILLA.';

-- Volcando datos para la tabla odin.documentos: ~1 rows (aproximadamente)
INSERT INTO `documentos` (`id_documento`, `id_radicado`, `tamano`, `nombre_archivo`, `ruta_archivo`, `tipo`, `fecha_subida`, `nombre`) VALUES
	(1, 1, 50, 'numero_radicado', 'c:local/escritorio', 'pdf', '0000-00-00 00:00:00', 'remitente externos o interno');

-- Volcando estructura para tabla odin.estados
CREATE TABLE IF NOT EXISTS `estados` (
  `id_estado` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `color` varchar(20) DEFAULT NULL,
  `flujo_pqrsf` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_estado`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 3 - Estados del Radicado** \r\nPropósito: Control del ciclo de vida (Radicado, En trámite, Devuelto, Finalizado, etc.). \r\nComunicación: 1:N → radicados. \r\nTrazabilidad: Cambios de estado quedan registrados en PLANTILLA.';

-- Volcando datos para la tabla odin.estados: ~7 rows (aproximadamente)
INSERT INTO `estados` (`id_estado`, `nombre`, `color`, `flujo_pqrsf`) VALUES
	(1, 'radicado', NULL, 0),
	(2, 'en tramite', NULL, 0),
	(3, 'devuelto', NULL, 0),
	(4, 'rechazado', NULL, 0),
	(5, 'archivado', NULL, 0),
	(6, 'trasladado', NULL, 0),
	(7, 'finalizado', NULL, 0);

-- Volcando estructura para tabla odin.firmas
CREATE TABLE IF NOT EXISTS `firmas` (
  `id_firma` bigint NOT NULL AUTO_INCREMENT,
  `id_radicado` int NOT NULL,
  `id_usuario` int NOT NULL,
  `fecha_firma` datetime DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('pendiente','firmado','rechazado') DEFAULT 'pendiente',
  PRIMARY KEY (`id_firma`),
  KEY `fk_firma_radicado` (`id_radicado`),
  KEY `fk_firma_usuario` (`id_usuario`),
  CONSTRAINT `fk_firma_radicado` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_firma_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 4 - Firmas y Aprobaciones** \r\nPropósito: Control de firmas digitales o aprobaciones. \r\nComunicación: N:1 → radicados. \r\nTrazabilidad: Registro de aprobación final.';

-- Volcando datos para la tabla odin.firmas: ~0 rows (aproximadamente)

-- Volcando estructura para tabla odin.historial_radicado
CREATE TABLE IF NOT EXISTS `historial_radicado` (
  `id_historial` bigint NOT NULL AUTO_INCREMENT,
  `id_radicado` int NOT NULL,
  `id_usuario` int NOT NULL,
  `accion` varchar(100) NOT NULL,
  `descripcion` text NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_historial`),
  KEY `fk_historial_radicado` (`id_radicado`),
  KEY `fk_historial_usuario` (`id_usuario`),
  KEY `idx_historial_radicado` (`id_radicado`,`fecha`),
  CONSTRAINT `fk_historial_radicado` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_historial_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 4 - Historial Secundario** \r\nPropósito: Complemento histórico de PLANTILLA. \r\nComunicación: N:1 → radicados. \r\nTrazabilidad: Soporte adicional para trazabilidad.';

-- Volcando datos para la tabla odin.historial_radicado: ~11 rows (aproximadamente)
INSERT INTO `historial_radicado` (`id_historial`, `id_radicado`, `id_usuario`, `accion`, `descripcion`, `fecha`) VALUES
	(13, 23, 2, 'radicado', 'Documento radicado exitosamente', '2026-05-27 23:14:44'),
	(14, 24, 2, 'traslado', 'Trasladado - Motivo: Traslado a coordinación para estudio', '2026-05-27 23:14:44'),
	(15, 24, 1, 'devuelto', 'Falta de documento de identidad legible', '2026-05-27 23:14:44'),
	(16, 24, 1, 'finalizado', 'Radicado resuelto favorablemente después de subsanación', '2026-05-27 23:14:44'),
	(17, 24, 1, 'traslado', 'Trasladado - Motivo: Intento inválido', '2026-05-27 23:14:44'),
	(24, 32, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 00:16:02'),
	(25, 34, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 00:18:32'),
	(26, 35, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 00:19:58'),
	(27, 36, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 22:52:02'),
	(28, 37, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 22:54:51'),
	(29, 38, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 22:55:02');

-- Volcando estructura para tabla odin.log_accesos
CREATE TABLE IF NOT EXISTS `log_accesos` (
  `id_log` bigint NOT NULL AUTO_INCREMENT,
  `id_usuario` int DEFAULT NULL,
  `accion` varchar(50) NOT NULL,
  `ip` varchar(45) DEFAULT NULL,
  `user_agent` text,
  `exito` tinyint(1) NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_log`),
  KEY `fk_log_usuario` (`id_usuario`),
  KEY `idx_log_fecha` (`fecha`),
  CONSTRAINT `fk_log_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 1 - Auditoría de Accesos (Login/Logout)**';

-- Volcando datos para la tabla odin.log_accesos: ~0 rows (aproximadamente)

-- Volcando estructura para tabla odin.notificaciones
CREATE TABLE IF NOT EXISTS `notificaciones` (
  `id_notificacion` bigint NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_radicado` int DEFAULT NULL,
  `titulo` varchar(150) NOT NULL,
  `mensaje` text NOT NULL,
  `leida` tinyint(1) DEFAULT '0',
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_notificacion`),
  KEY `fk_notif_usuario` (`id_usuario`),
  KEY `fk_notif_radicado` (`id_radicado`),
  CONSTRAINT `fk_notif_radicado` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 4 - Notificaciones** \r\nPropósito: Alertas del sistema a los usuarios. \r\nComunicación: N:1 → usuarios y radicados. \r\nTrazabilidad: Notificación de cambios registrados en PLANTILLA.';

-- Volcando datos para la tabla odin.notificaciones: ~3 rows (aproximadamente)
INSERT INTO `notificaciones` (`id_notificacion`, `id_usuario`, `id_radicado`, `titulo`, `mensaje`, `leida`, `fecha`) VALUES
	(3, 2, NULL, 'Radicado Devuelto', 'RAD-2026-004 fue devuelto por falta de documentos.', 0, '2026-05-27 21:43:47'),
	(4, 2, NULL, 'Radicado Devuelto', 'RAD-2026-004 fue devuelto por falta de documentos.', 0, '2026-05-27 21:44:00'),
	(5, 2, NULL, 'Radicado Devuelto', 'RAD-2026-004 fue devuelto por falta de documentos.', 0, '2026-05-27 22:39:00');

-- Volcando estructura para tabla odin.observaciones
CREATE TABLE IF NOT EXISTS `observaciones` (
  `id_observacion` bigint NOT NULL AUTO_INCREMENT,
  `id_radicado` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `comentario` varchar(255) NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_observacion`),
  KEY `fk_observacion_radicado` (`id_radicado`),
  KEY `fk_observacion_usuario` (`id_usuario`),
  KEY `idx_observaciones_radicado` (`id_radicado`),
  CONSTRAINT `fk_observacion_radicado` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_observacion_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 4 - Observaciones** \r\nPropósito: Comentarios operativos durante el trámite. \r\nComunicación: N:1 → radicados. \r\nTrazabilidad: Información cualitativa complementaria a PLANTILLA.';

-- Volcando datos para la tabla odin.observaciones: ~1 rows (aproximadamente)
INSERT INTO `observaciones` (`id_observacion`, `id_radicado`, `id_usuario`, `comentario`, `fecha`) VALUES
	(4, 24, 1, 'Falta de documento de identidad legible', '0000-00-00 00:00:00');

-- Volcando estructura para tabla odin.permisos
CREATE TABLE IF NOT EXISTS `permisos` (
  `id_permiso` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `modulo` varchar(50) NOT NULL,
  `accion` varchar(50) NOT NULL,
  PRIMARY KEY (`id_permiso`),
  UNIQUE KEY `uk_permiso` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 1 - Permisos Granulares**';

-- Volcando datos para la tabla odin.permisos: ~9 rows (aproximadamente)
INSERT INTO `permisos` (`id_permiso`, `nombre`, `descripcion`, `modulo`, `accion`) VALUES
	(1, 'ver_radicados', 'Ver listado y detalle de radicados', 'radicados', 'ver'),
	(2, 'crear_radicado', 'Crear nuevos radicados', 'radicados', 'crear'),
	(3, 'editar_radicado', 'Editar radicados', 'radicados', 'editar'),
	(4, 'trasladar_radicado', 'Trasladar radicados entre dependencias', 'radicados', 'trasladar'),
	(5, 'finalizar_radicado', 'Finalizar radicados', 'radicados', 'finalizar'),
	(6, 'ver_usuarios', 'Ver usuarios del sistema', 'usuarios', 'ver'),
	(7, 'admin_usuarios', 'Gestionar usuarios y roles', 'usuarios', 'admin'),
	(8, 'ver_reportes', 'Acceder a reportes y estadísticas', 'reportes', 'ver'),
	(9, 'admin_ccd', 'Gestionar Cuadro de Clasificación Documental', 'ccd', 'admin');

-- Volcando estructura para tabla odin.plantilla
CREATE TABLE IF NOT EXISTS `plantilla` (
  `id_plantilla` bigint NOT NULL AUTO_INCREMENT,
  `id_radicado` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `accion` varchar(255) NOT NULL,
  `descripcion` text NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ip` varchar(45) DEFAULT NULL,
  `datos` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT 'Datos adicionales del cambio',
  `accion_detalle` varchar(100) DEFAULT NULL COMMENT 'Subtipo de acción',
  PRIMARY KEY (`id_plantilla`),
  KEY `fk_plantilla_radicado` (`id_radicado`),
  KEY `fk_plantilla_usuario` (`id_usuario`),
  KEY `idx_plantilla_radicado` (`id_radicado`),
  KEY `idx_plantilla_radicado_fecha` (`id_radicado`,`fecha`),
  KEY `idx_plantilla_usuario` (`id_usuario`),
  CONSTRAINT `fk_plantilla_radicado` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_plantilla_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `plantilla_chk_1` CHECK (json_valid(`datos`))
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 4 - TABLA PRINCIPAL DE TRAZABILIDAD** \r\nPropósito: Registro histórico completo de todas las acciones sobre los radicados. \r\nComunicación: Recibe información de Grupo 1 (usuarios), Grupo 2 (CCD/dependencias) y Grupo 3 (radicados). \r\nTrazabilidad: Tabla central de auditoría y seguimiento del ciclo de vida completo.';

-- Volcando datos para la tabla odin.plantilla: ~13 rows (aproximadamente)
INSERT INTO `plantilla` (`id_plantilla`, `id_radicado`, `id_usuario`, `accion`, `descripcion`, `fecha`, `ip`, `datos`, `accion_detalle`) VALUES
	(1, 1, 2, 'radicado', 'radicado', '0000-00-00 00:00:00', NULL, NULL, NULL),
	(2, 1, 1, 'traslado', 'traslado de coordinacion a subdireccion', '0000-00-00 00:00:00', NULL, NULL, NULL),
	(3, 1, 2, 'traslado', 'traslado de subdireccion a coordinacion', '0000-00-00 00:00:00', NULL, NULL, NULL),
	(4, 2, 1, 'finalizado\r\n', 'respuesta al radicado 1', '0000-00-00 00:00:00', NULL, NULL, NULL),
	(14, 24, 1, 'finalizado', 'Radicado resuelto favorablemente después de subsanación', '0000-00-00 00:00:00', NULL, NULL, NULL),
	(15, 23, 2, 'radicado', 'Documento radicado exitosamente', '2026-05-27 23:14:44', NULL, '{"origen": "historial_migrado"}', NULL),
	(16, 24, 2, 'traslado', 'Trasladado - Motivo: Traslado a coordinación para estudio', '2026-05-27 23:14:44', NULL, '{"origen": "historial_migrado"}', NULL),
	(17, 24, 1, 'devuelto', 'Falta de documento de identidad legible', '2026-05-27 23:14:44', NULL, '{"origen": "historial_migrado"}', NULL),
	(18, 24, 1, 'traslado', 'Trasladado - Motivo: Intento inválido', '2026-05-27 23:14:44', NULL, '{"origen": "historial_migrado"}', NULL),
	(19, 32, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 00:16:02', NULL, '{"origen": "historial_migrado"}', NULL),
	(20, 34, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 00:18:32', NULL, '{"origen": "historial_migrado"}', NULL),
	(21, 35, 2, 'radicado', 'Documento radicado exitosamente en recepción', '2026-05-29 00:19:58', NULL, '{"origen": "historial_migrado"}', NULL),
	(22, 40, 2, 'RADICADO', 'Documento radicado exitosamente en recepción', '2026-05-29 22:58:30', NULL, NULL, 'CREACION');

-- Volcando estructura para tabla odin.radicados
CREATE TABLE IF NOT EXISTS `radicados` (
  `id_radicado` int NOT NULL AUTO_INCREMENT,
  `numero_radicado` varchar(255) NOT NULL,
  `id_tramite` int DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `id_dependencia` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `id_remitente` bigint DEFAULT NULL,
  `remitente` varchar(255) NOT NULL,
  `asunto` varchar(255) NOT NULL,
  `fecha_radicado` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `codigo_serie` varchar(50) DEFAULT NULL,
  `codigo_subserie` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id_radicado`),
  UNIQUE KEY `numero_radicado` (`numero_radicado`),
  KEY `fk_radicado_tramite` (`id_tramite`),
  KEY `fk_radicado_estado` (`id_estado`),
  KEY `fk_radicado_dependencia` (`id_dependencia`),
  KEY `fk_radicado_usuario` (`id_usuario`),
  KEY `idx_radicados_numero` (`numero_radicado`),
  KEY `idx_radicados_estado` (`id_estado`),
  KEY `idx_radicados_usuario` (`id_usuario`),
  KEY `idx_radicados_remitente` (`id_remitente`),
  KEY `fk_radicado_ccd_subserie` (`codigo_subserie`),
  KEY `idx_numero_radicado` (`numero_radicado`),
  KEY `idx_fecha_radicado` (`fecha_radicado`),
  KEY `idx_estado` (`id_estado`),
  KEY `idx_dependencia` (`id_dependencia`),
  KEY `idx_radicados_fecha_estado` (`fecha_radicado`,`id_estado`),
  KEY `idx_ccd_codigo_completo` (`codigo_serie`,`codigo_subserie`),
  CONSTRAINT `fk_radicado_ccd_serie` FOREIGN KEY (`codigo_serie`) REFERENCES `ccd_series` (`codigo_serie`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_radicado_ccd_subserie` FOREIGN KEY (`codigo_subserie`) REFERENCES `ccd_subseries` (`codigo_subserie`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_radicado_dependencia` FOREIGN KEY (`id_dependencia`) REFERENCES `dependencias` (`id_dependencia`),
  CONSTRAINT `fk_radicado_estado` FOREIGN KEY (`id_estado`) REFERENCES `estados` (`id_estado`),
  CONSTRAINT `fk_radicado_remitente` FOREIGN KEY (`id_remitente`) REFERENCES `remitentes_externos` (`id_remitente`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_radicado_tramite` FOREIGN KEY (`id_tramite`) REFERENCES `tramites` (`id_tramite`),
  CONSTRAINT `fk_radicado_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 3 - TABLA CENTRAL (Radicados)** \r\nPropósito: Núcleo del sistema. Contiene todos los radicados (internos, externos, anónimos, PQRSF). \r\nComunicación: \r\n- Recibe de Grupo 1 (usuarios), Grupo 2 (CCD y dependencias). \r\n- Envía a Grupo 4 (flujo y trazabilidad). \r\nTrazabilidad: Origen principal de todos los registros en PLANTILLA.';

-- Volcando datos para la tabla odin.radicados: ~11 rows (aproximadamente)
INSERT INTO `radicados` (`id_radicado`, `numero_radicado`, `id_tramite`, `id_estado`, `id_dependencia`, `id_usuario`, `id_remitente`, `remitente`, `asunto`, `fecha_radicado`, `codigo_serie`, `codigo_subserie`) VALUES
	(1, '1', 1, 6, 2, 1, NULL, 'anonimo', 'asunto para resolver por otro asunto', '0000-00-00 00:00:00', NULL, NULL),
	(2, '2\r\n', 6, 7, 2, 1, NULL, '1', 'asunto repuesta', '0000-00-00 00:00:00', NULL, NULL),
	(23, 'RAD-BAS-001', 1, 1, 2, 2, NULL, 'Juan Pérez', 'Solicitud de información general', '0000-00-00 00:00:00', NULL, NULL),
	(24, 'RAD-EXT-002', 1, 6, 2, 2, 6, 'María López García', 'Petición formal', '0000-00-00 00:00:00', NULL, NULL),
	(32, 'TEST-BAS-001', 1, 1, 2, 2, NULL, 'Pedro Gómez', 'Solicitud de certificación de estudios', '0000-00-00 00:00:00', NULL, NULL),
	(34, 'TEST-BAS-002', 1, 1, 2, 2, NULL, 'Pedro Gómez', 'Prueba de traslado', '0000-00-00 00:00:00', NULL, NULL),
	(35, 'TEST-BAS-0044', 1, 1, 2, 2, NULL, 'juaco Gómez', 'Prueba de traslado', '0000-00-00 00:00:00', NULL, NULL),
	(36, 'BAS-20260529-005', 1, 1, 2, 2, NULL, 'Laura Gómez', 'Solicitud de información general', '0000-00-00 00:00:00', NULL, NULL),
	(37, 'EXT-20260529-006', 1, 1, 2, 2, NULL, 'María López', 'Petición formal externa', '0000-00-00 00:00:00', NULL, NULL),
	(38, 'ANON-20260529-007', 1, 1, 2, 2, NULL, 'ANÓNIMO', 'Queja sin identificación', '0000-00-00 00:00:00', NULL, NULL),
	(40, 'BAS-20260529-008', 1, 1, 2, 2, NULL, 'Juan Pérez', 'Prueba corregida', '2026-05-29 22:58:30', NULL, NULL);

-- Volcando estructura para tabla odin.reasignaciones
CREATE TABLE IF NOT EXISTS `reasignaciones` (
  `id_reasignacion` bigint NOT NULL AUTO_INCREMENT,
  `id_radicado` int DEFAULT NULL,
  `id_usuario_anterior` int DEFAULT NULL,
  `id_usuario_nuevo` int DEFAULT NULL,
  `id_dependencia_nueva` int DEFAULT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_reasignacion`),
  KEY `fk_reasignacion_radicado` (`id_radicado`),
  KEY `fk_usuario_anterior` (`id_usuario_anterior`),
  KEY `fk_usuario_nuevo` (`id_usuario_nuevo`),
  KEY `fk_dependencia_nueva` (`id_dependencia_nueva`),
  CONSTRAINT `fk_dependencia_nueva` FOREIGN KEY (`id_dependencia_nueva`) REFERENCES `dependencias` (`id_dependencia`),
  CONSTRAINT `fk_reasignacion_radicado` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_usuario_anterior` FOREIGN KEY (`id_usuario_anterior`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_usuario_nuevo` FOREIGN KEY (`id_usuario_nuevo`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 4 - Reasignaciones / Traslados** \r\nPropósito: Registro detallado de traslados entre usuarios y dependencias. \r\nComunicación: N:1 → radicados. \r\nTrazabilidad: Movimientos internos quedan registrados en PLANTILLA.';

-- Volcando datos para la tabla odin.reasignaciones: ~2 rows (aproximadamente)
INSERT INTO `reasignaciones` (`id_reasignacion`, `id_radicado`, `id_usuario_anterior`, `id_usuario_nuevo`, `id_dependencia_nueva`, `fecha`) VALUES
	(1, 24, 2, 1, 1, '0000-00-00 00:00:00'),
	(2, 24, 1, 2, 2, '0000-00-00 00:00:00');

-- Volcando estructura para tabla odin.remitentes_externos
CREATE TABLE IF NOT EXISTS `remitentes_externos` (
  `id_remitente` bigint NOT NULL AUTO_INCREMENT,
  `tipo_identificacion` varchar(10) NOT NULL,
  `num_identificacion` varchar(30) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `apellido` varchar(255) NOT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `fecha_registro` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_remitente`),
  UNIQUE KEY `num_identificacion` (`num_identificacion`),
  UNIQUE KEY `uk_identificacion` (`tipo_identificacion`,`num_identificacion`),
  KEY `idx_remitentes_cedula` (`num_identificacion`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 3 - Remitentes Externos** \r\nPropósito: Registro de ciudadanos y entidades externas. \r\nComunicación: 1:N → radicados. \r\nTrazabilidad: Identificación del origen externo de los radicados.';

-- Volcando datos para la tabla odin.remitentes_externos: ~0 rows (aproximadamente)

-- Volcando estructura para tabla odin.reset_password_tokens
CREATE TABLE IF NOT EXISTS `reset_password_tokens` (
  `id_token` bigint NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `token` varchar(255) NOT NULL,
  `expira_en` datetime NOT NULL,
  `usado` tinyint(1) DEFAULT '0',
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_token`),
  KEY `fk_reset_usuario` (`id_usuario`),
  CONSTRAINT `fk_reset_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='**GRUPO 1 - Tokens para Recuperación de Contraseña**';

-- Volcando datos para la tabla odin.reset_password_tokens: ~0 rows (aproximadamente)

-- Volcando estructura para tabla odin.rol_permisos
CREATE TABLE IF NOT EXISTS `rol_permisos` (
  `id_rol` int DEFAULT NULL,
  `id_permiso` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- Volcando datos para la tabla odin.rol_permisos: ~0 rows (aproximadamente)

-- Volcando estructura para tabla odin.sesiones_usuario
CREATE TABLE IF NOT EXISTS `sesiones_usuario` (
  `id_sesion` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- Volcando datos para la tabla odin.sesiones_usuario: ~0 rows (aproximadamente)

-- Volcando estructura para tabla odin.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `id_rol` int DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `estado` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `fecha_creacion` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `clave` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `correo` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `dependencia` int NOT NULL,
  `direccion` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `num_identificacion` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `telefono` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  `tipo_identificacion` varchar(255) CHARACTER SET utf16 COLLATE utf16_spanish_ci NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- Volcando datos para la tabla odin.usuarios: ~0 rows (aproximadamente)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
