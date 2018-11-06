import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Class TCPServer ...
 * Stub of a TCP Server ... goes with TCP Client ... connect only
 * @author Pete Lutz
 * @version 9-16-2017
 */
public class TCPServer extends Application {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root;
   
   // GUI Components
   public Label lblLog = new Label("Log:");
   public TextArea taLog = new TextArea();
   
   // Socket stuff
   private ServerSocket sSocket = null;
   public static final int SERVER_PORT = 49153;
   
   /**
    * main program
    */
   public static void main(String[] args) {
      launch(args);
   }
   
   /**
    * Launch, draw and set up GUI
    * Do server stuff
    */
   public void start(Stage _stage) {
      // Window setup
      stage = _stage;
      stage.setTitle("TCPServer1");
      stage.setOnCloseRequest(
         new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) { System.exit(0); }
         } );
      stage.setResizable(false);
      root = new VBox(8);
   
      // TOP components
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      taLog.setPrefRowCount(10);
      taLog.setPrefColumnCount(35);
      fpBot.getChildren().addAll(lblLog, taLog);
      root.getChildren().add(fpBot);
      
      // Show window
      scene = new Scene(root, 475, 200);
      stage.setScene(scene);
      stage.show();      
      
      // Do server work in a thread
      Thread t = 
         new Thread() {
            public void run() { doServerWork(); }
         };
      t.start();
   }
   
   
   /** doServerWork - does the basic non-GUI work of the server */
   public void doServerWork() {
      // Claim the port and start listening for new connections
      try {
         sSocket = new ServerSocket(SERVER_PORT);
      }
      catch(IOException ioe) {
         taLog.appendText("IO Exception (1): "+ ioe + "\n");
         return;
      }
   
      // Socket for comm with client      
      Socket cSocket = null;
      
      while(true) {
         try {
         // Wait for a connection
            cSocket = sSocket.accept();
         }
         catch(IOException ioe) {
            taLog.appendText("IO Exception (2): "+ ioe + "\n");
            return;
         }
      
      // No real processing yet
         taLog.appendText("Client connected!\n");
         DataInputStream in = null;
         DataOutputStream out = null;
         try {
            in = new DataInputStream(cSocket.getInputStream());
            out = new DataOutputStream(cSocket.getOutputStream());
         
         }
         catch(Exception e) {
            taLog.appendText("Exception opening streams: " + e + "]n");
         }
      
         try {
            String line = in.readUTF();
            taLog.appendText("Received: " + line + "\n");
            line = line.toUpperCase();
            taLog.appendText("Replying: " + line + "\n"); 
            out.writeUTF(line);
         }
         catch(Exception e) {
            taLog.appendText("Error during transmission: " + e + "\n");
         }
      
      
         try {
            cSocket.close();
            in.close();
            out.close();
         }
         catch(Exception e) {
            taLog.appendText("Exception closing streams: " + e + "\n");
         } 
      
         taLog.appendText("Client disconnected!\n"); 
      }          
   }   
}