Hospital Patient Triage & Routing System 🏥An industrial Core Java, JDBC, and MySQL capstone project designed to automate hospital patient intake processing, validation, triage classification, and conditional routing.📋 Project OverviewThe Hospital Patient Triage & Routing System reads unprocessed patient intake records from a source database table (hospital_patient_intake), validates each record against strict clinical and data criteria, evaluates business routing rules based on triage scores and condition statuses, and securely distributes patients into either Critical Care or General Care destination tables using transaction management (commit/rollback).🛠️ Tech Stack & PrerequisitesProgramming Language: Core Java (JDK 8+)Database: MySQL Server / MySQL WorkbenchDatabase Connectivity: JDBC API (java.sql)Build / Structure: Standard Java Project / Maven-compatible structureDriver: MySQL Connector/J📂 Project Directory Structurecom.kiranacademy.hospital
│
├── com.dao.interfac
│   └── DaoInter.java               # DAO Interface defining data operations
│
├── com.entity
│   └── Hospital_patient_intake.java # Patient entity model with getters/setters/toString
│
├── com.get.connection
│   └── GetConnection.java          # Utility class for secure JDBC connection from properties
│
├── com.implement
│   └── MainOperations.java         # Core implementation of DAO interface (Validation, Triage, Routing)
│
└── com.main
    └── Main.java                   # Main execution entry point with transaction control & summary
🗄️ Database Setup (01_database.sql)Run the following SQL script in MySQL Workbench to set up your database and tables:CREATE DATABASE IF NOT EXISTS hospital_db;
USE hospital_db;

-- 1. Source Table: Patient Intake
CREATE TABLE IF NOT EXISTS hospital_patient_intake (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    disease VARCHAR(120) NOT NULL,
    admission_type VARCHAR(20) NOT NULL,
    condition_status VARCHAR(20) NOT NULL,
    triage_score INT NOT NULL,
    doctor_name VARCHAR(100) NOT NULL,
    admission_date DATE NOT NULL,
    mobile VARCHAR(15) NOT NULL,
    transfer_status VARCHAR(20) DEFAULT 'PENDING',
    processed_at DATETIME DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Destination Table: Critical Care
CREATE TABLE IF NOT EXISTS critical_care_patients (
    critical_id INT AUTO_INCREMENT PRIMARY KEY,
    source_patient_id INT UNIQUE NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    disease VARCHAR(120) NOT NULL,
    admission_type VARCHAR(20) NOT NULL,
    condition_status VARCHAR(20) NOT NULL,
    triage_score INT NOT NULL,
    doctor_name VARCHAR(100) NOT NULL,
    routed_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. Destination Table: General Care
CREATE TABLE IF NOT EXISTS general_care_patients (
    general_id INT AUTO_INCREMENT PRIMARY KEY,
    source_patient_id INT UNIQUE NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    disease VARCHAR(120) NOT NULL,
    admission_type VARCHAR(20) NOT NULL,
    condition_status VARCHAR(20) NOT NULL,
    triage_score INT NOT NULL,
    doctor_name VARCHAR(100) NOT NULL,
    routed_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
⚙️ Configuration (db_info.properties)Create a db_info.properties file inside your src/main/resources folder:driver_Class=com.mysql.cj.jdbc.Driver
databaseUrl=jdbc:mysql://localhost:3306/
databaseName=hospital_db
userName=root
password=your_mysql_password
📐 Core Business & Routing RulesCritical Routing Precedence: Critical routing rules take precedence over general routing.Routing Matrix:If condition_status = 'Critical' $\rightarrow$ Critical CareIf admission_type = 'Emergency' AND triage_score >= 7 $\rightarrow$ Critical CareIf condition_status = 'Moderate' AND triage_score >= 8 $\rightarrow$ Critical CareAll other valid records $\rightarrow$ General CareValidation Constraints:Patient name must be $\ge 3$ characters.Age must be between $0$ and $120$.Gender: Male, Female, or Other.Admission Type: Emergency or Regular.Condition Status: Critical, Moderate, or Stable.Triage Score: $1$ to $10$.Admission date cannot be in the future.Mobile number must be exactly 10 digits.Transfer status must be PENDING.🚀 Execution & Transaction SafetyPer-Record Transactions: setAutoCommit(false) is enabled. Each patient record is processed independently within its own transaction boundary. If an insertion or update fails for a record, only that record rolls back (conn.rollback()), allowing the application to securely process remaining valid records.Idempotency Check: Prevents duplicate transfers by verifying whether the source_patient_id already exists in either destination table.Status Update: The source table status updates to PROCESSED with a timestamp only after the destination table insertion successfully completes.📊 Sample Console OutputKIRAN ACADEMY - HOSPITAL PATIENT ROUTING
----------------------------------------
Patient 101 -> GENERAL CARE -> SUCCESS
Patient 102 -> CRITICAL CARE -> SUCCESS
Patient 103 -> FAILED -> Invalid triage score
Patient 104 -> CRITICAL CARE -> SUCCESS
Patient 105 -> SKIPPED -> Already processed

PROCESSING SUMMARY
----------------------------------------
Total Pending Records : 20
Critical Care         : 7
General Care          : 10
Validation Failed     : 2
Skipped Duplicate     : 1
Successfully Processed: 17
🔍 Workbench Verification QueriesTo verify data movement and status updates:-- Check source status breakdown
SELECT transfer_status, COUNT(*) 
FROM hospital_patient_intake 
GROUP BY transfer_status;

-- View routed critical patients
SELECT * FROM critical_care_patients;

-- View routed general patients
SELECT * FROM general_care_patients;
