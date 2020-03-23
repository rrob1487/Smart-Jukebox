package com.blinds.blinds_project.Server;


import com.blinds.blinds_project.MusicManager.MusicQueue;
import com.blinds.blinds_project.MusicManager.PlayableSong;
import com.blinds.blinds_project.MusicManager.Song;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/api")
public class ApiController {


    @GetMapping("/current")
    public String getCurrent(){
        Gson gson = new Gson();
        Song s;
        if(MusicQueue.getCurrentSong() != null){
            s = new Song(MusicQueue.getCurrentSong());
        }
        else{
            return gson.toJson(new PlayableSong("No Songs In Queue", "", "", ""));
        }
        return gson.toJson(s);
    }

    @GetMapping("/next")
    public String getNext(){
        Gson gson = new Gson();
        Song s;
        if(MusicQueue.getNextSong() != null){
            s = new Song(MusicQueue.getNextSong());
        }
        else{
            return gson.toJson(new PlayableSong("No Songs In Queue", "", "", ""));
        }
        return gson.toJson(s);
    }

    @GetMapping("/likes")
    public String getLikes(){
        Gson gson = new Gson();
        return gson.toJson(MusicQueue.getCurrentSong().getLikes());
    }
    @GetMapping("/list")
    public String getList(){
        Gson gson = new Gson();
        ArrayList<Song> temp = new ArrayList<>(MusicQueue.getQueue());
        if(temp.size()>0){
            temp.remove(0);
        }
        return gson.toJson(temp);
    }

    @PostMapping("/add")
    public String addToList(@RequestBody String jsonString){
        System.out.println("New Song: \n" + jsonString);
        Gson gson = new Gson();
        Song s = gson.fromJson(jsonString, Song.class);
        return MusicQueue.addSong(s);
    }

    @PutMapping("/l/like")
    public void like(){
        MusicQueue.getCurrentSong().like();
    }

    @PutMapping("/dislkike")
    public void dislike(){
        MusicQueue.getCurrentSong().dislike();
    }

    @GetMapping("/QUIT")
    public String exit(){
        MusicQueue.stopRunning();
        return "Server Shutting Down";
    }
}
