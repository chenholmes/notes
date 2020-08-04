package com.chenli.dailytest.netty;

import com.alibaba.fastjson.JSON;
import com.chenli.dailytest.netty.model.Device;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        byte[] data = new byte[] {
                3,1,97,97,0,98,98,0,0,99,99,0,100,100,0,1,101,101,0,102,102,0
        };
        List<Device> deviceList = parse(data);
        System.out.println(JSON.toJSONString(deviceList));
    }

    public static List<Device> parse(byte[] data) {
        List<Device> deviceList = new ArrayList<>();
        int n = 0;
        int start = 1;
        byte len = data[0];
        for(int i = 1; i < data.length; i++) {
            if(n == 0) i++;
            if(data[i] == 0) n++;
            if(n == 2) {
                byte[] dest = ArrayUtils.subarray(data,start,i + 1);
                Device device = new Device();
                device.setStatus(dest[0]);
                for(int j = 1; j < dest.length; j++){
                    if(dest[j] == 0) {
                        device.setName(new String(ArrayUtils.subarray(dest,1,j)));
                        device.setVsg(new String(ArrayUtils.subarray(dest,j+1,dest.length -1 )));
                        deviceList.add(device);
                        break;
                    }
                    continue;
                }
                n = 0;
                start = i + 1;
            }
            continue;

        }
        return deviceList;
    }
}
