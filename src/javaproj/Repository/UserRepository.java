/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Repository;

import java.util.*;

import javaproj.Model.Role.User;
import javaproj.Methods.Parser.LineParser;
import javaproj.Utils.*;

/**
 *
 * @author NICK
 */
public class UserRepository implements Repository<User>{
    private final String filename;
    private final LineParser<? extends User> parser;
    
    public UserRepository(String filename, LineParser<? extends User> parser) {
        this.filename = filename;
        this.parser = parser;
    }
    
    @Override
    public List< User> findAll(){
        List<User> list = (List<User>) Utils.readFile(filename, parser);
        return list;
    }
    public Optional<User> findById(String id){
         return findAll().stream().filter(u -> u.getSystemId().equals(id)).findFirst();
    }
    public void saveAll(List<User> items){
        Utils.writeFile(filename, items);
    }

}
