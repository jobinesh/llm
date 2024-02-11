package com.jobinesh.ai.example.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.jobinesh.ai.example.model.IDGenerator.generateUUID;

public class Patient {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate bithDate;
    private Gender gender;
    private boolean active;
    private boolean deceased;
    private Date deceasedDateTime;

    public Patient(String firstName, String lastName, LocalDate bithDate, Gender gender, boolean active, boolean deceased, Date deceasedDateTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bithDate = bithDate;
        this.gender = gender;
        this.active = active;
        this.deceased = deceased;
        this.deceasedDateTime = deceasedDateTime;
        this.id = generateUUID();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBithDate() {
        return bithDate;
    }

    public void setBithDate(LocalDate bithDate) {
        this.bithDate = bithDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeceased() {
        return deceased;
    }

    public void setDeceased(boolean deceased) {
        this.deceased = deceased;
    }

    public Date getDeceasedDateTime() {
        return deceasedDateTime;
    }

    public void setDeceasedDateTime(Date deceasedDateTime) {
        this.deceasedDateTime = deceasedDateTime;
    }

}
