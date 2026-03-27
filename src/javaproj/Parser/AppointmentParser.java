package javaproj.Methods.Parser;
import javaproj.Model.Appointment;

public class AppointmentParser extends LineParser<Appointment>{

    @Override
    public Appointment parse(String line) {
        String[] tokens = splitline(line);
        // Split the text file line into parts and directly pass them into the Appointment constructor
        return new Appointment(
                tokens[0],
                tokens[1],
                tokens[2],
                tokens[3],
                tokens[4],
                tokens[5],
                tokens[6],
                // Converting status and type to enums (uppercase so it matches the enum names)
                Appointment.Status.valueOf(tokens[7].toUpperCase()),
                Appointment.Type.valueOf(tokens[8].toUpperCase())
        ); 
    }
}
