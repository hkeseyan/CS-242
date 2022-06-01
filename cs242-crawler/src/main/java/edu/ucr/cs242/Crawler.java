package edu.ucr.cs242;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Crawler {

    private BlockingQueue<Page> queue;

    public Crawler() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public static void main(String[] args) {

        try {

            String site = "http://lotr.wikia.com";
            Crawler c = new Crawler();

            c.go(site, "/robots.txt");

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void go(String url, String robotsFile) {

        try {

            Page p = new Page();
            p.setUrl(url);
            queue.add(p);

            Scheduler scheduler = new Scheduler(queue);
            scheduler.parseRobots(url, robotsFile);

            new Thread( scheduler  ).start();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
