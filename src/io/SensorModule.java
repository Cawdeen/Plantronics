/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import javafx.concurrent.Task;

/**
 * This class handles all of the serial parsing received through the USB port.
 * @author Collin Farrell <cmf5955@psu.edu>
 */
public class SensorModule extends Task<SerialPacket>{
    
    private boolean serialInput;
    SerialPort comPort;
    
    public SensorModule()
    {
        
    }

    private SerialPacket readConsole()
    {
        String strDHTTemp = "";
        String strSHTTemp = "";
        String strHumidDHT = "";
        String strHumidSHT = "";
        StringBuffer sb = new StringBuffer();
        boolean readingDHTTemp = false;
        boolean readingSHTTemp = false;
        boolean readingHumidDHT = false;
        boolean readingHumidSHT = false;
        if(comPort != null)
        {
            if(comPort.isOpen())
            {
                comPort.closePort();
            }
        }
        comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        InputStream in = comPort.getInputStream();
//        try {
//            System.out.println((char)in.read());
//        } catch (IOException ex) {
//            Logger.getLogger(SensorModule.class.getName()).log(Level.SEVERE, null, ex);
//        }

        for(int i = 0; i < 200; i++)
        {
            char ch='!';
            try {
                ch = (char)in.read();
            } catch (IOException ex) {
                //Logger.getLogger(SensorModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(ch=='<' && readingDHTTemp == false)//start of temp. Next char is temp
            {
                readingDHTTemp = true;
            }
            else if(ch != ',' && readingDHTTemp == true) //reading temps DHT
            {
                sb.append(ch);
            }
            else if(ch ==',' && readingDHTTemp == true) //just read DHT, time to read SHT
            {
                strDHTTemp = sb.toString(); //save string buffer to temperature string
                System.out.println("str: "+strDHTTemp);
                sb.delete(0, sb.length()); //clear the string buffer so it can start fresh with humidity
                readingDHTTemp = false;
                readingSHTTemp = true;
            }
            else if(ch != ',' && readingSHTTemp == true) //reading temps SHT
            {
                sb.append(ch);
            }
            else if(ch ==',' && readingHumidDHT == false && readingSHTTemp == true) //we're done reading temp. Next char will be humidity
            {
                strSHTTemp = sb.toString(); //save string buffer to temperature string
                System.out.println("str: "+strSHTTemp);
                sb.delete(0, sb.length()); //clear the string buffer so it can start fresh with humidity
                readingSHTTemp = false;
                readingHumidDHT = true;
            }
            else if(ch != ',' && readingHumidDHT == true) //reading humidity
            {
                sb.append(ch);
            }
            else if(ch ==',' && readingHumidDHT == true)
            {
                strHumidDHT = sb.toString(); //save string buffer to humidSHT string
                System.out.println("str: "+strHumidDHT);
                sb.delete(0, sb.length()); //clear the string buffer
                readingHumidDHT = false;
                readingHumidSHT = true;
            }
            else if(ch != '>' && readingHumidSHT == true) //reading humidity
            {
                sb.append(ch);
            }
            else if(ch == '>' && readingHumidSHT == true) //end of reading humidity. W're done
            {
                strHumidSHT = sb.toString(); //save string buffer to Humidity string
                sb.delete(0, sb.length()); //clear the string buffer
                readingHumidSHT = false;
                break;
            }
        }
        comPort.closePort();
        
        float numDHTTemp = Float.parseFloat(strDHTTemp);
        float numSHTTemp = Float.parseFloat(strSHTTemp);
        float numHumidDHT = Float.parseFloat(strHumidDHT);
        float numHumidSHT = Float.parseFloat(strHumidSHT);
        boolean success = true;
        if(!Float.isNaN(numDHTTemp) || !Float.isNaN(numHumidDHT) || !Float.isNaN(numSHTTemp) ||!Float.isNaN(numHumidSHT)) //if any float is a number return true;
        {
            success = true;
        }
        System.out.println("SHTTemp = " + numSHTTemp);
        SerialPacket sp = new SerialPacket(numDHTTemp, numSHTTemp, numHumidDHT, numHumidSHT, success);
        //System.out.println("Serial Packet: "+ sp.getTempDHT()+" "+sp.getHumidityDHT()+" "+sp.isSuccess());
        return sp;
    }

    @Override
    public SerialPacket call() throws Exception {
        SerialPacket sp = readConsole();
        return sp;
    }
}
