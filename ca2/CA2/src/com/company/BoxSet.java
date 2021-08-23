package com.company;
// Methods and concepts here explained in more detail in Movie.java to avoid repetition of explanations

// Extends product to also have all the attributes of its parent
// Extends is just a keyword to let us know it will inherit from the Product class
public class BoxSet extends Product {
    // numSeries is unique to BoxSet
    private double numSeries;

    // Constructor with everything
    public BoxSet(int id,float rt, String t, int a, String dt,int did,double ns){
        super(id,rt,t,a,dt,did);
        this.numSeries = ns;
    }

    // Constructor with only an ID
    // At this stage, we don't have an id, it hasn't yet been created in the database,
    // so we just pass a -1 instead to satisfy the constructor's requirements
    public BoxSet( float rt, String t, int a, String dt,int did,double ns){
        super(-1,rt,t,a,dt,did);
        this.numSeries = ns;
    }

    // Constructor for only title, we use this when retrieving director info in the DirectorsTableGateway
    public BoxSet(String t){
        super(t);
    }

    @Override
    public void printDetailedReport() {
        System.out.println("******DETAILED BOXSET REPORT **********");
        System.out.println("ID : " + super.getId());
        System.out.println("Title : " + super.getTitle());
        System.out.println("Age rating : " + super.getAgeRating());
        System.out.println("Premiere Date : " + super.getPremiereDate());
        System.out.println("Director ID : " + super.getDirector_id());
        System.out.println("Number of series : " + numSeries);
        System.out.println("Running time : " + calcRunningTime());
        System.out.println("**************************************");
    }

    @Override
    public void printSummary() {
        System.out.println("********* SUMMARY ***************");
        System.out.println("ID : " + super.getId());
        System.out.println("Title : " + super.getTitle());
        System.out.println("********************************");
    }

    public double getNumSeries() {
        return numSeries;
    }

    public void setNumSeries(double numSeries) {
        this.numSeries = numSeries;
    }

    @Override
    //runningTime will be unreasonably long for a boxset of films, so calculate in hours rather than minutes.
    public double calcRunningTime(){
        // I don't want the value to have too many decimal places, so just getting a substring and changing back to double
        // Means runningTime will be a small number
        // Old version, didn't make much sense
//        return Double.parseDouble(String.valueOf(runningTime / 60).substring(0,3));

        // This will give us the number of minutes per series
        // Some of these would be ridiculously long doubles, so we convert to a String, and get a 2 decimal Substring following the decimal point
        String tempRT = String.valueOf(runningTime*60/numSeries);
        int indexOfDecimal = tempRT.indexOf('.');
        return Double.parseDouble(tempRT.substring(0,indexOfDecimal+2));
    }
}

