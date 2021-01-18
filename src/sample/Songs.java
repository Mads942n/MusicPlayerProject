package sample;

public class Songs {

    String SongId;
    String title;

    String artist;


    public Songs(String SongId, String title, String artist) {
        this.SongId = SongId;
        this.title = title;
        this.artist = artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setSongId(String SongId) {
        this.SongId = SongId;
    }

    public String getSongId() {
        return SongId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
