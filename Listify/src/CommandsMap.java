import java.util.Map;

public class CommandsMap {
	public Map<String, ListPlay> map;
	
	public CommandsMap() {
	}
	
	public Map<String, ListPlay> getMap(){
        return map;
    }

    /* ... and a public setter */
    public void setMap(Map<String, ListPlay> map) {
        this.map = map;
    }
}
