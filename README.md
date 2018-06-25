# Listify

## Documentation

This is a language created specially to manage User`s Spotify Playlists.

SnakeYaml is used to parse Listify files.

First thing is that only files with .ltfy extension shall be interpreted. In order to do so, compile 
Listify.java giving your .ltfy file as argument

Second is that User need to set userconfig.txt file in order to manage her playlists.


### User Config file

userconfig.txt file should be written as follow

    Token: -
    UserID: 12151182984
    Name: Vilma

User should get the Token requested on userconifg.txt file following the steps bellow:
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

File with Listify extension (.ltfy) should be given as argument when compiling Listify.java.

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
      edit Playlist Test:
         removeTracks: 
             - name: Andança
