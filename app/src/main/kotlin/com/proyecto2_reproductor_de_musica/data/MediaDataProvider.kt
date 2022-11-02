
package com.proyecto2_reproductor_de_musica.data

class MediaDataProvider {
    companion object{
        var fakeSongsList = listOf(
                MediaItemData("person1",
                    "s1",
                    "subtitle1 ",
                    //.parse("android.resource://app/src/main/raw/cage_the_elephant_trouble"),
                    true,
                    0) ,
            MediaItemData("person2",
                "s2",
                "subtitle2 ",
                //Uri.parse("android.resource://app/src/main/raw/cage_the_elephant_trouble"),
                true,
                0)
        )
    }
}