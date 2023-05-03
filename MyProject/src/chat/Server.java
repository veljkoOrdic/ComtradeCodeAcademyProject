/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author Rokvic Nkola
 */
public class Server extends javax.swing.JFrame {
    
    ServerSocket ss;
    HashMap clientHash = new HashMap();

    /**
     * Creates new form Server
     */
    public Server() {
       
        try {
        initComponents();
        this.setLocationRelativeTo(null);
            ss = new ServerSocket(2089);
            sStatus.setText("Server Started");
            new ClientAccept().start();
        } catch (Exception e) {
            System.out.println("Error iz ServeraChata: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        
    }
    
    class ClientAccept extends Thread {
		public void run (){
            while(true){
                try {
                    Socket s = ss.accept();

                    String username = new DataInputStream(s.getInputStream()).readUTF();
                    if(clientHash.containsKey(username)){
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("You Are Alredy Registered....!!");
                    }else{
                        clientHash.put(username, s);
                        textAreaMsgBox.append(username + " Joined! " + "\n");
                        //----------------------------------
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("");
                        //-----------------------------------
                        Set k = clientHash.keySet();
                        Iterator itr = k.iterator();
                        while(itr.hasNext()){
                            String key = (String)itr.next();
                            if(!key.equalsIgnoreCase(username)){
                                try {
                                    new DataOutputStream(((Socket)clientHash.get(key))
                                            .getOutputStream()).writeUTF(username + " has Joined!");
                                } catch (Exception e) {
                                    clientHash.remove(key);
                                    textAreaMsgBox.append(key  + ": removed!");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                        //------------------------------------------
                        new MsgRead(s,username).start();
                        new PrepareClientList().start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
	}
    
    	class MsgRead extends Thread {
		Socket s;
        String username;
        
        MsgRead(Socket s, String username) {
            this.s = s;
            this.username = username;
        }
        @Override
        public void run(){
            while(!clientHash.isEmpty()){
                try {
                    String i = new DataInputStream(s.getInputStream()).readUTF();
                    if(i.equals("mkoihgteazdcvgyhujb096785542AXTY")){//prepoznavanje za remove
                        clientHash.remove(username);
                        String msgForRemove = username + ":  removed! \n";
                        textAreaMsgBox.append(username + ": removed! " + " \n");
                        new PrepareClientList().start();
                        //kada smo smakli klijenta opet pokrecemo PrepareClientList()
                        //potom svim korisnicima saljemo novu listu klijenata bez uklonjenog korisnika
                        Set k = clientHash.keySet();

                        Iterator itr = k.iterator();
                        while(itr.hasNext()){
                            String key = (String)itr.next();
                            if(!key.equalsIgnoreCase(username)){
                                try {
                                    new DataOutputStream(((Socket)clientHash.get(key)).getOutputStream()).writeUTF(msgForRemove);
                                } catch (Exception e) {
                                    clientHash.remove(key);
                                    textAreaMsgBox.append(key  + ": removed!");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                    }else if(i.contains("#4344554@@@@@67667@@")){//prepoznavanje za slanje poruke odredjenoj osobi
                        //Ovde pravimo substring koji krece od 20 karaktera da bi preskocili #4344554@@@@@67667@@
                        //Zatim preko StringTokenizera ubacujemo u njega , pa ga odvajemo sa : na dva tokena
                        //prvi token ce sadrzati ID onoga ko salje drugi id sadrzi kome se salje 
                        //i je poruka koja se salje
                                i = i.substring(20);
                                StringTokenizer st = new StringTokenizer(i,":");
                                // prvi token je username onoga kome se salje
                                String usernamekomeSeSalje = st.nextToken();
                                // drugi je poruka koja se tom usernamu salje
                                i = st.nextToken();
                                try {
                                    new DataOutputStream(((Socket)clientHash.get(usernamekomeSeSalje))
                                            .getOutputStream())
                                            .writeUTF("< " + username + " to " + usernamekomeSeSalje + " > " +i);
                                } catch (Exception e) {
                                    clientHash.remove(usernamekomeSeSalje);
                                    textAreaMsgBox.append(usernamekomeSeSalje  + ": removed!");
                                    new PrepareClientList().start();
                                }

                    }else{//ovde je slanje svim korisnika nema potrebe za tekstom prepoznavanja
                        Set k = clientHash.keySet();
                        Iterator itr = k.iterator();
                        while(itr.hasNext()){
                            String key = (String)itr.next();
                            if(!key.equalsIgnoreCase(username)){
                                try {
                                    new DataOutputStream(((Socket)clientHash.get(key))
                                            .getOutputStream()).writeUTF("< " + username + " to ALL > " + i);
                                } catch (Exception e) {
                                    clientHash.remove(key);
                                    textAreaMsgBox.append(key  + ": removed!");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                    }
//                    }
                    } catch (Exception e) {
                        System.out.println("Korisnik je izasao");
//                        e.printStackTrace();
                }
            }
        }

	}
        
        class PrepareClientList extends Thread {

		 public void run(){
	            try {
	                String ids = "";
	                Set k = clientHash.keySet();
	                Iterator itr = k.iterator();
	                while(itr.hasNext()){
	                    //ovde pravimo spisak keyeva
	                    String key = (String)itr.next();
	                    ids += key + ",";  
	                }
	                if(ids.length()!=0)
	                    ids=ids.substring(0,ids.length()-1);
	                
	                itr= k.iterator();
	                
	                while(itr.hasNext()){
	                    String key = (String)itr.next();
	                    try {
	                         new DataOutputStream(((Socket)clientHash.get(key)).getOutputStream()).writeUTF(":;.,/=" + ids);
	                    } catch (Exception e) {
	                        clientHash.remove(key);
	                        textAreaMsgBox.append(key + ": removed!");
	                    } 
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }

	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaMsgBox = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Server Status :");

        sStatus.setText("-------------------------------------");

        textAreaMsgBox.setColumns(20);
        textAreaMsgBox.setRows(5);
        jScrollPane1.setViewportView(textAreaMsgBox);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel sStatus;
    private javax.swing.JTextArea textAreaMsgBox;
    // End of variables declaration//GEN-END:variables
}
