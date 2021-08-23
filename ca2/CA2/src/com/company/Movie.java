package com.company;
// Our child class 'Movie' extends product, ie will inherit all of its values and methods
public class Movie extends Product {
    // Instantiating a variable which is unique to Movies
    private boolean is3d;
    // Movie constructor with no ID. We need this, as our database generates IDs. We don't necessarily need to add an ID to a movie instance.
    // One to many relationship between movies+boxsets and directors, they need a director id in their constructors
    public Movie(float rt, String t, int a, String dt, int did, boolean is3dTemp) {
        // We super (inherit) all these attributes from our 'Product' superclass
        // At this stage, we don't have an id, it hasn't yet been created in the database,
        // so we just pass a -1 instead to satisfy the constructor's requirements
        // The call to the super class will always be the first line in our constructor
        super(-1,rt,t,a,dt,did);
        // Specify a value for our is3d variable, which is unique to the Movie class
        this.is3d = is3dTemp;
    }

    // Constructor with id included
    public Movie(int id,float rt, String t, int a, String dt, int did,boolean is3dTemp) {
        super(id,rt,t,a,dt,did);
        this.is3d = is3dTemp;
    }

    // Constructor for only title, we use this when retrieving director info in the DirectorsTableGateway
    public Movie(String t){
        super(t);
    }
    

    // Overriding the methods accessed via our 'Report' interface. Our superclass implements this interface.
    // The child classes will have their own versions of this.
    @Override
    public void printDetailedReport() {
        System.out.println("******DETAILED MOVIE REPORT **********");
        System.out.println("ID : " + super.getId());
        System.out.println("Title : " + super.getTitle());
        System.out.println("Age rating : " + super.getAgeRating());
        System.out.println("Premiere Date : " + super.getPremiereDate());
        System.out.println("Director ID : " + super.getDirector_id());
        System.out.println("Is 3d : " + is3d);
        System.out.println("Running time : " + calcRunningTime() + " hours");
        System.out.println("**************************************");
    }

    @Override
    public void printSummary() {
        System.out.println("********* SUMMARY ***************");
        System.out.println("ID : " + super.getId());
        System.out.println("Title : " + super.getTitle());
        System.out.println("********************************");
    }

    // Getters and setters for Movie-specific variable, we have to do this as the superclass doesn't have methods for this
    public boolean getIs3d() {
        return is3d;
    }

    public void setIs3d(boolean is3d) {
        this.is3d = is3d;
    }

    @Override
    public String toString() {
        return super.toString() + "Movie{" +
                "is3d=" + is3d +
                '}' +
                "\n ******************";
    }

    //Leave runningTime as whatever has been provided. Will be a reasonable number for a single film.
    // Boxsets would generally have longer runningTimes, so we'll divide those by 60 to show in minutes (or fraction of an hour)
    @Override
    public double calcRunningTime(){
        return runningTime;
    }

}
