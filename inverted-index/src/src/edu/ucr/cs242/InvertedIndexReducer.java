package edu.ucr.cs242;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;



/**
 * reducer yields multiple outputs
 * 	1.Actual output with indexde json (file name :JSON)
 * 	2.CSV with Keyword,timetaken (filename :REDUCER_INDEX_TIME)
 * Part-r-000 files would be empty
 * @author
 *
 */

public class InvertedIndexReducer extends Reducer<Text, Text, Text, NullWritable> {
	
	private MultipleOutputs mos;

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		mos = new MultipleOutputs(context); 

	}
	
    public void reduce(Text keyIndexWord, Iterable<Text> valuesDocNoAndPos, Context context)
            throws IOException, InterruptedException {
    	
    	long start= System.nanoTime();
        // This set will store the names of the document where the key had word(key) appeared
        Set<String> docNoAndPos = new HashSet<>();
        HashMap<String, ArrayList<String>> docIdKeypositions =new HashMap<String, ArrayList<String>>();// docid ,<positionarray>


        for (Text valueDocNoAndPos : valuesDocNoAndPos) {
        	//len=len+1;
            // Duplicates not included
            String[] splitValueDocNoAndPos = valueDocNoAndPos.toString().split("~");
            String docId =splitValueDocNoAndPos[0];
            String frequency=splitValueDocNoAndPos[1];
            ArrayList<String> values = docIdKeypositions.get(docId);
            if (values != null && !values.isEmpty() ) {
            	if(!values.contains(frequency)){
            		docIdKeypositions.get(docId).add(frequency);
            	}
                
            } else {
            	
            	ArrayList<String> pos= new ArrayList<String>();
            	pos.add(splitValueDocNoAndPos[1]);
            	docIdKeypositions.put(docId,pos);
            }
            
            
            docNoAndPos.add(frequency);
        }
        
       
      
        String indexString = "";

        // Read the set one by one and concat to a string
     
      
       
        for (String valueDocNoAndPos : docNoAndPos) {
            indexString = new String(indexString.concat(valueDocNoAndPos.concat(" ")));
        	
        	
        }
        String keyIndexString=keyIndexWord.toString();
        JSONObject jsnMain = new JSONObject();
        jsnMain.put("KEYWORD",keyIndexString);
        
        JSONObject jsn = null;
        JSONArray jsn1 =null;
        ArrayList<JSONObject> jsonobjArray = new ArrayList<JSONObject>();
        for (Map.Entry<String, ArrayList<String>> entry : docIdKeypositions.entrySet()) // Array of json objects for each keyword
        {
            
        	 jsn = new JSONObject();
        	 jsn1 =new JSONArray();
        	 jsn1.add(entry.getValue());
        	  try {
      			//
      			jsn.put("DOCID",entry.getKey());
      			jsn.put("KEYWORDPOSITIONS",jsn1);
      			jsn.put("FREQUENCY",entry.getValue().size());
      			jsonobjArray.add(jsn);
      			
      		} 
              
              catch (Exception e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
        	
        	  
        }
        
        jsnMain.put("DOCS", jsonobjArray);
        mos.write(new Text(jsnMain.toString()), null,"JSON"); // Json oputput to file
       
        
        	
        long end= System.nanoTime();
        long timeDiff= end- start;
        String timeDiffString =Long.toString(end- start);
        
       // context.write(keyIndexWord, new Text(indexString));
        mos.write( new Text(keyIndexString+","+timeDiffString),null,"REDUCERINDEXTIME"); // csv output
        
    }
    
    protected void cleanup(Context context) throws IOException, InterruptedException {
    	   mos.close();
    	 }
}