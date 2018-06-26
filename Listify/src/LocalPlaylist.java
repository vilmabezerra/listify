import java.util.ArrayList;
import java.util.List;

public class LocalPlaylist {
	 public String name = null;
	 public String description = null;
	 public Boolean publicAccess = null;
	 public Boolean collaborative = null;
	 public List<LocalTrack> addTracks = new ArrayList<>();
	 public List<LocalTrack> removeTracks = new ArrayList<>();
	 	
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
    public List<LocalTrack> getAddTracks(){
        return addTracks;
    }
    public List<LocalTrack> getRemoveTracks(){
        return removeTracks;
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
    public void setAddTracks(List<LocalTrack> tracks) {
        this.addTracks.addAll(tracks);
    }
    
    public void setRemoveTracks(List<LocalTrack> tracks) {
        this.removeTracks.addAll(tracks);
    }
    
    @Override
    public String toString() {
       return name;
    }
	    
}
