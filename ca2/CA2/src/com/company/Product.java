package com.company;
import java.sql.Date;
// This is our superclass, BoxSet and Movie are its children, they inherit its attributes
// We use a superclass so child classes can inherit its attributes/methods and avoid duplication of effort on our part
// So in our case, we can say our super class contains data common to Movies and BoxSets,
// but Movies and BoxSets also implement their own data specific to those classes

// When a class contains abstract methods, the class itself will also have to be abstract
// We use abstract methods to share code among a few closely-related classes

// We implement our Report interface, and override all of its methods below
// We use our interface to hide the details of a method's implementation from a user, the method is just used instead, no detail involved
// Being abstract, Product cannot be instantiated
public abstract class Product implements Report {
    // Declare attributes
    private int id;
    protected double runningTime;
    private String title;
    private int ageRating;
    private Date premiereDate;
    private int director_id;

    // Constructor with all values
    public Product(int id,float rt, String t, int a, String dt,int did) {
        this.id = id;
        this.runningTime = rt;
        this.title = t;
        this.ageRating = a;
        this.premiereDate = Date.valueOf(dt);
        this.director_id = did;
    }

    // Constructor with only title
    public Product(String t) {
        this.title = t;
    }


    // Our Product superclass implements our Report interface,
    // so we instantiate these empty methods in here so our children (Movies, BoxSets) can access them
    @Override
    public void printDetailedReport() {

    }

    @Override
    public void printSummary() {

    }

    // Getter methods, we use these to retrieve the values from an instance object
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // An abstract method forces a subclass to implement it
    // The child classes will run an override on this function to implement their own version of it
    public abstract double calcRunningTime();

    public String getTitle() {
        return title;
    }

    // Setters used to set or change the values of our object
    public void setTitle(String title) {
        this.title = title;
    }

    public int getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(int ageRating) {
        this.ageRating = ageRating;
    }

    public Date getPremiereDate() {
        return premiereDate;
    }

    public void setPremiereDate(Date premiereDate) {
        this.premiereDate = premiereDate;
    }

    public int getDirector_id() {
        return director_id;
    }

    public void setDirector_id(int did) {
        this.director_id = did;
    }

    // Useful toString method to view a String representation of our object
    @Override
    public String toString() {
        return "Product: " +
                "id: " + id +
                "\n runningTime: " + runningTime +
                "\n title: '" + title + '\'' +
                "\n ageRating: " + ageRating +
                "\n premiereDate: " + premiereDate +
                "\n director_id: " + director_id;
    }
}
