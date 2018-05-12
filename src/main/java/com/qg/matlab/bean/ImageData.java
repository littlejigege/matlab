package com.qg.matlab.bean;

import com.google.gson.Gson;
import org.springframework.boot.json.GsonJsonParser;

public class ImageData {
    private int height;
    private int width;
    private int[][][] data;

    public ImageData(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public ImageData() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int[][][] getData() {
        return data;
    }

    public void setData(int[][][] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
