package com.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dao.interfac.DaoInter;
import com.entity.Hospital_patient_intake;

public class MainOperations implements DaoInter{
	 @Override
	    public List<Hospital_patient_intake> getPendingPatients(Connection conn)
	            throws SQLException {

	        List<Hospital_patient_intake> patients = new ArrayList<>();

	        String sql =
	                "SELECT patient_id, patient_name, age, gender, disease, " +
	                "admission_type, condition_status, triage_score, doctor_name, " +
	                "admission_date, mobile, transfer_status, processed_at, created_at " +
	                "FROM hospital_patient_intake " +
	                "WHERE transfer_status = ? " +
	                "ORDER BY patient_id";

	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setString(1, "PENDING");

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    patients.add(mapPatient(rs));
	                }
	            }
	        }

	        return patients;
	    }

	    
	    
	    private Hospital_patient_intake mapPatient(ResultSet rs)
	            throws SQLException {

	        return new Hospital_patient_intake(
	                rs.getInt("patient_id"),
	                rs.getString("patient_name"),
	                rs.getInt("age"),
	                rs.getString("gender"),
	                rs.getString("disease"),
	                rs.getString("admission_type"),
	                rs.getString("condition_status"),
	                rs.getInt("triage_score"),
	                rs.getString("doctor_name"),
	                rs.getDate("admission_date"),
	                rs.getString("mobile"),
	                rs.getString("transfer_status"),
	                rs.getTimestamp("processed_at"),
	                rs.getTimestamp("created_at")
	        );
	    }

	    @Override
	    public List<String> validatePatient(Hospital_patient_intake p) {

	        List<String> errors = new ArrayList<>();

	        if (p == null) {
	            errors.add("Patient object is null");
	            return errors;
	        }

	        if (p.getPatient_name() == null ||
	                p.getPatient_name().trim().length() < 3) {
	            errors.add("Invalid patient name");
	        }

	        if (p.getAge() < 0 || p.getAge() > 120) {
	            errors.add("Invalid age");
	        }

	        if (!isOneOf(p.getGender(), "Male", "Female", "Other")) {
	            errors.add("Invalid gender");
	        }

	        if (p.getDisease() == null ||
	                p.getDisease().trim().isEmpty()) {
	            errors.add("Disease is mandatory");
	        }

	        if (!isOneOf(p.getAdmission_type(), "Emergency", "Regular")) {
	            errors.add("Invalid admission type");
	        }

	        if (!isOneOf(
	                p.getCondition_status(),
	                "Critical", "Moderate", "Stable")) {
	            errors.add("Invalid condition status");
	        }

	        if (p.getTriage_score() < 1 || p.getTriage_score() > 10) {
	            errors.add("Invalid triage score");
	        }

	        if (p.getDoctor_name() == null ||
	                p.getDoctor_name().trim().isEmpty()) {
	            errors.add("Doctor name is mandatory");
	        }

	        if (p.getAdmission_date() == null) {
	            errors.add("Admission date is mandatory");
	        } else if (p.getAdmission_date().after(new Date(System.currentTimeMillis()))) {
	            errors.add("Admission date cannot be future date");
	        }

	        if (p.getMobile_number() == null ||
	                !p.getMobile_number().matches("\\d{10}")) {
	            errors.add("Invalid mobile number");
	        }

	        if (!"PENDING".equalsIgnoreCase(p.getTransfer_status())) {
	            errors.add("Transfer status must be PENDING");
	        }

	        return errors;
	    }

	    private boolean isOneOf(String value, String... allowed) {
	        if (value == null) {
	            return false;
	        }

	        for (String item : allowed) {
	            if (item.equalsIgnoreCase(value.trim())) {
	                return true;
	            }
	        }

	        return false;
	    }

	    @Override
	    public boolean isCritical(Hospital_patient_intake p) {

	        if ("Critical".equalsIgnoreCase(p.getCondition_status())) {
	            return true;
	        }

	        if ("Emergency".equalsIgnoreCase(p.getAdmission_type())
	                && p.getTriage_score() >= 7) {
	            return true;
	        }

	        return "Moderate".equalsIgnoreCase(p.getCondition_status())
	                && p.getTriage_score() >= 8;
	    }

	    @Override
	    public boolean alreadyTransferred(
	            int patientId,
	            Connection conn) throws SQLException {

	        String sql =
	                "SELECT 1 FROM critical_care_patients " +
	                "WHERE source_patient_id = ? " +
	                "UNION " +
	                "SELECT 1 FROM general_care_patients " +
	                "WHERE source_patient_id = ? " +
	                "LIMIT 1";

	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setInt(1, patientId);
	            ps.setInt(2, patientId);

	            try (ResultSet rs = ps.executeQuery()) {
	                return rs.next();
	            }
	        }
	    }

	    @Override
	    public void insertCriticalPatient(
	            Hospital_patient_intake p,
	            Connection conn) throws SQLException {

	        String sql =
	                "INSERT INTO critical_care_patients " +
	                "(source_patient_id, patient_name, age, disease, " +
	                "admission_type, condition_status, triage_score, " +
	                "doctor_name, routed_at) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

	        try (PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, p.getPatient_id());
	            ps.setString(2, p.getPatient_name());
	            ps.setInt(3, p.getAge());
	            ps.setString(4, p.getDisease());
	            ps.setString(5, p.getAdmission_type());
	            ps.setString(6, p.getCondition_status());
	            ps.setInt(7, p.getTriage_score());
	            ps.setString(8, p.getDoctor_name());

	            ps.executeUpdate();
	        }
	    }

	    @Override
	    public void insertGeneralPatient(
	            Hospital_patient_intake p,
	            Connection conn) throws SQLException {

	        String sql =
	                "INSERT INTO general_care_patients " +
	                "(source_patient_id, patient_name, age, disease, " +
	                "admission_type, condition_status, triage_score, " +
	                "doctor_name, routed_at) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

	        try (PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, p.getPatient_id());
	            ps.setString(2, p.getPatient_name());
	            ps.setInt(3, p.getAge());
	            ps.setString(4, p.getDisease());
	            ps.setString(5, p.getAdmission_type());
	            ps.setString(6, p.getCondition_status());
	            ps.setInt(7, p.getTriage_score());
	            ps.setString(8, p.getDoctor_name());

	            ps.executeUpdate();
	        }
	    }

	    @Override
	    public void markProcessed(
	            int patientId,
	            Connection conn) throws SQLException {

	        String sql =
	                "UPDATE hospital_patient_intake " +
	                "SET transfer_status = ?, processed_at = CURRENT_TIMESTAMP " +
	                "WHERE patient_id = ? AND transfer_status = ?";

	        try (PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setString(1, "PROCESSED");
	            ps.setInt(2, patientId);
	            ps.setString(3, "PENDING");

	            int updated = ps.executeUpdate();

	            if (updated != 1) {
	                throw new SQLException(
	                        "Source patient status was not updated for patient_id="
	                                + patientId);
	            }
	        }
	    }

}
