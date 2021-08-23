package com.company;

import java.util.ArrayList;
import java.util.List;
// This class doesn't inherit its values from the Product superclass
public class Director {
    // Declare attributes
    private int id;
    private String fname;
    private String lname;
    private List<Product> products;

    // Constructor for all values
    public Director(int id, String fname, String lname){
        this.id = id;
        this.fname = fname;
        this.lname = lname;

        products = new ArrayList<>();
    }

    // Constructor for only fname/lname
    public Director(String fname, String lname){
        this.fname = fname;
        this.lname = lname;

        products = new ArrayList<>();
    }


    // I don't use these methods, instead opting to use a series of SQL LEFT JOINS when
    // the user wishes to view related Products (Movies and BoxSets)
    // Product objects would have been passed in here if I had opted to use these methods
    public void addProductToDirector(Product p){
        products.add(p);
    }

    public void removeProductFromDirector(Product p){
        products.remove(p);
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public List <Product> getProducts() { return products; }

    public void setProducts(List <Product> products){
        this.products = products;
    }

    @Override
    public String toString() {
        return "Director" +
                " id: " + id +
                "\nfname: " + fname + '\'' +
                "\nlname: " + lname + '\'';
    }
}
