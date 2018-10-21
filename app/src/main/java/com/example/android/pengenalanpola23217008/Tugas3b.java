package com.example.android.pengenalanpola23217008;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Tugas3b extends AppCompatActivity {

    int kernelSize = 13;
    int tobeKernelSize = kernelSize;
    //int [][] bwCopyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas3b);

        ImageView img = findViewById(R.id.img);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fabiola_s1_cropped);

        Bitmap bitmapBW = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int [][] bwCopyValue = new int[height][width];

        for(int i=0; i<height; i++) //create grayscale image
        {
            for(int j=0; j<width; j++)
            {
                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                int bwValue = (redValue+blueValue+greenValue)/3;
                bwCopyValue[i][j] = bwValue;

                int currentBWColor = 0xFF000000 | (bwValue<<16 | bwValue<<8 | bwValue);
                bitmapBW.setPixel(j,i,currentBWColor);
            }
        }
        img.setImageBitmap(bitmapBW);

    }

    public void increment(View view){
        if(tobeKernelSize<=97)
            tobeKernelSize = tobeKernelSize+2;
        else
            tobeKernelSize = 99;
        display(tobeKernelSize);
    }

    /**
     * This method is called when - button is clicked
     */
    public void decrement(View view){
        if(tobeKernelSize>=5)
            tobeKernelSize = tobeKernelSize-2;
        else
            tobeKernelSize = 3;
        display(tobeKernelSize);
    }

    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.kernelsize_text_view);
        quantityTextView.setText("" + number);
    }

    public void applyKernel(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fabiola_s1_cropped);
        TextView kernelTV = (TextView) findViewById(R.id.kernelsize_text_view);
        String kernelTVstr = kernelTV.getText().toString();
        int kernelTVint = Integer.parseInt(kernelTVstr);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        secondImage(kernelTVint, height, width);
        Toast.makeText(Tugas3b.this, "Average kernel with size " + kernelTVint + " has applied",
                Toast.LENGTH_SHORT).show();
    }

    private void secondImage(int kernelTVint, int height, int width){
        int pad = (kernelTVint-1)/2;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fabiola_s1_cropped);
        Bitmap bitmapBW = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int bwCopyValue[][] = new int[height][width];
        for(int i=0; i<height; i++) //create grayscale level matrix for convolution need
        {
            for(int j=0; j<width; j++)
            {
                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                int bwValue = (redValue+blueValue+greenValue)/3;
                bwCopyValue[i][j] = bwValue;

                int currentBWColor = 0xFF000000 | (bwValue<<16 | bwValue<<8 | bwValue);
                bitmapBW.setPixel(j,i,currentBWColor);
            }
        }
        int bwAvgValue[][] = bwCopyValue; //default value from the original BW image
        Bitmap bitmapAfterBW = bitmapBW.copy(Bitmap.Config.ARGB_8888, true); //default pixel from the original BW image

        for(int i=pad; i<height-pad; i++) //convolution
        {
            for(int j=pad; j<width-pad; j++)
            {
                int bwSumValue = 0;
                for(int p=i-pad; p<=i+pad; p++)
                {
                    for (int q = j - pad; q <= j + pad; q++)
                    {
                        bwSumValue += bwCopyValue[p][q];
                    }
                }
                bwAvgValue[i][j] = bwSumValue/(kernelTVint*kernelTVint);
                int convBWColor = 0xFF000000 | (bwAvgValue[i][j]<<16 | bwAvgValue[i][j]<<8 | bwAvgValue[i][j]);
                bitmapAfterBW.setPixel(j,i,convBWColor);
            }
        }

        ImageView img2 = findViewById(R.id.img2);
        img2.setImageBitmap(bitmapAfterBW);
    }

}
