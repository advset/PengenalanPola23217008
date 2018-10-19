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

import com.example.android.pengenalanpola23217008.util.NewImageUtil;

public class UTS extends AppCompatActivity {
    private int[][] charNum = {{0,3,3,3,3,3,1,2,0,1},{2,8,64,0,74,384,1,256,133,4}};
    private int[][] charUpper = {{2,0,2,0,3,3,2,4,2,2,4,2,4,4,0,1,2,2,2,3,2,2,5,4,3,2},
            {68,485,0,354,480,416,0,168,8,8,32,96,40,40,2,420,2,420,0,272,40,0,0,0,0,320}};
    private int[][] charLower = {{2,2,2,2,1,4,1,3,2,2,4,2,4,3,0,2,2,3,2,4,3,3,5,4,3,3},
            {2,2,2,2,1,4,1,3,2,2,4,2,4,3,0,2,2,3,2,4,3,3,5,4,3,3}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uts);
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

    public void apply(View view) {
        ImageView img = findViewById(R.id.img);
        Bitmap image = ((BitmapDrawable) img.getDrawable()).getBitmap();
        TextView debugText = findViewById(R.id.debugText);
        int[] kick = NewImageUtil.getSkeletonFeature(image, debugText);
        debugText.setText(" Endpoints: " + kick[0] + "\n 9 parameters: " + kick[1]);
        int result = inference(kick[0],kick[1]);
        //debugText.setText(" Endpoints: " + kick[0] + "\n 9 parameters: " + kick[1] +"\n Identified as: " + (char)result);
        TextView  resultText = findViewById(R.id.Result);
        resultText.setText("Character Identified as: " + (char)result);
        //System.out.println(" Identified as "+(char)result);
        //ImageView imgSkeleton = findViewById(R.id.imgSkeleton);
        //imgSkeleton.setImageBitmap(result);
    }

    private int inference(int endpoint, int fPow){
        int identAs = -1;
        boolean identified = false;

        for(int i=0;i<10;i++){
            //check for numeric character
            if((charNum[0][i] == endpoint) && (charNum[1][i] == fPow) && !identified){
                identAs = i+48;
                identified = true;
            }
        }

        for(int j=0;j<26;j++){
            //check for uppercase alphabet character
            if((charUpper[0][j] == endpoint) && (charUpper[1][j] == fPow) && !identified){
                identAs = j+65;
                identified = true;
            }
        }

        for(int k=0;k<26;k++){
            //check for lowercase alphabet character
            if((charLower[0][k] == endpoint) && (charLower[1][k] == fPow) && !identified){
                identAs = k+97;
                identified = true;
            }
        }

        if(identAs == -1 ) //not detected until the end
        {
            identAs = 63;
        }
        return identAs;
    }
}
