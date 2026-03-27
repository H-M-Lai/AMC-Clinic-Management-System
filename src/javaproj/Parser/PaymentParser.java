package javaproj.Methods.Parser;
import javaproj.Model.Payment;

public class PaymentParser extends LineParser<Payment>{
    
    @Override
    public Payment parse(String line) {
        String[] tokens = splitline(line);
        // Split the CSV line into tokens, then build a Payment object
        // Convert numbers to double and strings to enums where required
        return new Payment(
                tokens[0],
                tokens[1],
                Double.parseDouble(tokens[2]),
                Payment.PaymentMethod.valueOf(tokens[3].toUpperCase()),
                tokens[4],
                tokens[5],
                Payment.Status.valueOf(tokens[6].toUpperCase())
        ); 
    }
}
