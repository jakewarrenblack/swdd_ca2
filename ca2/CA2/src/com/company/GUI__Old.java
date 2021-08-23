package com.company;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// This is my original GUI, which used buttons to show the three tables,
// the updated version uses a tabbedPane to only show one table at a time
public class GUI__Old extends JFrame implements ActionListener {
    JButton boxsetButton;
    JButton movieButton;
    JButton directorButton;
    JButton yesButton;
    JButton noButton;
    DefaultTableModel boxset_table_model;
    DefaultTableModel movie_table_model;
    DefaultTableModel director_table_model;
    static Main Main;
    static Model model;

    GUI__Old() {
        model = Model.getInstance();
        JTable boxsetTable = new JTable();
        JTable movieTable = new JTable();
        JTable directorTable = new JTable();

        //boxset table
        boxset_table_model = new DefaultTableModel();
        Object[] boxset_columnNames = new Object[7];
        boxset_columnNames[0] = "ID";
        boxset_columnNames[1] = "runningTime";
        boxset_columnNames[2] = "title";
        boxset_columnNames[3] = "ageRating";
        boxset_columnNames[4] = "premiereDate";
        boxset_columnNames[5] = "numSeries";
        boxset_columnNames[6] = "director_id";

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

        //director table
        director_table_model = new DefaultTableModel();
        Object[] director_columnNames = new Object[3];
        director_columnNames[0] = "ID";
        director_columnNames[1] = "fname";
        director_columnNames[2] = "lname";

        //VIEW MOVIES BUTTON
        movieButton = new JButton("VIEW MOVIES");//creating instance of JButton
        movieButton.setBounds(140, 300, 200, 40);//x axis, y axis, width, height
        movieButton.addActionListener(this);
        this.add(movieButton);//adding button in JFrame

        //VIEW BOXSETS BUTTON
        boxsetButton = new JButton("VIEW BOXSETS");//creating instance of JButton
        boxsetButton.setBounds(140, 100, 200, 40);//x axis, y axis, width, height
        boxsetButton.addActionListener(this);
        this.add(boxsetButton);//adding button in JFrame

        //VIEW DIRECTORS BUTTON
        directorButton = new JButton("VIEW DIRECTORS");//creating instance of JButton
        directorButton.setBounds(140, 200, 200, 40);//x axis, y axis, width, height
        directorButton.addActionListener(this);
        this.add(directorButton);//adding button in JFrame

        boxset_table_model.setColumnIdentifiers(boxset_columnNames);
        boxsetTable.setModel(boxset_table_model);

        movie_table_model.setColumnIdentifiers(movie_columnNames);
        movieTable.setModel(movie_table_model);

        director_table_model.setColumnIdentifiers(director_columnNames);
        directorTable.setModel(director_table_model);


        //jpanel/frame
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //add tables
        this.add(new JScrollPane(boxsetTable));
        this.add(new JScrollPane(movieTable));
        this.add(new JScrollPane(directorTable));


        this.setLayout(new BorderLayout());


        this.add(new JScrollPane(boxsetTable), BorderLayout.CENTER);
        this.add(new JScrollPane(movieTable), BorderLayout.SOUTH);
        this.add(new JScrollPane(directorTable), BorderLayout.NORTH);

        this.setVisible(true);
        this.setSize(200, 200);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //if boxset button is pressed
        if (e.getSource() == boxsetButton) {
            {
                Object[] rowData = new Object[7];

                for (int i = 0; i < model.viewBoxsets().size(); i++) {
                    rowData[0] = model.viewBoxsets().get(i).getId();
                    rowData[1] = model.viewBoxsets().get(i).calcRunningTime();
                    rowData[2] = model.viewBoxsets().get(i).getTitle();
                    rowData[3] = model.viewBoxsets().get(i).getAgeRating();
                    rowData[4] = model.viewBoxsets().get(i).getPremiereDate();
                    rowData[5] = model.viewBoxsets().get(i).getDirector_id();
                    boxset_table_model.addRow(rowData);
                }
            }
        }
        else if (e.getSource() == movieButton) {
            {
                Object[] rowData = new Object[7];

                for (int i = 0; i < model.viewMovies().size(); i++) {
                    rowData[0] = model.viewMovies().get(i).getId();
                    rowData[1] = model.viewMovies().get(i).calcRunningTime();
                    rowData[2] = model.viewMovies().get(i).getTitle();
                    rowData[3] = model.viewMovies().get(i).getAgeRating();
                    rowData[4] = model.viewMovies().get(i).getPremiereDate();
                    rowData[5] = model.viewMovies().get(i).getDirector_id();
                    movie_table_model.addRow(rowData);
                }
            }
        }
        else if (e.getSource() == directorButton) {
            {
                Object[] rowData = new Object[3];

                for (int i = 0; i < model.viewMovies().size(); i++) {
                    rowData[0] = model.viewDirectors().get(i).getId();
                    rowData[1] = model.viewDirectors().get(i).getFname();
                    rowData[2] = model.viewDirectors().get(i).getLname();
                    director_table_model.addRow(rowData);
                }
            }
        }
    }
}
