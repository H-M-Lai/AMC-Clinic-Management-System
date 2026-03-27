package javaproj.Methods.Parser;

import javaproj.Model.Service;

public class ServiceParser extends LineParser<Service> {
    public Service parse(String line){
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Service object
        return new Service(tokens[0],tokens[1],tokens[2],Double.parseDouble(tokens[3]),tokens[4]);
    }
}
