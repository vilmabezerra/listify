import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.constructor.Constructor;

import com.neovisionaries.i18n.CountryCode;
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

import javax.swing.plaf.basic.BasicSliderUI.TrackListener;



public class Listify{
	/*Information to access Spotify API*/
	private static String clientID = "42fc15b62f8c4b60b845daf59b9adf96";
	private static String clientSecret = "-";
	static SpotifyApi spotifyApi = null;
	 
	/*To get from userconfig.txt file*/
	static String accessToken = null;
    static String userId = null;
    static String name = null;
    
    /*File to be interpreted*/
    static String fileName = null;
   
    
	public static void main (String[] args) throws SpotifyWebApiException, FileNotFoundException {
		    Map<String, LocalPlaylist> parsed = new HashMap<String, LocalPlaylist>();
		    String path = "./src/userconfig.txt";
		    
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
		    			fileName = "./src/"+ args[0];
		    			
		    			String document = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
		    			
		    			System.out.println(document);
		    		    
		    		    System.out.println("---------------------------------");
		    		    
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
				//System.out.println(Error.SYNTAXE_ERROR);
				break;
			}
	    }
		
		System.out.println("Hey, "+ name +", check your Spotify account out to see the changes you made (:");
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
			
			commandDetail = parts[2];
			
		}else {
			commandDetail = null;
		}
		
		return commandDetail;
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

	/*
	 * Method used to execute command create
	 * 
	 * */
	private static void createPlaylist(LocalPlaylist listPlay) {
		
		System.out.println("Creating Playlist " + listPlay.name +" ...");
		
		CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, listPlay.name)
		          .description(listPlay.description)
		          .build();
		
		try {
		      final Playlist playlist = createPlaylistRequest.execute();
		      
		      if (playlist != null) {
		    	  	
		    	  	addTracks(playlist, listPlay);
		    	  	System.out.println("Playlist ID " + listPlay.getName() +" successfully created!");
		    	  	
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
		
		System.out.println("Editing "+ playlist2bEdited + " ...");
		
		// Search for Playlist to be edited
		int ite = 0;
		int quantity = userPlaylists.length;
		while(ite < quantity) {
			
			if(userPlaylists[ite].getName().equals(playlist2bEdited)) {
				
				System.out.println("It is this one!");
				playlistId = userPlaylists[ite].getId();
				playlist = getPlaylist(playlistId);
				
				//Get tracks from Playlist to be edited
				listOfTracks = getPlaylistTracks(playlistId);
				
				//Remove tracks from Playlist, if requested by User
				if (localPlaylist.removeTracks != null) {
					removeTracks(playlist, localPlaylist);
				}
				
				//Add tracks to Playlist, if requested by User
				if (localPlaylist.tracks != null) {
					addTracks(playlist, localPlaylist);
				}
				
				
				break;
			}else {
				System.out.println("!");
				ite++;
			}
		}
		
		if (ite>= quantity) {
			System.err.println(Error.NOT_FOUND_PLAYLIST_ERROR);
			System.exit(0);
		}
	}
	
	/*
	 * Method to remove several Tracks from a Playlist
	 * 
	 * */
	private static void removeTracks(Playlist playlist, LocalPlaylist localPlaylist) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Method to get list of the Tracks of a Playlist
	 * 
	 * */
	private static PlaylistTrack[] getPlaylistTracks(String playlistId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Method to get Playlist object using its spotify ID
	 * 
	 * */
	private static Playlist getPlaylist(String playlistId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/*
	 * Method to get list of User`s Playlists
	 * 
	 * */
	private static PlaylistSimplified[] getListofPlaylists() {
		final GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi
		          .getListOfUsersPlaylists(userId)
		          .limit(10)
		          .offset(0)
		          .build();
		
		try {
		      final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
		      PlaylistSimplified[] playlists = playlistSimplifiedPaging.getItems();
		      return playlists;
		    } catch (IOException | SpotifyWebApiException e) {
		      System.out.println("Error: " + e.getMessage());
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
		String[] tracksIdsv2 = new String[listPlay.tracks.size()];
		
		/*Searching for each track*/
		for (LocalTrack element : listPlay.tracks) {
			
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
		      
		      System.out.println("Total: " + trackPaging.getTotal());
		      
		      if(trackPaging.getTotal() == 0) {
		    	  	System.out.println("There is no song such as "+ query);
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
		System.out.println("QUERY: "+ query);
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

