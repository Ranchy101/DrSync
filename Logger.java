package heartratelogger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Logger {
    public static void main(String[] args) throws InterruptedException {
            int[] heartArray = new int[10];//declare avg array
            int index = 0;
            while(true) {
            long millis = System.currentTimeMillis();
            //code to run every 1000 ms (1 second)
            RandomAccessFile fileHandler = null;
            try {
                File file = new File("C:\\Users\\dranc\\Desktop\\SimulANT+ 2.3.0/SimulANT+/SimulANT+ Logs - logs/Heart Rate Display ANT Messages.txt");
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
                	break;
                }
                else {
                    String hex = lastLine.substring(45,47);    //get heartrate hex value
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
                index = 0; //reset index (no need to wipe array as it will be overwritten)
                if (avg < 60 || avg > 100)
                	System.out.println("WARNING");
                
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