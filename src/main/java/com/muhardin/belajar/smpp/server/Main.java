/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muhardin.belajar.smpp.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.smpp.Connection;
import org.smpp.Data;
import org.smpp.NotSynchronousException;
import org.smpp.Receiver;
import org.smpp.TCPIPConnection;
import org.smpp.TimeoutException;
import org.smpp.Transmitter;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.PDU;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.UnknownCommandIdException;
import org.smpp.pdu.ValueNotSetException;
import org.smpp.pdu.WrongLengthOfStringException;

/**
 *
 * @author endy
 */
public class Main {
    private Integer port = 12345;
    
    private Connection connection;
    private Transmitter transmitter;
    private Receiver receiver;
    
    private Boolean running = false;
    
    public void startServer(){
        try {
            // start connection
            TCPIPConnection tcpConnection = new TCPIPConnection(port);
            System.out.print("Opening connection at port "+port+" .... ");
            tcpConnection.open();
            System.out.println("OK");
            
            System.out.print("Accepting connection .... ");
            connection = tcpConnection.accept();
            System.out.println("OK");
            
            
            System.out.print("Starting transmitter & receiver .... ");
            transmitter = new Transmitter(connection);
            receiver = new Receiver(transmitter, connection);
            receiver.start();
            System.out.println("OK");
            
            running = true;
            
            while(running){
                System.out.println("Waiting for data ... ");
                PDU pdu = receiver.receive(Data.RECEIVER_TIMEOUT);
                
                if(pdu == null){
                    System.out.println("No data until timeout. Keep waiting");
                    continue;
                }
                
                if(pdu.isRequest()){
                    handleRequest(pdu);
                    continue;
                }
                
                if(pdu.isResponse()){
                    handleResponse(pdu);
                    continue;
                }
                
                throw new IllegalStateException("PDU not request nor response");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownCommandIdException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSynchronousException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PDUException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleRequest(PDU pdu) {
        if(Data.BIND_TRANSCEIVER == pdu.getCommandId() || 
                Data.BIND_RECEIVER == pdu.getCommandId() || 
                Data.BIND_TRANSMITTER == pdu.getCommandId()){
            handleBindRequest(pdu);
        }
    }

    private void handleResponse(PDU pdu) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleBindRequest(PDU pdu) {
        try {
            // cek system id dan password
            BindRequest request = (BindRequest) pdu;
            System.out.println("System ID : "+request.getSystemId());
            System.out.println("Password  : "+request.getPassword());
            
            // tidak dicek user/pass, langsung sukses saja
            BindResponse response = (BindResponse) request.getResponse();
            response.setSystemId("Server System ID");
            
            // debug
            System.out.println("Request  : ["+request.debugString()+"]");
            System.out.println("Response : ["+response.debugString()+"]");
            transmitter.send(response);
        } catch (WrongLengthOfStringException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueNotSetException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void main(String[] args) {
        new Main().startServer();
    }
}
