package com.entity;

import java.util.Date;

import com.google.protobuf.Timestamp;

public class Hospital_patient_intake {
	
	private int patient_id;
    private String patient_name;
    private int age;
    private String gender;
    private String disease;
    private String admission_type;
    private String condition_status;
    private int triage_score;
    private String doctor_name;
    private Date admission_date;
    private String mobile_number;
    private String transfer_status;
    private java.sql.Timestamp processed_at;
    private java.sql.Timestamp created_at;

    public Hospital_patient_intake() {
    }

    public Hospital_patient_intake(
            int patient_id,
            String patient_name,
            int age,
            String gender,
            String disease,
            String admission_type,
            String condition_status,
            int triage_score,
            String doctor_name,
            Date admission_date,
            String mobile_number,
            String transfer_status,
            java.sql.Timestamp timestamp,
            java.sql.Timestamp timestamp2) {

        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
        this.admission_type = admission_type;
        this.condition_status = condition_status;
        this.triage_score = triage_score;
        this.doctor_name = doctor_name;
        this.admission_date = admission_date;
        this.mobile_number = mobile_number;
        this.transfer_status = transfer_status;
        this.processed_at = timestamp;
        this.created_at = timestamp2;
    }

    public int getPatient_id() { return patient_id; }
    public void setPatient_id(int patient_id) { this.patient_id = patient_id; }

    public String getPatient_name() { return patient_name; }
    public void setPatient_name(String patient_name) { this.patient_name = patient_name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getAdmission_type() { return admission_type; }
    public void setAdmission_type(String admission_type) { this.admission_type = admission_type; }

    public String getCondition_status() { return condition_status; }
    public void setCondition_status(String condition_status) { this.condition_status = condition_status; }

    public int getTriage_score() { return triage_score; }
    public void setTriage_score(int triage_score) { this.triage_score = triage_score; }

    public String getDoctor_name() { return doctor_name; }
    public void setDoctor_name(String doctor_name) { this.doctor_name = doctor_name; }

    public Date getAdmission_date() { return admission_date; }
    public void setAdmission_date(Date admission_date) { this.admission_date = admission_date; }

    public String getMobile_number() { return mobile_number; }
    public void setMobile_number(String mobile_number) { this.mobile_number = mobile_number; }

    public String getTransfer_status() { return transfer_status; }
    public void setTransfer_status(String transfer_status) { this.transfer_status = transfer_status; }

    public java.sql.Timestamp getProcessed_at() { return processed_at; }
    public void setProcessed_at(java.sql.Timestamp processed_at) { this.processed_at = processed_at; }

    public java.sql.Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(java.sql.Timestamp created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "Hospital_patient_intake{" +
                "patient_id=" + patient_id +
                ", patient_name='" + patient_name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", disease='" + disease + '\'' +
                ", admission_type='" + admission_type + '\'' +
                ", condition_status='" + condition_status + '\'' +
                ", triage_score=" + triage_score +
                ", doctor_name='" + doctor_name + '\'' +
                ", admission_date=" + admission_date +
                ", mobile_number='" + mobile_number + '\'' +
                ", transfer_status='" + transfer_status + '\'' +
                '}';
    }

}
