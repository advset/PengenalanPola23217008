package com.example.android.pengenalanpola23217008;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Tugas3a extends AppCompatActivity {

    int a = 40;
    int b = 50;
    int c = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas3a);

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

        int bwSpec[] = new int[256];

        for(int p=0; p<256; p++){
            if(p<=b){
                bwSpec[p] = (100 - a) * p / b + a;
            }
            else {
                bwSpec[p] = ((c - 100) * p + (100 - c) * 255 )/ (255 - b) + c;
            }
        }

        ImageView img2 = findViewById(R.id.img2);
        img2.setImageBitmap(bitmapBW);

        //plot fabricated histogram
        GraphView fabHist = (GraphView) findViewById(R.id.fabHist);
        LineGraphSeries<DataPoint> seriesBW = new LineGraphSeries<>();
        seriesBW.setDrawBackground(true);
        seriesBW.setBackgroundColor(0x33999999);
        for(int d = 0; d < 256; d++)
        {
            seriesBW.appendData(new DataPoint(d,bwSpec[d]), true, 256);
        }
        seriesBW.setColor(Color.GRAY);
        fabHist.addSeries(seriesBW);
        fabHist.getViewport().setMinX(0);
        fabHist.getViewport().setMaxX(255);
        fabHist.getViewport().setXAxisBoundsManual(true);
        fabHist.getViewport().setMinY(0);
        fabHist.getViewport().setMaxY(100);
        fabHist.getViewport().setYAxisBoundsManual(true);

        /** Seekbar A */
        SeekBar seekbarA = (SeekBar) findViewById(R.id.seekbarA);
        seekbarA.setMax(100);
        seekbarA.setProgress(a);

        // perform seek bar change listener event used for getting the progress value
        seekbarA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                a = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                TextView progressTextView = (TextView) findViewById(R.id.progressA);
                progressTextView.setText("current a value = "+a);
                Toast.makeText(Tugas3a.this, "Seek bar progress is :" + a,
                        Toast.LENGTH_SHORT).show();
                displayGraph(a,b,c);
            }
        });

        /** Seekbar B */
        SeekBar seekbarB = (SeekBar) findViewById(R.id.seekbarB);
        seekbarB.setMax(255);
        seekbarB.setProgress(b);

        // perform seek bar change listener event used for getting the progress value
        seekbarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                TextView progressTextView = (TextView) findViewById(R.id.progressB);
                progressTextView.setText("current b value = "+b);
                Toast.makeText(Tugas3a.this, "Seek bar progress is :" + b,
                        Toast.LENGTH_SHORT).show();
                displayGraph(a,b,c);
            }
        });

        /** Seekbar C */
        SeekBar seekbarC = (SeekBar) findViewById(R.id.seekbarC);
        seekbarC.setMax(100);
        seekbarC.setProgress(c);

        // perform seek bar change listener event used for getting the progress value
        seekbarC.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                c = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                TextView progressTextView = (TextView) findViewById(R.id.progressC);
                progressTextView.setText("current c value = "+c);
                Toast.makeText(Tugas3a.this, "Seek bar progress is :" + c,
                        Toast.LENGTH_SHORT).show();
                displayGraph(a,b,c);
            }
        });
    }

    public void applyChanges (View view) {
        displayGraph(a,b,c);
        doMatch(a,b,c);
        Toast.makeText(Tugas3a.this, "Change applied with a = " + a + ", b = " + b + ", c = "+c,
                Toast.LENGTH_SHORT).show();
    }

    private void displayGraph(int a, int b, int c){
        int bwSpec[] = new int[256]; //create desired histogram

        for(int p=0; p<256; p++){
            if(p<=b){
                bwSpec[p] = (100 - a) * p / b + a;
            }
            else {
                bwSpec[p] = ((c - 100) * p + (100 - c) * 255 )/ (255 - b) + c;
            }
        }

        //plot fabricated histogram
        GraphView fabHist = (GraphView) findViewById(R.id.fabHist);
        fabHist.removeAllSeries();
        LineGraphSeries<DataPoint> seriesBW = new LineGraphSeries<>();
        seriesBW.setDrawBackground(true);
        seriesBW.setBackgroundColor(0x33999999);
        for(int d = 0; d < 256; d++)
        {
            seriesBW.appendData(new DataPoint(d,bwSpec[d]), true, 256);
        }
        seriesBW.setColor(Color.GRAY);
        fabHist.addSeries(seriesBW);
        fabHist.getViewport().setMinX(0);
        fabHist.getViewport().setMaxX(255);
        fabHist.getViewport().setXAxisBoundsManual(true);
        fabHist.getViewport().setMinY(0);
        fabHist.getViewport().setMaxY(100);
        fabHist.getViewport().setYAxisBoundsManual(true);
    }

    private void doMatch(int a, int b, int c){
        int bwSpec[] = new int[256]; //create desired histogram

        for(int p=0; p<256; p++){
            if(p<=b){
                bwSpec[p] = (100 - a) * p / b + a;
            }
            else {
                bwSpec[p] = ((c - 100) * p + (100 - c) * 255 )/ (255 - b) + c;
            }
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fabiola_s1_cropped);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int [][] bwCopyValue = new int[height][width]; //store image bw intensity as matrix

        int [] bwCount = new int[256]; //image bw intensity histogram
        int [] bwCDF = new int[256]; //image bw intensity CDF

        for(int i=0; i<height; i++) //create grayscale image array and calculate histogram
        {
            for(int j=0; j<width; j++)
            {
                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                int bwValue = (redValue+blueValue+greenValue)/3;
                bwCopyValue[i][j] = bwValue;
                bwCount[bwValue] += 1; //add this pixel to corresponding intensity value bin
            }
        }

        for(int k=0; k<256; k++){ //calculate bw intensity CDF original image
            for(int l=0; l<=k; l++){
                bwCDF[k] += bwCount[l];
            }
        }

        //calculate CDF of desired histogram bwSpec
        int cdfSpec[] = new int[256];
        for(int q=0; q<256; q++){
            for(int r=0; r<=q; r++){
                cdfSpec[q] += bwSpec[r];
            }
        }

        //scaled both cdf to 1000
        for(int x=0; x<256; x++){
            bwCDF[x] = bwCDF[x]*1000/bwCDF[255];
            cdfSpec[x] = cdfSpec[x]*1000/cdfSpec[255];
        }

        //display desired/fabricated scaled CDF
        GraphView fabCDF = (GraphView) findViewById(R.id.fabCDF);
        fabCDF.removeAllSeries();
        LineGraphSeries<DataPoint> seriesFabCDF = new LineGraphSeries<>();
        seriesFabCDF.setDrawBackground(true);
        seriesFabCDF.setBackgroundColor(0x33999999);
        for(int d = 0; d < 256; d++)
        {
            seriesFabCDF.appendData(new DataPoint(d,cdfSpec[d]), true, 256);
        }
        seriesFabCDF.setColor(Color.GRAY);
        fabCDF.addSeries(seriesFabCDF);
        fabCDF.getViewport().setMinX(0);
        fabCDF.getViewport().setMaxX(255);
        fabCDF.getViewport().setXAxisBoundsManual(true);
        fabCDF.getViewport().setMinY(0);
        fabCDF.getViewport().setMaxY(1000);
        fabCDF.getViewport().setYAxisBoundsManual(true);

        //display original image bw intensity scaled CDF
        GraphView oriCDF = (GraphView) findViewById(R.id.oriCDF);
        oriCDF.removeAllSeries();
        LineGraphSeries<DataPoint> seriesOriCDF = new LineGraphSeries<>();
        seriesOriCDF.setDrawBackground(true);
        seriesOriCDF.setBackgroundColor(0x33999999);
        for(int d = 0; d < 256; d++)
        {
            seriesOriCDF.appendData(new DataPoint(d,bwCDF[d]), true, 256);
        }
        seriesOriCDF.setColor(Color.GRAY);
        oriCDF.addSeries(seriesOriCDF);
        oriCDF.getViewport().setMinX(0);
        oriCDF.getViewport().setMaxX(255);
        oriCDF.getViewport().setXAxisBoundsManual(true);
        oriCDF.getViewport().setMinY(0);
        oriCDF.getViewport().setMaxY(1000);
        oriCDF.getViewport().setYAxisBoundsManual(true);

        //do the match, create map /LUT
        int mapMatch[] = new int[256];
        for(int m=0; m<256; m++){
            int match = 0;
            int index = 255;

            for(int n=0; n<256; n++){
                int heng = cdfSpec[n] - bwCDF[m];
                if(heng >= 0){
                    index = n;
                    break;
                }
            }
            mapMatch[m] = index;
        }

        //plot mapMatch
        GraphView mmDisp = (GraphView) findViewById(R.id.mapMatchDisp);
        mmDisp.removeAllSeries();
        LineGraphSeries<DataPoint> seriesMM = new LineGraphSeries<>();
        seriesMM.setDrawBackground(true);
        seriesMM.setBackgroundColor(0x33999999);
        for(int d = 0; d < 256; d++)
        {
            seriesMM.appendData(new DataPoint(d,mapMatch[d]), true, 256);
        }
        seriesMM.setColor(Color.GRAY);
        mmDisp.addSeries(seriesMM);
        mmDisp.getViewport().setMinX(0);
        mmDisp.getViewport().setMaxX(255);
        mmDisp.getViewport().setXAxisBoundsManual(true);
        mmDisp.getViewport().setMinY(0);
        mmDisp.getViewport().setMaxY(255);
        mmDisp.getViewport().setYAxisBoundsManual(true);

        //apply map to image
        Bitmap bitMatch = bitmap.copy(Bitmap.Config.ARGB_8888, true); //bitmap to store matched image
        int resultCount[] = new int[256];
        for(int i=0; i<height; i++) //create grayscale image array and calculate histogram
        {
            for(int j=0; j<width; j++)
            {
                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                int bwValue = (redValue+blueValue+greenValue)/3;
                int resultBW = 0xFF000000 | (mapMatch[bwValue]<<16 | mapMatch[bwValue]<<8 | mapMatch[bwValue]);
                bitMatch.setPixel(j,i,resultBW);
                resultCount[mapMatch[bwValue]] += 1;
            }
        }
        ImageView img2 = findViewById(R.id.img2);
        img2.setImageBitmap(bitMatch);

        //plot mapMatch
        GraphView rcDisp = (GraphView) findViewById(R.id.rcDisp);
        rcDisp.removeAllSeries();
        LineGraphSeries<DataPoint> seriesRC = new LineGraphSeries<>();
        seriesRC.setDrawBackground(true);
        seriesRC.setBackgroundColor(0x33999999);
        for(int d = 0; d < 256; d++)
        {
            seriesRC.appendData(new DataPoint(d,resultCount[d]), true, 256);
        }
        seriesRC.setColor(Color.GRAY);
        rcDisp.addSeries(seriesRC);
        rcDisp.getViewport().setMinX(0);
        rcDisp.getViewport().setMaxX(255);
        rcDisp.getViewport().setXAxisBoundsManual(true);
    }
}
