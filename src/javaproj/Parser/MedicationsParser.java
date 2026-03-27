package javaproj.Methods.Parser;

import javaproj.Model.Medication;

public class MedicationsParser extends LineParser<Medication> {
    public Medication parse(String line){
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Medication object
        return new Medication(tokens[0],tokens[1],tokens[2],Double.parseDouble(tokens[3]),tokens[4]);
    }
}
