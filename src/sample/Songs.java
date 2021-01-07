package sample;

public class Songs {

    int SongId;
    String title;

    String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Songs(int SongId, String title, String artist) {
        this.SongId = SongId;
        this.title = title;
        this.artist = artist;

    }

    public int getSongId() {
        return SongId;
    }

    public void setSongId(int SongId) {
        this.SongId = SongId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
