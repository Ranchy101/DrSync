package heartrate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Logger {
    public static void main(String[] args) throws InterruptedException, IOException {
    	 	//---------------------------------------------PATIENT NAME INPUT-------------------------------------------------------------------------
    		Scanner sc = new Scanner(System.in);  
    		System.out.println("Enter patient first and last name:");
    		String name = sc.nextLine();
    		//once finished
    		sc.close();
    		
    		Runtime.getRuntime().exec("C:\\Users\\Daniel\\eclipse-workspace\\heartrate\\SimulANT+ 2.3.0\\SimulANT+\\SimulANT+.exe", null, new File("C:\\Users\\Daniel\\eclipse-workspace\\heartrate\\SimulANT+ 2.3.0\\SimulANT+\\"));
    		 //----------------------------------------------PRODUCT VERIFICATION-------------------------------------------------------------------------
	    	 File events = new File("C:\\Users\\Daniel\\eclipse-workspace\\heartrate\\SimulANT+ 2.3.0\\SimulANT+\\SimulANT+ Logs - logs\\Heart Rate Display Events.txt");
		     if (events.delete()) {
		         System.out.println("Deleting old logs...");
		         System.out.println("Start logging now.");
		         Thread.sleep(10000);
		         System.out.println("Turn on the heart rate monitor");
		         Thread.sleep(15000);
		         String word = "53848";
		         boolean flag = false;
		         int count = 0;
		         Scanner sc2 = new Scanner(new FileInputStream("C:\\Users\\Daniel\\eclipse-workspace\\heartrate\\SimulANT+ 2.3.0\\SimulANT+\\SimulANT+ Logs - logs\\Heart Rate Display Events.txt"));
		         while(sc2.hasNextLine()) {
		            String line = sc2.nextLine();
		            if(line.indexOf(word)!=-1) {
		               flag = true;
		               count = count+1;
		            }
		         }
		         if(flag) {
		        	 System.out.println("Product ID Verification Successful");
		            //Correct Product ID
		            //do nothing
		         } else {
		            System.out.println("Product ID Verification Failure");
		            //terminate program
		         }
		     }
		     else {
		    	 System.out.println("Failed to delete old logs. Logging must be stopped first on SimulANT+ first.");
		    	 System.out.println("Terminating Program...");
		    	 return;
		     }
		    //------------------------------------------------------------------------------------------------------------------------------------------
		    @SuppressWarnings("resource")
			PrintWriter writer = new PrintWriter("Patient Data.txt", "UTF-8"); // create printable txt file
            int[] heartArray = new int[10];//declare avg array
            int index = 0;
            while(true) {
            long millis = System.currentTimeMillis();
            //code to run every 1000 ms (1 second)
            RandomAccessFile fileHandler = null;
            try {
                File file = new File("C:\\Users\\Daniel\\eclipse-workspace\\heartrate\\SimulANT+ 2.3.0\\SimulANT+\\SimulANT+ Logs - logs\\Heart Rate Display ANT Messages.txt");
                fileHandler = new RandomAccessFile( file, "r" );
                long fileLength = fileHandler.length() - 1;
                StringBuilder sb = new StringBuilder();
    
                for(long filePointer = fileLength; filePointer != -1; filePointer--){
                    fileHandler.seek( filePointer );
                    int readByte = fileHandler.readByte();
    
                    if( readByte == 0xA ) {
                        if( filePointer == fileLength ) {
                            continue;
                        }
                        break;
                        
                    } else if( readByte == 0xD ) {
                        if( filePointer == fileLength - 1 ) {
                            continue;
                        }
                        break;
                    }
    
                    sb.append( ( char ) readByte );
                }
                String lastLine = sb.reverse().toString(); //get last line
                if (lastLine.contains("fail") || lastLine.contains("search")) {
                    //do nothing
                }
                else if (lastLine.contains("closed")) {
                	System.out.println("Device shut off");
                	//break;
                }
                else {
                	String hexString = lastLine.substring(lastLine.length() - 3);
                    String hex = hexString.replaceAll("]","");    //get heartrate hex value
                    int heartrate = Integer.parseInt(hex,16);  //convert to decimal
                    System.out.println(heartrate); //print heartrate
                    heartArray[index] = heartrate;//add to array
                    index++; //only goes up if not fail
                }
            //if array full, print avg
            if (index == 10) {
                //get average of array
                int total = 0;
                for(int i=0; i<heartArray.length; i++){
                    total = total + heartArray[i];
                }
                int avg = total / heartArray.length;
                System.out.println("Average: " + avg); //print avg
                writer.println(name);
                writer.println(avg); //print avg to file
                writer.flush();
                index = 0; //reset index (no need to wipe array as it will be overwritten)
                if (avg < 60 || avg > 100)
                	System.out.println("WARNING");
		Thread.sleep(10000);
                break;
                
            }

            } catch( java.io.FileNotFoundException e ) {
                e.printStackTrace();
                //return null;
            } catch( java.io.IOException e ) {
                e.printStackTrace();
                //return null;
            } finally {
                if (fileHandler != null )
                    try {
                        fileHandler.close();
                    } catch (IOException e) {
                        /* ignore */
                    }
            }
            Thread.sleep(1000 - millis % 1000);
            }//timer
    }//main
}
