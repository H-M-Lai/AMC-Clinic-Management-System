/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Parser;

import javaproj.Model.Service;

/**
 *
 * @author NICK
 */
public class ServiceParser extends LineParser<Service> {
    public Service parse(String line){
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Service object
        return new Service(tokens[0],tokens[1],tokens[2],Double.parseDouble(tokens[3]),tokens[4]);
    }
}
