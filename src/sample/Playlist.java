package sample;

public class Playlist {
    String PlayListTitle;


    public Playlist(String PlayListTitle){

        this.PlayListTitle = PlayListTitle;
    }
    public String getPlayListTitle() {
        return PlayListTitle;
    }

    public void setPlayListTitle(String playListTitle) {
        PlayListTitle = playListTitle;
    }

    @Override
    public String toString() {
        return PlayListTitle;
    }
}


