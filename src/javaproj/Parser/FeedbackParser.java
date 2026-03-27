/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Parser;

import javaproj.Model.Feedback;

/**
 *
 * @author mayvi
 */
public class FeedbackParser extends LineParser<Feedback>{
    
    @Override
    public Feedback parse(String line) {
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Feedback object
        return new Feedback(
                tokens[0],
                tokens[1],
                tokens[2],
                tokens[3],
                tokens[4],
                tokens[5]
        ); 
    }
    
}
