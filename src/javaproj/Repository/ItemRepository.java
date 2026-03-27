/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Repository;

import java.util.List;
import java.util.Optional;
import javaproj.Model.Item;
import javaproj.Methods.Parser.LineParser;
import javaproj.Utils.Utils;

/**
 *
 * @author NICK
 */
public class ItemRepository<T extends Item> implements Repository<T>{
        private final String filePath;
    private final LineParser<T> parser;
    
     public ItemRepository(String filePath, LineParser<T> parser) {
        this.filePath = filePath;
        this.parser = parser;
    }
    
    public List<T> findAll(){
        return Utils.readFile(filePath, parser);
    }
    public Optional<T> findById(String id){
        return findAll().stream().filter(u -> u.getId().equals(id)).findFirst();
    }
    public void saveAll(List<T> items){
        Utils.writeFile(filePath, items);
    }

}