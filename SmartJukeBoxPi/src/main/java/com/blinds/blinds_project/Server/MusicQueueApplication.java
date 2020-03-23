package com.blinds.blinds_project.Server;

import com.blinds.blinds_project.MusicManager.MusicQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MusicQueueApplication {

    public static void main(String[] args) {
        Thread t = new Thread(new MusicQueue());
        t.start();
        ConfigurableApplicationContext x = SpringApplication.run(MusicQueueApplication.class, args);

        try {
            t.join();
            x.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
