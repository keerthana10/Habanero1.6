package com.openbravo.pos.forms;

/**
 *
 * @author  Sivesh B
 * @company Sysfore Technologies
 * @on      May 6, 2013 3:08:43 PM
 * @email   sivesh.b@sysfore.com
 */
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.CRC32;

public class GetMACAddress {

    public String getAddress(){
        return calculateMacIdentifier();
    }

    public String getCurrenetAddress(){
        return calculateSystemMacAddress();
    }
    
    private final static String calculateMacIdentifier() {
    	StringBuilder sb = new StringBuilder();
    	List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            Collections.sort(interfaces, new Comparator<NetworkInterface>() {
                @Override
                public int compare(NetworkInterface o1, NetworkInterface o2) {
                    try {
                        if (o1.isLoopback() && !o2.isLoopback()) {
                            return 1;
                        }
                        if (!o1.isLoopback() && o2.isLoopback()) {
                            return -1;
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            });

            for (NetworkInterface iface : interfaces) {
                if (iface.getHardwareAddress() != null) {
                    // get the first not null hw address and CRC it
                    byte[] mac = iface.getHardwareAddress();
                   // System.out.println("mac-----"+mac);
                   
                    
                    for (int i = 0; i < mac.length; i++) {
                        System.out.println("mac[i]----"+mac[i]);
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                   System.out.println("sb---"+sb.toString());
                    sb.append("|");
                    CRC32 crc = new CRC32();
                    crc.update(iface.getHardwareAddress());
                    System.out.println("crc---"+crc.getValue());
                    //return Long.toHexString(crc.getValue());
                    
                }
            }

            if (interfaces.isEmpty()) {
                //System.out.println("Not found mac adress");
            }
            return sb.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
    }
    private final static String calculateSystemMacAddress() {
        List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            Collections.sort(interfaces, new Comparator<NetworkInterface>() {
                @Override
                public int compare(NetworkInterface o1, NetworkInterface o2) {
                    try {
                        if (o1.isLoopback() && !o2.isLoopback()) {
                            return 1;
                        }
                        if (!o1.isLoopback() && o2.isLoopback()) {
                            return -1;
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            });

            for (NetworkInterface iface : interfaces) {
                if (iface.getHardwareAddress() != null) {
                    // get the first not null hw address and CRC it
                    byte[] mac = iface.getHardwareAddress();

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                   
                    
                 //   System.out.println(crc.getValue());
                    //return Long.toHexString(crc.getValue());
                    return sb.toString();
                }
            }

            if (interfaces.isEmpty()) {
              //  System.out.println("Not found mac adress");
            }
            return "";
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
    }
}
