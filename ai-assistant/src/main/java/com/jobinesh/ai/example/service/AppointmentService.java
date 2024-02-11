package com.jobinesh.ai.example.service;

import com.jobinesh.ai.example.model.Appointment;
import com.jobinesh.ai.example.model.AppointmentStatus;
import com.jobinesh.ai.example.model.Gender;
import com.jobinesh.ai.example.model.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    private List<Patient> patientList = new ArrayList<>();
    private List<Appointment> appointmentList = new ArrayList<>();

    public AppointmentService() {
        initData();
    }

    private void initData() {

        patientList.add(new Patient("Jobinesh", "Manakkattil", LocalDate.parse("2000-07-10"), Gender.MALE, true, true, new Date()));
        patientList.add(new Patient("Remya", "Jobinesh", LocalDate.parse("2001-04-02"), Gender.FEMALE, true, true,
                new Date()));
        appointmentList.add(new Appointment(patientList.get(0), "general", AppointmentStatus.CONFIRMED, "walkin", "Covid",
                LocalDateTime.now().plusDays(1),
                30, "Foster City", "Dr. Tony Lee"));
        appointmentList.add(new Appointment(patientList.get(1), "general",  AppointmentStatus.CONFIRMED, "walkin", "Covid",
                LocalDateTime.now().plusDays(2),
                30, "Foster City", "Dr. Anu Reddy"));

    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public Appointment getAppointment(String id, String patientFirstName, String patientLastName) {
        return appointmentList.stream()
                .filter(appointment -> appointment.getId().equalsIgnoreCase(id))
                .filter(b -> b.getPatient().getFirstName().equalsIgnoreCase(patientFirstName))
                .filter(b -> b.getPatient().getLastName().equalsIgnoreCase(patientLastName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
    }

    public void changeAppointment(String id, String firstName, String lastName, String newDate) {
        var appointment = getAppointment(id, firstName, lastName);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:MM");
        appointment.setAppointDate(LocalDateTime.parse(newDate,dateTimeFormatter));
    }

    public void cancelAppointment(String bookingNumber, String firstName, String lastName) {
        var appointment = getAppointment(bookingNumber, firstName, lastName);
        appointment.setStatus( AppointmentStatus.CANCELLED);
    }
}
