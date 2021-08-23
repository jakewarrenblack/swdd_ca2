package com.company.database;
// Methods used here are explained further in the DirectorsTableGateway to avoid repetition of explanations

import com.company.BoxSet;
import com.company.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Defining our column names from the database.
public class BoxsetTableGateway {
    private static final String TABLE_NAME = "boxsets";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RUNNINGTIME = "runningTime";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DIRECTOR = "director";
    private static final String COLUMN_AGERATING = "ageRating";
    private static final String COLUMN_PREMIERE = "premiereDate";
    private static final String COLUMN_NUMSERIES = "numSeries";
    //Instantiate a connection object.
    private Connection mConnection;

    public BoxsetTableGateway(Connection connection) {
        this.mConnection = connection;
    }

    //Return type of this method is an arraylist.
    public List<BoxSet> getBoxSets() {
        List<BoxSet> boxsets = new ArrayList();
        String query = "SELECT * FROM boxsets";


        try {
            Statement stmt = this.mConnection.createStatement();
            //A ResultSet is a table of data which represents the data returned from our database.
            ResultSet rs = stmt.executeQuery(query); //We define our ResultSet as the result of our query.

            //This will loop through our ResultSet while there are still values that haven't been passed.
            while(rs.next()) {
                int id = rs.getInt("id");
                int runningTime = rs.getInt("runningTime");
                String title = rs.getString("title");
                int ageRating = rs.getInt("ageRating");
                String premiereDate = rs.getString("premiereDate");
                int numSeries = rs.getInt("numSeries");
                int director_id = rs.getInt("director_id");
                //Create a new BoxSet object for each result of our ResultSet, grab the values from the column in each case and drop them into our variables.
                BoxSet b = new BoxSet(id, runningTime, title, ageRating, premiereDate, numSeries,director_id);
                //For each item in the ResultSet, we've created a BoxSet instance and now we'll append this onto our BoxSet arraylist.
                boxsets.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in BoxsetsTableGateway : getBoxSets(), Check the SQL you have created to see where your error is", ex);
        }
        return boxsets;
    }

    public BoxSet getBoxsetById(int id) {
        // Initialise empty BoxSet object to have its values set from while loop
        BoxSet b = null;

        String query = "SELECT * FROM boxsets WHERE id =" +id;


        try {
            Statement stmt = this.mConnection.createStatement();
            //A ResultSet is a table of data which represents the data returned from our database.
            ResultSet rs = stmt.executeQuery(query); //We define our ResultSet as the result of our query.

            //This will loop through our ResultSet while there are still values that haven't been passed.
            while(rs.next()) {
                id = rs.getInt("id");
                int runningTime = rs.getInt("runningTime");
                String title = rs.getString("title");
                int ageRating = rs.getInt("ageRating");
                String premiereDate = rs.getString("premiereDate");
                int numSeries = rs.getInt("numSeries");
                int director_id = rs.getInt("director_id");
                b = new BoxSet(id,runningTime,title,ageRating,premiereDate,numSeries,director_id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in BoxsetsTableGateway : getBoxsetById(), Check the SQL you have created to see where your error is", ex);
        }

        //When finished, return our BoxSet arraylist to the Model, which will return the BoxSet arraylist to the Main.
        return b;
    }


    public boolean deleteBoxset(int id){
        String query;
        PreparedStatement stmt;
        int numRowsAffected;
        query = "DELETE FROM boxsets WHERE id = "+id;

        try {
            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                return true;
            }
        }
        catch (SQLException e)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in BoxsetsTableGateway : deleteBoxSet(), Check the SQL you have created to see where your error is", e);
        }
        return false;
    };

    public int insertBoxSet(BoxSet b)  {
        String query;
        PreparedStatement stmt;
        int numRowsAffected;
        int generatedId;

        // Our preparedStatement will act to replace these question mark values
        query = "INSERT INTO `boxsets`(`runningTime`, `title`, `ageRating`, `premiereDate`, `numSeries`, `director_id`) " +
                "VALUES (?,?,?,?,?,?" +
                ")";
        try {
            // create a PreparedStatement object to execute the query and insert the values into the query
            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setDouble(1, b.calcRunningTime());
            stmt.setString(2, b.getTitle());
            stmt.setInt(3, b.getAgeRating());
            stmt.setDate(4, b.getPremiereDate());
            stmt.setDouble(5, b.getNumSeries());
            stmt.setInt(6, b.getDirector_id());

            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                // An SQL query which inserts values will return generatedKeys to correspond with the insertion, we'll use this to retrieve the new ID
                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();
                generatedId = keys.getInt(1);
                return generatedId;
            }
        }
        catch (SQLException e)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in BoxsetsTableGateway : insertBoxSet(), Check the SQL you have created to see where your error is", e);
        }
        return -1;
    }


    public boolean updateBoxset(List<List> arraylist2D, int id){
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
        query = "UPDATE boxsets SET " + column_values_final + " WHERE id = " + id;
        try{
            stmt = this.mConnection.prepareStatement(query);;
            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                return true;
            }
        }
        catch(SQLException var5){
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in BoxsetsTableGateway : updateBoxset(), Check the SQL you have created to see where your error is", var5);
        }
        return false;
    }


}
