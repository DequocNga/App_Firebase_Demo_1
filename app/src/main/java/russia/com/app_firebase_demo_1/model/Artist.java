package russia.com.app_firebase_demo_1.model;

/**
 * Created by VLADIMIR PUTIN on 3/5/2018.
 */

public class Artist {
    private String artistID;
    private String artistName;
    private String artistGenre;

    public Artist() {
    }

    public Artist(String artistID, String artistName, String artistGenre) {
        this.artistID = artistID;
        this.artistName = artistName;
        this.artistGenre = artistGenre;
    }

    public String getArtistID() {
        return artistID;
    }

    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistGenre() {
        return artistGenre;
    }

    public void setArtistGenre(String artistGenre) {
        this.artistGenre = artistGenre;
    }
}
