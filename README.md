# Listify 
## version 1.0

## Documentation

This is a language created specially to manage User`s Spotify Playlists, like editing and creating them.

SnakeYaml library is used to parse Listify files.

So only files with .ltfy extension shall be interpreted. In order to do so, compile 
Listify.java giving your .ltfy file as argument.

Also, User need to set userconfig.txt file in order to be able to manage her playlists.


### User Config file

userconfig.txt file should be written as follow

    Token: -
    UserID: 12151182984
    Name: Vilma

This file should be located inside Project folder in order to be read.
User should get the **Token** requested to userconifg.txt file by following the steps bellow:
1. Access the link: https://developer.spotify.com/console/post-playlist-tracks/
2. Click on Get Token
3. Check the options 
    - playlist-modify-public
    - playlist-modify-private
    - user-library-read
    - user-library-modify
4. Click on Request Token
5. Copy token shown inside the text box
6. Paste token on userconfig.txt file
    
### Listify extension file

File with Listify extension (.ltfy) should be given as argument when compiling Listify.java. The file to be interpreted must be located inside Project folder.

Listify files should follow YAML syntax in a way similar to the example that follows.

*Note: Do not use tabs to indentate*

#### Example

    listify:
      create Playlist:
         name: Test
         description: Lorem Ipsum
         addTracks:
             - name: Love on Top
               artist: Beyoncé
             - name: Indecente
               artist: Anitta
      edit Playlist Test #2:
         addTracks:
             - name: Andança
               artist: Elis Regine
         removeTracks: 
             - name: I Guess
               artist: ABRA
               

The commands are interpreted as keys so far. Thus, to repeat a command (create or edit), it should be added another 
character to the last word of the command (eg. "create Playlist1:", "create Playlist2:"). Or, in case of using command edit,
it should be applied to a Playlist with a different name.

### Playlists

Until now the playlists are able to have 
 - Name
 - Description 
 
In order to add tracks, it should be used 
   addTracks:
Notice that this is possible to be used while editing or creting a playlist.

On the other side, in order to remove tracks, it should be used
   removeTracks:
This can be used only while editing the playlists.

All tracks listed bellow these commands  shall be either added or removed from the playlist User has indicated.

### Tracks

Until now the playlists are able to have 
 - Name
 - Artist
 - Album
 
And these information will be used to search for these tracks in order to remove or add them to a playlist.

### Command "create"

This command has as interpretation the creation of a playlist on User' Spotify account. 

The new playlist should be named, however description and the list of tracks to be added are optional.

In case User wants to add Tracks, she must do it as an undordered list preceded by "addTracks:" and using hifens before providing the Track information

### Command "edit"

This command has as interpretation the editing of a playlist on User' Spotify account. 

It should be provided the name of the playlist to be edited. 
*Notice that it is possible to have more than one playlists with the same name. In this case, the playlist edited will 
be the one with the most recent creation date*

In case User wants to add or remove Tracks, she must do it as an undordered list preceded by "addTracks:" or "removeTracks:"
and using hifens before providing the Track information.

## Action Plan for future versions

- Add command "merge Playlists";
- Allow user to create playlist with Image, public access, collaborative and some other playlist's information;
- Allow user to edit name, description, image and some other playlist's information;
- Do some Code Refactoring;
- Do not need to ask user to provide a userconfig.txt file;



