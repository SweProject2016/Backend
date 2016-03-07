-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 09, 2016 at 05:41 PM
-- Server version: 10.1.10-MariaDB
-- PHP Version: 7.0.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `swe_project`
--

-- Cleanup

DROP TABLE IF EXISTS `tbl_committee`;
DROP TABLE IF EXISTS `tbl_judgement`;
DROP TABLE IF EXISTS `tbl_results`;
DROP TABLE IF EXISTS `tbl_law_sector`;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_committee`
--

CREATE TABLE `tbl_committee` (
  `id` int(11) NOT NULL,
  `name` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_judgement`
--

CREATE TABLE `tbl_judgement` (
  `id` int(11) NOT NULL,
  `file_reference` varchar(100) NOT NULL,
  `committee` int(11) NOT NULL,
  `law_sector` int(11) NOT NULL,
  `date` date NOT NULL,
  `sentence` blob NOT NULL,
  `offence` blob NOT NULL,
  `page_rank` float NOT NULL,
  `pdf_filename` varchar(100) NOT NULL,
  `pdf_link` varchar(300) NOT NULL,
  `keywords` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_law_sector`
--

CREATE TABLE `tbl_law_sector` (
  `id` int(11) NOT NULL,
  `name` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_results`
--

CREATE TABLE `tbl_results` (
  `id` int(11) NOT NULL,
  `user_input` varchar(1000) NOT NULL,
  `picked_file` int(11) NOT NULL,
  `similarity` float NOT NULL,
  `date` date NOT NULL,
  `user_rating` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_committee`
--
ALTER TABLE `tbl_committee`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `tbl_judgement`
--
ALTER TABLE `tbl_judgement`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `file_reference` (`file_reference`),
  ADD KEY `committee` (`committee`),
  ADD KEY `law_sector` (`law_sector`);

--
-- Indexes for table `tbl_law_sector`
--
ALTER TABLE `tbl_law_sector`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `tbl_results`
--
ALTER TABLE `tbl_results`
  ADD PRIMARY KEY (`id`),
  ADD KEY `picked_file` (`picked_file`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_committee`
--
ALTER TABLE `tbl_committee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_judgement`
--
ALTER TABLE `tbl_judgement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_law_sector`
--
ALTER TABLE `tbl_law_sector`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_results`
--
ALTER TABLE `tbl_results`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_judgement`
--
ALTER TABLE `tbl_judgement`
  ADD CONSTRAINT `tbl_judgement_ibfk_1` FOREIGN KEY (`committee`) REFERENCES `tbl_committee` (`id`),
  ADD CONSTRAINT `tbl_judgement_ibfk_2` FOREIGN KEY (`law_sector`) REFERENCES `tbl_law_sector` (`id`);

--
-- Constraints for table `tbl_results`
--
ALTER TABLE `tbl_results`
  ADD CONSTRAINT `tbl_results_ibfk_1` FOREIGN KEY (`picked_file`) REFERENCES `tbl_judgement` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
