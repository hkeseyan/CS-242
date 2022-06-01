package edu.ucr.cs242;


import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Indexer {

    private IndexWriter writer;

    public Indexer() {
    }

    public void init(String directory) {
        try {
            Directory indexDirectory = FSDirectory.open(Paths.get(directory));

            //Use StopAnalyzer to remove stop words ("and," "or," "the," etc.) from the token stream.
            Analyzer analyzer = new StopAnalyzer();

            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            writer = new IndexWriter(indexDirectory, config);

            //create the indexer
            //writer = new IndexWriter(indexDirectory,
            //        new StandardAnalyzer(Version.LUCENE_36),true,
            //        IndexWriter.MaxFieldLength.UNLIMITED);


        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    public void indexDocument(Document document) throws IOException {
        writer.addDocument(document);
    }

    public Document getDocument(Page page) throws IOException {

        Document document = new Document();

        document.add(new TextField(LuceneConstants.TITLE,
                page.getTitle(),
                Field.Store.YES));

        document.add(new TextField(LuceneConstants.CONTENTS,
                page.getData(),
                Field.Store.YES));

        document.add(new TextField(LuceneConstants.URL,
                page.getUrl(),
                Field.Store.YES));

        return document;
    }

}