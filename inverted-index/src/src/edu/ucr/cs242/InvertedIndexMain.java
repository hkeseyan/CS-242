package edu.ucr.cs242;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

public class InvertedIndexMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 Configuration conf = new Configuration();
	        /**
	         * start tag and end tag added
	         */
	        conf.set("xmlinput.start", "<DOC>");
	        conf.set("xmlinput.end", "</DOC>");
			  int res = 0;
			try {
				res = ToolRunner.run(conf, new edu.ucr.cs242.InvertedIndex(), args);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  System.exit(res);
		

	}

}
