package javaproj.Methods.Parser;
import javaproj.Model.Role.Customer;

public class CustomerParser extends LineParser<Customer>{

    @Override
    public Customer parse(String line) {
        String[] tokens = splitline(line);
        // Split the text file line into parts and use them to create a Customer object
        return new Customer(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]); 
    }
}
