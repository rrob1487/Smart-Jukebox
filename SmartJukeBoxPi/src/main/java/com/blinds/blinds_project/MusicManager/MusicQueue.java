package com.blinds.blinds_project.MusicManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicQueue implements Runnable
{

    private static ArrayList<Song> queue;
    private static PlayableSong currentSong, nextSong;
    private static boolean running;

    public MusicQueue(){
        queue = new ArrayList<Song>();
        currentSong = null;//new PlayableSong("Champagne", "Ganja White Night", "https://open.spotify.com/track/12meTNzgcD8Eup11KXEDR6", "Robbie");
        nextSong = null;//new PlayableSong("Flight of The Flamingo", "Flamingosis", "https://soundcloud.com/flamingosis/flight-of-the-flamingo-2", "Robbie");
        //queue.add(new Song(currentSong));
        //queue.add(new Song(nextSong));
        running = true;
    }

    @Override
    public void run() {
        while(running){
            if(MusicQueue.getCurrentSong() == null || queue.size() == 0){
                restThread(5);
                System.out.println("No Songs In Queue");
                continue;
            }
            currentSong.play();
            next();

        }
    }

    public static PlayableSong getCurrentSong(){
        return MusicQueue.currentSong;
    }

    public static PlayableSong getNextSong(){
        return MusicQueue.nextSong;
    }

    public static synchronized ArrayList<Song> getQueue(){
        return queue;
    }

    public static String addSong(Song s){
        System.out.println("Song To Add:\n" + s);

        //Check That Its Not A Spam
        for (Song q: queue) {
            if(s.getName().equals(q.getName())){
                System.out.println("Song To Add:\n" + s);
                return "Song Is Already In List";
            }
        }

        if(currentSong == null){
            queue.add(s);
            currentSong = loadSong(s);
        }
        else if(nextSong == null){
            queue.add(s);
            nextSong = loadSong(s);
        }
        else{
            queue.add(s);
        }
        return "Song Added. It Is #" + queue.size()+ " In The Queue";
    }


    public static void stopRunning(){
        MusicQueue.running = false;
    }

    //Deletes Current Song (File And From Queue), sets current song to next song, then initializes next song
    private void next(){
        /*currentSong.delete();
        if(queue.size() > 0) {
            queue.remove(0);
        }
        currentSong = nextSong;
        if(queue.size() < 2){
            System.out.println("Next Song Set To: Null");
            nextSong = null;
            return;
        }
        System.out.println("Current Song Set To: " + nextSong.getName());
        nextSong = loadSong(queue.get(1));
        System.out.println("Next Song Set To: " + nextSong.getName());*/

        currentSong.delete(); // Call To Delete File And Manage Memory On Device
        if(queue.size() > 0){
            queue.remove(0);
        }

        switch(queue.size()){
            case 0 :
                currentSong = null;
                break;
            case 1 :
                currentSong = nextSong;
                nextSong = null;
                break;
            default :
                currentSong = nextSong;
                nextSong = loadSong(queue.get(1));
                break;
        }
        System.out.println("Current Song Set To: " + currentSong);
    }

    private static PlayableSong loadSong(Song s){
        return new PlayableSong(s);
    }

    private static void restThread(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

