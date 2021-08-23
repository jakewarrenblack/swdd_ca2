package com.company;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


// This class pairs with our Main class to form the View component of the MVC architecture
// GUI will inherit the properties of a JPanel and behave as a JPanel would
public class GUI extends JPanel {
    // Instantiate DefaultTableModels to represent our three tables
    JButton boxsetButton;
    DefaultTableModel boxset_table_model;
    DefaultTableModel movie_table_model;
    DefaultTableModel director_table_model;
    // We use the 'viewAll' type methods from the Model to retrieve our values
    static Model model;

    public GUI() {
        super(new BorderLayout());
        // Pane with tabs, we'll use the tabs to separate our tables
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create a JPanel for each table, adding methods and labels for each
        JPanel labelAndComponent = new JPanel();
        labelAndComponent.add(viewMovies());
        tabbedPane.addTab("Movies", labelAndComponent);

        JPanel labelAndComponent2 = new JPanel();
        labelAndComponent2.add(viewBoxsets());
        tabbedPane.addTab("Boxsets", labelAndComponent2);

        JPanel labelAndComponent3 = new JPanel();
        labelAndComponent3.add(viewDirectors());
        tabbedPane.addTab("Directors", labelAndComponent3);

        //TabbedPane now comprises 3 tabs, add this tabbedPane to the current container.
        add(tabbedPane, BorderLayout.CENTER);

        // Instantiating our JFrame and giving it a title
        JFrame frame = new JFrame("MovieFlix");
        // Tells the JFrame to exit when we press the X, rather than just minimising
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);

        // Pack must be done last
        frame.pack();
        // Needs to be set to visible, won't be visible by default
        frame.setVisible(true);

    }

    // We've added these JPanels to our tabbedPane, now we define them
    protected JPanel viewBoxsets() {
        // Getting an instance of the model we can use
        model = Model.getInstance();
        JTable boxsetTable = new JTable();
        //boxset table
        boxset_table_model = new DefaultTableModel();
        // Defining our column_names as a generic object of size 7
        Object[] boxset_columnNames = new Object[7];
        // Defining the actual names
        boxset_columnNames[0] = "ID";
        boxset_columnNames[1] = "runningTime";
        boxset_columnNames[2] = "title";
        boxset_columnNames[3] = "ageRating";
        boxset_columnNames[4] = "premiereDate";
        boxset_columnNames[5] = "numSeries";
        boxset_columnNames[6] = "director_id";

        // Now we use setColumnIdentifiers on the table_model to add our column_names
        boxset_table_model.setColumnIdentifiers(boxset_columnNames);

        // Add this table to the boxset_table_model
        boxsetTable.setModel(boxset_table_model);

        // Now we'll run the viewBoxsets method in and loop through the values running their respective getters
        Object[] rowData = new Object[7];

        for (int i = 0; i < model.viewBoxsets().size(); i++) {
            rowData[0] = model.viewBoxsets().get(i).getId();
            rowData[1] = model.viewBoxsets().get(i).calcRunningTime();
            rowData[2] = model.viewBoxsets().get(i).getTitle();
            rowData[3] = model.viewBoxsets().get(i).getAgeRating();
            rowData[4] = model.viewBoxsets().get(i).getPremiereDate();
            rowData[5] = model.viewBoxsets().get(i).getNumSeries();
            rowData[6] = model.viewBoxsets().get(i).getDirector_id();
            boxset_table_model.addRow(rowData);
        }
        JPanel pane = new JPanel();

        JComponent component = new JPanel();
        Dimension size = new Dimension(1000,1000);
        boxsetTable.setMaximumSize(size);
        boxsetTable.setPreferredSize(size);
        boxsetTable.setMinimumSize(size);

        JLabel label = new JLabel("Boxsets Table:");
        String title = "Boxsets";
        label.setAlignmentX(CENTER_ALIGNMENT);

        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);

        // Pane with a scrollbar, instantiate one and add our boxsetTable to it
        JScrollPane scrollPane = new JScrollPane(boxsetTable);
        scrollPane.setPreferredSize(size);
        pane.add(scrollPane);
        // Return our final pane object to be added to our tabbedPane
        return pane;
    }

    // We repeat the above process for viewMovies() and viewDirectors()

    protected JPanel viewMovies() {
        model = Model.getInstance();
        JTable movieTable = new JTable();
        //movie table
        movie_table_model = new DefaultTableModel();
        Object[] movie_columnNames = new Object[7];
        movie_columnNames[0] = "ID";
        movie_columnNames[1] = "runningTime";
        movie_columnNames[2] = "title";
        movie_columnNames[3] = "ageRating";
        movie_columnNames[4] = "premiereDate";
        movie_columnNames[5] = "is3d";
        movie_columnNames[6] = "director_id";

        movie_table_model.setColumnIdentifiers(movie_columnNames);
        movieTable.setModel(movie_table_model);

        Object[] rowData = new Object[7];

        for (int i = 0; i < model.viewMovies().size(); i++) {
            rowData[0] = model.viewMovies().get(i).getId();
            rowData[1] = model.viewMovies().get(i).calcRunningTime();
            rowData[2] = model.viewMovies().get(i).getTitle();
            rowData[3] = model.viewMovies().get(i).getAgeRating();
            rowData[4] = model.viewMovies().get(i).getPremiereDate();
            rowData[5] = model.viewMovies().get(i).getIs3d();
            rowData[6] = model.viewMovies().get(i).getDirector_id();
            movie_table_model.addRow(rowData);
        }

        JPanel pane = new JPanel();

        JComponent component = new JPanel();
        Dimension size = new Dimension(1000,1000);
        movieTable.setMaximumSize(size);
        movieTable.setPreferredSize(size);
        movieTable.setMinimumSize(size);

        JLabel label = new JLabel("Movies Table:");
        String title = "Movies";
        label.setAlignmentX(CENTER_ALIGNMENT);

        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);

        JScrollPane scrollPane = new JScrollPane(movieTable);
        scrollPane.setPreferredSize(size);

        pane.add(scrollPane);
        return pane;
    }

    protected JPanel viewDirectors() {
        model = Model.getInstance();
        JTable directorTable = new JTable();
        //director table
        director_table_model = new DefaultTableModel();
        Object[] director_columnNames = new Object[3];
        director_columnNames[0] = "ID";
        director_columnNames[1] = "fname";
        director_columnNames[2] = "lname";

        director_table_model.setColumnIdentifiers(director_columnNames);
        directorTable.setModel(director_table_model);


        Object[] rowData = new Object[3];

        for (int i = 0; i < model.viewDirectors().size(); i++) {
            rowData[0] = model.viewDirectors().get(i).getId();
            rowData[1] = model.viewDirectors().get(i).getFname();
            rowData[2] = model.viewDirectors().get(i).getLname();
            director_table_model.addRow(rowData);
        }

        JPanel pane = new JPanel();

        JComponent component = new JPanel();
        Dimension size = new Dimension(1000,1000);
        directorTable.setMaximumSize(size);
        directorTable.setPreferredSize(size);
        directorTable.setMinimumSize(size);

        JLabel label = new JLabel("Directors Table:");
        String title = "Directors";
        label.setAlignmentX(CENTER_ALIGNMENT);


        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);

        JScrollPane scrollPane = new JScrollPane(directorTable);
        scrollPane.setPreferredSize(size);

        pane.add(scrollPane);
        return pane;
    }
}


