package com.company;

import com.company.database.DBConnection;
import com.company.database.BoxsetTableGateway;
import com.company.database.DirectorsTableGateway;
import com.company.database.MoviesTableGateway;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// We're using a Model-View-Contrller design pattern. This is the 'Model' in MVC.
// This class encapsulates the state of our application.
// This class is a go-between for our Main (view) and Database classes (Controller).
public class Model {
    // Instantiate a model
    private static Model instance = null;

    // Synchronized, only one thread may access this resource at a time
    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    // Declare variables
    private List<BoxSet> boxsets;
    private List<Director> directors;
    private List<Director> director;
    private List<Movie> movies;
    private BoxsetTableGateway bGateway;
    private DirectorsTableGateway dGateway;
    private MoviesTableGateway mGateway;


    private Model() {

        try {
            // Instantiate our DB connection, declare our gateways and pass connection as parameter
            Connection conn = DBConnection.getInstance();
            this.bGateway = new BoxsetTableGateway(conn);
            this.dGateway = new DirectorsTableGateway(conn);
            this.mGateway = new MoviesTableGateway(conn);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //casting as a boxset is important, we're going to access this object's get methods in our GUI
    public List<BoxSet> viewBoxsets() {
        this.boxsets = this.bGateway.getBoxSets();
        return boxsets;
    }
    // Again it's necessary for this to be of type 'Director' for our GUI to run the accessor methods
    public List<Director> viewDirectors() {
        this.directors = this.dGateway.getDirectors();

        return directors;
    }

    public List<Movie>  viewMovies() {
        this.movies = this.mGateway.getMovies();

        return movies;
    }
    // Above methods are run from GUI //

    // Accepts id and boolean viewAll as parameter
    public String viewDirectorById(int id, boolean bool) {
        // Passing id and boolean to directorsTableGateway
        String d = this.dGateway.getDirectorById(id, bool);
        return d;
    }
    // These methods will return object rather than strings, like our method above
    public BoxSet viewBoxSetById(int id) {
        BoxSet b = this.bGateway.getBoxsetById(id);
        return b;
    }

    public Movie getMovieById(int id) {
        Movie m = this.mGateway.getMovieById(id);
        return m;
    }


    //create
    public int createMovie(Movie m) { return (mGateway.insertMovie(m)); }
    public int createBoxSet(BoxSet b) {
        return (bGateway.insertBoxSet(b));
    }
    public int createDirector(Director d) {
        return (dGateway.insertDirector(d));
    }

    //delete
    public boolean deleteMovie(int id){ return (mGateway.deleteMovie(id)); }
    public boolean deleteBoxset(int id){ return (bGateway.deleteBoxset(id)); }
    public boolean deleteDirector(int id){ return (dGateway.deleteDirector(id)); }

    //update - passing our 2d arraylists to these functions and on to their respective gateways
    public boolean updateBoxset(List<List> arraylist2D, int id){return (bGateway.updateBoxset(arraylist2D, id)); }
    public boolean updateMovie(List<List> arraylist2D, int id){return (mGateway.updateMovie(arraylist2D, id)); }
    public boolean updateDirector(List<List> arraylist2D, int id){return (dGateway.updateDirector(arraylist2D, id)); }
}


