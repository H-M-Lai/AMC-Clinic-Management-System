/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Model;

import java.util.*;
import javaproj.Methods.Parser.FeedbackParser;
import javaproj.Utils.Utils;

/**
 *
 * @author NICK
 */
public class Feedback {
    private String feedbackId,appointmentId,userType,rating,comment1,comment2;

    public Feedback(String feedbackId, String appointmentId, String userType, String rating, String comment1, String comment2) {
        this.feedbackId = feedbackId;
        this.appointmentId = appointmentId;
        this.userType = userType;
        this.rating = rating;
        this.comment1 = comment1;
        this.comment2 = comment2;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    @Override
    public String toString() {
        return feedbackId + "," + appointmentId + "," + userType + "," + rating + "," + comment1 + "," + comment2 ;
    }
    
}
