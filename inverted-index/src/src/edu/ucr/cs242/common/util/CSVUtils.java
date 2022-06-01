package edu.ucr.cs242.common.util;



import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

import edu.ucr.cs242.common.dto.IndexTime;

/*
 * Write  object list to CSV
 */
public class CSVUtils {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    //CSV file header
    private static final String FILE_HEADER = "DocTitle,IndexerTime";
    
    /***
     * entry point where all the methods are called
     * @param fileName
     * @param IndexTimeObjectList
     */
    public void writeDocIdIndexTimeToCsv(String fileName,List<IndexTime> IndexTimeObjectList){
    	try{
    		FileWriter fileWriter=openCsvFile(fileName);
    		writeCsvFile(fileWriter,IndexTimeObjectList);
    		closeCsvFile(fileWriter);
    		
    	}
    	catch(Exception e)
    	{
    		System.out.println("Error in writing to scv file !!!");
    		e.printStackTrace();
    		
    	}
    	
    	
    }

    /***
     * Open csv file
     * @param fileName
     * @return FileWriter object
     */
    public FileWriter openCsvFile (String fileName) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName,true);
            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            return fileWriter;

        } catch (Exception e) {
            System.out.println("Error in CsvFileOpen !!!");
            e.printStackTrace();

        } 
		return fileWriter;
    }
    
    /***
     * Write to csv file
     * @param fileWriter
     * @param IndexTimeObjectList
     */
    public static void writeCsvFile(FileWriter fileWriter, List<IndexTime> IndexTimeObjectList) {

        try {
        	
        
            for (IndexTime IndexTime : IndexTimeObjectList) {
            	//Write a new IndexDocId object  to the CSV file
                fileWriter.append(IndexTime.getDocName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(IndexTime.getIndexerTime()));
                //fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();

        } 
    }
    
    /**
     * Close csv file .
     * @param fileWriter
     */
    public void closeCsvFile(FileWriter fileWriter){
    	

        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
        }
    
    }
    
}
