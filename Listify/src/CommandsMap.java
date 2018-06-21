import java.util.Map;

public class CommandsMap {
	public Map<String, LocalPlaylist> map;
	
	public CommandsMap() {
	}
	
	public Map<String, LocalPlaylist> getMap(){
        return map;
    }

    /* ... and a public setter */
    public void setMap(Map<String, LocalPlaylist> map) {
        this.map = map;
    }
}
