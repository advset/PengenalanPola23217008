package com.example.android.pengenalanpola23217008;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.hypot;
import static java.lang.Math.sin;

public class UAS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uas);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = findViewById(R.id.img);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1 && data.getData() != null) {
                Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);

                if (cursor == null)
                    return;

                cursor.moveToFirst();
                String imageString = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(imageString);

                Bitmap grayBitmap = grayscale(bitmap);
                img.setImageBitmap(grayBitmap);
            } else if (requestCode == 0 && data.getExtras().get("data") != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                Bitmap grayBitmap = grayscale(bitmap);
                img.setImageBitmap(grayBitmap);
            }
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private Bitmap grayscale(Bitmap bitmap){
        Bitmap grayBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                int color = bitmap.getPixel(j,i);
                int redVal = Color.red(color);
                int greenVal = Color.green(color);
                int blueVal = Color.blue(color);
                int bwVal = (redVal+greenVal+blueVal)/3;
                int bwColor = 0xFF000000 | (bwVal<<16 | bwVal<<8 | bwVal);

                grayBitmap.setPixel(j,i,bwColor);

            }
        }

        return grayBitmap;
    }

    public void doDFT(View view){
        ImageView img = findViewById(R.id.img);
        Bitmap spatial = ((BitmapDrawable) img.getDrawable()).getBitmap();
        Bitmap freq = spatial.copy(Bitmap.Config.ARGB_8888, true); //magnitude
        Bitmap freqR = spatial.copy(Bitmap.Config.ARGB_8888, true); //Real
        Bitmap freqI = spatial.copy(Bitmap.Config.ARGB_8888, true); //Imaginary
        Bitmap freqA = spatial.copy(Bitmap.Config.ARGB_8888, true); //angle
        Bitmap hat = spatial.copy(Bitmap.Config.ARGB_8888, true); //magnitude
        Bitmap hatR = spatial.copy(Bitmap.Config.ARGB_8888, true); //Real
        Bitmap hatI = spatial.copy(Bitmap.Config.ARGB_8888, true); //Imaginary
        Bitmap hatA = spatial.copy(Bitmap.Config.ARGB_8888, true); //angle
        int width = spatial.getWidth();
        int height = spatial.getHeight();
        int[][] g = new int[height][width]; /** original image */
        double[][] GReal = new double[height][width]; /** Fourier real component */
        double[][] GImaginer = new double[height][width]; /** Fourier imaginary component */
        double[][] GMagnitude = new double[height][width]; /** Fourier Magnitude component*/
        double[][] GAngle = new double[height][width]; /** Fourier Phase component */
        double[][] gReal = new double[height][width]; /** Inverse Fourier real component */
        double[][] gImaginer = new double[height][width]; /** Inverse Fourier imaginary component */
        double[][] gMagnitude = new double[height][width]; /** InverseFourier Magnitude component*/
        double[][] gAngle = new double[height][width]; /** Inverse Fourier Phase component */

        /** Storing Image into regular Array */
        for(int p=0; p<height; p++){
            for(int q=0; q<width; q++){
                int color = spatial.getPixel(q,p);
                int sVal = Color.red(color);
                g[p][q] = sVal;
            }
        }

        /** DFT */
        for(int p=0; p<height; p++){
            for(int q=0; q<width; q++){

                for(int x=0; x<width; x++){
                    GReal[p][q] += g[p][x]*cos(2*PI*x*q/width);
                    GImaginer[p][q] += g[p][x]*sin(2*PI*x*q/width);
                }
                GReal[p][q] /= width; /** Scaling, only in Fourier not in inverse*/
                GImaginer[p][q] /= width;
                //GMagnitude[p][q] = sqrt(GReal[p][q]*GReal[p][q] + GImaginer[p][q]*GImaginer[p][q]);
                //GMagnitude[p][q] = GReal[p][q]*GReal[p][q] + GImaginer[p][q]*GImaginer[p][q];
                GMagnitude[p][q] = hypot(GReal[p][q],GImaginer[p][q])*width;
                GAngle[p][q] = atan(GImaginer[p][q]/GReal[p][q]);

                int GVal = (int)GMagnitude[p][q];
                int GColor = 0xFF000000 | (GVal<<16 | GVal<<8 | GVal);
                freq.setPixel(q,p,GColor);

                int GRVal = (int)GReal[p][q];
                int GRColor = 0xFF000000 | (GRVal<<16 | GRVal<<8 | GRVal);
                freqR.setPixel(q,p,GRColor);

                int GIVal = (int)GImaginer[p][q];
                int GIColor = 0xFF000000 | (GIVal<<16 | GIVal<<8 | GIVal);
                freqI.setPixel(q,p,GIColor);

                int GAVal = (int)((GAngle[p][q] + PI/2)*255/PI); /** scaling for display */
                int GAColor = 0xFF000000 | (GAVal<<16 | GAVal<<8 | GAVal);
                freqA.setPixel(q,p,GAColor);
            }
        }
        ImageView imgFourier = findViewById(R.id.imgFourier);
        imgFourier.setImageBitmap(freq);

        ImageView imgFourierReal = findViewById(R.id.imgFourierReal);
        imgFourierReal.setImageBitmap(freqR);

        ImageView imgFourierImaginer = findViewById(R.id.imgFourierImaginer);
        imgFourierImaginer.setImageBitmap(freqI);

        ImageView imgFourierAngle = findViewById(R.id.imgFourierAngle);
        imgFourierAngle.setImageBitmap(freqA);

        /** Memanggil Fungsi Low Pass Filter untuk Real dan Imaginer*/
        /*GReal = LPF(GReal,width,height);
        GImaginer = LPF(GImaginer,width,height); */

        /** Memanggil Fungsi Gaussian untuk Real dan Imaginer*/
        GReal = Gaussian1(GReal,width,height);
        GImaginer = Gaussian1(GImaginer,width,height);

        /** Copy atas, ubah sinnya jadi minus, ingat Real = real*real - imag*imag dan Imag = real*imag + imag*real
         * Karena kan hasil fourier ada real dan imag
         * Ubah XML nya juga */

        /** Inverse DFT */
        for(int p=0; p<height; p++){
            for(int q=0; q<width; q++){

                for(int x=0; x<width; x++){
                    gReal[p][q] += GReal[p][x]*cos(2*PI*x*q/width) + GImaginer[p][x]*sin(2*PI*x*q/width);
                    gImaginer[p][q] += GImaginer[p][x]*cos(2*PI*x*q/width) - GReal[p][x]*sin(2*PI*x*q/width);
                }
                //gReal[p][q] /= width;
                //gImaginer[p][q] /= width;
                //GMagnitude[p][q] = sqrt(GReal[p][q]*GReal[p][q] + GImaginer[p][q]*GImaginer[p][q]);
                //GMagnitude[p][q] = GReal[p][q]*GReal[p][q] + GImaginer[p][q]*GImaginer[p][q];
                gMagnitude[p][q] = hypot(gReal[p][q],gImaginer[p][q]);
                gAngle[p][q] = atan(gImaginer[p][q]/gReal[p][q]);

                int gVal = (int)gMagnitude[p][q];
                int gColor = 0xFF000000 | (gVal<<16 | gVal<<8 | gVal);
                hat.setPixel(q,p,gColor);

                int gRVal = (int)gReal[p][q];
                int gRColor = 0xFF000000 | (gRVal<<16 | gRVal<<8 | gRVal);
                hatR.setPixel(q,p,gRColor);

                int gIVal = (int)gImaginer[p][q];
                int gIColor = 0xFF000000 | (gIVal<<16 | gIVal<<8 | gIVal);
                hatI.setPixel(q,p,gIColor);

                int gAVal = (int)((gAngle[p][q] + PI/2)*255/PI); /** scaling for display */
                int gAColor = 0xFF000000 | (gAVal<<16 | gAVal<<8 | gAVal);
                hatA.setPixel(q,p,gAColor);
            }
        }
        ImageView imgInverseMagnitude = findViewById(R.id.imgInverseMagnitude);
        imgInverseMagnitude.setImageBitmap(hat);

        ImageView imgInverseReal = findViewById(R.id.imgInverseReal);
        imgInverseReal.setImageBitmap(hatR);

        ImageView imgInverseImaginer = findViewById(R.id.imgInverseImaginer);
        imgInverseImaginer.setImageBitmap(hatI);

        ImageView imgInverseAngle = findViewById(R.id.imgInverseAngle);
        imgInverseAngle.setImageBitmap(hatA);
    }

    public void doIDFT(View view){

    }

    private double[][] LPF (double[][] fullMatrix, int width, int height){
        double[][] croppedMatrix =  fullMatrix;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                double distance = hypot((i-height/2),(j-width/2));
                if(distance<height/3 && distance<width/3){
                    croppedMatrix[i][j] =0 ;
                }
            }
        }
        return croppedMatrix;
    }

    private double[][] Gaussian1 (double[][] fullMatrix, int width, int height){
        double[][] gaussMatrix =  fullMatrix;
        int sigma = 6;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                //double distance = hypot(height/2, width/2) - hypot((i-height/2),(j-width/2));
                //double distance = hypot((i-height/2),(j-width/2));
                double distance = hypot((double)i,(double)j);
                //gaussMatrix[i][j] = fullMatrix[i][j] * exp(-1*distance/(2*sigma*sigma))/(2*PI*sigma*sigma);
                gaussMatrix[i][j] = fullMatrix[i][j] * exp(-1*distance/(2*sigma*sigma));
            }
        }
        return gaussMatrix;
    }
}