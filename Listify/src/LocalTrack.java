
public class LocalTrack {
	public String name;
	public String artist;
	public Integer popularity;
	public Boolean explicit;
	
	public LocalTrack(){}
	
	/*GETTERS*/
	public String getName(){
        return name;
    }
    public String getArtist(){
        return artist;
    }
    
    public Integer getPopularity(){
        return popularity;
    }
    public Boolean getExplicit(){
        return explicit;
    }
    
    /*SETTERS*/
    
    public void setDescription(String artist) {
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPopularity(Integer pop){
        this.popularity = pop;
    }
    public void setExplicit(Boolean exp){
        this.explicit = exp;
    }
}
