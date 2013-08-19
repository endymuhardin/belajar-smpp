package com.muhardin.belajar.smpp.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.smpp.Data;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.AddressRange;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransciever;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.ValueNotSetException;
import org.smpp.pdu.WrongLengthOfStringException;

public class Main {
    
    // SMPP Connection Session
    private Session session;
    private String serverHost = "127.0.0.1";
    private Integer serverPort = 12345;

    // Connection Parameter
    private String systemId = "artivisi";
    private String password = "test123";
    private String systemType = "";
    private byte smppVersion = (byte)0x34;
    private AddressRange addressRange = new AddressRange();
    
    
    public void connect(){
        try {
            // connection
            System.out.print("Preparing connection .... ");
            TCPIPConnection conn = new TCPIPConnection(serverHost, serverPort);
            session = new Session(conn);
            System.out.println("OK");
            
            // bind transceiver request
            BindRequest bindRequest = new BindTransciever();
            bindRequest.setSystemId(systemId);
            bindRequest.setPassword(password);
            bindRequest.setSystemType(systemType);
            bindRequest.setInterfaceVersion(smppVersion);
            bindRequest.setAddressRange(addressRange);
            
            Thread.sleep(1000);
            
            // perform request
            System.out.println("Bind Request : ["+bindRequest.debugString()+"]");
            BindResponse bindResponse = session.bind(bindRequest);
            System.out.println("Bind Response : ["+bindResponse.debugString()+"]");
            
            System.out.println("Success : "+ (Data.ESME_ROK == bindResponse.getCommandStatus() ? "Ok" : "Failed"));
            
        } catch (WrongLengthOfStringException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueNotSetException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PDUException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongSessionStateException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void sendMessage(String message){
        
    }
    
    public void receiveDeliveryReport(){
        
    }
    
    public static void main(String[] args) {
        new Main().connect();
    }
}
