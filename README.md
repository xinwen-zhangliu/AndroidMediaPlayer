# Proyecto 2 - MusicPlayer | Class: Modelado y Programación

## Design of the application
This project is using the MVVM architecture, denoting the use of Models, Views and ViewModels. To organize the fragments, a navigation grpah has been used. 


###### The Music Player
To play each media file, a Service class was created to run in the foreground, to avoid interrupting the 
MediaPlayer when  changing fragments or minimizing, and to keep the operation running for as long as the user likes. The music will keep playing until the user
closes the application completely.
 A simple dialog box is shown in the notification panel when the service is started, with the name of the song and a picture if available.

## Database
This project uses the google recommended library, Room. Each entity represents a table in the database, with relations classes in retrieveing and viewing information.



# Usage
## Searches
Every search must start with the key word 
```
search?:
```
the followed by the fields and key word to search, for example 
```
search?:artist:Cage the Elephant
```
be careful not to leave space in between searches and fields.
Available fields are 
```
song, album, artist, year, genre
```


## Adding and editing rows in database tables
To edit Song, Artist or Album information, click ont he three dots beside the item.
On the botton right side you'll also fin two buttons,the oone on the left opens a new list of the performers available in the data base.
And according to their type you'll be able to edi the information, that's why you must choose a type different than unknown to be able to edit the information.
The type information will be saved from the Song infomation edit view, so be careful to edit in that one first before going to the list of performers.


## Adding Media
The application automatically searches for audio files in the device, on both the internal and external storage. But if you've added new songs or if the songs you want do not show up, you can add a folfer path after clicking the botton on the bottom right corner. 
Deleted songs are automatically deleted from the database at the start of the application. 
