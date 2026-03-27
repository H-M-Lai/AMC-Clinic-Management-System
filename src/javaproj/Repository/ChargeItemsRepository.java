/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Repository;

import java.util.*;
import javaproj.Model.ChargeItems;
import javaproj.Methods.Parser.ChargeItemParser;
import javaproj.Methods.Parser.LineParser;
import javaproj.Utils.Utils;

/**
 *
 * @author NICK
 */
public class ChargeItemsRepository implements Repository<ChargeItems>{
    private final String filePath;
    private final LineParser<ChargeItems> parser;
    
    public ChargeItemsRepository() {
        this("data/chargeItems.txt", new ChargeItemParser());
    }
    
    public ChargeItemsRepository(String filePath, LineParser<ChargeItems> parser) {
        this.filePath = filePath;
        this.parser = parser;
    }
    
    @Override
    public List<ChargeItems> findAll(){
        List<ChargeItems> list = (List<ChargeItems>) Utils.readFile(filePath, parser);
        return list;
    }
    public Optional<ChargeItems> findById(String id){
         return findAll().stream().filter(u -> u.getChargeItemId().equals(id)).findFirst();
    }
    public void saveAll(List<ChargeItems> items){
        Utils.writeFile(filePath, items);
    }
}
