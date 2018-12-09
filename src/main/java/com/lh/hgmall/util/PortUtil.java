package com.lh.hgmall.util;

import javax.swing.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

public class PortUtil {
    public static boolean testPort(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return false;
        }
        catch (BindException e)
        {
            return true;
        }
        catch (IOException e)
        {
            return true;
        }
    }

    public static void checkPort(int port,String server,boolean shutdown){
        if(!testPort(port))
        {
            if (shutdown)
            {
                String msg = String.format("在端口%d未检查%n启动",port,server);
                JOptionPane.showMessageDialog(null,msg);
                System.exit(1);
            }
            else
            {
                String msg = String.format("在端口%d未检查%n启动",port,server);
                if(JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null,msg))
                    System.exit(1);
            }
        }
    }
}
