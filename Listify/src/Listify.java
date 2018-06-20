import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;



public class Listify{
	private static String clientID = "42fc15b62f8c4b60b845daf59b9adf96";
	private static String clientSecret = "-";
	static String accessToken = null;
    static String userId = null;
    static String name = null;
    static SpotifyApi spotifyApi = null;
    
	public static void main (String[] args) throws SpotifyWebApiException {
		    Map<String, ListPlay> parsed = new HashMap<String, ListPlay>();
		    String path = "./src/userconfig.txt";
		    
		    readConfigFile(path);
		    
		    spotifyApi = new SpotifyApi.Builder()
		    		  .setClientId(clientID)
		    		  .setClientSecret(clientSecret)
		    		  .setAccessToken(accessToken)
		    		  .build();
		    
		    //Checking if there are any arguments
		    /*if (args.length == 0) {
		    		System.out.println(Error.NO_FILE_ERROR);
		    }else {
		    		if (args.length > 1) {
		    			System.out.println(Error.MORE_THAN_ONE_ERROR);
		    		}else {
		    			//parsed = parser(args[0]);
		    		}
		    }*/
		    
		    String document = "map:\n  create Playlist:\n     name: blabla\n     description: tchutchu\n  edit Playlist blabla:\n     name: bleble";
		    
		    System.out.println(document);
		    System.out.println("---------------------------------");
		    parsed = parser(document);
		    System.out.println(parsed);
		    System.out.println("---------------------------------");
		    interpreter(parsed);
		    

	}
	
	/*Method to parse YAML file*/
	private static Map<String, ListPlay> parser(String file) {
	    Yaml yaml = new Yaml(new Constructor(CommandsMap.class));
	    CommandsMap commands = (CommandsMap) yaml.load(file);
	    Map<String, ListPlay> commandMap = commands.map;
	    return commandMap;
	}
	
	/*Method to interpret command just getted by parser*/
	private static void interpreter(Map<String, ListPlay> commands) {
		ArrayList<String> commandDetail = new ArrayList<String>();
		int commandKey = 0;
		
		for (Map.Entry<String, ListPlay> entry : commands.entrySet())
	    {
			commandDetail.clear();
	        commandKey = getCommandKey(entry.getKey(), commandDetail);
	        
	        switch (commandKey) {
			case 0:
				createPlaylist(entry.getValue());
				break;
			/*case 1:
				editPlaylist();
				break;
			case 2:
				mergePlaylists();
				break;*/
			default:
				//System.out.println(Error.SYNTAXE_ERROR);
				break;
			}
	        
			System.out.println(commandDetail.get(0));
	        
	    }
	}
	
	private static void createPlaylist(ListPlay listPlay) {
		
		System.out.println("Creating..." + listPlay.name);
		
		CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, listPlay.name)
		          .description(listPlay.description)
		          .build();
		
		try {
		      final Playlist playlist = createPlaylistRequest.execute();
		      if (playlist != null) {
		    	  	System.out.println("Playlist " + playlist.getDescription() +" successfully created");
		      }
		} catch (IOException | SpotifyWebApiException e) {
		      System.out.println("Error: " + e.getMessage());
		}
		  
	}
	
	/*Used to get the command that should be executed*/
	public static int getCommandKey(String key, ArrayList<String> commandDetail) {
		int i = 0;
		String[] parts = key.split(" ");
		
		String firstWord = parts[0];
		
		while (!firstWord.equals(Command.COMMANDS[i])) {
			i++;
		}
		
		if(parts.length > 2 && parts[2] != null) {
			commandDetail.add(Command.COMMANDS[i]);
			commandDetail.add(parts[2]);
		}else {
			commandDetail.add(Command.COMMANDS[i]);
			commandDetail.add(null);
		}
		
		return i;
	}
	
	/*Read config file and set accessToken, name and id static variables*/
	public static void readConfigFile(String path) {
		String configfile = path;
		
		BufferedReader buffer = null;
		FileReader freader = null;

		try {
			Boolean nameB = false;
			Boolean tokenB = false;
			Boolean idB = false;
			
			freader = new FileReader(configfile);
			buffer = new BufferedReader(freader);

			String sCurrentLine;

			while ((sCurrentLine = buffer.readLine()) != null) {
				String[] config = sCurrentLine.split(": ");
				
				if (config[1] == null) {
					System.out.println(Error.CONFIGFILE_ERROR);
				}else {
					//System.out.println("config[0] "+config[0]+"   config[1] "+config[1]);
					if ((config[0].equals("Name")) && (nameB != true)) {
						name = config[1];
						nameB = true;
					}else {
						if ((config[0].equals("Token")) && (tokenB != true)) {
							accessToken = config[1];
							tokenB = true;
						}else {
							if ((config[0].equals("UserID")) && (idB != true)) {
								userId = config[1];
								idB = true;
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null)
					buffer.close();

				if (freader != null)
					freader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/*Method basically used for tests*/
	public void showCommandsMap(Map<String, ListPlay> commands) {
		for (Map.Entry<String, ListPlay> entry : commands.entrySet())
	    {
	        System.out.println(entry.getKey() + "/" + entry.getValue());
	    }
	}
}
	

