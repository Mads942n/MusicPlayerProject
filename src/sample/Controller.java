package sample;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {
    @FXML
    private Button autoplayNext;

    @FXML
    private Button pause;

    @FXML
    private Button play;

    @FXML
    private Button AllSongs;

    @FXML
    private AnchorPane ap;

    @FXML
    private BorderPane bp;

    @FXML
    private TableView<Songs> SongsTable;

    @FXML
    private TableColumn<Songs, Integer> SongId;

    @FXML
    private TableColumn<Songs, String> Title;

    @FXML
    private TableColumn<Songs, String> Artist;

    @FXML
    private TextField Search;

    @FXML
    private MouseEvent event;

    @FXML
    private ListView<?> PlayListList;


    private MediaPlayer mp;
    private Media me;

    public String path;

    private Boolean autoplay;
    private int autoplayInt;


    private String filePath;
    private String SongSelected;

    public int getFocusedIndex;

    ObservableList<Songs> Songlist = FXCollections.observableArrayList(
            new Songs(1,"Bando Bitch", "Branco"),
            new Songs(2,"WITHOUT YOU", "The Kid LAROI"),
            new Songs(3,"Anyone", "Justin Bieber")
    );



    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){


        // Build the path to the location of the media file
        path = new File("src/sample/media/SampleAudio.mp3").getAbsolutePath();
        // Create new Media object (the actual media content)
        me = new Media(new File(path).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);
        //
        // mp.setAutoPlay(true);
        // If autoplay is turned of the method play(), stop(), pause() etc controls how/when medias are played
        mp.setAutoPlay(false);

        SongsTable.setItems(Songlist);

        //Search function

        SongId.setCellValueFactory(new PropertyValueFactory<Songs, Integer>("SongId"));
        Title.setCellValueFactory(new PropertyValueFactory<Songs, String>("Title"));
        Artist.setCellValueFactory(new PropertyValueFactory<Songs, String>("Artist"));

        //ObservableList to filteredList
        FilteredList<Songs> filteredData = new FilteredList<>(Songlist, b -> true);


        Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Songs -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (Songs.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches song title
                }
                else if  (Songs.getArtist().toLowerCase().contains(lowerCaseFilter))
                    return true;// Filter matches artist
                else
                    return false; // Does not match.
            });
        });
        SortedList<Songs> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(SongsTable.comparatorProperty());
        SongsTable.setItems(sortedData);




    }

    public void autoplayNext(){

        Songs Song = SongsTable.getSelectionModel().getSelectedItem();


        while (autoplay = true) {
            mp.setOnEndOfMedia(() -> {

                DB.selectSQL("Select fldFilePath from tblSonglist where fldSongID = " + Song.SongId + " + 1");


                filePath = DB.getData();

                System.out.println(filePath);

                path = new File("src/sample/media/" + filePath + ".mp3").getAbsolutePath();

                me = new Media(new File(path).toURI().toString());

                mp = new MediaPlayer(me);

                mp.setAutoPlay(true);

                System.out.println(path);

            });
        }



    }



    @FXML
    private void displaySelectedItem(){

        Songs Song = SongsTable.getSelectionModel().getSelectedItem();


        DB.selectSQL("Select fldFilePath from tblSonglist where fldSongID = " + Song.SongId);

        filePath = DB.getData();

        path = new File("src/sample/media/" + filePath + ".mp3").getAbsolutePath();

        me = new Media(new File(path).toURI().toString());

        mp = new MediaPlayer(me);

        mp.setAutoPlay(true);


        System.out.println("Song ID = " + Song.SongId + "\nFilename = " + filePath);






    }

    @FXML
    /**
     * Handler for the play button
     */
    private void handlePlay()
    {

        mp.play();
        mp.setAutoPlay(true);
    }

    @FXML
    private void handlePause()
    {


        // Play the mediaPlayer with the attached media
        mp.pause();
    }

    @FXML
    private void handleautoplayNext()
    {

        mp.getStatus();

        autoplay = true;

        autoplayNext();

        System.out.println(autoplay);

    }




}
