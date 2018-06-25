
public class LocalTrack {
	public String name;
	public String artist;
	public String album;
	//public Integer popularity;
	//public Boolean explicit;
	
	public LocalTrack(){}
	
	/*GETTERS*/
	public String getName(){
        return name;
    }
	
    public String getArtist(){
        return artist;
    }
    
    public String getAlbum(){
        return album;
    }
    
    /*SETTERS*/
    
    public void setDescription(String artist) {
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setAlbum(String album){
        this.album = album;
    }
}
