package javaproj.Methods.Parser;
import javaproj.Model.Role.Doctor;

public class DoctorParser extends LineParser<Doctor>{

    @Override
    public Doctor parse(String line) {
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Doctor object
        return new Doctor(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6],tokens[7]); 
    }
}
