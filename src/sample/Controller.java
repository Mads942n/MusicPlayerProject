package sample;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.*;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField NewPlaylistName;

    @FXML
    private Button autoplayNext;

    @FXML
    private Button pause;

    @FXML
    private Button play;

    @FXML
    private Button Libary;

    @FXML
    private AnchorPane ap;

    @FXML
    private BorderPane bp;

    @FXML
    private TableView<Songs> SongsTable;

    @FXML
    private TableColumn<Songs, String> SongId;

    @FXML
    private TableColumn<Songs, String> Title;

    @FXML
    private TableColumn<Songs, String> Artist;

    @FXML
    private TextField Search;

    @FXML
    private MouseEvent event;

    @FXML
    private ListView<Playlist> PlayListList;

    @FXML
    private Button NewPlaylist;

    @FXML
    private Button DeletePlayList;
    @FXML
    private ContextMenu ContexmenuSonglist;

    @FXML
    private ListView<Playlist> ContextMenuPlaylists;


    @FXML
    private MenuItem RemoveFromPlaylist;



    private MediaPlayer mp;
    private Media me;

    public String path;

    private Boolean autoplay;
    private int autoplayInt;
    private int SelectedSongid;

    private String getPlaylistID;
    private String filePath;
    private String SongSelected;

    private String getPlaylistName;
    ObservableList<Songs> Songlist = FXCollections.observableArrayList();
    ObservableList<Playlist> PlayListArray = FXCollections.observableArrayList();

    ArrayList<String> songIdArray = new ArrayList<>();
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<String> artistArray = new ArrayList<>();

    @FXML
    private void choosePlaylist(){
        songIdArray.clear();
        titleArray.clear();
        artistArray.clear();
        Songlist.clear();
        RemoveFromPlaylist.setVisible(true);
        try {
            Playlist SelectedPlaylist = PlayListList.getSelectionModel().getSelectedItem();
            getPlaylistName = SelectedPlaylist.PlayListTitle;


            DB.selectSQL("SELECT fldPlaylistID FROM tblPlaylists WHERE fldPlaylistName = '" + SelectedPlaylist.PlayListTitle + "'");
            getPlaylistID = DB.getData();



            DB.selectSQL("SELECT fldSongID FROM tblPlaylistsSongs where fldPlaylistID = " + getPlaylistID);

            do {
                String songId = DB.getData();
                if (songId.equals(DB.NOMOREDATA)) {
                    break;
                } else {
                    songIdArray.add(songId);
                }
            } while (true);

            DB.selectSQL("SELECT fldTitle FROM tblSonglist INNER JOIN tblPlaylistsSongs ON " +
                    "tblSonglist.fldSongID = tblPlaylistsSongs.fldSongID where tblPlaylistsSongs.fldPlaylistID = " + getPlaylistID);
            do{
                String title = DB.getData();
                if (title.equals(DB.NOMOREDATA)) {
                    break;
                } else {
                    titleArray.add(title);
                }
            } while (true);

            DB.selectSQL("SELECT fldArtist FROM tblSonglist INNER JOIN tblPlaylistsSongs ON " +
                    "tblSonglist.fldSongID = tblPlaylistsSongs.fldSongID where tblPlaylistsSongs.fldPlaylistID = " + getPlaylistID);
            do{
                String artist = DB.getData();
                if (artist.equals(DB.NOMOREDATA)){
                    break;
                }else{
                    artistArray.add(artist);
                }
            }while (true);

            for (int i = 0; i < songIdArray.size(); i++) {
                Songlist.add(new Songs(songIdArray.get(i), titleArray.get(i), artistArray.get(i)));
            }

            SongsTable.setItems(Songlist);


        }catch (Exception e){
        }

    }

    void getPlaylists(){

        ContextMenuPlaylists.setItems(PlayListArray);

    }

    void songList(){
        songIdArray.clear();
        titleArray.clear();
        artistArray.clear();

        Songlist.clear();


        SongId.setCellValueFactory(new PropertyValueFactory<Songs, String>("SongId"));
        Title.setCellValueFactory(new PropertyValueFactory<Songs, String>("Title"));
        Artist.setCellValueFactory(new PropertyValueFactory<Songs, String>("Artist"));

        DB.selectSQL("Select fldSongId from tblSonglist");
        do {
            String songId = DB.getData();
            if (songId.equals(DB.NOMOREDATA)) {
                break;
            } else {
                songIdArray.add(songId);
            }
        } while (true);

        DB.selectSQL("Select fldTitle from tblSonglist");
        do{
            String title = DB.getData();
            if (title.equals(DB.NOMOREDATA)) {
                break;
            } else {
                titleArray.add(title);
            }
        } while (true);

        DB.selectSQL("Select fldArtist from tblSonglist");
        do{
            String artist = DB.getData();
            if (artist.equals(DB.NOMOREDATA)){
                break;
            }else{
                artistArray.add(artist);
            }
        }while (true);

        for (int i = 0; i < songIdArray.size(); i++) {
            Songlist.add(new Songs(songIdArray.get(i), titleArray.get(i), artistArray.get(i)));
        }
        SongsTable.setItems(Songlist);
    }



    //updates the PLaylistviewer
    public void updatePlaylistTable() {

        DB.selectSQL("Select fldPlaylistName FROM tblPlaylists");

        PlayListArray.clear();

        do{
            String PlaylistData = DB.getData();
            if (PlaylistData.equals(DB.NOMOREDATA)){
                break;
            } else {

                PlayListArray.add(new Playlist(PlaylistData));

            }
        }while(true);

        PlayListList.setItems(PlayListArray);
        getPlaylists();

    }

    public void newPlayList(){

        //gets the text from the textfield
        String NewPlayListTyped = NewPlaylistName.getText();

        //inserts the playlist name into database
        DB.insertSQL("INSERT INTO tblPLaylists (fldPlaylistName) VALUES ('" + NewPlayListTyped + "')");

        NewPlaylistName.setVisible(false);
        NewPlaylistName.clear();

        //updating the Listview with the new playlist
        updatePlaylistTable();
    }


    void search_song() {
        //Search function


        SongsTable.setItems(Songlist);
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
                } else if (Songs.getArtist().toLowerCase().contains(lowerCaseFilter))
                    return true;// Filter matches artist
                else
                    return false; // Does not match.
            });
        });
        SortedList<Songs> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(SongsTable.comparatorProperty());
        SongsTable.setItems(sortedData);
    }

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        songList();
        updatePlaylistTable();
        search_song();
        getPlaylists();
    }

    public void autoplayNext() {

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
    private void displaySelectedItem() {
        try {
            //song selection:
            Songs Song = SongsTable.getSelectionModel().getSelectedItem();
            SelectedSongid = Integer.parseInt(Song.SongId);

        } catch (Exception e){
            System.out.println("no song clicked");
        }

    }

    @FXML
    void addSongtoPlaylist() {
        Playlist getPlaylistName = ContextMenuPlaylists.getSelectionModel().getSelectedItem();

        DB.insertSQL("INSERT INTO tblPlaylistsSongs (fldPlaylistID) SELECT fldPlaylistID " +
                "FROM tblPlaylists WHERE fldPlaylistName = '" + getPlaylistName.PlayListTitle + "'" );

        DB.updateSQL("UPDATE tblPlaylistsSongs SET fldSongID = " + SelectedSongid +" WHERE fldSongID IS NULL");



    }

    @FXML
    /**
     * Handler for the play button
     */
    private void handlePlay() {
        try {


            //getting selected song from database
            DB.selectSQL("Select fldFilePath from tblSonglist where fldSongID = " + SelectedSongid);

            filePath = DB.getData();

            //location of the media files
            path = new File("src/sample/media/" + filePath + ".mp3").getAbsolutePath();

            // new Media object
            me = new Media(new File(path).toURI().toString());

            mp = new MediaPlayer(me);


            // If autoplay is turned of the method play(), stop(), pause() etc controls how/when medias are played
            mp.setAutoPlay(false);

            mp.stop();


            //starts the new song

            mp.play();

            System.out.println("SELECTED SONG:" + SelectedSongid);

        } catch (Exception e){
            System.out.println("no song selected");
        }

    }

    @FXML
    private void handlePause() {
        try {
            mp.pause();
        } catch (Exception e){
            System.out.println("no song is currently playing");
        }

    }

    @FXML
    private void handleautoplayNext() {

        mp.getStatus();

        autoplay = true;

        autoplayNext();

        System.out.println(autoplay);

    }

    @FXML
    private void handleNewPlayList() {

        //shows the textfield
        NewPlaylistName.setVisible(true);


    }

    @FXML
    public void handleEnterNewPlaylist(KeyEvent event){
    if (event.getCode() == KeyCode.ENTER) {
        newPlayList();
    }

    }

    @FXML
    private void handleDeletePlayList() {
        System.out.println(getPlaylistID);

        DB.deleteSQL("DELETE FROM tblPlaylistsSongs where fldPlaylistID = " + getPlaylistID);
        DB.deleteSQL("Delete FROM tblPlaylists where fldPlaylistName = '" + getPlaylistName + "'");


        updatePlaylistTable();

    }

    @FXML
    private void handleLibary() {
        RemoveFromPlaylist.setVisible(false);
        songList();
        search_song();
    }

    @FXML
    void deleteSongFromPlaylist() {
        DB.deleteSQL("DELETE From tblPlaylistsSongs where fldPlaylistID = " + getPlaylistID + " AND fldSongID = " + SelectedSongid);
        choosePlaylist();


    }

}
