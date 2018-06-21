import java.util.ArrayList;
import java.util.List;

public class LocalPlaylist {
	 public String name;
	 public String description;
	 public Boolean publicAccess;
	 public Boolean collaborative;
	 public List<LocalTrack> tracks = new ArrayList<>();
	 	
	 public LocalPlaylist() {}

    /* Every field has a public getter... */
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public Boolean getPublicAccess(){
        return publicAccess;
    }
    public Boolean getCollaborative(){
        return collaborative;
    }
    public List<LocalTrack> getTracks(){
        return tracks;
    }

    
    
    /* ... and a public setter */
    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setPublicAccess(Boolean access) {
        this.publicAccess = access;
    }
    
    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }
    public void setTracks(List<LocalTrack> tracks) {
        this.tracks.addAll(tracks);
    }
    
    @Override
    public String toString() {
       return name;
    }
	    
}
