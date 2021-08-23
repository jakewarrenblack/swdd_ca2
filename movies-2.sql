-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 26, 2021 at 09:38 PM
-- Server version: 10.4.16-MariaDB
-- PHP Version: 7.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `movies`
--

-- --------------------------------------------------------

--
-- Table structure for table `boxsets`
--

CREATE TABLE `boxsets` (
  `id` int(11) NOT NULL,
  `runningTime` int(11) NOT NULL,
  `title` varchar(256) NOT NULL,
  `ageRating` int(11) NOT NULL,
  `premiereDate` date NOT NULL,
  `numSeries` int(11) NOT NULL,
  `director_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `boxsets`
--

INSERT INTO `boxsets` (`id`, `runningTime`, `title`, `ageRating`, `premiereDate`, `numSeries`, `director_id`) VALUES
(1, 2, 'Ginny & Georgia', 7, '2018-06-20', 22, 1),
(2, 8, 'Adventures of fireman sam', 18, '2021-03-10', 4, 2),
(3, 8, 'Mad Max', 18, '1998-02-22', 14, 3),
(4, 22, 'The Crown', 12, '2021-03-02', 12, 4),
(5, 18, 'Vikings', 18, '2021-03-02', 2, 5),
(6, 50, 'Doctor Who Season 11', 12, '2018-11-20', 1, 6),
(7, 1, 'titanic the animated series', 12, '2020-05-22', 12, 7),
(8, 1, 'The Animated Adventures of Batman and Robin', 3, '1998-02-20', 12, 8),
(9, 1, 'Spongebob Squarepants Series 1-3', 7, '1999-08-22', 3, 9),
(11, 450, 'Blue Planet', 7, '2011-04-30', 12, 7),
(12, 343, 'Planet Earth', 7, '2011-02-20', 14, 9);

-- --------------------------------------------------------

--
-- Table structure for table `directors`
--

CREATE TABLE `directors` (
  `id` int(11) NOT NULL,
  `fname` varchar(256) NOT NULL,
  `lname` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `directors`
--

INSERT INTO `directors` (`id`, `fname`, `lname`) VALUES
(1, 'Fred', 'Scorsese'),
(2, 'Steven', 'Spielberg'),
(3, 'Alfred', 'Hitchcock'),
(4, 'Ridley', 'Scott'),
(5, 'Stanley', 'Kubrick'),
(6, 'William Wyler', 'Billy Wilder'),
(7, 'James', 'Hallison'),
(8, 'John', 'Dollison'),
(9, 'Joe', 'Bloggson'),
(11, 'Teddy', 'Roosevelt'),
(12, 'Michael', 'Martin');

-- --------------------------------------------------------

--
-- Table structure for table `movies`
--

CREATE TABLE `movies` (
  `id` int(11) NOT NULL,
  `runningTime` float NOT NULL,
  `title` varchar(255) NOT NULL,
  `ageRating` int(11) NOT NULL,
  `premiereDate` date NOT NULL,
  `is3D` tinyint(1) NOT NULL,
  `director_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `movies`
--

INSERT INTO `movies` (`id`, `runningTime`, `title`, `ageRating`, `premiereDate`, `is3D`, `director_id`) VALUES
(1, 2, 'Captain America', 3, '2020-05-30', 0, 2),
(2, 2, 'Transformers', 12, '2008-02-11', 1, 2),
(3, 4.5, 'Interstellar', 16, '2017-07-21', 1, 3),
(4, 3, 'Monsters University', 12, '2021-02-11', 1, 4),
(5, 2, 'Toy Story', 3, '1998-05-30', 1, 5),
(6, 3, 'Ratatouille', 7, '2012-02-22', 1, 6),
(7, 2, 'Titanic', 12, '2002-05-30', 0, 7),
(8, 3.5, 'Guardians of the Galaxy', 12, '2019-05-22', 1, 8),
(9, 4, 'Avengers Assemble', 12, '2021-04-30', 1, 9),
(10, 2.5, 'Talladega Nights: The Ballad of Ricky Bobby', 18, '2007-02-20', 0, 9),
(12, 3.5, 'Brokeback Mountain', 18, '2002-05-22', 0, 9),
(13, 3, 'The Incredibles', 7, '2008-05-20', 0, 8),
(14, 3.2, 'Zoolander', 18, '2009-05-20', 1, 8);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `boxsets`
--
ALTER TABLE `boxsets`
  ADD PRIMARY KEY (`id`),
  ADD KEY `director_id` (`director_id`);

--
-- Indexes for table `directors`
--
ALTER TABLE `directors`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`id`),
  ADD KEY `director_id` (`director_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `boxsets`
--
ALTER TABLE `boxsets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `directors`
--
ALTER TABLE `directors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `movies`
--
ALTER TABLE `movies`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `boxsets`
--
ALTER TABLE `boxsets`
  ADD CONSTRAINT `fk_boxset_director` FOREIGN KEY (`director_id`) REFERENCES `directors` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `movies`
--
ALTER TABLE `movies`
  ADD CONSTRAINT `fk_movie_director` FOREIGN KEY (`director_id`) REFERENCES `directors` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
