package edu.ucr.cs242;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class Scheduler implements Runnable {

    private BlockingQueue<Page> queue;

    private HashSet<String> completedSet = new HashSet<>();
    private Set<String> disallowed = new HashSet<>();

    private final int MAX_DEPTH = 2;

    private Indexer indexer;

    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public Scheduler(BlockingQueue<Page> queue) {
        this.queue = queue;

        String currentDir = System.getProperty("user.dir");

        this.indexer = new Indexer();
        this.indexer.init(currentDir);

    }

    public void parseRobots(String url, String robotsFile) {

        BufferedReader bufReader = null;
        try {

            URL robotURL = new URL(url + robotsFile);

            bufReader = new BufferedReader(new InputStreamReader(robotURL.openStream()));

            String agent = "User-agent: *";

            String line = null;
            boolean userAgentFound = false;

            while ((line = bufReader.readLine()) != null) {

                if (userAgentFound
                        && line.startsWith("User-agent: ")
                        && !line.equals(agent)) {
                    break;
                }

                if (line.startsWith(agent)) {
                    userAgentFound = true;
                }

                if (userAgentFound && line.startsWith("Disallow")) {
                    String s = url + line.replace("Disallow: ", "");
                    disallowed.add(s);
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (Exception ex) {
                    //no op
                }
            }
        }
    }

    public void run() {


        List<Future<DownloaderResult>> futures = new ArrayList<>();

        // CompletionService<DownloaderResult> completionService =
        //        new ExecutorCompletionService<DownloaderResult>(executor);

        try {

            Page page = queue.peek();

            while (page != null) {

                if (page.getUrl() == null) {
                    continue;
                }

                try {

                    //skip URLs already processed AND
                    // those that are disallowed by the robots.txt
                    if (completedSet.contains(page.getUrl())
                            || disallowed.contains(page.getUrl())
                            || page.getDepth() > MAX_DEPTH) {

                        if(!queue.remove(page)) {
                            System.out.println("Something wrong, not able to remove: " + page.toString());
                        }
                    } else {

                        Downloader downloader = new Downloader(queue, page, indexer);
                        Future<DownloaderResult> future = executor.submit(downloader);
                        futures.add(future);

                        boolean r = queue.remove(page);
                        completedSet.add(page.getUrl());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //allow some time prime the queue
                if (queue.size() < 10) {
                    Thread.sleep(3000);
                } else {
                    Thread.sleep(1000);
                }

                page = queue.peek();
            }

            //now retrieve the futures after computation (auto wait for it)

            /*
            int received = 0;
            while(received < futures.size()) {
                Future<DownloaderResult> resultFuture = completionService.take();
                DownloaderResult result = resultFuture.get();
                received ++;
            }
             */

            System.out.println("Shutting down executor");
            executor.shutdown();
            indexer.close();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
