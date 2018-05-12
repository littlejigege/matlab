package com.qg.matlab.controller;


import com.mathworks.toolbox.javabuilder.MWException;
import com.qg.matlab.ImageHandler;
import com.qg.matlab.bean.ImageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.plugin.dom.css.RGBColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ImageController {
    @Autowired
    private ImageHandler handler;
    private double[] key = {0.3, 65536, 65535, 65534, 1000};

//    @Autowired
//    public void setHandler(ImageHandler handler) {
//        this.handler = handler;
//    }


//    @PostMapping(value = "/encrypt")
//    public Object handleEncrypt(@RequestBody ImageData imageData) throws MWException {
//        System.out.println("handleEncrypt收到数据==============" + imageData);
//        return handler.encrypt(imageData.getData(), key);
//    }
//
//    @PostMapping(value = "/decrypt")
//    public Object handleDecrypt(@RequestBody ImageData imageData) throws MWException {
//        System.out.println("handleCIDecrypt收到数据==============" + imageData);
//        return handler.decrypt(imageData.getData(), key);
//    }

    @PostMapping(value = "/uploadencrypt")
    public Object uploadEncrypt(@RequestParam("file") MultipartFile file) throws IOException, MWException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        System.out.println("图片宽高： " + image.getWidth() + " " + image.getHeight());
        //取出一维RGB数组
        int[] rgbIntArray = image.getRGB(0, 0, image.getWidth(), image.getHeight(), new int[image.getWidth() * image.getHeight()], 0, 1);
        //构造三维RGB数组，简单起见，只有一行
        int[][][] rgbArray = new int[1][rgbIntArray.length][3];
        for (int i = 0; i < rgbIntArray.length; i++) {
            rgbArray[0][i] = toRgbArray(rgbIntArray[i]);
        }
        System.out.println("加密前第一个像素上的rgb：" + rgbArray[0][0][0] + " " + rgbArray[0][0][1] + " " + rgbArray[0][0][2]);
        //把一行的三维数组进行加密，得到加密后的一行三维数组
        int[][][] result = (int[][][]) handler.encrypt(rgbArray, key);
        //把一行三维数组转为一维数组
        for (int i = 0; i < rgbIntArray.length; i++) {
            rgbIntArray[i] = toRgbInt(result[0][i]);
        }
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        output.setRGB(0, 0, image.getWidth(), image.getHeight(), rgbIntArray, 0, 1);
        ImageIO.write(output, "JPEG", new File("E:\\a.jpg"));
        return result;
    }

    @PostMapping(value = "/uploaddecrypt")
    public Object uploadDecrypt(@RequestParam("file") MultipartFile file) throws IOException, MWException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        int[][][] rgbArray = new int[image.getWidth()][image.getHeight()][3];
        System.out.println("图片宽高： " + image.getWidth() + " " + image.getHeight());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int i1 = 0; i1 < image.getHeight(); i1++) {
                rgbArray[i][i1] = toRgbArray(image.getRGB(i, i1));
            }
        }
        System.out.println("解密前第一个像素上的rgb：" + rgbArray[0][0][0] + " " + rgbArray[0][0][1] + " " + rgbArray[0][0][2]);
        int[][][] result = (int[][][]) handler.decrypt(rgbArray, key);
        int[] ints = new int[image.getWidth() * image.getHeight()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int i1 = 0; i1 < image.getWidth(); i1++) {
                ints[i * image.getWidth() + i1] = toRgbInt(result[i1][i]);
            }
        }
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        output.setRGB(0, 0, image.getWidth(), image.getHeight(), ints, 0, 1);
        ImageIO.write(output, "JPEG", new File("E:\\b.jpg"));
        return result;
    }

    private int[] toRgbArray(int color) {
        int[] rgb = new int[3];
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
        return rgb;
    }

    private int toRgbInt(int[] rgb) {
        return 0xff000000 | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }
}
