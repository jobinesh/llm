package com.jobinesh.ai.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.jobinesh.ai.example.model.IDGenerator.generateUUID;

public class Appointment {
    private String id;
    private Patient patient;
    private String serviceCategory;
    private AppointmentStatus status;
    private String appointmentType;
    private String reason;
    private LocalDateTime appointDate;
    private int minutesDuration;
    private String address;
    private String providerName;

    public Appointment(Patient patient, String serviceCategory, AppointmentStatus status, String appointmentType, String reason, LocalDateTime appointDate, int minutesDuration, String address, String providerName) {
        this.patient = patient;
        this.serviceCategory = serviceCategory;
        this.status = status;
        this.appointmentType = appointmentType;
        this.reason = reason;
        this.appointDate = appointDate;
        this.minutesDuration = minutesDuration;
        this.address = address;
        this.providerName = providerName;
        this.id = generateUUID();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(LocalDateTime appointDate) {
        this.appointDate = appointDate;
    }

    public int getMinutesDuration() {
        return minutesDuration;
    }

    public void setMinutesDuration(int minutesDuration) {
        this.minutesDuration = minutesDuration;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

}
