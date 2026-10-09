package com.dao.interfac;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.entity.Hospital_patient_intake;

public interface DaoInter {
	

    List<Hospital_patient_intake> getPendingPatients(Connection conn)
            throws SQLException;

    List<String> validatePatient(Hospital_patient_intake patient);

    boolean isCritical(Hospital_patient_intake patient);

    boolean alreadyTransferred(int patientId, Connection conn)
            throws SQLException;

    void insertCriticalPatient(
            Hospital_patient_intake patient,
            Connection conn
    ) throws SQLException;

    void insertGeneralPatient(
            Hospital_patient_intake patient,
            Connection conn
    ) throws SQLException;

    void markProcessed(
            int patientId,
            Connection conn
    ) throws SQLException;
	

}
