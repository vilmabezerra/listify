# Listify

## Documentation

This is a language created specially to manage User`s Spotify Playlists, like editing and creating them.

SnakeYaml library is used to parse Listify files.

So only files with .ltfy extension shall be interpreted. In order to do so, compile 
Listify.java giving your .ltfy file as argument.

Second is that User need to set userconfig.txt file in order to manage her playlists.


### User Config file

userconfig.txt file should be written as follow

    Token: -
    UserID: 12151182984
    Name: Vilma

User should get the **Token** requested on userconifg.txt file following the steps bellow:
1. Access the link: https://developer.spotify.com/console/post-playlist-tracks/
2. Click on Get Token
3. Check the options 
    - playlist-modify-public
    - playlist-modify-private
    - user-library-read
    - user-library-modify
4. Click on Request Token
5. Copy token shown inside text box
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
         tracks:
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


##### Action Plan for future versions

- Add command "merge Playlists "
- Allow user to create playlist with Image, public access, description and some other playlist's information
- Allow user to edit name, description, image and some other playlist's information
- Code Refactoring
- Do not ask user for providing a userconfig.txt file



