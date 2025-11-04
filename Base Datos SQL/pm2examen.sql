-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-11-2025 a las 01:20:49
-- Versión del servidor: 10.4.11-MariaDB
-- Versión de PHP: 7.2.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `pm2examen`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contactos`
--

CREATE TABLE `contactos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `video_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `contactos`
--

INSERT INTO `contactos` (`id`, `nombre`, `lat`, `lng`, `video_url`, `created_at`) VALUES
(1, 'Alex Nain', 15.8106605, -87.9026321, NULL, '2025-11-03 08:37:27'),
(2, 'jdjdjhb', 15.8106605, -87.9026321, NULL, '2025-11-03 08:47:49'),
(3, 'alex', 494949, 46646, 'uploads/1762160840_vid_video.mp4', '2025-11-03 09:07:20'),
(4, 'djdjdj', 0, 0, NULL, '2025-11-03 09:07:56'),
(5, 'Alex Nain Mejia', 15.8106605, -87.9026323, 'uploads/1762161198_vid_video.mp4', '2025-11-03 09:13:18'),
(6, 'Alex Nain 07', 15.8106636, -87.9026332, 'uploads/1762161646_vid_video.mp4', '2025-11-03 09:20:46'),
(7, 'Victor', 15.8106681, -87.9026378, 'uploads/1762211135_vid_video.mp4', '2025-11-03 23:05:35'),
(8, 'udjdhdh', 15.8106713, -87.9026387, 'uploads/1762212850_vid_video.mp4', '2025-11-03 23:34:10'),
(9, 'meco', 15.8106764, -87.9026405, 'uploads/1762214285_vid_video.mp4', '2025-11-03 23:58:05'),
(10, 'Heasy', 15.8106656, -87.9026354, 'uploads/1762214726_vid_video.mp4', '2025-11-04 00:05:26');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `contactos`
--
ALTER TABLE `contactos`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `contactos`
--
ALTER TABLE `contactos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
