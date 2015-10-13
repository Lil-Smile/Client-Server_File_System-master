package lilsmile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Smile on 07.10.15.
 */
public class Form  {

    private Controller controller;


    Form()
    {
        System.out.println("Host IP:"+getHostIP());
    }

    public void setController(Controller controller)
    {
        this.controller=controller;
    }


    private String getHostIP()
    {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
            return  inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }






}
