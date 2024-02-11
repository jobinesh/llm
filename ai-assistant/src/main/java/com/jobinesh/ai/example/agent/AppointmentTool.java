package com.jobinesh.ai.example.agent;

import com.jobinesh.ai.example.model.Appointment;
import com.jobinesh.ai.example.service.AppointmentService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentTool {
    private final AppointmentService appointmentService;

    public AppointmentTool(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Tool
    public List<Appointment> getAppointments() {
        return appointmentService.getAppointmentList();
    }
    @Tool
    public Appointment getAppointment(String id, String firstName, String lastName) {
        return appointmentService.getAppointment(id, firstName, lastName);
    }

    @Tool
    public void changeAppointment(String id, String firstName, String lastName, String date) {
        appointmentService.changeAppointment(id, firstName, lastName, date);
    }

    @Tool
    public void cancelAppointment(String id, String firstName, String lastName) {
        appointmentService.cancelAppointment(id, firstName, lastName);
    }
}
