package com.company.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.company.BoxSet;
import com.company.Director;
import com.company.Movie;

import static java.lang.String.valueOf;

public class Input {
    // We call this class to read userInput from Main
    // This class is helpful to keep the Main clean and provide some abstraction from how user input actually works
    // We perform a lot of validation in here, making sure user input is of the right type and format
    public static Movie readMovie() {
        int dId;
        String title;
        int ageRating;
        String premiereDate;
        boolean is3d;
        float runningTime;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter title : ");
        String titleInput = keyboard.nextLine();
        // Using trim to remove trailing and leading spaces, make sure the value is between 1 and 255 chars for MySQL
        while(titleInput.trim().length() <= 0 || titleInput.trim().length() > 255){
            System.out.println("Title must be between 1 and 255 chars! Try again:");
            titleInput = keyboard.nextLine();
        } title = titleInput;

        System.out.print("Enter ageRating :\nOptions are 3, 7, 12, 18\n");
        // Defining a list of options, and make sure the user input matches one of them
        List<Integer> ageRatingOptions = new ArrayList<>(Arrays.asList(3,7,12,18));

        String ageRatingInput = keyboard.nextLine();
        // If input doesn't match a predefined option, or input is less than or equal to 0
        while(!ageRatingOptions.toString().contains(ageRatingInput) || ageRatingInput.length() <= 0){
            System.out.println("Options are 3,7,12,18 - Try again:");
            ageRatingInput = keyboard.nextLine();
        }ageRating = Integer.parseInt(ageRatingInput);
         /*
        Very specific regular expression to check our premiereDate input
         \d is a digit, to match any digit character (0-9)
         Our {4} is a quantifier, saying we're expecting 4 of the preceeding '\d' token
         With 0? we're saying we need there to be a 0 preceeding a number, for our month eg 2020-05-30
         OR it could start with a 1 be 10,11, or 12
         We then match our day with numbers preceeded by 0 up to 9,
         or preceeded by 1,2,or 3 with 30s ending in 0 or 1, finally * expects 0 or more or this expression
         */
        System.out.print("Enter premiere date - YYYY-MM-DD : ");
        while(!keyboard.hasNext("\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])*")){
            System.out.println("Pattern must match YYYY-MM-DD - Try again:");
            keyboard.nextLine();
        } premiereDate = keyboard.nextLine();

        System.out.print("Is 3D? true/false: ");
        // Making sure a boolean is entered
        while(!keyboard.hasNextBoolean()){
            System.out.println("Options are true/false - Try again:");
            keyboard.nextLine();
        } is3d = keyboard.nextBoolean();

        System.out.print("Enter runningTime : ");

        // Value must be of type float
        while(!keyboard.hasNextFloat()){
            System.out.println("Please enter a number:");
            keyboard.next();
        } runningTime = keyboard.nextFloat();

        // We don't do a check to see if director ID exists in the database here, that's done in Main instead
        // Here we just make sure we have an int
        System.out.print("Enter director ID : ");
        while(!keyboard.hasNextInt()){
            System.out.println("Please enter an integer:");
            keyboard.next();
        } dId = keyboard.nextInt();

        // Insert values into new Movie object and return it to wherever it was called from
        Movie m =
                new Movie(runningTime,title,ageRating,premiereDate,dId,is3d);
        return m;
    }

    public static BoxSet readBoxSet() {
        int dId;
        String title;
        int ageRating;
        String premiereDate;
        float runningTime;
        double numSeries;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter title : ");
        String titleInput = keyboard.nextLine();
        while(titleInput.trim().length() <= 0 || titleInput.trim().length() > 255){
            System.out.println("Title must be between 1 and 255 chars! Try again:");
            titleInput = keyboard.nextLine();
        } title = titleInput;

        System.out.print("Enter ageRating :\nOptions are 3, 7, 12, 18\n");
        List<Integer> ageRatingOptions = new ArrayList<>(Arrays.asList(3,7,12,18));

        String ageRatingInput = keyboard.nextLine();
        while(!ageRatingOptions.toString().contains(ageRatingInput)){
            System.out.println("Options are 3,7,12,18 - Try again:");
            ageRatingInput = keyboard.nextLine();
        }ageRating = Integer.parseInt(ageRatingInput);
        System.out.print("Enter premiere date - YYYY-MM-DD : ");
        while(!keyboard.hasNext("\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])*")){
            System.out.println("Pattern must match YYYY-MM-DD - Try again:");
            keyboard.next();
        } premiereDate = keyboard.nextLine();

        System.out.print("Number of series : ");
        while(!keyboard.hasNextDouble()){
            System.out.println("Please enter a double:");
            keyboard.next();
        }numSeries = keyboard.nextDouble();

        System.out.print("Enter runningTime : ");
        while(!keyboard.hasNextFloat()){
            System.out.println("Please enter a number:");
            keyboard.next();
        } runningTime = keyboard.nextFloat();

        System.out.print("Enter director ID : ");
        while(!keyboard.hasNextInt()){
            System.out.println("Please enter an integer:");
            keyboard.next();
        } dId = keyboard.nextInt();

        BoxSet b =
                new BoxSet(runningTime,title,ageRating,premiereDate,dId,numSeries);
        return b;
    }

    public static Director readDirector() {
        String fname;
        String lname;

        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter director first name : ");
        String fnameInput = keyboard.nextLine();
        while(fnameInput.trim().length() <= 0 || fnameInput.trim().length() > 255){
            System.out.println("Fname must be between 1 and 255 chars! Try again:");
            fnameInput = keyboard.nextLine();
        } fname = fnameInput;

        System.out.print("Enter director surname : ");
        String lnameInput = keyboard.nextLine();
        while(lnameInput.trim().length() <= 0 || lnameInput.trim().length() > 255){
            System.out.println("Lname must be between 1 and 255 chars! Try again:");
            lnameInput = keyboard.nextLine();
        } lname = lnameInput;


        Director d =
                new Director(fname,lname);
        return d;
    }
}
