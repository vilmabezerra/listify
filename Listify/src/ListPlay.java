public class ListPlay {
	 public String name;
	 public String description;
	 public Boolean publicC;
	 public Boolean collaborative;
	 	
	 public ListPlay() {
		 
	 }

    /* Every field has a public getter... */
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }

    /* ... and a public setter */
    public void setDescription(String description) {
        this.description = description;
    }

    /* ... and a public setter */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
       return name;
    }
	    
}
