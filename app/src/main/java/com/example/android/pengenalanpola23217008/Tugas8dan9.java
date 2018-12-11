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

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Tugas8dan9 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas8dan9);
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

                img.setImageBitmap(bitmap);
            } else if (requestCode == 0 && data.getExtras().get("data") != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                img.setImageBitmap(bitmap);
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

    public void doProcess1(View view){
        ImageView img = findViewById(R.id.img);
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();

        Bitmap result1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap grayBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bitmapsq = bitmap.copy(Bitmap.Config.ARGB_8888, true);
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
                int opacity = color>>24;



                /*if(redVal>95 && greenVal>40 && blueVal>20
                        && redVal>greenVal && redVal>blueVal
                        && opacity>15 && abs(redVal-greenVal)>15){*/
                if(redVal>95 && greenVal>40 && blueVal>20
                        && redVal>greenVal && redVal>blueVal
                        && abs(redVal-greenVal)>15){

                    grayBitmap.setPixel(j,i,bwColor);
                }
                else{
                    result1.setPixel(j,i,0xFF000000);
                    grayBitmap.setPixel(j,i,0xFF000000);
                }

            }
        }

        /** Searching Ymin*/
        int found = 0;
        int yMin = 0;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                int thisColor = grayBitmap.getPixel(j,i);
                int thisVal = Color.blue(thisColor);
                if(found == 0 && thisVal > 0){
                    found = 1;
                    yMin = i;
                    break;
                }
                else{
                    found = 0;
                }
            }
        }

        /** Searching Ymax*/
        found = 0;
        int yMax = 0;
        for(int i=height-1; i>=0; i--){
            for(int j=0; j<width; j++){
                int thisColor = grayBitmap.getPixel(j,i);
                int thisVal = Color.blue(thisColor);
                if(found == 0 && thisVal > 0){
                    found = 1;
                    yMax = i;
                    break;
                }
                else{
                    found = 0;
                }
            }
        }

        /** Searching Xmin*/
        found = 0;
        int xMin = 0;
        for(int j=0; j<width; j++){
            for(int i=0; i<height; i++){
                int thisColor = grayBitmap.getPixel(j,i);
                int thisVal = Color.blue(thisColor);
                if(found == 0 && thisVal > 0){
                    found = 1;
                    xMin = j;
                    break;
                }
                else{
                    found = 0;
                }
            }
        }

        /** Searching Xmax*/
        found = 0;
        int xMax = 0;
        for(int j=width-1; j>=0; j--){
            for(int i=0; i<height; i++){
                int thisColor = grayBitmap.getPixel(j,i);
                int thisVal = Color.blue(thisColor);
                if(found == 0 && thisVal > 0){
                    found = 1;
                    xMax = j;
                    break;
                }
                else{
                    found = 0;
                }
            }
        }

        /** Draw Xmin and Xmax Line*/

        for(int k=yMax; k<=yMin; k++){
            bitmapsq.setPixel(xMin,k,0xFFFFFF00);
            bitmapsq.setPixel(xMax,k,0xFFFFFF00);
        }

        /** Draw Ymin and Ymax Line*/

        for(int k=xMax; k<=xMin; k++){
            bitmapsq.setPixel(k,yMin,0xFFFFFF00);
            bitmapsq.setPixel(k,yMax,0xFFFFFF00);
        }

        ImageView imgsq = findViewById(R.id.imgsq);
        imgsq.setImageBitmap(bitmapsq);

        ImageView imgP1 = findViewById(R.id.imgP1);
        imgP1.setImageBitmap(result1);

        ImageView imgP1bw = findViewById(R.id.imgP1bw);
        imgP1bw.setImageBitmap(grayBitmap);
    }

    public void doProcess2(View view){
        ImageView imgP1bw = findViewById(R.id.imgP1bw);
        Bitmap grayBitmap = ((BitmapDrawable) imgP1bw.getDrawable()).getBitmap();
        Bitmap edgeBitmap = grayBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int width = grayBitmap.getWidth();
        int height = grayBitmap.getHeight();

        int gM[][] = new int[height][width];

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                int color = grayBitmap.getPixel(j,i);
                gM[i][j] = 0x000000FF & color;
            }
        }
            /*
            for(int i=2; i<height-2; i++){
                for(int j=2; j<width-2; j++){
                    int currentVal = gM[i-2][j-2] + 2*gM[i-2][j-1] - 2*gM[i-2][j+1] - gM[i-2][j+2]
                            + 2*gM[i-1][j-2] + 4*gM[i-1][j-1] - 4*gM[i-1][j+1] - 2*gM[i-1][j+2]
                            - 2*gM[i+1][j-2] - 4*gM[i+1][j-1] + 4*gM[i+1][j+1] + 2*gM[i+1][j+2]
                            - gM[i+2][j-2] - 2*gM[i+2][j-1] + 2*gM[i+2][j+1] + gM[i+2][j+2];

                    int currentNorm = abs(currentVal)/9;

                    int shortVal = 4*gM[i-1][j-1] - 4*gM[i-1][j+1] - 4*gM[i+1][j-1] + 4*gM[i+1][j+1];

                    int edgeColor = 0;
                    if(currentVal > 0){
                        edgeColor = 0xFF;
                    }
                    int currentColor = 0xFF000000 | (currentNorm<<16 | currentNorm<<8 | currentNorm);
                    //int currentColor = 0xFF000000 | edgeColor << 16 | edgeColor << 8 | edgeColor;
                    //int currentColor = 0xFF000000 | shortVal << 16 | shortVal << 8 | shortVal;
                    edgeBitmap.setPixel(j,i,currentColor);
                }
            }
            */
        /** Sobel Operator **/
        for(int i = 1; i<height-1; i++){
            for(int j = 1; j<width-1; j++){
                int dV = gM[i-1][j+1] - gM[i-1][j-1] + 2*gM[i][j+1] - 2*gM[i][j-1] + gM[i+1][j+1] - gM[i+1][j-1];
                int dH = gM[i+1][j-1] - gM[i-1][j-1] + 2*gM[i+1][j] - 2*gM[i-1][j] + gM[i+1][j+1] - gM[i-1][j+1];
                double dTotal = sqrt((double)(dV*dV + dH*dH));
                int currentNorm = abs((int)dTotal)/4;
                int currentColor = 0xFF000000 | (currentNorm<<16 | currentNorm<<8 | currentNorm);
                edgeBitmap.setPixel(j,i,currentColor);
            }
        }

        /** Kirsch Compass */
            /*
            for(int i = 1; i<height-1; i++){
                for(int j = 1; j<width-1; j++){
                    int dN = gM[i-1][j+1] - gM[i-1][j-1] + 2*gM[i][j+1] - 2*gM[i][j-1] + gM[i+1][j+1] - gM[i+1][j-1];
                    int dH = gM[i+1][j-1] - gM[i-1][j-1] + 2*gM[i+1][j] - 2*gM[i-1][j] + gM[i+1][j+1] - gM[i-1][j+1];
                    double dTotal = sqrt((double)(dV*dV + dH*dH));
                    int currentNorm = abs((int)dTotal)/4;
                    int currentColor = 0xFF000000 | (currentNorm<<16 | currentNorm<<8 | currentNorm);
                    edgeBitmap.setPixel(j,i,currentColor);
                }
            }
            */
        ImageView imgP2 = findViewById(R.id.imgP2);
        imgP2.setImageBitmap(edgeBitmap);
        //imgP2.setImageBitmap(grayBitmap);
    }
}
