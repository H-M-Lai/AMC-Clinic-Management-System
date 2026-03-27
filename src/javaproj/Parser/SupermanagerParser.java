package javaproj.Methods.Parser;

import javaproj.Model.Role.Supermanager;
public class SupermanagerParser extends LineParser<Supermanager>{
    @Override
    public Supermanager parse(String line) {
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Supermanager object
        return new Supermanager(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]); 
    }
}
