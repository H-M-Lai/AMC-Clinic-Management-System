package javaproj.Methods.Parser;
import javaproj.Model.Role.Manager;

public class ManagerParser extends LineParser<Manager>{
    
    @Override
    public Manager parse(String line) {
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Manager object
        return new Manager(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]); 
    }
}
