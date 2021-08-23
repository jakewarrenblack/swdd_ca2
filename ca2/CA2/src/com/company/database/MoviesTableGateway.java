package com.company.database;
// Methods used here are explained further in the DirectorsTableGateway to avoid repetition of explanations

import com.company.BoxSet;
import com.company.Director;
import com.company.Model;
import com.company.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Defining our column names from the database.
public class MoviesTableGateway {
    private static final String TABLE_NAME = "movies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RUNNINGTIME = "runningTime";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DIRECTOR = "director";
    private static final String COLUMN_AGERATING = "ageRating";
    private static final String COLUMN_PREMIERE = "premiereDate";
    private static final String COLUMN_3D = "is3D";
    //Instantiate a connection object.
    private Connection mConnection;

    public MoviesTableGateway(Connection connection) {
        this.mConnection = connection;
    }

    //Return type of this method is an arraylist.
    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList();
        String query = "SELECT * FROM movies";


        try {
            Statement stmt = this.mConnection.createStatement();
            //A ResultSet is a table of data which represents the data returned from our database.
            ResultSet rs = stmt.executeQuery(query); //We define our ResultSet as the result of our query.

            //This will loop through our ResultSet while there are still values that haven't been passed.
            while(rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String premiereDate = rs.getString("premiereDate");
                int runningTime = rs.getInt("runningTime");
                int ageRating = rs.getInt("ageRating");
                boolean is3d = rs.getBoolean("is3D");
                int director_id = rs.getInt("director_id");
                //Create a new movie object for each result of our ResultSet, grab the values from the column in each case and drop them into our variables.
                Movie m = new Movie(id, runningTime, title, ageRating, premiereDate,director_id, is3d);
                //For each item in the ResultSet, we've created a Movie instance and now we'll append this onto our movies arraylist.
                movies.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in MoviesTableGateway : getMovies(), Check the SQL you have created to see where your error is", ex);
        }

        //When finished, return our movies arraylist to the Model, which will return the movies arraylist to the Main.
        return movies;
    }


    public Movie getMovieById(int id) {
        Movie m = null;

        String query = "SELECT * FROM movies WHERE id =" +id;


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
                boolean is3d = rs.getBoolean("is3D");
                int director_id = rs.getInt("director_id");
                m = new Movie(id,runningTime,title,ageRating,premiereDate,director_id,is3d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in MoviesTableGateway : getMovieById(), Check the SQL you have created to see where your error is", ex);
        }

        //When finished, return our movies arraylist to the Model, which will return the movies arraylist to the Main.
        return m;
    }


    public int insertMovie(Movie m)  {
        String query;
        PreparedStatement stmt;
        int numRowsAffected;
        int generatedId;


        query = "INSERT INTO `movies`(`runningTime`, `title`, `ageRating`, `premiereDate`, `is3D`, `director_id`) " +
                "VALUES (?,?,?,?,?,?" +
                ")";
        try {

            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setDouble(1, m.calcRunningTime());
            stmt.setString(2, m.getTitle());
            stmt.setInt(3, m.getAgeRating());
            stmt.setDate(4, m.getPremiereDate());
            stmt.setBoolean(5, m.getIs3d());
            stmt.setInt(6, m.getDirector_id());

            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();
                generatedId = keys.getInt(1);
                return generatedId;
            }
        }
        catch (SQLException e)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in MoviesTableGateway : insertMovie(), Check the SQL you have created to see where your error is", e);
        }
        return -1;
    }





    public boolean deleteMovie(int id){
        String query;
        PreparedStatement stmt;
        int numRowsAffected;
        query = "DELETE FROM movies WHERE id = "+id;

        try {
            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Enter a proper error message here");
        }
        return false;
    };

    public boolean updateMovie(List<List> arraylist2D, int id){
        String query;
        PreparedStatement stmt;
        int numRowsAffected;

        List columns;
        List values;

        columns = arraylist2D.get(0);
        values = arraylist2D.get(1);
        List<String> list = new ArrayList<>();

        for(var i=0; i<columns.size(); i++){
                System.out.println(columns.get(i));
                list.add(columns.get(i).toString());
                list.add(values.get(i).toString());
        }

        List<String> result = new ArrayList<>();

        for(var i=0; i<list.size();i+=2){
                result.add(list.get(i)+("=" + "'" + list.get(i+1) + "'"));
        }

        String columns_values = result.toString();
        String column_values_final = columns_values.toString().replaceAll("\\[|\\]", "");

        query = "UPDATE movies SET " + column_values_final + " WHERE id = " + id;
        try{
            stmt = this.mConnection.prepareStatement(query);;
            numRowsAffected = stmt.executeUpdate();
            if (numRowsAffected == 1) {
                return true;
            }
        }
        catch(SQLException var5){
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, "SQL Exception in MoviesTableGateway : updateMovie(), Check the SQL you have created to see where your error is", var5);
        }
        return false;
    }
}
