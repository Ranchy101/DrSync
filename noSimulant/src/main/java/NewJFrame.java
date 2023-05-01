import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.Timer;



/**
 *
 * @author Glen Roe
 * 
 */


public class NewJFrame extends javax.swing.JFrame {
//december 4
    /**
     * Creates new form NewJFrame, variables can be declared(but not initialized here)
     */
    LinkedList<String> colsUsed = new LinkedList<>();
    
    String IDLogin;
    String currDir = System.getProperty("user.dir");
    ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    //log out
                    dispose();
                    new login(IDLogin).setVisible(true);
                    timer.stop();

                }
           };
    /*display medical history in side window*/
    public void displayHistory(String ID, LinkedList<String> toUse) throws ClassNotFoundException, SQLException{
        //---------------------------------------------JDBC MySQL Setup-------------------------------------------------------------------------
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://database-1.cqwrgzkfrky6.us-west-2.rds.amazonaws.com/SyncData";
        String user = "admin";
        String password = "get-blame-lateral";
        Connection connection = DriverManager.getConnection(url, user, password);
        //---------------------------------------------PATIENT ID INPUT-------------------------------------------------------------------------
        String query = "SELECT * FROM PatientVisits where PatientID=? ORDER BY pkey DESC";
        
        PreparedStatement createquery=connection.prepareStatement(query);
        createquery.setString(1, ID);
        ResultSet returnHist=createquery.executeQuery();
        String histString = "";
        while(returnHist.next()){
            for(int i=0; i<toUse.size(); i++){
                String Temp=returnHist.getString(toUse.get(i));
                if(returnHist.wasNull()){
                    Temp="";
                }
                histString= histString+""+Temp+"     ";
                
            }
            histString=histString+"\n";
        }
        DisplayHistory.setText(histString);



    }
    public void Measurement(String toMeasure, LinkedList<String> Cols){
        //restart timer
             try {
                timer.restart();
                //create mysql connection
                //---------------------------------------------JDBC MySQL Setup-------------------------------------------------------------------------
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://database-1.cqwrgzkfrky6.us-west-2.rds.amazonaws.com/SyncData";
                String user = "admin";
                String password = "get-blame-lateral";
                Connection connection = DriverManager.getConnection(url, user, password);
                //---------------------------------------------PATIENT ID INPUT-------------------------------------------------------------------------
                Scanner sc = new Scanner(System.in);
                String returned="";
                String patientID = ID.getText();
                            int avg;
                            do {
                                int[] heartArray = new int[10];//declare avg array
                                int index = 0;
                                Scanner fileScanner = new Scanner(new File(currDir+"\\ANT-SDK_PC.3.5\\Debug\\"+toMeasure));
                                while (fileScanner.hasNextInt()){
                                    heartArray[index++] = fileScanner.nextInt();
                                }
                                int total = 0;
                                for(int i=0; i<heartArray.length; i++){
                                    total = total + heartArray[i];
                                }
                                //calculate average heartrate of array
                                avg = total / heartArray.length;
                            } while (avg == 0);

                            DisplayResults.setText("Average: " + avg); //print avg
                            String query = "insert into PatientVisits (patientID, heartrate) values (?,?)";
                            PreparedStatement createquery=connection.prepareStatement(query);
                            createquery.setString(1, ID.getText());
                            createquery.setString(2, Integer.toString(avg));
                            Header.setText("Uploaded data");
                            createquery.executeUpdate();
                            query = "SELECT * FROM PatientVisits where PatientID="+ID.getText()+" ORDER BY pkey DESC LIMIT 1";
                            createquery=connection.prepareStatement(query);
                            ResultSet returnlatest= createquery.executeQuery();

                            while (returnlatest.next()){
                                returned=returnlatest.getString("heartRate");
                                CurrentVisit=returnlatest.getInt("pkey");
                            }
                            query = "SELECT * FROM PatientVisits where PatientID="+ID.getText()+" ORDER BY pkey DESC limit 5";
                            createquery=connection.prepareStatement(query);
                            ResultSet runningaverage= createquery.executeQuery();
                            runningaverage.next();
                            int runningavg=0;
                            int divby=0;

                            while(runningaverage.next()){
                                runningavg+=runningaverage.getInt("heartRate");
                                divby++;
                            }
                            if(divby!=0){
                                runningavg=runningavg/divby;
                            }
                            displayHistory(ID.getText(),Cols);

                            DisplayResults.setText("Average: " + returned+"\n recent Heartrate average:"+runningavg); //print avg
            }catch (ClassNotFoundException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }catch (SQLException ex) {
                 Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                 Header.setText("Patient does not exist");
                 DisplayResults.setText("No reading; check patient ID"); //print avg
                 DisplayHistory.setText("");
            }
            catch( java.io.FileNotFoundException e ) {
            }


    }
        int CurrentVisit=-1;
//create and begin logout timer
        Timer timer = new Timer(600000 ,taskPerformer);
/*-------------------------------------------------------------------------------------------------------------------*/
   /*-------------------------------------INITIALIZE METHOD STARTS HERE-----------------------------------------------------------*/
/*-------------------------------------------------------------------------------------------------------------------*/
    public NewJFrame(String IDInput) throws IOException {
        initComponents();
        /*-------------------------------------Cols iteration to allow unused rows-----------------------------------------------------------*/
        
        colsUsed.add("heartRate");
        colsUsed.add("Comment");
        /*-------------------------------------Cols iteration to allow unused rows-----------------------------------------------------------*/
        Boolean Beat=false;
        Runtime.getRuntime().exec(currDir+"\\ANT-SDK_PC.3.5\\Debug\\DEMO_HR_RECEIVER.exe", null, new File(currDir+"\\ANT-SDK_PC.3.5\\Debug\\")); 
        timer.start();
        IDLogin=IDInput;

        timer.start();
         ImageIcon icon = new ImageIcon (currDir+"\\ANT-SDK_PC.3.5\\Debug\\logo.png");
         Image img = icon.getImage();
         Image imgscale = img.getScaledInstance(iconLabel.getWidth(), iconLabel.getHeight(), Image.SCALE_FAST);
         ImageIcon ScaledIcon = new ImageIcon(imgscale);
         iconLabel.setIcon(ScaledIcon);
    
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        DisplayResults = new javax.swing.JTextArea();
        Header = new javax.swing.JLabel();
        Heartrate = new javax.swing.JButton();
        ID = new javax.swing.JTextField();
        CommentBox = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        DisplayHistory = new javax.swing.JTextArea();
        CommentButton = new javax.swing.JButton();
        iconLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        DisplayResults.setColumns(20);
        DisplayResults.setRows(5);
        DisplayResults.setText("\t1. Press Logging button\n\n         RESULTS WILL APPEAR HERE");
        jScrollPane1.setViewportView(DisplayResults);

        Header.setText("Dr Sync Measurement System");

        Heartrate.setText("Heart Rate");
        Heartrate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HeartrateActionPerformed(evt);
            }
        });

        ID.setText("Medical Record Number");
        ID.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                IDMouseDragged(evt);
            }
        });
        ID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IDMouseClicked(evt);
            }
        });
        ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDActionPerformed(evt);
            }
        });
        ID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IDKeyPressed(evt);
            }
        });

        CommentBox.setText("Comment");
        CommentBox.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                CommentBoxMouseDragged(evt);
            }
        });
        CommentBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CommentBoxMouseClicked(evt);
            }
        });
        CommentBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CommentBoxActionPerformed(evt);
            }
        });
        CommentBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CommentBoxKeyPressed(evt);
            }
        });

        DisplayHistory.setColumns(20);
        DisplayHistory.setRows(5);
        DisplayHistory.setText("Record");
        jScrollPane2.setViewportView(DisplayHistory);

        CommentButton.setText("Comment");
        CommentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CommentButtonActionPerformed(evt);
            }
        });

        iconLabel.setIcon(new javax.swing.ImageIcon("C:\\Users\\Groe\\Downloads\\unnamed.png")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(iconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(142, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ID, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CommentBox, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Heartrate))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 3, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(CommentButton)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(Header))
                    .addComponent(iconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CommentBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CommentButton)
                    .addComponent(Heartrate))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(174, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HeartrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HeartrateActionPerformed
        Measurement("ANTHeartrate.txt", colsUsed);
    }//GEN-LAST:event_HeartrateActionPerformed

    private void IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDActionPerformed

    }//GEN-LAST:event_IDActionPerformed

    private void IDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IDKeyPressed
        if(ID.getText().equals("Medical Record Number"))
        {
            ID.setText("");
        }
    }//GEN-LAST:event_IDKeyPressed

    private void CommentBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CommentBoxActionPerformed
       
    }//GEN-LAST:event_CommentBoxActionPerformed

    private void CommentBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CommentBoxKeyPressed
        if(CommentBox.getText().equals("Comment"))
        {
            CommentBox.setText("");
        }
    }//GEN-LAST:event_CommentBoxKeyPressed

    private void CommentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CommentButtonActionPerformed
        
        try {
            ///restart timer
            timer.restart();
            //create mysql connection
            //---------------------------------------------JDBC MySQL Setup-------------------------------------------------------------------------
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://database-1.cqwrgzkfrky6.us-west-2.rds.amazonaws.com/SyncData";
            String user = "admin";
            String password = "get-blame-lateral";
            Connection connection = DriverManager.getConnection(url, user, password);
            //---------------------------------------------PATIENT ID INPUT-------------------------------------------------------------------------
            //Header.setText(Integer.toString(CurrentVisit));
            String query = "UPDATE PatientVisits SET Comment= ? WHERE pkey = ?";
            PreparedStatement createquery=connection.prepareStatement(query);
            createquery.setString(1, CommentBox.getText());
            createquery.setString(2, Integer.toString(CurrentVisit));
            createquery.executeUpdate();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            displayHistory(ID.getText(), colsUsed);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_CommentButtonActionPerformed

    private void IDMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IDMouseDragged
        if(ID.getText().equals("Medical Record Number"))
        {
            ID.setText("");
        }
    }//GEN-LAST:event_IDMouseDragged

    private void IDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IDMouseClicked
        if(ID.getText().equals("Medical Record Number"))
        {
            ID.setText("");
        }
    }//GEN-LAST:event_IDMouseClicked

    private void CommentBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CommentBoxMouseClicked
        if(CommentBox.getText().equals("Comment"))
        {
            CommentBox.setText("");
        }
    }//GEN-LAST:event_CommentBoxMouseClicked

    private void CommentBoxMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CommentBoxMouseDragged
        if(CommentBox.getText().equals("Comment"))
        {
            CommentBox.setText("");
        }
    }//GEN-LAST:event_CommentBoxMouseDragged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               // try {
               //     new NewJFrame().setVisible(true);
               // } catch (IOException ex) {
              //      Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
               // }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CommentBox;
    private javax.swing.JButton CommentButton;
    private javax.swing.JTextArea DisplayHistory;
    private javax.swing.JTextArea DisplayResults;
    private javax.swing.JLabel Header;
    private javax.swing.JButton Heartrate;
    private javax.swing.JTextField ID;
    private javax.swing.JLabel iconLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
