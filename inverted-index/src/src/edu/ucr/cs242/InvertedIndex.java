package edu.ucr.cs242;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.mapreduce.Counter; 
import edu.ucr.cs242.common.dto.IndexTime;
import edu.ucr.cs242.common.util.CSVUtils;

public class InvertedIndex extends Configured implements Tool  {

	public final int run(final String[] args) throws Exception {
       
        Job job = Job.getInstance(super.getConf(), "Inverted Index");
        job.setJarByClass(InvertedIndex.class);

        /**
         *  Code can be inserted to do the Data Munging
         *  That can include removing stop words, punctuation, etc.
         */

        
        deleteFolder(super.getConf(), args[1]);

        myMapReduceTask(job, args[0], args[1]);
		return 1;
    }

	
	//C:\Users\N903353\Downloads\Deliverables
    public static void deleteFolder(Configuration conf, String folderPath) throws IOException {
        // Delete the Folder
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path(folderPath);
        if (fs.exists(path)) {
            fs.delete(path, true);
        }
    }

    public static void myMapReduceTask(Job job, String inputPath, String outputPath) throws
            IllegalArgumentException,
            IOException,
            ClassNotFoundException,
            InterruptedException {

        // Set Mapper Class
        job.setMapperClass(InvertedIndexMapper.class);

        // Set Mapper Output Types
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // Set Combiner Class
        //job.setCombinerClass(InvertedIndexReducer.class); 

        // Set Reducer Class
       job.setReducerClass(InvertedIndexReducer.class);
        
        //input files are of xml format  
        job.setInputFormatClass(XmlInputFormat.class);

        // Set the Reducer Output Types
        //job.setOutputKeyClass(Text.class);
        //job.setOutputValueClass(NullWritable.class);
        
        job.setNumReduceTasks(5);

        // Specify input and output Directories


        //TODO: validate the next two lines
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        
     // Defines JSON output specification as the mapreduce output
        MultipleOutputs.addNamedOutput(job, "JSON", TextOutputFormat.class,
        		NullWritable.class, Text.class);

        // Defines CSV output specification( key as word and value as the time taken to write the corresponding JSON in reducer) as the mapreduce output 
        MultipleOutputs.addNamedOutput(job, "REDUCERINDEXTIME",
        		TextOutputFormat.class,
        		NullWritable.class, Text.class);
        
        // Wait condition for the Mapper and Reducer Class to finish their execution
       job.waitForCompletion(true) ;
        
        //Executes after mapreduce job gets completeted
        List<IndexTime> IndexDocIdTimeObjectList = new ArrayList<IndexTime> ();
        IndexTime indexDocIdTime=null;
        //Obtain value of custom counters defined key as filename  and value as total time taken to process file in mapper specified in nanoseconds
        CounterGroup counters = job.getCounters().getGroup("MAP_INDEX_TIME"); 
           for(Counter counter:counters){ 
        	   indexDocIdTime= new IndexTime();
        	   indexDocIdTime.setDocName(counter.getDisplayName());// filename in mapper
        	   indexDocIdTime.setIndexerTime(counter.getValue());// totaltime in nanoseconds
        	   IndexDocIdTimeObjectList.add(indexDocIdTime);
            	
          } 
           CSVUtils csvUtils =new CSVUtils();
           
           csvUtils.writeDocIdIndexTimeToCsv("MAPPER_INDEX_TIME.csv", IndexDocIdTimeObjectList); // write to csv in local path from where the jar gets executed
    }


}