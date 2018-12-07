package github.lual;

import com.fazecast.jSerialComm.SerialPort;

import java.math.BigInteger;

public class JSerialTest {

    public static void main(String[] args) {
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        if (serialPorts.length < 1) {
            System.err.println("No serial port found");
            return;
        }
        for (int i = 0; i < serialPorts.length; i++) {
            System.out.println("COM-Port Index" + i + " => " + serialPorts[i].getDescriptivePortName() + " / " + serialPorts[i].getPortDescription() + " / " + serialPorts[i].getSystemPortName());
        }
        SerialPort serialPort = serialPorts[0];
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        serialPort.openPort();
        try {
            while (true) {
                int bytesAvailable = serialPort.bytesAvailable();
                while (bytesAvailable <= 0) {
                    Thread.sleep(20);
                }

                byte[] readBuffer = new byte[bytesAvailable];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                String hex = String.format("%040x", new BigInteger(1, readBuffer));
                System.out.println("Serialport result: " + hex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
