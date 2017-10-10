/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orbit.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;

import javafx.animation.Timeline;
import javafx.application.Platform;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Duration;


/**
 *
 * @author General Steinacoz
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private TextField apogeeField;
    @FXML
    private TextField perigeeField;
    @FXML
    private Button calculateBtn;
    @FXML
    private Label radiusApogeeField;
    @FXML
    private Label radiusPerigeeField;
    @FXML
    private Label axisField;
    @FXML
    private Label ecceField;
    @FXML
    private Label motionField;
    @FXML
    private Label periodField;
    @FXML
    private Button startBtn;
    @FXML
    private Label timerField;
    @FXML
    private Label altField;
    @FXML
    private Label velocityField;
    @FXML
    private Label trueField;
    @FXML
    private Label meanField;
    @FXML
    private Label orbitField;
    @FXML
    private Label statusField;
    
    public double i = 0.0;
     
     public double T, timer; //variable name of the period
     
      int completed_orbit = 0;
      
      Double apogee_time, perigee_time ;
      Double Ma;
      Double Vm;
      Double alt;
      Double vel;
      
      private Timeline timeline;
      Double timeSeconds;
    @FXML
    private TextField speedField;

    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
        startBtn.setDisable(true); //this line will disable the start button
    }    

    @FXML
    private void calculateBtnAction(ActionEvent event) {
        
   if(apogeeField.getText().toString().equals("") || apogeeField.getText().toString().length() == 0){
       apogeeField.setText("Please enter value for apogee");
   } else if(perigeeField.getText().toString().equals("") || perigeeField.getText().toString().length() == 0){
       perigeeField.setText("Please enter value for perigee");
   } else{   
        calcParams();
        startBtn.setDisable(false); //this line will enable the start button
   }

    }

    @FXML
    private void startBtnAction(ActionEvent event) {
       if(timeline != null){
           timeline.stop();
       }
       timeSeconds = 0.0;
     //timeSeconds = Double.parseDouble(periodField.getText());
       timerField.setText(timeSeconds.toString());
       timeline = new Timeline();
       timeline.setCycleCount(Timeline.INDEFINITE);
       timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(Double.parseDouble(speedField.getText())), 
               new EventHandler(){
                   public void handle(Event event){
                       
                        //this if statement counts the number of completed orbits
  if (timeSeconds == Double.parseDouble(periodField.getText())){
  //imeSeconds = 0;  
  completed_orbit++;
}
                       
                        if(timeSeconds == 0.0){
                           timeSeconds = Double.parseDouble(periodField.getText());
                       }
                        if(timeSeconds == Double.parseDouble(periodField.getText())){
                           timeSeconds = 0.0;
                       }
                        
                               
       apogee_time = Double.parseDouble(periodField.getText());
        perigee_time = Double.parseDouble(periodField.getText())/2;
             
             //this if statement checks for when the staellite gets to apogee or perigee
  if (timeSeconds == apogee_time){
      statusField.setText("APOGEE");
      //statusField.setForeground(Color.green);
  }  else if(timeSeconds == perigee_time){
      statusField.setText("PERIGEE");
     // statusField.setForeground(Color.green);
  }   else{
      statusField.setText("Orbiting");
     // statusField.setForeground(Color.red);
  }
  
  
 
                        
                       timeSeconds++;
                       timerField.setText(timeSeconds.toString());
                       
     Double rad_Ma = Double.parseDouble(motionField.getText()) * timeSeconds; // the mean anomaly
    Ma = Math.toDegrees(rad_Ma); //converting from radian to degree
      meanField.setText(String.valueOf(Ma)); 
      
   Double rad_Vm = rad_Ma + (2* Double.parseDouble(ecceField.getText()) *Math.sin(rad_Ma)) + (1.25* Double.parseDouble(ecceField.getText()) *  Double.parseDouble(ecceField.getText())*Math.sin(2*rad_Ma));
    Vm = Math.toDegrees(rad_Vm); //calculating true anomaly
    
    
    Double rad_Em = Math.acos((Double.parseDouble(ecceField.getText()) + Math.cos(rad_Vm))/(1 + (Double.parseDouble(ecceField.getText()) * Math.cos(rad_Vm))));
    Double Em = Math.toDegrees(rad_Em);
    
     alt = (Double.parseDouble(axisField.getText()) * (1 -  Double.parseDouble(ecceField.getText()) *Math.cos(rad_Em))) - 6378;  //calculating altitude
    


vel = Math.sqrt(398600 * ((2/(alt + 6378)) - (1/  Double.parseDouble(axisField.getText()) ))); //calculationg velocity

 trueField.setText(String.valueOf(Vm));
             altField.setText(String.valueOf(alt));
             velocityField.setText(String.valueOf(vel));
             orbitField.setText(String.valueOf(completed_orbit));  
             
 
      
                      
                   }
               }));
       timeline.playFromStart();
    }
    
    
    public void calcParams(){
         double apo = Double.parseDouble(apogeeField.getText());
         double peri = Double.parseDouble(perigeeField.getText());
          double u = 398600.0;
          
           Double Ra = 6378.0 + apo; //calculating radius of apogee
           radiusApogeeField.setText(String.valueOf(Ra));
           
            Double Rp = 6378.0 + peri;
             radiusPerigeeField.setText(String.valueOf(Rp));
             
              Double sma = (Ra + Rp)/2; //calculating semi-major axis
              axisField.setText(String.valueOf(sma));
              
          Double ec = (Ra - Rp)/(Ra + Rp); //calculating eccentricity
            ecceField.setText(String.valueOf(ec));
            
            Double Mm = Math.sqrt(u/(sma*sma*sma)); //calculating mean motion
             motionField.setText(String.valueOf(Mm)); 
             
               Double pe  = (2 * 3.1415) * Math.sqrt((    Double.parseDouble(axisField.getText())* Double.parseDouble(axisField.getText())  * Double.parseDouble(axisField.getText()))/398600); //calculating period
          
//truncates to 2 decimal places
 Double toBeTruncated = new Double(pe);
           
           T = new BigDecimal(toBeTruncated).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
             periodField.setText(String.valueOf(T));
             
          perigee_time = T;
          Double at = T/2;
          Double tbt = new Double(at);
           
           apogee_time = new BigDecimal(tbt).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
         
    }
    
    
    
    
    

    
}
