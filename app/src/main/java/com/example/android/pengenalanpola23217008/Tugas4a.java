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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Tugas4a extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas4a);
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

                img.setImageBitmap(getBinaryBitmap(bitmap));
            } else if (requestCode == 0 && data.getExtras().get("data") != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                img.setImageBitmap(getBinaryBitmap(bitmap));
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

    public void applyDetect(View view) {
        ImageView img = findViewById(R.id.img);
        Bitmap image = ((BitmapDrawable) img.getDrawable()).getBitmap();
        String[] result = detectNumber(image);
        TextView rDisplay = findViewById(R.id.Result);
        TextView ccDisplay = findViewById(R.id.ChainedCode);
        rDisplay.setText("Number Identified as: "+result[0]);
        ccDisplay.setText("The Chain Code is: "+result[1]);
    }

    private Bitmap getBinaryBitmap(Bitmap colorBitmap){
        int width = colorBitmap.getWidth();
        int height = colorBitmap.getHeight();
        Bitmap bwBitmap = colorBitmap.copy(Bitmap.Config.ARGB_8888, true);
        for(int i=0; i<height; i++){
            for(int j = 0; j<width; j++){
                int color = colorBitmap.getPixel(j,i);
                int redVal = Color.red(color);
                int greenVal = Color.green(color);
                int blueVal = Color.blue(color);
                int bwVal = (redVal+greenVal+blueVal)/3;
                if(bwVal > 100){
                    bwBitmap.setPixel(j,i,0xFFFFFFFF);
                }
                else{
                    bwBitmap.setPixel(j,i, 0xFF000000);
                }
            }
        }
        return(bwBitmap);
    }

    public String[] detectNumber(Bitmap bitmapNum) {
        Bitmap bitmap = bitmapNum.copy(bitmapNum.getConfig(), true);
        int bwLevel;
        String result = "";
        String chains = "";

        for (int j = 0; j < bitmap.getHeight(); j++) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                bwLevel = bitmap.getPixel(i,j) & 0x000000FF;

                if (bwLevel == 0) {
                    String chain = getChainCode(bitmap, i, j);
                    chains = chains + chain + "\n";
                    result = result + translate(chain);
                    Log.i("chain", chain);
                    Log.i("num", "" + translate(chain));
                    floodFill(bitmap, i, j);
                }
            }
        }
        return new String[] {result, chains};
    }

    private int translate(String chain) {
        double[][] ratio = {
                {0.250, 0.075, 0.098, 0.075, 0.250, 0.075, 0.098, 0.075},
                {0.329, 0.079, 0.074, 0.000, 0.361, 0.053, 0.095, 0.005},
                {0.108, 0.161, 0.172, 0.044, 0.134, 0.112, 0.243, 0.022},
                {0.143, 0.098, 0.158, 0.105, 0.132, 0.098, 0.169, 0.094},
                {0.186, 0.146, 0.090, 0.005, 0.328, 0.005, 0.232, 0.005},
                {0.161, 0.060, 0.218, 0.067, 0.147, 0.070, 0.211, 0.063},
                {0.189, 0.086, 0.133, 0.103, 0.163, 0.086, 0.159, 0.077},
                {0.211, 0.091, 0.201, 0.000, 0.201, 0.105, 0.183, 0.004},
                {0.175, 0.095, 0.132, 0.101, 0.164, 0.101, 0.132, 0.095},
                {0.168, 0.090, 0.147, 0.086, 0.181, 0.086, 0.142, 0.095}
        };
        double max = 0;
        double[] sum = new double[8];
        int number = 0;

        for (int i = 0; i < chain.length(); i++) {
            sum[Character.getNumericValue(chain.charAt(i))]++;
        }

        for (int i = 0; i < 8; i++) {
            sum[i] = sum[i] / chain.length();
        }

        for (int i = 0; i < 10; i++) {
            double res = 0;
            for (int j = 0; j < 8; j++) {
                res = res + ratio[i][j] * sum[j];
            }
            res = res / getVectorLength(ratio[i]) / getVectorLength(sum);
            if (res > max) {
                max = res;
                number = i;
            }
        }

        return number;
    }

    private double getVectorLength(double[] vector) {
        double sum = 0;

        for (int i = 0; i < 8; i++) {
            sum = sum + vector[i] * vector[i];
        }

        return Math.sqrt(sum);
    }

    private String getChainCode(Bitmap bitmap, int x, int y) {
        int a = x;
        int b = y;
        int[] next;
        int source = 6;
        String chain = "";

        do {
            next = getNextPixel(bitmap, a, b, source);
            a = next[0];
            b = next[1];
            source = (next[2] + 4) % 8;
            chain = chain + next[2];
        }
        while (!(a == x && b == y));

        return chain;
    }

    private int[] getNextPixel(Bitmap bitmap, int x, int y, int source) {
        int a, b, target = source;
        int[][] points = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

        do {
            target = (target + 1) % 8;
            a = x + points[target][0];
            b = y + points[target][1];
        }
        while ((bitmap.getPixel(a,b) & 0x000000FF) == 255);

        return new int[]{a, b, target};
    }

    private void floodFill(Bitmap bitmap, int x, int y) {
        int bwLevel = bitmap.getPixel(x,y) & 0x000000FF; //only works for binary image

        if (bwLevel != 255) {
            bitmap.setPixel(x,y,0xFFFFFFFF);
            floodFill(bitmap, x - 1, y);
            floodFill(bitmap, x + 1, y);
            floodFill(bitmap, x, y - 1);
            floodFill(bitmap, x, y + 1);
        }
    }
}
