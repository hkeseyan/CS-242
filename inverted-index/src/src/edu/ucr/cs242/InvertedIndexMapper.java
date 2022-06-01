package edu.ucr.cs242;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;

import java.util.StringTokenizer;

import edu.ucr.cs242.common.dto.*;
import edu.ucr.cs242.common.util.JaxbUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;


import javax.xml.bind.JAXBContext;


public class InvertedIndexMapper extends Mapper<Object, Text, Text, Text> {

    private JAXBContext jaxbContext = null;

    private Text word = new Text();

    private static String MAP_INDEX_TIME = "MAP_INDEX_TIME";

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
    	
        
    	long start = System.nanoTime();// starttime in nanoseconds
    	
        String fileName = ((FileSplit) context.getInputSplit()).getPath().getName(); // current filename
        
        
        //TODO:  Migrate code to use the Page object
    	try{
        Page p = xmlToPage(value.toString());
      
        String docNo = p.getDocNo();
        //TODO: Content has special characters and spaces and stop words. It should be captured and cleansed before data set to key
        String docTitleAndContent = p.getTitle() + " " + p.getContent();
        int pos = 0;
        StringTokenizer itr = new StringTokenizer( docTitleAndContent );

        while (itr.hasMoreTokens()) {
        	
        	String contentToken = itr.nextToken();
        	pos++;
        	// Ignore  empty strings and exclude invalid data from being indexed
        	if(contentToken != null && !contentToken.trim().isEmpty()){
        	
            
            String docNoAndPos = docNo + "~" + pos; 
            Text docNoAndPosText = new Text(docNoAndPos);

            word.set(contentToken);
            
            //@TODO: Stop words elimination
            context.write(word, docNoAndPosText);
            
        	}
        }}catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    	long end =System.nanoTime();
    	
    	long timeDiff= end -start;
    	
    	context.getCounter(MAP_INDEX_TIME,fileName).increment(timeDiff); // add the time against the filename counter in nanoseconds
    	
    }

    private Page xmlToPage(String value) {

        try {

            try {
                if (this.jaxbContext == null) {
                    this.jaxbContext = JAXBContext
                            .newInstance(Page.class,
                                    Image.class,
                                    Images.class,
                                    HttpHeader.class,
                                    HttpHeaders.class,
                                    OutgoingLink.class,
                                    OutgoingLinks.class);

                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }


            InputStream stream = new ByteArrayInputStream(value.trim().getBytes());

            Page page = JaxbUtils.getInstance(jaxbContext).xmlToBean(stream);
            return page;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
