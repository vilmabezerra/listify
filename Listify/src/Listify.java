import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.constructor.Constructor;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.playlists.*;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;




public class Listify{
	/*Coloring the messages*/
	public static String colorBegin = "\u001b[1;35m";
	public static String colorEnd = "\u001b[0m";
	
	/*Information to access Spotify API*/
	private static String clientID = "42fc15b62f8c4b60b845daf59b9adf96";
	private static String clientSecret = "bdcd87b65f2f4065ab740952dccca89c";
	static SpotifyApi spotifyApi = null;
	 
	/*To get from userconfig.txt file*/
	static String accessToken = null;
    static String userId = null;
    static String name = null;
    
    /*File to be interpreted*/
    static String fileName = null;
   
    
	public static void main (String[] args) throws SpotifyWebApiException, FileNotFoundException {
		    Map<String, LocalPlaylist> parsed = new HashMap<String, LocalPlaylist>();
		    String path = "./userconfig.txt";
		    
		    readConfigFile(path);
		    
		    spotifyApi = new SpotifyApi.Builder()
		    		  .setClientId(clientID)
		    		  .setClientSecret(clientSecret)
		    		  .setAccessToken(accessToken)
		    		  .build();
		    
		    
		    //Checking if there are any arguments
		    if (args.length == 0) {
		    		System.err.println(Error.NO_FILE_ERROR);
		    		System.exit(0);
		    }else {
		    		if (args.length > 1) {
		    			System.err.println(Error.MORE_THAN_ONE_ERROR);
		    			System.exit(0);
		    		}else {
		    			
		    			checkFileExtension(args[0]);
		    			fileName = "./"+ args[0];
		    			
		    			String document = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
		    			
		    			//System.out.println(document);
		    		    
		    		    //System.out.println("---------------------------------");
		    		    
		    			parsed = parser(document);
		    			
		    			interpreter(parsed);
		    		}
		    }

	}
	
	/*
	 * Method to check file extension form file given as argument 
	 * 
	 * */
	private static void checkFileExtension(String file) {
		String[] parts = file.split("\\.");
		
		if((parts[1] != null) && (parts[1].equals("ltfy"))) {
			return;
		}else {
			System.err.println(Error.FILE_EXTENSION_ERROR);
			System.exit(0);
		}
	}

	/*
	 * Method to parse YAML file
	 * 
	 * */
	private static Map<String, LocalPlaylist> parser(String file) {
		
	    Yaml yaml = new Yaml(new Constructor(CommandsMap.class));
	    
	    CommandsMap commands = (CommandsMap) yaml.load(file);
	    
	    Map<String, LocalPlaylist> commandMap = commands.listify;
	    
	    return commandMap;
	}
	
	/*
	 * Method to interpret command just getted by parser
	 * 
	 * */
	private static void interpreter(Map<String, LocalPlaylist> commands) {
		String playlist2BEdited = null;
		int commandKey = 0;
		
		System.out.println(colorBegin + "Listify"+ colorEnd +"\n");
		for (Map.Entry<String, LocalPlaylist> entry : commands.entrySet())
	    {
	        commandKey = getCommandKey(entry.getKey());
	        
	        switch (commandKey) {
			case 0:
				createPlaylist(entry.getValue());
				break;
			case 1:
				playlist2BEdited = getPlaylist2bEdited(entry.getKey());
				editPlaylist(entry.getValue(), playlist2BEdited);
				break;
			/*case 2:
				mergePlaylists();
				break;*/
			default:
				break;
			}
	    }
		System.out.println("");
		System.out.println(colorBegin+ "Hey, "+ name +", check your Spotify account out to see the changes you made (:"+ colorEnd +"\n");
	}
	

	/*
	 * Used to get the command that should be executed
	 * 
	 * */
	public static int getCommandKey(String key) {
		int i = 0;
		String[] parts = key.split(" ");
		
		String firstWord = parts[0];
		
		while (!firstWord.equals(Command.COMMANDS[i])) {
			i++;
		}
		
		return i;
	}
	
	/*
	 * Method to get the name of the Playlist that need to be edited
	 * 
	 * */
	public static String getPlaylist2bEdited(String key) {
		String commandDetail = null;
		String[] parts = key.split(" ");
		
		
		if(parts.length > 2 && parts[2] != null) {
			commandDetail = "";
			for(int i = 2; i < parts.length; i++) {
				commandDetail += parts[i] + " ";
			}
			
			
			
		}else {
			commandDetail = null;
			System.err.println(Error.PLAYLIST_NAME_MISSING_EDIT_ERROR);
			System.exit(0);
		}
		
		return commandDetail.trim();
	}
	
	/*
	 * Read config file and set accessToken, name and id static variables
	 * 
	 * */
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
					System.err.println(Error.CONFIGFILE_ERROR);
					System.exit(0);
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

	/*
	 * Method used to execute command create
	 * 
	 * */
	private static void createPlaylist(LocalPlaylist listPlay) {
	
		CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, listPlay.name)
		          .description(listPlay.description)
		          .build();
		
		try {
		      final Playlist playlist = createPlaylistRequest.execute();
		      
		      if (playlist != null) {
		    	  	System.out.println("Creating Playlist " + listPlay.name +" ...");
		    	  	
		    	  	if(!listPlay.addTracks.isEmpty()) {
		    	  		addTracks(playlist, listPlay);
		    	  	}
		    	  	
		    	  	System.out.println("Playlist " + listPlay.getName() +" successfully created!\n");
		    	  	
		      }
		} catch (IOException | SpotifyWebApiException e) {
		      System.err.println("Error: " + e.getMessage());
		      System.exit(0);
		}
		  
	}
	
	/*
	 * Method used to execute command edit
	 * 
	 * */
	private static void editPlaylist(LocalPlaylist localPlaylist, String playlist2bEdited) {
		PlaylistSimplified[] userPlaylists = getListofPlaylists();
		PlaylistTrack[] listOfTracks = null;
		String playlistId = null;
		Playlist playlist = null;
		
		checkItems2BEdited(localPlaylist);
		
		
		
		// Search for Playlist to be edited
		int ite = 0;
		int quantity = userPlaylists.length;
		while(ite < quantity) {
			
			if(userPlaylists[ite].getName().equals(playlist2bEdited)) {
				
				//System.out.println("It is this one!");
				
				System.out.println("Editing "+ playlist2bEdited + " ...");
				
				playlistId = userPlaylists[ite].getId();
				playlist = getPlaylist(playlistId);
				
				//Get tracks from Playlist to be edited
				listOfTracks = getPlaylistTracks(playlistId);
				
				
				//Check if Playlist does not have any track
				if(listOfTracks != null) {
					
					//Remove tracks from Playlist, if requested by User
					if (localPlaylist.removeTracks != null) {
						removeTracks(playlist, localPlaylist, listOfTracks);
					}
				}
				
				//Add tracks to Playlist, if requested by User
				if (!localPlaylist.addTracks.isEmpty()) {
					addTracks(playlist, localPlaylist);
				}
				
				System.out.println("Playlist " + playlist.getName() +" successfully edited!\n");
				
				break;
			}else {
				//System.out.println("!");
				ite++;
			}
		}
		
		if (ite >= quantity) {
			System.err.println(Error.PLAYLIST_NOT_FOUNDED_ERROR);
			System.exit(0);
		}
	}
	
	/*
	 * Method to check whether elements are editable or not in this version of Listify
	 * 
	 * */
	private static void checkItems2BEdited(LocalPlaylist localPlaylist) {
		if((localPlaylist.name != null) || (localPlaylist.description != null) || 
				(localPlaylist.publicAccess != null) || (localPlaylist.collaborative != null))
		{
			System.err.println(Error.NOT_EDITABLE_ERROR);
			System.exit(0);
		}
	}

	/*
	 * Method to remove several Tracks from a Playlist
	 * 
	 * */
	private static void removeTracks(Playlist playlist, LocalPlaylist localPlaylist, PlaylistTrack[] listOfTracks) {
		JsonArray tracks = null;
		
		tracks = prepareJsonArray(localPlaylist, listOfTracks);
		
		
		final RemoveTracksFromPlaylistRequest removeTracksFromPlaylistRequest = spotifyApi
		          .removeTracksFromPlaylist(userId, playlist.getId(), tracks)
		          .build();
		
		try {
		      final SnapshotResult snapshotResult = removeTracksFromPlaylistRequest.execute();

		      //System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());
		    } catch (IOException | SpotifyWebApiException e) {
		      System.err.println("Error: " + e.getMessage());
		      System.exit(0);
		    }
		
	}

	private static JsonArray prepareJsonArray(LocalPlaylist localPlaylist, PlaylistTrack[] listOfTracks) {
		int trackQuantity = listOfTracks.length;
		int removeTrackQuantity = localPlaylist.removeTracks.size(); 
		
		// Variable to be returned
		JsonArray result = null;
		
		// Number of matches between removeTracks and listOfTracks
		int matchN = 0;
		
		// Indexes
		int j = 0;
		int i = 0;
		
		//Instatiated with the openning [
		String toBeJson = "[";
		
		
		/* Iterate over listOfTracks and removeTracks lists
		 * and prepare the JsonArray to be argument 
		 * of the call of removeTracks method */
		
		for(j = 0; j < removeTrackQuantity ; j++) {
			for(i = 0; i< trackQuantity; i++) {
				String trackInPlaylistName = listOfTracks[i].getTrack().getName();
				
				/* If track in removeTracks has the same name of some track in playlist informed,
				 * it should be added to JsonArray*/
				
				if (localPlaylist.removeTracks.get(j).name.equalsIgnoreCase(trackInPlaylistName)) {
					toBeJson += "{\"uri\":\"spotify:track:"+ listOfTracks[i].getTrack().getId() +"\"},";
					matchN++;
				}
			}
			if(matchN == 0) {
				System.err.println(Error.TRACK_NOT_FOUNDED_REMOVE_ERROR);
				System.exit(0);
			}
		}
		
		//Removing the last comma, since it is not needed
		if (toBeJson != null && toBeJson.length() > 0 && toBeJson.charAt(toBeJson.length() - 1) == ',') {
	        toBeJson = toBeJson.substring(0, toBeJson.length() - 1);
	    }
		
		//Adding the closing ]
		toBeJson+="]";
		//System.out.println("toBeJson: "+ toBeJson);
		
		result = new JsonParser()
		          .parse(toBeJson).getAsJsonArray();
		
		//System.out.println("toBeJson parsed to Json: "+ result);
		
		return result;
	}

	/*
	 * Method to get list of the Tracks of a Playlist
	 * 
	 * */
	private static PlaylistTrack[] getPlaylistTracks(String playlistId) {
		PlaylistTrack[] playlistTracks = null;
		final GetPlaylistsTracksRequest getPlaylistsTracksRequest = spotifyApi
		          .getPlaylistsTracks(userId, playlistId)
		          .build();
		
		try {
		      final Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();
		      
		      playlistTracks = playlistTrackPaging.getItems();
		     // System.out.println("Total: " + playlistTrackPaging.getTotal());
		      
		    } catch (IOException | SpotifyWebApiException e) {
		      System.err.println("Error: " + e.getMessage());
		      System.exit(0);
		    }
		return playlistTracks;
	}

	/*
	 * Method to get Playlist object using its spotify ID
	 * 
	 * */
	private static Playlist getPlaylist(String playlistId) {
		Playlist play = null;
		final GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(userId, playlistId)
		          .build();
		
		try {
		      final Playlist playlist = getPlaylistRequest.execute();
		      
		      play = playlist;
		      //System.out.println("Name: " + playlist.getName());
		      
		    } catch (IOException | SpotifyWebApiException e) {
		      System.err.println("Error: " + e.getMessage());
		      System.exit(0);
		    }
		  
	
		return play;
	}
	
	
	/*
	 * Method to get list of User`s Playlists
	 * 
	 * */
	private static PlaylistSimplified[] getListofPlaylists() {
		final GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi
		          .getListOfUsersPlaylists(userId)
		          .offset(0)
		          .build();
		
		try {
		      final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
		      PlaylistSimplified[] playlists = playlistSimplifiedPaging.getItems();
		      return playlists;
		    } catch (IOException | SpotifyWebApiException e) {
		      System.err.println("Error: " + e.getMessage());
		      System.exit(0);
		    }
		return null;
	}
	
	/*
	 * Method to add several Tracks to a Playlist
	 * 
	 * */
	private static void addTracks(Playlist playlist, LocalPlaylist listPlay) {
		Track[] trackslist = null;
		List<String> tracksIds = new ArrayList<String>();
		String[] tracksIdsv2 = new String[listPlay.addTracks.size()];
		
		/*Searching for each track*/
		for (LocalTrack element : listPlay.addTracks) {
			
			trackslist = searchTrack(element);
			
			if(trackslist != null) {
				tracksIds.add("spotify:track:" + trackslist[0].getId());
			}
		}
		 tracksIdsv2 = tracksIds.toArray(tracksIdsv2);
		
		/*Adding Tracks to Playlist*/
		AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotifyApi
		          .addTracksToPlaylist(userId, playlist.getId(), tracksIdsv2)
		          .position(0)
		          .build();
		try {
		      final SnapshotResult snapshotResult = addTracksToPlaylistRequest.execute();
	
		      //System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());
		    } catch (IOException | SpotifyWebApiException e) {
		      System.err.println("Error: " + e.getMessage());
		      System.exit(0);
		    }
	}
	
	/*
	 * Method used to search for specific track
	 * 
	 * */
	private static Track[] searchTrack(LocalTrack element) {
		Track[] trackslist = null;
		String query = null;
		query = prepareQuery(element);
		
		SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query)
		          .limit(1)
		          .offset(0)
		          .build();
		
		try {
		      final Paging<Track> trackPaging = searchTracksRequest.execute();
		      
		      //System.out.println("Total: " + trackPaging.getTotal());
		      
		      if(trackPaging.getTotal() == 0) {
		    	  	System.err.println("There is no song such as "+ query);
		    	  	System.exit(0);
		      }else {
		    	  	trackslist = trackPaging.getItems();
			    return trackslist;
		      }
		      
		} catch (IOException | SpotifyWebApiException e) {
		      System.err.println("Error: " + e.getMessage());
		      System.exit(0);
		}
		
		return null;
	}
	
	private static String prepareQuery (LocalTrack track) {
		String query = null;
		
		if (track.name != null){
			query = track.name ;
			
			if (track.artist != null) {
				query += " "+ track.artist;
			}
			
			if (track.album != null) {
				query += " " + track.album;
			}
		} else {
			System.err.println(Error.TRACK_NOT_NAMED_ERROR);
			System.exit(0);
		}
		//System.out.println("QUERY: "+ query);
		return query;
	} 

	/*
	 * Method basically used for tests
	 * 
	 * */
	public void showCommandsMap(Map<String, LocalPlaylist> commands) {
		for (Map.Entry<String, LocalPlaylist> entry : commands.entrySet())
	    {
	        System.out.println(entry.getKey() + "/" + entry.getValue());
	    }
	}
}

