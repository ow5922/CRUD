-- Tworzenie tabeli clients zaiwrającej dane klientów
CREATE TABLE `clients` (
                           `cl_id` int NOT NULL AUTO_INCREMENT,
                           `cl_surname` char(50) NOT NULL,
                           `cl_firstname` char(50) DEFAULT NULL,
                           `date_of_birth` date DEFAULT NULL,
                           `pesel` bigint DEFAULT NULL,
                           `id_card` varchar(10) DEFAULT NULL,
                           PRIMARY KEY (`cl_id`),
                           UNIQUE KEY `idx_clients_cl_id` (`cl_id`)
);

-- Tworzenie tabeli operators zawierającej dane operatorów
CREATE TABLE `operators` (
                             `oper_type` varchar(1) NOT NULL,
                             `oper_name` varchar(20) NOT NULL COMMENT 'Login operatora',
                             `oper_password` varchar(255) NOT NULL,
                             UNIQUE KEY `idx_operators_oper_name` (`oper_name`)
)