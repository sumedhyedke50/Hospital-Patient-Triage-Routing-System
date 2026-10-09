package com.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.dao.interfac.DaoInter;
import com.entity.Hospital_patient_intake;
import com.get.connection.GetConnection;
import com.implement.MainOperations;

public class Main {
	
	
	 public static void main(String[] args) {

	        System.out.println("KIRAN ACADEMY - HOSPITAL PATIENT ROUTING");
	        System.out.println("----------------------------------------");

	        DaoInter dao = new MainOperations();

	        int totalPending = 0;
	        int criticalCount = 0;
	        int generalCount = 0;
	        int validationFailed = 0;
	        int skippedDuplicate = 0;
	        int successfullyProcessed = 0;

	        try (Connection conn = GetConnection.getConnection()) {

	            // One connection is used for the whole processing run.
	            // Each patient is committed or rolled back independently.
	            conn.setAutoCommit(false);

	            List<Hospital_patient_intake> patients =
	                    dao.getPendingPatients(conn);

	            totalPending = patients.size();

	            for (Hospital_patient_intake patient : patients) {

	                int patientId = patient.getPatient_id();

	                try {
	                    // Duplicate check
	                    if (dao.alreadyTransferred(patientId, conn)) {

	                        skippedDuplicate++;

	                        System.out.println(
	                                "Patient " + patientId +
	                                " -> SKIPPED -> Already processed");

	                        continue;
	                    }

	                    // Validation
	                    List<String> errors =
	                            dao.validatePatient(patient);

	                    if (!errors.isEmpty()) {

	                        validationFailed++;

	                        System.out.println(
	                                "Patient " + patientId +
	                                " -> FAILED -> " +
	                                String.join(", ", errors));

	                        continue;
	                    }

	                    // Business routing
	                    if (dao.isCritical(patient)) {

	                        dao.insertCriticalPatient(patient, conn);
	                        criticalCount++;

	                        System.out.println(
	                                "Patient " + patientId +
	                                " -> CRITICAL CARE -> SUCCESS");

	                    } else {

	                        dao.insertGeneralPatient(patient, conn);
	                        generalCount++;

	                        System.out.println(
	                                "Patient " + patientId +
	                                " -> GENERAL CARE -> SUCCESS");
	                    }

	                    // Update source only after destination insert succeeds.
	                    dao.markProcessed(patientId, conn);

	                    // Commit this patient's complete transaction.
	                    conn.commit();

	                    successfullyProcessed++;

	                } catch (SQLException e) {

	                    // Roll back only the current patient's transaction.
	                    try {
	                        conn.rollback();
	                    } catch (SQLException rollbackException) {
	                        e.addSuppressed(rollbackException);
	                    }

	                    System.out.println(
	                            "Patient " + patientId +
	                            " -> FAILED -> " + e.getMessage());
	                }
	            }

	        } catch (SQLException e) {

	            System.err.println(
	                    "Fatal database error: " + e.getMessage());
	        }

	        System.out.println();
	        System.out.println("PROCESSING SUMMARY");
	        System.out.println("----------------------------------------");
	        System.out.println("Total Pending Records : " + totalPending);
	        System.out.println("Critical Care         : " + criticalCount);
	        System.out.println("General Care          : " + generalCount);
	        System.out.println("Validation Failed     : " + validationFailed);
	        System.out.println("Skipped Duplicate     : " + skippedDuplicate);
	        System.out.println("Successfully Processed: " + successfullyProcessed);
	    }
	 

}
