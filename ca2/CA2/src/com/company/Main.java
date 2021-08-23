package com.company;
import com.company.utils.Input;

import java.util.*;
// Note, no use of polymorphism here - I have 3 separate database tables and so separate objects must be created
// Eg, could not declare Product p = new Boxset, a Movie or Boxset must be specifically declared
// We have three classes which are related by inheritance: Movie, BoxSet (children, inheriting from superclass) and Product (parent, superclass)
// A Movie *is a* Product, and a BoxSet *is a* product, so it would be possible for us to declare: Product p = new Boxset(constructor info) or Product p = new Movie(constructor info)
// In this way, polymorphism means our related objects may take many forms

// In the model-view-controller architecture, this class would (along with the gui) represent our View layer.
// This layer will display the model data, fetched from our controller (the gateways)
public class Main {
    //instantiate our model, static refers to an instance of the class rather than a particular object instance
    static Model model;
    //instantiate scanner, used to receive user input
    static Scanner keyboard;

    public static void main(String[] args) {
        //GUI immediately instantiated, just display it when the program runs
        GUI gui = new GUI();
        keyboard = new Scanner(System.in);
        /*
        The model represents the state of our application in the database.
        We're using it as the middle-man between Java and the database gateways.
        We pass objects to the model to retrieve data from our gateway and pass it back to our Main (view).
        The concept of the Model comes from the Model-View-Controller architecture.
         */
        model = Model.getInstance();
        int opt;

        //beginning of do...while loop, guaranteed to execute at least once, unlike a while loop, which may not run at all
        do {
            System.out.println("\n \u2B50 \u2728 Menu \u2728 \u2B50 ");
            System.out.println("1. View a record by ID \n2. Insert a record \n3. Delete a record \n4. Update a record \n5. Exit \n");
            opt = 0;

            System.out.println("\u25B6 Enter option:");
            // Our scanner can use 'hasNext' to read regular expressions.
            // Here '\b' is a word boundary to mark the beginning/end of our character. We use this to make sure the scanner has one of the options we're looking for.
            while(!keyboard.hasNext("\\b1\\b") && !keyboard.hasNext("\\b2\\b") && !keyboard.hasNext("\\b3\\b") && !keyboard.hasNext("\\b4\\b") && !keyboard.hasNext("\\b5\\b")){
                System.out.println("\u25B6 Enter option: - try again:");
                keyboard.next();
            }
            // Scanning the next input received as an int
            opt = keyboard.nextInt();

            switch (opt) {
                case 1 -> viewById();
                case 2 -> create();
                case 3 -> delete();
                case 4 -> update();
                // Note the absence of viewAll methods. I do have these, but removed them from the options as they are made redundant by the GUI, which is populated by the viewAll methods.
            }
        }
        while (opt != 5);
        System.out.println("Goodbye \uD83D\uDC4B");
    }


    private static void viewById() {
        System.out.print("Enter name of table to view from \n Options are:\n 1. Movies \n 2. Directors \n 3. Boxsets \n");
        // Instantiate our tableChoice string as empty, we'll fill it with user input then
        String tableChoice ="";

        // Array of strings, these are our table options
        String[] options = {"directors","movies","boxsets"};
        // We want to use the 'contains' operator on these values, so we convert to an arrayList
        // In this way, we can make certain the user has provided one of these three options, and nothing else. Could also have used a regular expression to check this.
        List<String> optionList = Arrays.asList(options);


        String choice = keyboard.nextLine();
        // This loop will keep running as long as the user hasn't provided one of our predetermined options
        while(!optionList.contains(choice)){
            System.out.println("\u25B6 Enter choice:");
            choice = keyboard.nextLine();
        } tableChoice = choice;

        //Now we have our tableChoice, and it definitely matches what we want. Continuing...
        System.out.println("Which " + tableChoice + " row are you viewing? Enter id:");

        // Our viewDirectorById function may be returning data from multiple tables, we'd need 3 object types, so instead I've stringified these objects.
        // Instantiate directorDetails to null, to be filled by the viewDirectorById function.
        String directorDetails = null;
        boolean viewAll;

        // if/else checks which value the user entered
        if(tableChoice.equals("directors")) {
            // true/false did we receive an int?
            boolean isInt;
            int dId = 0;

            // runs while we don't have an int
            do{
                if(keyboard.hasNextInt()){
                    dId = keyboard.nextInt();
                    // If we just received our int, this loop will end
                    isInt = true;
                }else{
                    // Otherwise, the scanner didn't receive an int and input was invalid
                    System.out.println("That's not a number! Try again:");
                    // Let the loop go again
                    isInt = false;
                    keyboard.next();
                }
            }while(!(isInt));

            // Now we have our int, but must check if a record for that ID even exists in the database
            while(model.viewDirectorById(dId,true).equals("")){
                System.out.println("Director does not exist, try again:");
                //If no record exists, get a new ID
                do{
                    // Again, must be of type int
                    if(keyboard.hasNextInt()){
                        dId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            }

            /*
            The DirectorsTableGateway's getDirectorById method will receive two parameters, a director ID and a boolean 'viewAll'.
            If we answer yes below here, viewAll is passed to the method as true and an SQL query using LEFT JOIN will run to retrieve data from
            all three tables (directors, movies, and boxsets).

            Otherwise, we will pass false and run a shorter SQL query just to retrieve director info.

            This is a 'one to many' method, calling on all 3 tables, utilising the one-to-many relationship implemented in the database to gather records related by their director_id
            */


            System.out.println("Do you also want to view the boxsets/movies for that director?");
                System.out.println("\u25B6 Enter choice: y/n");

                String[] viewOptions = {"y","n"};
                // Again we're creating an arrayList of options to use the 'contains' method to check our input. Could also use regex.
                List<String> viewOptionList = Arrays.asList(viewOptions);
                String view_choice = keyboard.nextLine();
                while(!viewOptionList.contains(view_choice)){
                    // While the user has entered anything other than 'y' or 'n', take input again
                    System.out.println("\u25B6 Enter choice:");
                    view_choice = keyboard.nextLine();
                }
                String view_choice_final = view_choice;

                // If 'y', pass true and retrieve records for that director, as well as info on boxsets and movies related to this director by their director_id foreign key
                if (view_choice_final.equals("y")) {
                    viewAll = true;

                    directorDetails = model.viewDirectorById(dId, viewAll);

                    // Check if viewDirectorById ran successfully
                    // Remember our viewDirectorById function will return our SQL result as a string, so if nothing is returned from the database we'll literally get a string with the value 'null'
                    // So rather than doing (directorDetails == null), we use the string operator 'equals'
                    if (directorDetails.equals("")) {
                        System.out.println("Something went wrong! That director may not exist.");
                    } else {
                        System.out.println("** PRINTING DIRECTOR DETAILS **\n\n" + directorDetails);
                    }
                    // If no, run a simpler SQL query, and just get the director's details alone
                } else if (view_choice_final.equals("n")) {
                    viewAll = false;

                    directorDetails = model.viewDirectorById(dId, viewAll);

                    if (directorDetails.equals("")) {
                        System.out.println("Something went wrong! That director may not exist.");
                    } else {
                        System.out.println("** PRINTING DIRECTOR DETAILS **\n\n" + directorDetails);
                    }
                }

        }
        else if(tableChoice.equals("movies")){

            boolean isInt;
            int mId = 0;

            // More error checking, make sure we have an int
            // This loop will run at least once, but always runs if isInt is false
            do{
                if(keyboard.hasNextInt()){
                    mId = keyboard.nextInt();
                    isInt = true;
                }else{
                    System.out.println("That's not a number! Try again:");
                    isInt = false;
                    keyboard.next();
                }
            }while(!(isInt));

            // Make sure a Movie record for this ID exists in the database
            while(model.getMovieById(mId) == null){
                System.out.println("Movie does not exist, try again:");
                do{
                    if(keyboard.hasNextInt()){
                        mId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        // If an int wasn't passed, ask again to make sure the user passes an int
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            // Movie must exist, so instantiate Movie m to the result of the getMovieById method with our chosen ID
            }Movie m = model.getMovieById(mId);


            System.out.println("Do you want to view all details, or just a summary? \n 1. Print summary \n 2. Print details \n 3. Return to main menu");
            int opt;
            do{
                // Making sure the user has selected one of our three options
                String[] mOptions = {"1","2","3"};
                List<String> mOptionList = Arrays.asList(mOptions);
                String line = keyboard.nextLine();

                opt = 0;
                while(!mOptionList.contains(line)){
                    // While input is invalid, keep prompting for one of our available choices
                    System.out.println("\u25B6 Enter choice:");
                    line = keyboard.next();
                }
                opt = Integer.parseInt(line);

                switch(opt){
                    /*
                    Switch case depending on the input received
                    printSummary() and printDetailedReport() are declared in the Report
                    This is an interface to be implemented in our child classes

                    We use @Override on these methods in the Product, and override
                    over the Product superclass in Movie and Boxset,
                    they each have their own printSummary() and printDetailedReport()
                    */
                    // Will just print id and title
                    // These are both like toString() methods
                    case 1->m.printSummary();
                    // Will print all values
                    case 2->m.printDetailedReport();
                }
            }while
            (opt != 3);
            System.out.println("Exiting");
        }
        else if(tableChoice.equals("boxsets")){
            boolean isInt;
            int bId = 0;

            do{
                if(keyboard.hasNextInt()){
                    bId = keyboard.nextInt();
                    isInt = true;
                }else{
                    System.out.println("That's not a number! Try again:");
                    isInt = false;
                    keyboard.next();
                }
            }while(!(isInt));

            while(model.viewBoxSetById(bId) == null){
                System.out.println("Boxset does not exist, try again:");
                do{
                    if(keyboard.hasNextInt()){
                        bId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            }BoxSet b = model.viewBoxSetById(bId);

            System.out.println("Do you want to view all details, or just a summary? \n 1. Print summary \n 2. Print details \n 3. Return to main menu");
            int opt;
            do{
                String[] mOptions = {"1","2","3"};
                List<String> mOptionList = Arrays.asList(mOptions);
                String line = keyboard.nextLine();

                opt = 0;
                while(!mOptionList.contains(line)){
                    System.out.println("\u25B6 Enter choice:");
                    line = keyboard.next();
                }
                opt = Integer.parseInt(line);

                switch(opt){
                    case 1->b.printSummary();
                    case 2->b.printDetailedReport();
                }
            }while
            (opt != 3);
            System.out.println("Exiting");
        }
    }


    public static void create(){
        System.out.print("Enter name of table to insert into \n Options are:\n 1. Movies \n 2. Directors \n 3. Boxsets \n");
        String tableChoice ="";

        // Our predefined possible answers, user must input a value equal to one of these
        String[] options = {"directors","movies","boxsets"};
        List<String> optionList = Arrays.asList(options);


        String choice = keyboard.nextLine();
        while(!optionList.contains(choice)){
            // u25B6 is an arrow symbol
            System.out.println("\u25B6 Enter choice:");
            choice = keyboard.nextLine();
        } tableChoice = choice;

        // Instantiate three empty objects for the three create methods
        BoxSet b;
        Movie m;
        Director d;

        if(tableChoice.equals("boxsets")) {
            // Receive our user input and perform error checking in the Input class, just to tidy up Main
            b = Input.readBoxSet();
            // Make sure our chosen director_id refers to an existing director
            while(model.viewDirectorById(b.getDirector_id(),true).equals("")){
                System.out.println("Director may not exist! Try a new id:");
                b.setDirector_id(keyboard.nextInt());
            }
            // This method is of type int. In our DB, our primary keys are auto-incremented. A method performing an insert will return generated keys,
            // including the ID. We return this generated ID.
            int resultId = model.createBoxSet(b);
            // Method is set to return -1 if it fails.
            if(resultId != -1){
                System.out.println("Success! Generated id: " + resultId);
            }else{
                System.out.println("Something went wrong!");
            }
        }else if(tableChoice.equals("movies")){
            m = Input.readMovie();
            while(model.viewDirectorById(m.getDirector_id(),true).equals("") || model.viewDirectorById(m.getDirector_id(),true).equals("")){
                System.out.println("Director may not exist! Try a new id:");
                m.setDirector_id(keyboard.nextInt());
            }

            int resultId = model.createMovie(m);
            if(resultId != -1){
                System.out.println("Success! Generated id: " + resultId);
            }else{
                System.out.println("Something went wrong!");
            }

        }else if(tableChoice.equals("directors")){
            d= Input.readDirector();
            int resultId = model.createDirector(d);
            if(resultId != -1){
                System.out.println("Success! Generated id: " + resultId);
            }else{
                System.out.println("Something went wrong!");
            }

        }
    }


    //delete functions
    public static void delete(){
        System.out.print("Enter name of table to delete from \n Options are:\n 1. Movies \n 2. Directors \n 3. Boxsets \n");
        String tableChoice ="";

        String[] options = {"directors","movies","boxsets"};
        List<String> optionList = Arrays.asList(options);


        String choice = keyboard.nextLine();
        while(!optionList.contains(choice)){
            System.out.println("\u25B6 Enter choice:");
            choice = keyboard.nextLine();
        } tableChoice = choice;

        System.out.println("Which " + tableChoice + " row are you deleting? Enter id:");

        // Error checking to make sure we've got a valid tableChoice,
        // the correct value type has been entered, and a record for that value exists in the DB
        if(tableChoice.equals("boxsets")){
            // I could easily loop through the available boxsets after calling viewBoxsets() and print a summary for each one, but that seemed like it would be redundant to me,
            // we already have the GUI open to view the available boxsets/movies/directors
            boolean isInt;
            int bId =0;
            do{
                if(keyboard.hasNextInt()){
                    bId = keyboard.nextInt();
                    isInt = true;
                }else{
                    System.out.println("That's not a number! Try again:");
                    isInt = false;
                    keyboard.next();
                }
            }while(!(isInt));

            while(model.viewBoxSetById(bId) ==null){
                System.out.println("Boxset does not exist, try again:");
                do{
                    if(keyboard.hasNextInt()){
                        bId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            }


            // This method returns a boolean. If success, print our message + the user's chosen boxset ID.
            if(model.deleteBoxset(bId)){
                System.out.println("Success: Deleted boxset " +bId);
            }else{
            // If false, print our error message.
                System.out.println("Something went wrong!");
            }
        }else if(tableChoice.equals("movies")){
            boolean isInt;
            int mId =0;
            do{
                if(keyboard.hasNextInt()){
                    mId = keyboard.nextInt();
                    isInt = true;
                }else{
                    System.out.println("That's not a number! Try again:");
                    isInt = false;
                    keyboard.next();
                }
            }while(!(isInt));

            while(model.getMovieById(mId) ==null){
                System.out.println("Movie does not exist, try again:");
                do{
                    if(keyboard.hasNextInt()){
                        mId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            }

            if(model.deleteMovie(mId)){
                System.out.println("Success: Deleted movie " +mId);
            }else{
                System.out.println("Something went wrong!");
            }
        }else if(tableChoice.equals("directors")){
            boolean isInt;
            int dId =0;
            do{
                if(keyboard.hasNextInt()){
                    dId = keyboard.nextInt();
                    isInt = true;
                }else{
                    System.out.println("That's not a number! Try again:");
                    isInt = false;
                    keyboard.next();
                }
            }while(!(isInt));

            while(model.viewDirectorById(dId, true).equals("")){
                System.out.println("Director does not exist, try again:");
                do{
                    if(keyboard.hasNextInt()){
                        dId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            }

            if(model.deleteDirector(dId)){
                System.out.println("Success: Deleted director " +dId);
            }else{
                System.out.println("Something went wrong!");
            }
        }
    }

    //update
    public static void update(){
        System.out.print("Enter name of table to update \n Options are:\n 1. Movies \n 2. Directors \n 3. Boxsets \n");
        // This is a 2D arraylist. Not entirely necessary, but wanted to try it.
        // We could have just passed two separate arrays instead, but it seems more concise to pass a single 2D array.
        List<List> arraylist2D = new ArrayList<List>();
        String tableChoice ="";

        String[] options = {"directors","movies","boxsets"};
        List<String> optionList = Arrays.asList(options);

        // Once again validating our table choice.
        String choice = keyboard.nextLine();
        // Loop runs while user has not entered a value contained within our optionList arrayList
        while(!optionList.contains(choice)){
            System.out.println("\u25B6 Enter choice:");
            choice = keyboard.nextLine();
        } tableChoice = choice;

        // We know our table choice is valid, so now which row does the user want?
        System.out.println("Which " + tableChoice + " row are you updating? Enter id:");
        while(!keyboard.hasNextInt()){
            //User must enter a number
            System.out.println("That's not a number! Try again:");
            keyboard.next();
        }
        int id = keyboard.nextInt();

        // Regardless of which table the user chose, we'll make sure the ID passed exists in the database
        if(tableChoice.equals("boxsets")){
            boolean isInt;
            int bId = 0;

            while(model.viewBoxSetById(bId) == null){
                // If ID does not exist in the database, get a new number from the user
                System.out.println("Boxset does not exist, try again:");
                do{
                    if(keyboard.hasNextInt()){
                        bId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            }id = bId;

        }else if(tableChoice.equals("movies")){
            boolean isInt;
            int mId = 0;

            while(model.getMovieById(mId) == null){
                // If ID does not exist in the database, get a new number from the user
                System.out.println("Movie does not exist, try again:");
                do{
                    if(keyboard.hasNextInt()){
                        mId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            }id = mId;
        }else if(tableChoice.equals("directors")){
            int dId = 0;
            boolean isInt;
            // Just pass true, makes no difference here, we just want to know if the director exists
            while(model.viewDirectorById(dId,true).equals("")){
                // If ID does not exist in the database, get a new number from the user
                System.out.println("Director does not exist, try again:");
                do{
                    if(keyboard.hasNextInt()){
                        dId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));
            }id = dId;

        }
        keyboard.nextLine();

        // At this point, we know the user has passed a valid tableName and a valid the row they want to update
        // We now get the user to enter the names of the columns they wish to update. It could be one, a few, or all.
        // Of course, different tables have different column names, so we'll validate the user's choices just below here...
        System.out.println("Enter columns to update...");
        if(tableChoice.equals("boxsets")) {
            System.out.println("**Column options are:**\n 1. runningTime \n 2. title \n 3. ageRating \n 4. premiereDate \n 5. numSeries \n 6. director_id \n Please list all values separated by spaces.");
        }else if(tableChoice.equals("movies")){
            System.out.println("**Column options are:**\n 1. runningTime \n 2. title \n 3. ageRating \n 4. premiereDate \n 5. is3D \n 6. director_id \n Please list all values separated by spaces.");
        }else if(tableChoice.equals("directors")){
            System.out.println("**Column options are:**\n 1. fname \n 2. lname \n Please list all values separated by spaces.");
        }

        // Read the user's column choices
        String columns = keyboard.nextLine();

        // Split the previously read string into an array, separate values by spaces
        // This works like the 'explode' method in PHP
        String[] strArray = columns.split(" ");


        // Instantiate an empty arrayList of type String
        List<String> columnArr = new ArrayList<String>();
        // Here's how we'll validate the user's column choices.
        // For whichever table the user has chosen, add the proper column names from the database to our 'columnArr' arrayList
        if(tableChoice.equals("boxsets")){
            columnArr.add("runningTime");
            columnArr.add("title");
            columnArr.add("ageRating");
            columnArr.add("premiereDate");
            columnArr.add("numSeries");
            columnArr.add("director_id");
        }else if(tableChoice.equals("movies")){
            columnArr.add("runningTime");
            columnArr.add("title");
            columnArr.add("ageRating");
            columnArr.add("premiereDate");
            columnArr.add("is3D");
            columnArr.add("director_id");
        }else if(tableChoice.equals("directors")){
            columnArr.add("fname");
            columnArr.add("lname");
        }

        // Loop through the values our user submitted
        for(int i=0; i< strArray.length; i++){
            // If the array of values submitted by our user contains any value that doesn't correspond with a value in our columnArr
            if(!columnArr.contains(strArray[i])){
                // The user must have given an invalid column name, get them to reenter the column names
                System.out.println("Value " + strArray[i] + " is an invalid column name! Re-enter column names:");
                columns = keyboard.nextLine();
                // Once again we split the user's submitted String into our strArray, and the loop will check against the proper column names again
                strArray = columns.split(" ");
            }
        }

        // Now we instantiate our first arraylist to be added to our 2 dimensional arrayList
        List list1=new ArrayList();

        // We now know our user has selected valid column names, so loop through the choices and add them to the list
        for(String str : strArray){
            list1.add(str);
        }

        // Instantiate our second list to be added to the 2 dimensional arrayList
        List list2=new ArrayList();
        // This String will be reused for every value to correspond with a column name
        String value = null;

        // This functionality is very similar to that found in Input.java,
        // but here I want to add values individually to an array, so returning a single object would be no good!
        // I need to access individual values rather than getting them all at once

        // Alternatively, it may have been just as good to use Input.readMovie/BoxSet/Director,
        // split the returned object into its components, and THEN insert these values into the array

        // Loop through all our columns and receive a value to correspond with that column name. We perform error checking for every input.
        // We make certain our SQL will work.
        for(int i = 0; i < list1.size(); i++)
        {   // Using toLowerCase() wasn't completely necessary, just to make typing the column names faster.
            // As we loop, we check what value we have for 'i' in our list1 (list of column names)
            // We'll never meet all of these conditions in one run, some are unique to particular tables
            if(list1.get(i).toString().toLowerCase().equals("title")){
                // Append the name of the column to the end of this println
                System.out.println("Enter value for column: " + list1.get(i));
                String titleInput = keyboard.nextLine();
                // trim() removes all leading and trailing spaces from the input, just in case
                // we make sure our title is between 1 and 255 chars, to make sure it's valid for our database
                while(titleInput.trim().length() <= 0 || titleInput.trim().length() > 255){
                    System.out.println("Title must be between 1 and 255 chars:");
                    titleInput = keyboard.nextLine();
                // We'll do this every time. Once we've got a valid input, set the 'value' String equal to our user input.
                // We add these values to our second list at the bottom of if statement.
                } value = titleInput;
            }else if(list1.get(i).toString().toLowerCase().equals("runningtime")){
                System.out.println("Enter value for column: " + list1.get(i));
                // Make sure the value for runningTime is a number
                while(!keyboard.hasNextFloat()){
                    System.out.println("Please enter a number:");
                    keyboard.next();
                } value = keyboard.next();
            }else if(list1.get(i).toString().toLowerCase().equals("agerating")){
                System.out.println("Enter value for column: " + list1.get(i));
                // These are our options for ageRating. We make sure the user's input matches one of these values.
                List<Integer> ageRatingOptions = new ArrayList<>(Arrays.asList(3,7,12,18));
                String ageRatingInput = keyboard.next();
                while(!ageRatingOptions.toString().contains(ageRatingInput)){
                    // While the user has entered any value not contained within ageRatingOptions, keep looping.
                    System.out.println("Options are 3,7,12,18 - Try again:");
                    ageRatingInput = keyboard.next();
                }value = ageRatingInput;
            }else if(list1.get(i).toString().toLowerCase().equals("premieredate")){
                System.out.println("Enter value for column: " + list1.get(i));
                 /*
                Very specific regular expression to check our premiereDate input
                 \d is a digit, to match any digit character (0-9)
                 Our {4} is a quantifier, saying we're expecting 4 of the preceeding '\d' token
                 With 0? we're saying we need there to be a 0 preceeding a number, for our month eg 2020-05-30
                 OR it could start with a 1 be 10,11, or 12
                 We then match our day with numbers preceeded by 0 up to 9,
                 or preceeded by 1,2,or 3 with 30s ending in 0 or 1, finally * expects 0 or more or this expression
                 */
                while(!keyboard.hasNext("\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])*")){
                    System.out.println("Pattern must match YYYY-MM-DD - Try again:");
                    keyboard.next();
                } value = keyboard.next();
            }else if(list1.get(i).toString().toLowerCase().equals("numseries")){
                System.out.println("Enter value for column: " + list1.get(i));
                // numSeries must be of type 'double'
                while(!keyboard.hasNextDouble()){
                    System.out.println("Please enter a double:");
                    keyboard.next();
                }value = keyboard.next();
            }else if(list1.get(i).toString().toLowerCase().equals("is3d")){
                System.out.println("Enter value for column: " + list1.get(i));
                // Is3d must be of type boolean
                while(!keyboard.hasNextBoolean()){
                    System.out.println("Options are true/false - Try again:");
                    keyboard.nextLine();
                    // Use regex to make sure the received value either matches 'true' or 'false' exactly.
                } if(keyboard.hasNext("\\btrue\\b")){
                    value = "1";
                }else if(keyboard.hasNext("\\bfalse\\b")){
                    value = "0";
                }
                // Performing more extensive checks on our director_id to make sure we receive a number and the ID is valid (exists in the database).
            }else if(list1.get(i).toString().toLowerCase().equals("director_id")){
                boolean isInt;
                int dId = 0;

                System.out.println("Enter value for column: " + list1.get(i));
                do{
                    if(keyboard.hasNextInt()){
                        dId = keyboard.nextInt();
                        isInt = true;
                    }else{
                        System.out.println("That's not a number! Try again:");
                        isInt = false;
                        keyboard.next();
                    }
                }while(!(isInt));

                while(model.viewDirectorById(dId,true).equals("")){
                    System.out.println("Director does not exist, try again:");
                    do{
                        if(keyboard.hasNextInt()){
                            dId = keyboard.nextInt();
                            isInt = true;
                        }else{
                            System.out.println("That's not a number! Try again:");
                            isInt = false;
                            keyboard.next();
                        }
                    }while(!(isInt));
                    // Remember our 2 dimensional arrayList is of type String, so everything passed must be of type String
                }value = String.valueOf(dId);

            }else if(list1.get(i).toString().toLowerCase().equals("fname")){
                System.out.println("Enter value for column: " + list1.get(i));
                String fnameInput = keyboard.nextLine();
                // Again making sure fname and lname are between 1 and 255 characters to suit our database.
                while(fnameInput.trim().length() <= 0 || fnameInput.trim().length() > 255){
                    System.out.println("Fname must be between 1 and 255 chars! Try again:");
                    fnameInput = keyboard.nextLine();
                } value = fnameInput;
            }else if(list1.get(i).toString().toLowerCase().equals("lname")){
                System.out.println("Enter value for column: " + list1.get(i));
                String lnameInput = keyboard.nextLine();
                while(lnameInput.trim().length() <= 0 || lnameInput.trim().length() > 255){
                    System.out.println("Lname must be between 1 and 255 chars! Try again:");
                    lnameInput = keyboard.nextLine();
                } value = lnameInput;
            }
            // For each increment of the loop, we'll add the value received to correspond to its column name to our second list
            list2.add(value);
        }

        // Finally, we add both of these arrayLists to our 2d arrayList
        arraylist2D.add(list1);
        arraylist2D.add(list2);

        // More String manipulation will take place in each table's respective gateway, but here we are simply running the method and returning success or failure...
        if(tableChoice.equals("boxsets")){
            if(model.updateBoxset(arraylist2D, id)){
                System.out.println("Success: Updated boxset " + id);
            }else{
                System.out.println("Something went wrong!");
            }
        }else if(tableChoice.equals("movies")){
            if(model.updateMovie(arraylist2D, id)){
                System.out.println("Success: Updated movie " + id);
            }else{
                System.out.println("Something went wrong!");
            }
        }else if(tableChoice.equals("directors")){
            if(model.updateDirector(arraylist2D, id)){
                System.out.println("Success: Updated director " + id);
            }else{
                System.out.println("Something went wrong!");
            }
        }
    }
}
