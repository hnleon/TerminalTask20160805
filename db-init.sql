-- phpMyAdmin SQL Dump
-- version 4.3.8deb0.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 08, 2016 at 02:42 AM
-- Server version: 5.5.50-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `task20160805`
--

-- --------------------------------------------------------

--
-- Table structure for table `file_statistic`
--

CREATE TABLE IF NOT EXISTS `file_statistic` (
  `id` bigint(20) NOT NULL,
  `file_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `longest_word` int(11) NOT NULL,
  `shortest_word` int(11) NOT NULL,
  `average_word` int(11) NOT NULL,
  `line_length` int(11) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `line_statistic`
--

CREATE TABLE IF NOT EXISTS `line_statistic` (
  `id` bigint(20) NOT NULL,
  `file_id` bigint(20) NOT NULL,
  `longest_word` int(11) NOT NULL,
  `shortest_word` int(11) NOT NULL,
  `average_word` int(11) NOT NULL,
  `line_length` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `file_statistic`
--
ALTER TABLE `file_statistic`
  ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `file_name` (`file_name`);

--
-- Indexes for table `line_statistic`
--
ALTER TABLE `line_statistic`
  ADD PRIMARY KEY (`id`), ADD KEY `file_id` (`file_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `file_statistic`
--
ALTER TABLE `file_statistic`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `line_statistic`
--
ALTER TABLE `line_statistic`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `line_statistic`
--
ALTER TABLE `line_statistic`
ADD CONSTRAINT `line_statistic_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `file_statistic` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
