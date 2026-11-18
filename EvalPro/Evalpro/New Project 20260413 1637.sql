-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.25a


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema studentmonitor
--

CREATE DATABASE IF NOT EXISTS studentmonitor;
USE studentmonitor;

--
-- Definition of table `mcqquetions`
--

DROP TABLE IF EXISTS `mcqquetions`;
CREATE TABLE `mcqquetions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `correct_answer` varchar(255) DEFAULT NULL,
  `optiona` varchar(255) DEFAULT NULL,
  `optionb` varchar(255) DEFAULT NULL,
  `optionc` varchar(255) DEFAULT NULL,
  `optiond` varchar(255) DEFAULT NULL,
  `question` varchar(255) DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mcqquetions`
--

/*!40000 ALTER TABLE `mcqquetions` DISABLE KEYS */;
INSERT INTO `mcqquetions` (`id`,`correct_answer`,`optiona`,`optionb`,`optionc`,`optiond`,`question`,`topic`,`department`) VALUES 
 (33,'B','To design buildings and bridges','To prepare maps and collect field data','To construct roads and canals','To manage construction teams','What is the primary purpose of surveying in civil engineering projects?','Surveying','Civil'),
 (34,'D','Total station','Theodolite','Leveling tools','All of the above','Which of the following instruments is used in surveying to collect accurate field data?','Surveying','Civil'),
 (35,'B','Architectural projects','Civil engineering projects','Mechanical engineering projects','Electrical engineering projects','What type of projects often rely on surveying for their foundation?','Surveying','Civil'),
 (36,'B','Construction plans','Maps and building designs','Budget reports','Scheduling timelines','What is one of the key outputs of the surveying process?','Surveying','Civil'),
 (37,'D','Theodolite','Total station','Leveling tool','Both A and B','Which instrument is used to measure angles and distances in surveying?','Surveying','Civil'),
 (38,'C','To measure distances','To measure angles','To determine elevations','To design buildings','What is the role of leveling tools in surveying?','Surveying','Civil'),
 (39,'B','It helps in designing the structures','It provides accurate field data for construction','It reduces construction costs','It speeds up the construction process','Why is surveying essential for setting out buildings, roads, and bridges?','Surveying','Civil'),
 (40,'C','Historical data','Environmental data','Accurate field data','Financial data','What type of data do surveyors collect using instruments like total stations and theodolites?','Surveying','Civil'),
 (41,'C','Reduced accuracy','Increased costs','Improved planning and design','Delayed construction','Which of the following is a benefit of using surveying in civil engineering projects?','Surveying','Civil'),
 (42,'B','Surveying is used to design maps','Surveying is used to prepare maps','Surveying is not related to map preparation','Map preparation is not a part of surveying','What is the relationship between surveying and map preparation?','Surveying','Civil'),
 (43,'B','To design buildings and bridges','To prepare maps and set out infrastructure','To manage construction costs','To supervise laborers','What is the primary purpose of surveying in civil engineering projects?','Surveying','Civil'),
 (44,'D','Total station','Theodolite','Leveling tools','Crane','Which of the following is NOT a common instrument used in surveying?','Surveying','Civil'),
 (45,'B','Laboratory data','Field data','Office data','Classroom data','What type of data is collected using surveying instruments?','Surveying','Civil'),
 (46,'B','Preparing architectural designs','Setting out buildings, roads, and bridges','Managing construction timelines','Supervising construction equipment','What is one of the key applications of surveying?','Surveying','Civil'),
 (47,'D','Leveling tool','Theodolite','Total station','Both B and C','Which instrument is used to measure angles and distances in surveying?','Surveying','Civil'),
 (48,'B','Geology','Surveying','Materials science','Structural analysis','What is the foundation of any civil engineering project?','Surveying','Civil'),
 (49,'A','Topographic maps','Political maps','Climate maps','All of the above','What type of maps are prepared using surveying techniques?','Surveying','Civil'),
 (50,'D','Increased labor costs','Improved accuracy','Reduced field time','Both B and C','Which of the following is a benefit of using total stations in surveying?','Surveying','Civil'),
 (51,'C','To measure distances','To measure angles','To determine elevations','To design buildings','What is the primary function of leveling tools in surveying?','Surveying','Civil'),
 (52,'C','To reduce construction costs','To improve architectural designs','To ensure accurate placement of infrastructure','To manage construction equipment','Why is surveying essential for civil engineering projects?','Surveying','Civil'),
 (53,'B','Very Large Scale Interface','Very Large Scale Integration','Very Low Scale Integration','Very Little Scale Integration','What does VLSI stand for in the context of integrated circuits?','VLSI','ECE'),
 (54,'B','To design and manufacture discrete transistors','To combine millions of transistors onto a single chip','To reduce the size of individual transistors','To increase the power consumption of ICs','What is the primary goal of VLSI technology?','VLSI','ECE'),
 (55,'C','Hundreds','Thousands','Millions or billions','Tens','How many transistors can be combined onto a single chip using VLSI technology?','VLSI','ECE'),
 (56,'D','Reduced circuit complexity','Increased circuit complexity','Decreased manufacturing cost','Improved circuit performance','What is the result of combining millions of transistors onto a single chip?','VLSI','ECE'),
 (57,'D','Analog circuits','Digital circuits','Integrated circuits','All of the above','What type of circuits are designed and manufactured using VLSI technology?','VLSI','ECE'),
 (58,'D','Low transistor density','High power consumption','Small chip size','High transistor density','What is a key characteristic of VLSI technology?','VLSI','ECE'),
 (59,'C','Increased size and weight','Reduced performance and efficiency','Improved performance and reduced size','Increased power consumption','What is the benefit of using VLSI technology in electronic devices?','VLSI','ECE'),
 (60,'C','Increased manufacturing cost','Reduced circuit complexity','Improved circuit reliability','All of the above','Which of the following is a consequence of VLSI technology?','VLSI','ECE'),
 (61,'C','VLSI technology reduces transistor count','VLSI technology has no effect on transistor count','VLSI technology increases transistor count','VLSI technology is independent of transistor count','What is the relationship between VLSI technology and transistor count?','VLSI','ECE'),
 (62,'C','It has reduced the demand for electronic devices','It has increased the complexity of electronic devices','It has enabled the development of smaller and more powerful devices','It has had no significant impact','What is the impact of VLSI technology on the electronics industry?','VLSI','ECE'),
 (63,'C','To amplify signals','To filter signals','To process and transmit information','To store data','What is the primary function of a signal system in electronics.','Signal Sistem','ECE'),
 (64,'C','Analog','Digital','Both A and B','None of the above','Which of the following is a type of signal.','Signal Sistem','ECE'),
 (65,'C','To increase the frequency of the signal','To decrease the amplitude of the signal','To encode information onto a carrier wave','To filter out noise','What is the purpose of modulation in a signal system.','Signal Sistem','ECE'),
 (66,'B','Analog is discrete, digital is continuous','Analog is continuous, digital is discrete','Analog is high-frequency, digital is low-frequency','Analog is low-frequency, digital is high-frequency','What is the difference between an analog and digital signal.','Signal Sistem','ECE'),
 (67,'A','Higher noise resistance','Lower bandwidth requirements','Greater signal attenuation','Increased signal distortion','Which of the following is a benefit of digital signals over analog signals.','Signal Sistem','ECE'),
 (68,'C','To receive signals','To process signals','To transmit signals over a medium','To store signals','What is the purpose of a signal transmitter.','Signal Sistem','ECE'),
 (69,'C','To transmit signals','To process signals','To convert received signals into usable information','To filter out noise','What is the function of a receiver in a signal system.','Signal Sistem','ECE'),
 (70,'D','Wire','Fiber optic','Wireless','All of the above','Which of the following is a type of signal transmission medium.','Signal Sistem','ECE'),
 (71,'C','To increase the amplitude of the signal','To decrease the frequency of the signal','To extract information from the signal','To store the signal','What is the purpose of signal processing in a signal system.','Signal Sistem','ECE'),
 (72,'D','Noise reduction','Signal amplification','Bandwidth management','All of the above','Which of the following is a challenge in signal system design.','Signal Sistem','ECE');
/*!40000 ALTER TABLE `mcqquetions` ENABLE KEYS */;


--
-- Definition of table `results`
--

DROP TABLE IF EXISTS `results`;
CREATE TABLE `results` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `exam_date` datetime DEFAULT NULL,
  `score` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `student_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfri0f5doafs6f1ob36de88b6a` (`student_id`),
  CONSTRAINT `FKfri0f5doafs6f1ob36de88b6a` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `results`
--

/*!40000 ALTER TABLE `results` DISABLE KEYS */;
INSERT INTO `results` (`id`,`exam_date`,`score`,`total`,`student_id`) VALUES 
 (5,'2026-03-01 11:52:10',1,10,1);
/*!40000 ALTER TABLE `results` ENABLE KEYS */;


--
-- Definition of table `staff`
--

DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `staff`
--

/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` (`id`,`email`,`mobile`,`name`,`password`,`role`,`department`) VALUES 
 (3,'mani@gmail.com','07338723231','Manikandan','22','STAFF','ECE'),
 (4,'thanush@gmail.com','2143235678','Thanush','tt11','STAFF','CIVIL');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;


--
-- Definition of table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `department` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `students`
--

/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` (`id`,`department`,`email`,`name`,`password`,`year`) VALUES 
 (1,'ECE','spiro@gmail.com','spiro','22','4'),
 (3,'Civil','sanjay@gmail.com','sanjay','33','3');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
