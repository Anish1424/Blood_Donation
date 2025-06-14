-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: blood_bridge
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_requested_time` datetime(6) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'Sweety Acharjee','sweetyacharjee1212@gmail.com',NULL,'Purulia,Hura','$2a$10$uC0J9Kb.zBMDGPnIUTF4A.d5j7UcUpE5DQ0K8UbEQN.nI9gwIFcNm',NULL,NULL,NULL);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_request`
--

DROP TABLE IF EXISTS `blood_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `requester_type` varchar(255) DEFAULT NULL,
  `requester_id` bigint DEFAULT NULL,
  `patient_name` varchar(255) DEFAULT NULL,
  `patient_age` int DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `blood_group` varchar(255) DEFAULT NULL,
  `unit` int DEFAULT NULL,
  `prescription_path` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_request`
--

LOCK TABLES `blood_request` WRITE;
/*!40000 ALTER TABLE `blood_request` DISABLE KEYS */;
INSERT INTO `blood_request` VALUES (2,'Patient',1,'Sweety Acharjee',23,'Accident','O+',200,'96d2ed62-8560-4b44-9a6c-10501cdc5cbe_Hassprescription.jpg','2025-06-11','Approved'),(3,'Patient',1,'Ayesha Khatun ',23,'Hemophilia','O-',100,'b10fee3d-a6fc-4408-8821-832d71c03c79_Hassprescription.jpg','2025-06-11','Pending'),(4,'Donor',1,'Soma Acharjee',21,'Emergency','AB+',200,'907db753-caaf-4bad-a82c-2ec5b3770faa_Hassprescription.jpg','2025-06-11','Approved'),(5,'Patient',1,'Disha Mondal',25,'Need Blood Urgent','B-',56,'925d8160-8fa6-450d-8c0b-935a4367e321_Hassprescription.jpg','2025-06-11','Rejected'),(6,'Patient',1,'Sneha Mukherjee',25,'Urgent','O-',50,'3d6133dc-5c15-414e-9fe6-2a00d94eb68a_Hassprescription.jpg','2025-06-12','Pending'),(7,'Donor',1,'Rina Mondal ',30,'Need Blood','O+',60,'03f18f65-3f4d-4645-a1be-2884822f0c50_Hassprescription.jpg','2025-06-12','Approved'),(8,'Patient',1,'Misty Acharjee',23,'Need','B-',50,'cfa76c16-ffba-4b8f-af5d-ee6676cb5aec_Hassprescription.jpg','2025-06-12','Pending'),(9,'Donor',1,'Kritika Kumari',32,'Accident ','AB-',70,'7cdec198-7e62-4f06-9c1a-c4ed79dd12e2_Hassprescription.jpg','2025-06-12','Pending');
/*!40000 ALTER TABLE `blood_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blood_stock`
--

DROP TABLE IF EXISTS `blood_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_stock` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `blood_group` varchar(255) DEFAULT NULL,
  `unit` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_stock`
--

LOCK TABLES `blood_stock` WRITE;
/*!40000 ALTER TABLE `blood_stock` DISABLE KEYS */;
INSERT INTO `blood_stock` VALUES (3,'AB+',230),(4,'A+',150),(5,'O-',60),(6,'B-',1),(7,'A-',1),(8,'O+',5);
/*!40000 ALTER TABLE `blood_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donation`
--

DROP TABLE IF EXISTS `donation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `donor_id` bigint DEFAULT NULL,
  `age` int DEFAULT NULL,
  `disease` varchar(255) DEFAULT NULL,
  `blood_group` varchar(255) DEFAULT NULL,
  `unit` int DEFAULT NULL,
  `prescription_path` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `donor_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_donation_donor_id` (`donor_id`),
  CONSTRAINT `FK21pq3ymhhlhelfmnqjc51cliy` FOREIGN KEY (`donor_id`) REFERENCES `donor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donation`
--

LOCK TABLES `donation` WRITE;
/*!40000 ALTER TABLE `donation` DISABLE KEYS */;
INSERT INTO `donation` VALUES (1,12,23,'Nothing','B+',50,'C:/bloodbridge/uploads/1749627959588_Hassprescription.jpg','2025-06-11','Approved',NULL),(2,13,23,'Nothing','AB+',200,'1749629376680_Hassprescription.jpg','2025-06-11','Approved',NULL),(3,14,22,'Nothing','A+',100,'1749629667252_Hassprescription.jpg','2025-06-11','Approved',NULL),(4,13,23,'Nothing','O-',60,'1749632278005_Hassprescription.jpg','2025-06-11','Approved',NULL),(5,13,25,'no','AB+',30,'1749711992432_Hassprescription.jpg','2025-06-12','Approved',NULL);
/*!40000 ALTER TABLE `donation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donor`
--

DROP TABLE IF EXISTS `donor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `blood_group` varchar(255) DEFAULT NULL,
  `disease` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `prescription_path` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_requested_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donor`
--

LOCK TABLES `donor` WRITE;
/*!40000 ALTER TABLE `donor` DISABLE KEYS */;
INSERT INTO `donor` VALUES (12,'Disha Mondal','dishamondal1212@gmail.com','1234567891','kolkata',23,'B+','Nothing','$2a$10$6.RfbeAOcdQwndjpKLuVLezKM34a51j7pB1kkszj0g5ydmGJBGMFq',NULL,NULL,NULL),(13,'Sweety Acharjee','sweetyacharjee1212@gmail.com','8501234484','Purulia,Hura',25,'AB+','Nothing','$2a$10$oVc5KcBB2D0adZG16IPMXutki5gpA.M/rfzkwLpq4AFLE/vCmnPHG',NULL,NULL,NULL),(14,'Ayesha Khatun','ayesha123@gmail.com','999991234','kolkata',22,'O-','Nothing','$2a$10$oezH8XLziUiW89w/3ZOfj.pg9BMVQ24mGoCRkC9oL2M8V/8BDp2rK',NULL,NULL,NULL);
/*!40000 ALTER TABLE `donor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_type` varchar(255) DEFAULT NULL,
  `overall_experience` varchar(255) DEFAULT NULL,
  `comfort_level` varchar(255) DEFAULT NULL,
  `process_clarity` varchar(255) DEFAULT NULL,
  `future_donation` varchar(255) DEFAULT NULL,
  `suggestions` text,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (1,'Donor','It was a great experience','Yes, I felt very comfortable. ','Absolutely. ','Definitely! I would be happy to donate again. ','Nothing','2025-06-11 10:34:18'),(2,'Patient','I’m truly grateful','Yes, very comfortable.','Yes, everything was explained in a simple and clear way. ','Absolutely.','Consider adding real-time chat support for urgent queries. ','2025-06-11 10:37:29');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otp`
--

DROP TABLE IF EXISTS `otp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otp`
--

LOCK TABLES `otp` WRITE;
/*!40000 ALTER TABLE `otp` DISABLE KEYS */;
/*!40000 ALTER TABLE `otp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `blood_group` varchar(255) DEFAULT NULL,
  `disease` varchar(255) DEFAULT NULL,
  `doctor_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_requested_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'Sweety Acharjee','sweety1212@gmail.com','5678901234','Purulia',23,'O+','Accident','Dr. Randhir Sud','$2a$10$xEtmHW.geWV1PAPhxkE.BO6/l.cvY6unJk7dbPVeuymkEUz3KjjPK',NULL,NULL),(2,'Disha Mondal','disha123@gmail.com','6739262709','Kolkata',25,'B-','nothing','Dr. Sourav Kumar','$2a$10$2o6SjrnbtunQbcFcjJXc9.VYOTwtCy0aTVbonPRtSkesv6RqNx5gO',NULL,NULL);
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-12 17:30:51
