/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaproj.Model;

/**
 *
 * @author NICK
 */
public abstract class Item {
    protected String id;
    protected String name;
    protected String description;
    protected String category;
    protected double unitPrice;

    public Item(String id, String name, String description, double unitPrice, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getUnitPrice() { return unitPrice; }
    public String getCategory() { return category; }
    
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public void setCategory(String category) { this.category = category; }


    @Override
    public String toString() {
        return id + "," + name + "," + description + "," + unitPrice + "," + category;
    }
}







