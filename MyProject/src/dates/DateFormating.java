/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dates;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import myConnection.MyConnection;

/**
 *
 * @author Rokvic Nkola
 */
public class DateFormating {
    
    private static Date thisDate = new Date();
    private static SimpleDateFormat dateForm = new SimpleDateFormat("dd/MM/YY hh:mm:ss");
   
    
    public static void main(String[] args) {
        System.out.println(dateForm.format(thisDate));
    }
    
    public void startTime() throws ClassNotFoundException {
    
        dateForm.format(thisDate);
  
    }
    
    public void endTime() {
        dateForm.format(thisDate);
    }
    
    public void pay() {
//        startTime() - endTime();
    }
    
}
