/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Methods.Parser;

import javaproj.Model.ChargeItems;

/**
 *
 * @author NICK
 */
public class ChargeItemParser extends LineParser<ChargeItems> {
    public ChargeItems parse(String line){
        String[] tokens = splitline(line);
        // Take the text file line, split it into tokens and build a ChargeItems object
        return new ChargeItems(tokens[0],
                tokens[1],
                tokens[2],
                tokens[3],
                // Convert string numbers into int or double 
                Integer.parseInt(tokens[4]),
                Double.parseDouble(tokens[5]),
                Double.parseDouble(tokens[6]),
                tokens[7]);
    }
}
