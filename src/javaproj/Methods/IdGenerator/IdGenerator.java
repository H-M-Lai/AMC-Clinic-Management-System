/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package javaproj.Methods.IdGenerator;

import java.util.List;

/**
 *
 * @author NICK
 */
public interface IdGenerator{
    String nextId(List<String> idList, String prefix);
}
