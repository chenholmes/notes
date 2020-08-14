package com.chenli.dailytest.netty;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client extends Thread {

    static int cnt = 0;

    Socket socket = null;
    private  static  String host = "localhost";
    private  static int port = 8899;

    public Client() {
        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            socket = new Socket(host, port);
            OutputStream b = socket.getOutputStream();

            byte[] retrunData = new byte[]{
                    0, 0, 71, 48, 56, 70, 78, 83, 84, 68, 49, 57, 48, 48, 52, 52, 0
            };
//
//        byte[] data1 = new byte[] {
//              3,1,97,97,0,98,98,0,0,99,99,0,100,100,0,1,101,101,0,102,102,0
//        };
            byte[] sendBytes = setAtxPacData(1,17,retrunData);
            System.out.println(Arrays.toString(sendBytes));

            String str = byteArrayToStr(retrunData);
            System.out.println(str);
            b.write(sendBytes);
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        //客户端一连接就可以写数据个服务器了
        super.run();
        try {
            // 读Sock里面的数据
            InputStream s = socket.getInputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int len = socket.getReceiveBufferSize();
                byte[] bytes = new byte[len];
                int lenR = s.read(bytes);
                String info = Arrays.toString(Arrays.copyOfRange(bytes, 0, lenR));
                System.out.println("长度" + lenR + "==========" + info);
            }
        } catch (Exception e) {
            System.out.println("socket连接断开！");

        }
    }

    //函数入口
    public static void main(String[] args) {
        //需要服务器的正确的IP地址和端口号
        Client clientTest = new Client();
        clientTest.start();
    }

    public static byte[] setAtxPacData(int cmd, int size, byte[] data) {
        byte sum = 0;
        int s = size + 9;
        byte[] sendbuf = new byte[15 + size];
        sendbuf[0] = 2;
        sendbuf[1] = (byte) s;
        sendbuf[2] = (byte) (s >> 8);
        sendbuf[3] = (byte) (0 - s);
        sendbuf[4] = (byte) ((0 - s) >> 8);
        sendbuf[5] = (byte) cnt;
        sendbuf[6] = (byte) (cnt >> 8);
        sendbuf[11] = (byte) cmd;
        sendbuf[12] = (byte) (cmd >> 8);
        System.arraycopy(data, 0, sendbuf, 13, size);
        cnt++;
        size += 13;
        for (int i = 1; i < size; i++) {
            sum += sendbuf[i];
        }
        sendbuf[size++] = sum;
        sendbuf[size++] = 3;
        return sendbuf;
    }

    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        String[] starr = str.split("\0");
        return str;
    }
}
