package javaproj.Methods.Parser;
import javaproj.Model.Role.Staff;

public class StaffParser extends LineParser<Staff>{

    @Override
    public Staff parse(String line) {
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Staff object
        return new Staff(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]); 
    }
}
