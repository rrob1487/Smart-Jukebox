package com.blinds.blinds_project.MusicManager;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class PlayableSong extends Song implements Runnable{

    private int likes;
    private File file = new File("hello.exe");
    private boolean downloaded = false;

    public PlayableSong(String name, String artist, String link, String user){
        super(name, artist, link, user);
        likes = 0;
        Thread t = new Thread(this);
        t.start();
    }

    public PlayableSong(Song s){
        super(s);
        likes = 0;
        Thread t = new Thread(this);
        t.start();
    }

    public void like(){this.likes++;}

    public void dislike(){this.likes--;}

    public int getLikes(){
        return this.likes;
    }

    public void play(){
        while(!downloaded){
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Song Not Yet Downloaded");
            } catch (InterruptedException e) {
            }
        }

        try {
            ProcessBuilder   ps=new ProcessBuilder("mpg123", this.file.getName());
            ps.redirectErrorStream(true);
            Process pr = null;
            pr = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            pr.waitFor();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(){
        boolean f = file.delete();
        System.out.println(this.getName() + " Deleted = " + f);
    }

    private void downloadSong(){
        System.out.println("Starting Download Of: " + this.getName());
        String link = this.getLink();
        try {
            ProcessBuilder ps = null;
            if(link.contains("soundcloud")) {
                //System.out.println("Soundcloud");
                ps = new ProcessBuilder("scdl", "-c", "-l", link);
                file = new File(this.getName() + ".mp3");
            }else{
                //System.out.println("Spotify");
                ps = new ProcessBuilder("spotdl", "-s", link, "-f", "./");
                String name = this.getArtist() + " - " + this.getName();
                name = name.replaceAll(Pattern.quote("."), "");
                name = name.replaceAll(Pattern.quote("\""), "");
                name = name.replaceAll(Pattern.quote(","), "");
                name = name.replaceAll(Pattern.quote("$"), "");
                name = name.replaceAll(Pattern.quote("&"), "");
                name = name.replaceAll(Pattern.quote("@"), "");
                name = name.replaceAll(Pattern.quote("!"), "");
                name = name.replaceAll(Pattern.quote("/"), "-");
                name = name.replaceAll(Pattern.quote("+"), "");
                name += ".mp3";
                file = new File(name);
                System.out.println("FileName: "+ name);
            }
            file.delete();
            ps.redirectErrorStream(true);
            Process pr = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.print(line+"\r");
            }
            pr.waitFor();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(this.getName() + " Done Downloading");
        this.downloaded = true;
    }

    @Override
    public void run() {
        this.downloadSong();
    }
}
