/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Model;

import java.util.*;

/**
 *
 * @author NICK
 */
public class Appointment {
    private String appointmentId,staffId,customerId,doctorId,date,time,note;
    private Status status;
    private Type type;
    
    public enum Status{SCHEDULED,COMPLETED,CANCELLED}
    public enum Type{FOLLOW_UP,CONSULTATION,CHECK_UP,PREVENTATIVE_CARE}

    public Appointment(String appointmentId, String staffId, String customerId, String doctorId, String date, String time, String note, Status status, Type type) {
        this.appointmentId = appointmentId;
        this.staffId = staffId;
        this.customerId = customerId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.note = note;
        this.status = status;
        this.type = type; 
    }
    public String getAppointmentId() {
        return appointmentId;
    }
    public String getStaffId() {
        return staffId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public String getDoctorId() {
        return doctorId;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getNote() {
        return note;
    }
    public Status getStatus() {
        return status;
    }
    public Type getType() {
        return type;
    }
    @Override
    public String toString() {
        return String.join(",",appointmentId,staffId,customerId,doctorId,date,time,note,status.toString(),type.toString()
        );
    }
   
    public boolean isChanged(Appointment other) {
    if (other == null) return true;
    return !Objects.equals(this.customerId, other.customerId)
        || !Objects.equals(this.doctorId, other.doctorId)
        || !Objects.equals(this.date, other.date)
        || !Objects.equals(this.time, other.time)
        || !Objects.equals(this.note, other.note)
        || this.status != other.status
        || this.type != other.type;
    }
    
    public void complete(){
        if (status == Status.CANCELLED) {
        throw new IllegalStateException("Cancelled appointment cannot be completed");
    }
        this.status = Status.COMPLETED;
    }
    public void cancel() {
        if (status == Status.COMPLETED) {
            throw new IllegalStateException("Completed appointment cannot be cancelled");
        }
        this.status = Status.CANCELLED;
    }
}
