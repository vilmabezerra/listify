import java.util.Map;

public class CommandsMap {
	public Map<String, LocalPlaylist> listify;
	
	public CommandsMap() {
	}
	
	public Map<String, LocalPlaylist> getListify(){
        return listify;
    }

    /* ... and a public setter */
    public void setListify(Map<String, LocalPlaylist> map) {
        this.listify = map;
    }
}
