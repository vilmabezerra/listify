
public class LocalTrack {
	public String name;
	public String artist;
	
	public LocalTrack(){}
	
	public String getName(){
        return name;
    }
    public String getArtist(){
        return artist;
    }
    
    public void setDescription(String artist) {
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
    }
}
