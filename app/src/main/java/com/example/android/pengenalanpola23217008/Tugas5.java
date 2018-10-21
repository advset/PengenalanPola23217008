package com.example.android.pengenalanpola23217008;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class Tugas5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas5);
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

    public void applyZS(View view){
        ImageView img = findViewById(R.id.img);
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();

        int widthOri = bitmap.getWidth();
        int heightOri = bitmap.getHeight();
        Bitmap workBmp = Bitmap.createScaledBitmap(bitmap, widthOri/5, heightOri/5, false);

        int width = workBmp.getWidth();
        int height = workBmp.getHeight();

        ArrayList<Point> changing1 = new ArrayList<>();
        ArrayList<Point> changing2 = new ArrayList<>();

        do{
            //Step 1
            //Checking Condition
            changing1.clear(); //clear this list after operation, for next iteration
            for(int i=1; i<height-1; i++){
                for(int j=1; j<width-1; j++){
                    int[] B = countNeighbours(i,j,workBmp);
                    int A = countTransitions(i,j,workBmp);

                    if(B[1] == 1 &&
                            B[0] >= 2 && B[0] <= 6 &&
                            A == 1 &&
                            (B[2] & B[4] & B[6]) == 0 &&
                            (B[4] & B[6] & B[8]) == 0){
                        changing1.add(new Point(j,i));
                    }
                }
            }

            if(changing1.size()>=1){
                //Remove all points that satisfy conditions in step 1
                for(int k=0; k<changing1.size(); k++){
                    Point coordinate1 = changing1.get(k);
                    workBmp.setPixel(coordinate1.x,coordinate1.y,0xFFFFFFFF);
                }
            }

            //Step 2
            changing2.clear(); //clear this list after operation, for next iteration
            for(int i=1; i<height-1; i++){
                for(int j=1; j<width-1; j++){
                    int[] B = countNeighbours(i,j,workBmp);
                    int A = countTransitions(i,j,workBmp);

                    if(B[1] == 1 &&
                            B[0] >= 2 && B[0] <= 6 &&
                            A == 1 &&
                            (B[2] & B[4] & B[8]) == 0 &&
                            (B[2] & B[6] & B[8]) == 0){
                        changing2.add(new Point(j,i));
                    }
                }
            }

            if(changing2.size()>=1){
                //Remove all points that satisfy conditions in step 2
                for(int k=0; k<changing2.size(); k++){
                    Point coordinate2 = changing2.get(k);
                    workBmp.setPixel(coordinate2.x,coordinate2.y,0xFFFFFFFF);
                }
            }

        }
        while(!(changing1.size()==0 && changing2.size()==0));

        Bitmap skeBmp = Bitmap.createScaledBitmap(workBmp, widthOri, heightOri, false);

        ImageView imgSkeleton = findViewById(R.id.imgSkeleton);
        imgSkeleton.setImageBitmap(skeBmp);
    }

    private int[] countNeighbours(int y, int x, Bitmap bmp){
        int[][] points = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};
        int currentPix = 0;
        int count = 0;
        int[] P = {0,0,0,0,0,0,0,0};
        if(bmp.getPixel(x,y) == 0xFF000000){ //check whether current pixel is black
            currentPix = 1;
        }

        for(int i=0; i<8; i++){
            int neighPix = bmp.getPixel(x+points[i][0],y+points[i][1]);
            if(neighPix == 0xFF000000){ //check whether (neighbour on this direction) pixel is black
                P[i] = 1;
                count += 1;
            }
        }
        return new int[]{count,currentPix,P[0],P[1],P[2],P[3],P[4],P[5],P[6],P[7]};
    }

    private int countTransitions(int y, int x, Bitmap bmp){
        int[] neighbour = countNeighbours(y,x,bmp);
        int count = 0;
        for(int i=2; i<9; i++){
            if(neighbour[i]==0 && neighbour[i+1]==1){
                count += 1;
            }
        }
        if(neighbour[9]==0 && neighbour[2]==1){
            count +=1;
        }
        return count;
    }
}
