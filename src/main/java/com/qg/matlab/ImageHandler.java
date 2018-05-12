package com.qg.matlab;

import Image.CIDecrypter;
import Image.CIEncrypyer;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import com.qg.matlab.bean.ImageData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class ImageHandler {
    public Object encrypt(int[][][] data, Object key) throws MWException {
        CIEncrypyer encrypyer = new CIEncrypyer();
        Object[] result;
        result = encrypyer.CIEncrypt(1, data, key);
        MWNumericArray arrays = (MWNumericArray) result[0];
        int[][][] temp = (int[][][]) arrays.toIntArray();
        System.out.println("加密后第一个像素上的rgb："+temp[0][0][0]+" "+temp[0][0][1]+" "+temp[0][0][2]);
        System.out.println("加密完成，发送数据");
        return temp;
    }

    public Object decrypt(int[][][] data, Object key) throws MWException {
        CIDecrypter decrypter = new CIDecrypter();
        Object[] result;
        result = decrypter.CIDecrypt(1, data, key);
        MWNumericArray arrays = (MWNumericArray) result[0];
        int[][][] temp = (int[][][]) arrays.toIntArray();
        System.out.println("解密后第一个像素上的rgb："+temp[0][0][0]+" "+temp[0][0][1]+" "+temp[0][0][2]);
        System.out.println("解密完成，发送数据");
        return temp;
    }
}
