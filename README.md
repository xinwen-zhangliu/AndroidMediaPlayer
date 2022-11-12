# Proyecto 2 - MusicPlayer | Class: Modelado y Programación

## Design of the application
This project is using the MVVM architecture, denoting the use of Models, Views and ViewModels. To organize the fragments, a navigation grpah has been used. 

###### The Music Player
To play each media file, a Service class was created to run in the foreground, to avoid interrupting the 
MediaPlayer when  changing fragments or minimizing, and to keep the operation running for as long as the user likes. 
 A simple dialog box is shown in the notification panel when the service is started. 

## Data Base
This project uses the google recommended library, Room. 




