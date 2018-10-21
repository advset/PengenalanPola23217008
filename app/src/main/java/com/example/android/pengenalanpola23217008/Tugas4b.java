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
import android.widget.TextView;

import java.util.ArrayList;

public class Tugas4b extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas4b);
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
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        String[] result = detectNumber(bitmap);
        TextView rDisplay = findViewById(R.id.Result);
        TextView ccDisplay = findViewById(R.id.ChainedCode);

        if(Integer.parseInt(result[0]) == -1){
            rDisplay.setText("Number Identified as: unidentified");
        }
        else{
            rDisplay.setText("Number Identified as: "+result[0]);
        }
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

        int bwLevel;
        Bitmap bitmap = bitmapNum.copy(bitmapNum.getConfig(), true);
        String result = "";
        String chains = "";

        for (int j = 0; j < bitmap.getHeight(); j++) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                bwLevel = bitmap.getPixel(i, j) & 0x000000FF;

                if (bwLevel == 0) {
                    String chain = getChainCode(bitmap, i, j);
                    chains = chains + chain + "\n";
                    result = result + translateBlock(chain);
                    floodFill(bitmap, i, j);
                }
            }
        }
        return new String[]{result,chains};
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


    public int translateBlock(String chain) {

        ArrayList<Integer> count;
        String shortChain = getShortChain(chain);

        if (shortChain.equals("2460")) {
            count = getLongChain(chain);

            if((double)count.get(0) / count.get(1) <= 0.2){
                return 1;
            }
            else{
                return 0;
            }
        } else if (shortChain.equals("246424602060")) {
            count = getLongChain(chain);

            if (count.get(1) > count.get(count.size() - 3)) {
                return 2;
            } else {
                return 5;
            }
        } else if (shortChain.equals("246020602060")) {
            return 3;
        } else if (shortChain.equals("2420246060")) {
            return 4;
        } else if (shortChain.equals("24642460")) {
            return 6;
        } else if (shortChain.equals("246060")) {
            return 7;
        } else if (shortChain.equals("24602060")) {
            return 9;
        } else {
            return -1;
        }
    }

    public String getShortChain(String chain) {

        if (chain.length() < 2) {
            return chain;
        }

        char current;
        char last = chain.charAt(0);
        StringBuilder result = new StringBuilder();

        result.append(last);

        for (int i = 1; i < chain.length(); i++) {
            current = chain.charAt(i);
            if (current != last && Character.getNumericValue(current) % 2 == 0) {
                last = chain.charAt(i);
                result.append(last);
            }
        }

        return result.toString();
    }

    public ArrayList<Integer> getLongChain(String chain) {

        if (chain.length() < 2) {
            return new ArrayList<>();
        }

        ArrayList<Integer> list = new ArrayList<>();
        char current;
        char last = chain.charAt(0);
        int counter = 1;

        for (int i = 1; i < chain.length(); i++) {
            current = chain.charAt(i);
            if (Character.getNumericValue(current) % 2 == 0) {
                if (current != last) {
                    list.add(counter);
                    last = chain.charAt(i);
                    counter = 1;
                } else {
                    counter++;
                }
            }
        }

        list.add(counter);

        return list;
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
