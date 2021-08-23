
package com.company.database;

import com.company.BoxSet;
import com.company.Director;
import com.company.Model;
import com.company.Movie;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//Defining our column names from the database.
public class DirectorsTableGateway {
    private static final String TABLE_NAME = "directors";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FNAME = "fname";
    private static final String COLUMN_LNAME = "lname";
    //Instantiate a connection object.
    private Connection mConnection;
    static Scanner keyboard;
    static Model model;

    public DirectorsTableGateway(Connection connection) {
        this.mConnection = connection;
    }

    //Return type of this method is an arraylist.
    public List<Director> getDirectors() {
        List<Director> directors = new ArrayList();
        String query = "SELECT * FROM directors";


        try {
            Statement stmt = this.mConnection.createStatement();
            //A ResultSet is a table of data which represents the data returned from our database.
            ResultSet rs = stmt.executeQuery(query); //We define our ResultSet as the result of our query.

            //This will loop through our ResultSet while there are still values that haven't been passed.
            while(rs.next()) {
                int id = rs.getInt("id");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                //Create a new movie Director for each result of our ResultSet, grab the values from the column in each case and drop them into our variables.
                Director d = new Director(id,fname,lname);
                //For each item in the ResultSet, we've created a Director instance and now we'll append this onto our directors arraylist.
                directors.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in DirectorsTableGateway : getDirectors(), Check the SQL you have created to see where your error is", ex);
        }

        //When finished, return our directors arraylist to the Model, which will return the directors arraylist to the Main.
        return directors;
    }


    //get director by id
    public String getDirectorById(int id, boolean bool) {
        Director d = null;
        Movie m = null;
        BoxSet b = null;
        String concat = "";
        String dString = null;
        String query;

        //if user has chosen to view movies and boxsets related to this director as well as the director info itself
        if(bool){
            // This is a flawed SQL query. It returns the data we want, but with duplicates, but this data will be passed to Main as a String,
            // so we'll fix this before passing...
            // notice the use of an alias, as 'Movie Title' or as 'Boxset Title', this is to help us differentiate between them,
            // as they are actually both just called title in their tables
            // LEFT JOIN will return records only where there is a match to correspond to a value on the left table, so we left join in each case
            // prior to this, I was just using an ordinary JOIN, which was returning seemingly random values
            query = "SELECT\n" +
                    "directors.id,\n" +
                    "  directors.fname,\n" +
                    "  directors.lname,\n" +
                    "  movies.title as 'Movie Title',\n" +
                    "  boxsets.title as 'Boxset Title'\n" +
                    "FROM directors\n" +
                    "LEFT JOIN movies\n" +
                    "  ON directors.id = movies.director_id\n" +
                    "LEFT JOIN boxsets\n" +
                    "  ON directors.id = boxsets.director_id\n" +
                    "  where directors.id = " + id;

            try {
                Statement stmt = this.mConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {
                    // Our resultSet will contain these values, we'll pull them out and insert them into variables
                    id = rs.getInt("id");
                    String fname = rs.getString("fname");
                    String lname = rs.getString("lname");
                    String mtitle = rs.getString("Movie title");
                    String btitle = rs.getString("Boxset title");
                    // Now we make objects with the values we received from the database
                    d = new Director(id,fname,lname);
                    b = new BoxSet(btitle);
                    m = new Movie(mtitle);

                    String boxsetTitle = null;
                    String movieTitle = null;

                    // Maybe there are no movies or boxsets, so we declare as null and do this check just in case
                    if(b.getTitle() != null){
                        boxsetTitle = b.getTitle();
                    }

                    if(m.getTitle() != null){
                        movieTitle = m.getTitle();
                    }

                    // Stringify our director info
                    dString = d.toString();
                    // We had an empty string at the top, now as it runs it will add the boxset and movie info for each row
                    concat += "\nBoxset title:" + boxsetTitle + "\nMovie title:" + movieTitle;
                }
            } catch (SQLException ex) {
                System.out.println("DirectorsTableGateway Line 39: " + ex);
            }
        }else{
            // If the user has bassed viewAll as false, only select the director info, no movies or boxsets
            query = "SELECT * FROM directors WHERE id = " + id;
            try {
                Statement stmt = this.mConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {
                    id = rs.getInt("id");
                    String fname = rs.getString("fname");
                    String lname = rs.getString("lname");
                    d = new Director(id,fname,lname);
                }
                dString = d.toString();
            } catch (SQLException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in DirectorsTableGateway : getDirectorById(), Check the SQL you have created to see where your error is", ex);
            }
        }
        // Split our results into an array, separate the values at every newline
        String[] concatArr = concat.trim().split("\n");
        // Convert to an arraylist so our LinkedHashSet can use it
        List<String> concatList = Arrays.asList(concatArr);

        // LinkedHashSet can contain each element only once, and in the same order as our list
        // This is a workaround for our SQL, which returns the correct data but with duplicates
        LinkedHashSet concathashSet = new LinkedHashSet(new LinkedHashSet<String>(concatList));
        // toString() will leave square brackets...remove them, just as we do in update director
        concat = concathashSet.toString().trim().replaceAll("\\[|\\]", "");
        // At this point, concat has no linebreaks, we'll replace all commas with linebreaks for nice formatting
        String concat_newLines = concat.trim().replaceAll(", ","\n");
        // Add our director info and the child table info
        // Remove leading quote from director fname and lname
        if(dString != null) {
            concat = dString.replaceAll("'", "") + "\n\n**Movies/Boxsets for this Director:**\n\n" + concat_newLines;
        }
        return concat;
    }

    public int insertDirector(Director d)  {
        String query;
        PreparedStatement stmt;
        int numRowsAffected;
        int generatedId;

        query = "INSERT INTO `directors`(`fname`, `lname`) " +
                "VALUES (?,?" +
                ")";
        try {
            // create a PreparedStatement object to execute the query and insert the values into the query
            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, d.getFname());
            stmt.setString(2, d.getLname());

            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                // if one row was inserted, retrieve the id that was assigned to that row in the database and return it
                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();
                generatedId = keys.getInt(1);
                return generatedId;
            }
        }
        catch (SQLException e)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in DirectorsTableGateway : insertDirector(), Check the SQL you have created to see where your error is", e);
        }
        // Just return -1 on failure, we'll check this in Main
        return -1;
    }

    public boolean deleteDirector(int id){
        // We're going to receive user input here
        // We've added the ability to delete the movies and boxsets that are referenced by this director's director_id
        // We can't delete a director which refers to a movie or boxset, a foreign key constraint would fail
        keyboard = new Scanner(System.in);
        // We'll access the model to use the methods to get movies and boxsets
        model = Model.getInstance();
        String query;
        PreparedStatement stmt;
        // We'll insert some movies and boxsets into these arrayLists
        List<BoxSet> boxsets = new ArrayList();
        List<Movie> movies = new ArrayList();

        int numRowsAffected;

        // Queries to select all boxsets/movies referred to by this director_id
        String boxSetselection = "SELECT * FROM boxsets WHERE director_id ="+id;
        String movieSelection = "SELECT * FROM movies WHERE director_id ="+id;

        // We have two try/catch blocks to execute both of these queries
        try {
            Statement boxsetSelectStmt = this.mConnection.createStatement();
            ResultSet rs = boxsetSelectStmt.executeQuery(boxSetselection);

            while(rs.next()) {
                int bId = rs.getInt("id");
                int runningTime = rs.getInt("runningTime");
                String title = rs.getString("title");
                int ageRating = rs.getInt("ageRating");
                String premiereDate = rs.getString("premiereDate");
                int numSeries = rs.getInt("numSeries");
                int director_id = rs.getInt("director_id");
                BoxSet b = new BoxSet(bId,runningTime,title,ageRating,premiereDate,numSeries,director_id);
                // Add boxset info to our arrayList
                boxsets.add(b);
            }
        } catch (SQLException ex) {
            System.out.println("DirectorsTableGateway Line 39: " + ex);
        }

        try {
            Statement movieSelectStmt = this.mConnection.createStatement();
            ResultSet rs = movieSelectStmt.executeQuery(movieSelection);

            while(rs.next()) {
                int mId = rs.getInt("id");
                String title = rs.getString("title");
                String premiereDate = rs.getString("premiereDate");
                int runningTime = rs.getInt("runningTime");
                int ageRating = rs.getInt("ageRating");
                boolean is3d = rs.getBoolean("is3D");
                int director_id = rs.getInt("director_id");
                Movie m = new Movie(mId, runningTime, title, ageRating, premiereDate,director_id, is3d);
                // Add movie info to our arrayList
                movies.add(m);
            }
        } catch (SQLException ex) {
            System.out.println("DirectorsTableGateway Line 39: " + ex);
        }

        // If we've retrieved at least 1 movie or at least 1 boxset, let the user know they'll need to delete these to proceed with deleting a director
        if(boxsets.size() > 0 || movies.size() > 0) {
            // u26A0 is a 'warning' emoji
            System.out.println("\u26A0 Warning! \u26A0 \nThis director references the following movies and/or boxsets:\n");
            // Loop through the retrieved boxsets and movies, printing their IDs and titles
            // We want to let the user know which boxsets and movies this director is referring to
            for (BoxSet boxSet : boxsets) {
                boxSet.printSummary();
                System.out.println("\n");
            }
            for (Movie movie : movies) {
                movie.printSummary();
                System.out.println("\n");
            }
            // Receive user input, do they want to delete these movies and boxsets too?
            System.out.println("************\n Do you want to delete these movies/boxsets too?");

            System.out.print("Enter option: \n 1. y/n \n");

            // User must only enter y or n
            String[] viewOptions = {"y","n"};
            List<String> viewOptionList = Arrays.asList(viewOptions);
            String view_choice = keyboard.nextLine();
            // Loop while the value received from the user is not 'y' or 'n'
            while(!viewOptionList.contains(view_choice)){
                System.out.println("Enter choice:");
                view_choice = keyboard.nextLine();
            }
            String view_choice_final = view_choice;

            if(view_choice_final.toLowerCase().equals("y")){
                // If the user has answered yes, loop through the boxsets and movies, get their IDs using the getter methods and delete them
                for (BoxSet boxSet : boxsets) {
                    model.deleteBoxset(boxSet.getId());
                }
                for (Movie movie : movies) {
                    model.deleteMovie(movie.getId());
                }
            //If the user has answered no, print a message to let them know this director cannot be deleted and return to the menu
            }else if(view_choice_final.toLowerCase().equals("n")){
                System.out.println("Exiting, cannot delete this director. A foreign key constraint would fail.");
                return false;
            // If anything else was somehow entered
            }else{
                System.out.println("Bad input. Exiting.");
                return false;
            }
        }

        // We will only reach this point if the user has agreed to delete the movies and boxsets the director refers to,
        // so now we may finally delete this director
        query = "DELETE FROM directors WHERE id = "+id;

        try {
            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                return true;
            }
        }
        catch (SQLException e)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in DirectorsTableGateway : deleteDirector(), Check the SQL you have created to see where your error is", e);
        }
        return false;
    };

    // Passing a 2d arraylist containing column names and values to correspond with them, and an id
    public boolean updateDirector(List<List> arraylist2D, int id){
        String query;
        PreparedStatement stmt;
        int numRowsAffected;

        // We will split our 2d arrayList into individual arrayLists
        List columns;
        List values;

        // First index is columns, second index is values
        columns = arraylist2D.get(0);
        values = arraylist2D.get(1);
        List<String> list = new ArrayList<>();

        // We could loop through either columns or values, they will always have the same length, we make sure of this in main
        for(var i=0; i<columns.size(); i++){
            System.out.println(columns.get(i));
            // Loop and add values one next to the other, eg we'll add: title, 'title value'
            list.add(columns.get(i).toString());
            list.add(values.get(i).toString());
        }

        List<String> result = new ArrayList<>();

        // Now we'll place each column together with its value in every index, eg we'll now have "title='title value'"
        for(var i=0; i<list.size();i+=2){
            // I always append single quotes around the values,
            // Numbers can take single quotes in mysql, and string values need quotes, so formatting will be correct
            result.add(list.get(i)+("=" + "'" + list.get(i+1) + "'"));
        }

        // Now stringify our columns and values
        String columns_values = result.toString();
        // Remove square brackets which will be left over from the arrayList
        String column_values_final = columns_values.toString().replaceAll("\\[|\\]", "");
        // Syntax of column_values_final is now correct and ready to be used in our SQL update query
        query = "UPDATE directors SET " + column_values_final + " WHERE id = " + id;
        try{
            stmt = this.mConnection.prepareStatement(query);;
            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                return true;
            }
        }
        catch(SQLException var5){
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in DirectorsTableGateway : updateDirectors(), Check the SQL you have created to see where your error is", var5);
        }
        return false;
    }


}
