package com.example.android.pengenalanpola23217008.util;

import android.graphics.Bitmap;
import android.widget.TextView;

import com.example.android.pengenalanpola23217008.model.SkeletonFeature;

public class NewImageUtil {

    // -----------------------------
    // Image Enhancement Algorithm
    // -----------------------------

    public static Bitmap getBinaryImage(Bitmap bitmap, int threshold) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int size = width * height;
        int[] pixels = new int[size];

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < size; i++) {
            int pixel = pixels[i];
            int grayscale = (((pixel & 0x00ff0000) >> 16) + ((pixel & 0x0000ff00) >> 8) + (pixel & 0x000000ff)) / 3;

            if (grayscale < threshold) {
                pixels[i] = pixel & 0xff000000;
            } else {
                pixels[i] = pixel | 0x00ffffff;
            }
        }

        return Bitmap.createBitmap(pixels, bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    }

    // -----------------------------
    // Feature Extraction Algorithm
    // -----------------------------

    public static Bitmap[] getSkeleton(Bitmap bitmap) {
        int count;
        int[] border;

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int size = height * width;
        int[] pixels = new int[size];
        int[] pixelsa = new int[size];
        int[] pixelsb = new int[size];

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.getPixels(pixelsa, 0, width, 0, 0, width, height);
        bitmap.getPixels(pixelsb, 0, width, 0, 0, width, height);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if ((pixels[i + j * width] & 0x000000ff) != 255) {
                    border = SubImageUtil.floodFill(pixels, i, j, width);

                    do {
                        count = SubImageUtil.zhangSuenStep(pixelsa, border[0], border[1], border[2], border[3], width);
                    }
                    while (count != 0);

                    SubImageUtil.customStep(pixelsb, border[0], border[1], border[2], border[3], i, j, width);
                }
            }
        }

        return new Bitmap[]{
                Bitmap.createBitmap(pixelsa, width, height, bitmap.getConfig()),
                Bitmap.createBitmap(pixelsb, width, height, bitmap.getConfig())
        };
    }

    public static int[] getSkeletonFeature(Bitmap bitmap, TextView textView) {
        int count;
        int[] border, border2;

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int size = height * width;
        int[] pixels = new int[size];
        int[] pixelsa = new int[size];
        StringBuffer stringBuffer = new StringBuffer();

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.getPixels(pixelsa, 0, width, 0, 0, width, height);

        int[] myarray = new int[2];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if ((pixels[i + j * width] & 0x000000ff) != 255) {
                    border = SubImageUtil.floodFill(pixels, i, j, width);

                    do {
                        count = SubImageUtil.zhangSuenStep(pixelsa, border[0], border[1], border[2], border[3], width);
                    }
                    while (count != 0);

                    border2 = SubImageUtil.getNewBorder(pixelsa, border[0], border[1], border[2], border[3], width);
                    SkeletonFeature sf = SubImageUtil.extractFeature(pixelsa, border2[0], border2[1], border2[2], border2[3], width);

                    stringBuffer.append(String.format("%d,%b,%b,%b,%b,%b,%b,%b,%b,%b\r\n",
                            sf.endpoints.size(),
                            sf.hTop, sf.hMid, sf.hBottom,
                            sf.vLeft, sf.vMid, sf.vRight,
                            sf.lTop, sf.lMid, sf.lBottom));

                    int epNumber = sf.endpoints.size();
                    /*
                    //int featuresPow = b2I(sf.hTop)<<8 + b2I(sf.hMid)<<7 + b2I(sf.hBottom)<<6 + b2I(sf.vLeft)<<5 + b2I(sf.vMid)<<4 + b2I(sf.vRight)<<3 + b2I(sf.lTop)<<2 + b2I(sf.lMid)<<1 + b2I(sf.lBottom);
                    //boolean sahn = sf.vMid;
                    //int featuresPow;
                    //featuresPow = b2I(sf.vMid)<<3 + b2I(sf.hTop)<<4; //error kalau 2
                    int sahn = b2I(sf.vMid)<<4;
                    int sol = b2I(sf.hTop)<<7;
                    int featuresPow = sahn + sol;
                    //int featuresPow = sol;
                    */
                    int f1 = b2I(sf.hTop)<<8;
                    int f2 = b2I(sf.hMid)<<7;
                    int f3 = b2I(sf.hBottom)<<6;
                    int f4 = b2I(sf.vLeft)<<5;
                    int f5 = b2I(sf.vMid)<<4;
                    int f6 = b2I(sf.vRight)<<3;
                    int f7 = b2I(sf.lTop)<<2;
                    int f8 = b2I(sf.lMid)<<1;
                    int f9 = b2I(sf.lBottom);
                    int featuresPow = f1+f2+f3+f4+f5+f6+f7+f8+f9;

                    myarray[0] = epNumber;
                    myarray[1] = featuresPow;
                }
            }
        }
        textView.setText(stringBuffer);
        return myarray;
    }

    public static int b2I(boolean value){ //boolean to integer
        int heng;
        if(value) {
            heng = 1;
        }else {
            heng = 0;
        }
        return heng;
    }
}
