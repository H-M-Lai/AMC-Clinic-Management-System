/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package javaproj.Repository;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author NICK
 */
public interface Repository<T> {
    List<T> findAll() throws IOException;
    Optional<T> findById(String id) throws IOException;
    void saveAll(List<T> items) throws IOException;
}