-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:8889
-- Tiempo de generación: 11-05-2026 a las 16:15:51
-- Versión del servidor: 8.0.35
-- Versión de PHP: 8.3.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `odin`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bitacoras`
--

CREATE TABLE `bitacoras` (
  `id_bitacora` int NOT NULL,
  `id_radicado` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `accion` varchar(100) COLLATE utf16_spanish_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `fecha` varchar(50) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dependencias`
--

CREATE TABLE `dependencias` (
  `id_dependencia` int NOT NULL,
  `nombre` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `estado` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `dependencia` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `documentos`
--

CREATE TABLE `documentos` (
  `id_documento` bigint NOT NULL,
  `id_radicado` int DEFAULT NULL,
  `tamano` int DEFAULT NULL,
  `nombre_archivo` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `ruta_archivo` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `tipo` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `fecha_subida` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estados`
--

CREATE TABLE `estados` (
  `id_estado` int NOT NULL,
  `nombre` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `observaciones`
--

CREATE TABLE `observaciones` (
  `id_observacion` bigint NOT NULL,
  `id_radicado` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `comentario` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `fecha` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `radicados`
--

CREATE TABLE `radicados` (
  `id_radicado` int NOT NULL,
  `numero_radicado` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `id_tramite` int DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `id_dependencia` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `remitente` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `asunto` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `fecha_radicado` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reasignaciones`
--

CREATE TABLE `reasignaciones` (
  `id_reasignacion` bigint NOT NULL,
  `id_radicado` int DEFAULT NULL,
  `id_usuario_anterior` int DEFAULT NULL,
  `id_usuario_nuevo` int DEFAULT NULL,
  `id_dependencia_nueva` int DEFAULT NULL,
  `fecha` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `respuestas`
--

CREATE TABLE `respuestas` (
  `id_respuesta` bigint NOT NULL,
  `id_radicado_entrada` int DEFAULT NULL,
  `id_radicado_salida` int DEFAULT NULL,
  `fecha` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id_rol` int NOT NULL,
  `nombre` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `estado` varchar(50) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_rol`, `nombre`, `estado`) VALUES
(1, 'Administrador', ''),
(2, 'Usuario', ''),
(3, 'Supervisor', ''),
(4, 'Invitado', ''),
(5, 'Editor', ''),
(6, 'Auditor', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `series`
--

CREATE TABLE `series` (
  `id_serie` bigint NOT NULL,
  `serie` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `estado` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

--
-- Volcado de datos para la tabla `series`
--

INSERT INTO `series` (`id_serie`, `serie`, `descripcion`, `estado`) VALUES
(1, '200', 'direccion', 'Inactivo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subseries`
--

CREATE TABLE `subseries` (
  `id_subserie` bigint NOT NULL,
  `id_serie` int NOT NULL,
  `subserie` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tramites`
--

CREATE TABLE `tramites` (
  `id_tramite` int NOT NULL,
  `nombre` varchar(255) COLLATE utf16_spanish_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf16_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL,
  `id_rol` int DEFAULT NULL,
  `id_dependencia` int NOT NULL,
  `nombre` varchar(255) COLLATE utf16_spanish_ci DEFAULT NULL,
  `tipo_identificacion` varchar(255) COLLATE utf16_spanish_ci DEFAULT NULL,
  `num_identificacion` varchar(255) COLLATE utf16_spanish_ci DEFAULT NULL,
  `correo` varchar(255) COLLATE utf16_spanish_ci DEFAULT NULL,
  `clave` varchar(255) COLLATE utf16_spanish_ci DEFAULT NULL,
  `estado` varchar(255) COLLATE utf16_spanish_ci DEFAULT NULL,
  `direccion` varchar(255) COLLATE utf16_spanish_ci DEFAULT NULL,
  `telefono` varchar(255) COLLATE utf16_spanish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `id_rol`, `id_dependencia`, `nombre`, `tipo_identificacion`, `num_identificacion`, `correo`, `clave`, `estado`, `direccion`, `telefono`) VALUES
(2, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(3, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(4, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(5, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, 5, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `bitacoras`
--
ALTER TABLE `bitacoras`
  ADD PRIMARY KEY (`id_bitacora`),
  ADD KEY `id_radicado` (`id_radicado`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `dependencias`
--
ALTER TABLE `dependencias`
  ADD PRIMARY KEY (`id_dependencia`);

--
-- Indices de la tabla `documentos`
--
ALTER TABLE `documentos`
  ADD PRIMARY KEY (`id_documento`),
  ADD KEY `id_radicado` (`id_radicado`);

--
-- Indices de la tabla `estados`
--
ALTER TABLE `estados`
  ADD PRIMARY KEY (`id_estado`);

--
-- Indices de la tabla `observaciones`
--
ALTER TABLE `observaciones`
  ADD PRIMARY KEY (`id_observacion`),
  ADD KEY `id_radicado` (`id_radicado`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `radicados`
--
ALTER TABLE `radicados`
  ADD PRIMARY KEY (`id_radicado`),
  ADD UNIQUE KEY `numero_radicado` (`numero_radicado`),
  ADD KEY `id_tramite` (`id_tramite`),
  ADD KEY `id_estado` (`id_estado`),
  ADD KEY `id_dependencia` (`id_dependencia`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `reasignaciones`
--
ALTER TABLE `reasignaciones`
  ADD PRIMARY KEY (`id_reasignacion`),
  ADD KEY `id_radicado` (`id_radicado`),
  ADD KEY `id_usuario_anterior` (`id_usuario_anterior`),
  ADD KEY `id_usuario_nuevo` (`id_usuario_nuevo`),
  ADD KEY `id_dependencia_nueva` (`id_dependencia_nueva`);

--
-- Indices de la tabla `respuestas`
--
ALTER TABLE `respuestas`
  ADD PRIMARY KEY (`id_respuesta`),
  ADD KEY `id_radicado_entrada` (`id_radicado_entrada`),
  ADD KEY `id_radicado_salida` (`id_radicado_salida`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_rol`);

--
-- Indices de la tabla `series`
--
ALTER TABLE `series`
  ADD PRIMARY KEY (`id_serie`);

--
-- Indices de la tabla `subseries`
--
ALTER TABLE `subseries`
  ADD PRIMARY KEY (`id_subserie`);

--
-- Indices de la tabla `tramites`
--
ALTER TABLE `tramites`
  ADD PRIMARY KEY (`id_tramite`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD KEY `id_rol` (`id_rol`),
  ADD KEY `id_dependencia` (`id_dependencia`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `bitacoras`
--
ALTER TABLE `bitacoras`
  MODIFY `id_bitacora` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `dependencias`
--
ALTER TABLE `dependencias`
  MODIFY `id_dependencia` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `documentos`
--
ALTER TABLE `documentos`
  MODIFY `id_documento` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `estados`
--
ALTER TABLE `estados`
  MODIFY `id_estado` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `observaciones`
--
ALTER TABLE `observaciones`
  MODIFY `id_observacion` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `radicados`
--
ALTER TABLE `radicados`
  MODIFY `id_radicado` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `reasignaciones`
--
ALTER TABLE `reasignaciones`
  MODIFY `id_reasignacion` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `respuestas`
--
ALTER TABLE `respuestas`
  MODIFY `id_respuesta` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `series`
--
ALTER TABLE `series`
  MODIFY `id_serie` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `subseries`
--
ALTER TABLE `subseries`
  MODIFY `id_subserie` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tramites`
--
ALTER TABLE `tramites`
  MODIFY `id_tramite` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `bitacoras`
--
ALTER TABLE `bitacoras`
  ADD CONSTRAINT `bitacoras_ibfk_1` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `bitacoras_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Filtros para la tabla `documentos`
--
ALTER TABLE `documentos`
  ADD CONSTRAINT `documentos_ibfk_1` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`);

--
-- Filtros para la tabla `observaciones`
--
ALTER TABLE `observaciones`
  ADD CONSTRAINT `observaciones_ibfk_1` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`),
  ADD CONSTRAINT `observaciones_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `radicados`
--
ALTER TABLE `radicados`
  ADD CONSTRAINT `radicados_ibfk_1` FOREIGN KEY (`id_tramite`) REFERENCES `tramites` (`id_tramite`),
  ADD CONSTRAINT `radicados_ibfk_2` FOREIGN KEY (`id_estado`) REFERENCES `estados` (`id_estado`),
  ADD CONSTRAINT `radicados_ibfk_3` FOREIGN KEY (`id_dependencia`) REFERENCES `dependencias` (`id_dependencia`),
  ADD CONSTRAINT `radicados_ibfk_4` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `reasignaciones`
--
ALTER TABLE `reasignaciones`
  ADD CONSTRAINT `reasignaciones_ibfk_1` FOREIGN KEY (`id_radicado`) REFERENCES `radicados` (`id_radicado`),
  ADD CONSTRAINT `reasignaciones_ibfk_2` FOREIGN KEY (`id_usuario_anterior`) REFERENCES `usuarios` (`id_usuario`),
  ADD CONSTRAINT `reasignaciones_ibfk_3` FOREIGN KEY (`id_usuario_nuevo`) REFERENCES `usuarios` (`id_usuario`),
  ADD CONSTRAINT `reasignaciones_ibfk_4` FOREIGN KEY (`id_dependencia_nueva`) REFERENCES `dependencias` (`id_dependencia`);

--
-- Filtros para la tabla `respuestas`
--
ALTER TABLE `respuestas`
  ADD CONSTRAINT `respuestas_ibfk_1` FOREIGN KEY (`id_radicado_entrada`) REFERENCES `radicados` (`id_radicado`),
  ADD CONSTRAINT `respuestas_ibfk_2` FOREIGN KEY (`id_radicado_salida`) REFERENCES `radicados` (`id_radicado`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
