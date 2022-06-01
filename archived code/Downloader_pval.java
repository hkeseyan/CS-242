package edu.ucr.cs242;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

public class Downloader_pval implements Callable<DownloaderResult> {

    public static String USER_AGENT = "Mozilla/5.0 (compatible; cs242-crawler/1.0; +https://github.com/jorgemariomercado/cs242-crawler/wiki/Bot)";

    private Page pageToDownload;
    private Queue<Page> queue;
    private Indexer indexer;

    public Downloader_pval(Queue<Page> queue, Page pageToDownload, Indexer indexer) {
        this.queue = queue;
        this.pageToDownload = pageToDownload;
        this.indexer = indexer;
    }


    /**
     * Download the URL, collect and post URLs to queue,
     * and send the page data to the indexer
     */
    @Override
    public DownloaderResult call() throws Exception {

        DownloaderResult result = new DownloaderResult(this.pageToDownload);

        try {

            long start = System.currentTimeMillis();

            Document doc = Jsoup
                    .connect(this.pageToDownload.getUrl())
                    .userAgent(USER_AGENT)
                    .get();

            long stop = System.currentTimeMillis();
            result.setDownloadTime(stop - start);

            Elements e = doc.getElementsByTag("html");
            if (e.hasAttr("lang") && !e.attr("lang").equals("en")) {
                result.setResult(true);
                result.setSkipped(Boolean.TRUE);
                return result;
            }

            //select element that identifies main content section of page
            Element content = doc.getElementById("mw-content-text");

            //select elements corresponding to personal information on side-bar
            Elements personalinfo = doc.getElementsByClass("pi-data");

            int n = personalinfo.size();            // count # of "personal info" item tags
            String[] plab = new String[n];  // array to be filled with property labels
            String[] pval = new String[n];  // array to be filled with property values

            int i = 0;  //counter
            for (Element property : personalinfo) {
                Elements label = property.getElementsByClass("pi-data-label");
                Elements value = property.getElementsByClass("pi-data-value");

                plab[i] = label.text();
                pval[i] = value.text();
                i++;
            }

            // 1. Get all URLs in page and post to queue
            Elements anchors = doc.select("a[href]");

            Set<Page> pages = new HashSet<>();
            int depth = this.pageToDownload.getDepth();
            depth++;
            for (Element anchor : anchors) {

                URL _u = new URL(anchor.absUrl("href"));

                //do not crawl a page that leaves the site
                if (new URL(this.pageToDownload.getUrl()).getHost().equals(_u.getHost())) {
                    Page p = new Page(  _u.toString(),
                                        anchor.attr("title"),
                                        depth,
                                        content.outerHtml(),
                                        plab,
                                        pval);

                    pages.add(p);
                }

            }

            postUrlsToQueue(pages);

            this.pageToDownload.setData(content.outerHtml());
            this.pageToDownload.setData(plab);
            this.pageToDownload.setData(pval);

            // 2. Send page to indexer
            indexPage(this.pageToDownload);

            result.setResult(true);

            System.out.println(result);
            return result;

        } catch (Exception ex) {
            result.setThrowable(ex);
            result.setResult(false);
            System.out.println(result);
            return result;
        }
    }

    /**
     * @param pages
     */
    private void postUrlsToQueue(Set<Page> pages) {
        for (Page p : pages) {
            queue.add(p);
        }

        System.out.println("Queue size: " + queue.size());
    }

    private void indexPage(Page page) throws IOException {
        indexer.indexDocument(indexer.getDocument(page));
    }
}
