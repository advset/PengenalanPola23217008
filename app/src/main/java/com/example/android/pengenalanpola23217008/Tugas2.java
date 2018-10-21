package com.example.android.pengenalanpola23217008;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Tugas2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas2);

        ImageView img = findViewById(R.id.img);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fabiola_s1_cropped);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap bitmapAfter = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        //bitmapAfter.eraseColor(0x00000000);
        Bitmap bitmapAfterBW = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        //bitmapAfterBW.eraseColor(0x00000000);
        Bitmap bitmapBW = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        //untuk image setelah pemrosesan
        ImageView img2 = findViewById(R.id.img2);

        int[] RCount = new int[256];
        int[] GCount = new int[256];
        int[] BCount = new int[256];
        int[] bwCount = new int[256];

        int[] RCDF = new int[256];
        int[] GCDF = new int[256];
        int[] BCDF= new int[256];
        int[] bwCDF = new int[256];

        int[] RMap = new int[256];
        int[] GMap = new int[256];
        int[] BMap = new int[256];
        int[] bwMap = new int[256];

        int[] RNewCount = new int[256];
        int[] GNewCount = new int[256];
        int[] BNewCount = new int[256];
        int[] bwNewCount = new int[256];

        for(int k=0; k<256; k++)
        {
            RCount[k] = 0;
            GCount[k] = 0;
            BCount[k] = 0;
            bwCount[k] = 0;

            RCDF[k] = 0;
            GCDF[k] = 0;
            BCDF[k] = 0;
            bwCDF[k] = 0;

            RMap[k] = 0;
            GMap[k] = 0;
            BMap[k] = 0;
            bwMap[k] = 0;

            RNewCount[k] = 0;
            GNewCount[k] = 0;
            BNewCount[k] = 0;
            bwNewCount[k] = 0;
        }

        for(int i=0; i<376; i++)
        {
            for(int j=0; j<312; j++)
            {
                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                /* int redValue = (pixel & 0x00FF0000) >> 16;
                int greenValue = (pixel & 0x0000FF00) >> 8;
                int blueValue = (pixel & 0x000000FF); */
                int bwValue = (redValue+blueValue+greenValue)/3;

                RCount[redValue] += 1;
                GCount[greenValue] += 1;
                BCount[blueValue] += 1;
                bwCount[bwValue] += 1;
            }
        }

        //Calculating CDF - not scaled
        for(int i=0; i<256; i++)
        {
            for(int j=0; j<=i; j++)
            {
                RCDF[i] += RCount[j];
                GCDF[i] += GCount[j];
                BCDF[i] += BCount[j];
                bwCDF[i] += bwCount[j];
            }
        }

        //Calculating Level Mapping
        for(int i=0; i<256; i++)
        {
            RMap[i] = RCDF[i]*255/(376*312);
            GMap[i] = GCDF[i]*255/(376*312);
            BMap[i] = BCDF[i]*255/(376*312);
            bwMap[i] = bwCDF[i]*255/(376*312);
        }

        //apply the map it to new bitmap
        for(int i=0; i<height; i++)
        {
            for(int j=0; j<width; j++)
            {


                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                /* int redValue = (pixel & 0x00FF0000) >> 16;
                int greenValue = (pixel & 0x0000FF00) >> 8;
                int blueValue = (pixel & 0x000000FF); */
                int bwValue = (redValue+blueValue+greenValue)/3;
                int bwColour = 0xFF000000 | (bwValue<<16 | bwValue<<8 | bwValue);
                bitmapBW.setPixel(j,i,bwColour);

                //int currentColor = 0xFF000000 | (RMap[redValue]<<16 + GMap[greenValue]<<8 + BMap[blueValue]);
                //int currentColor = 0xFF000000 | (redValue<<16 + greenValue<<8 + blueValue);
                int currentColor = 0xFF000000 | RMap[redValue]*256*256 + GMap[greenValue]*256 + BMap[blueValue];

                int currentBWColor = 0xFF000000 | (bwMap[bwValue]<<16 | bwMap[bwValue]<<8 | bwMap[bwValue]);
                //int currentBWColor = 0xFF000000 | bwMap[bwValue]*256*256 + bwMap[bwValue]*256 + bwMap[bwValue];

                bitmapAfter.setPixel(j,i,currentColor);
                bitmapAfterBW.setPixel(j,i,currentBWColor);

                //bitmapAfter.setPixel(j,i,0xFFFFFF00);//ini ngawur

                RNewCount[RMap[redValue]] += 1;
                GNewCount[GMap[greenValue]] += 1;
                BNewCount[BMap[blueValue]] += 1;
                bwNewCount[bwMap[bwValue]] += 1;

            }
        }
        img.setImageBitmap(bitmapBW);
        img2.setImageBitmap(bitmapAfterBW);
        //img2.setImageBitmap(Bitmap.createScaledBitmap(bitmapAfter, width, height, false));

        GraphView Rgraph = (GraphView) findViewById(R.id.Rgraph);
        LineGraphSeries<DataPoint> seriesR = new LineGraphSeries<>();
        seriesR.setDrawBackground(true);
        seriesR.setBackgroundColor(0x33990000);
        for(int a = 0; a < 256; a++)
        {
            seriesR.appendData(new DataPoint(a,RCount[a]), true, 256);
        }
        seriesR.setColor(Color.RED);
        Rgraph.addSeries(seriesR);

        //add another graph
        GraphView Ggraph = (GraphView) findViewById(R.id.Ggraph);
        LineGraphSeries<DataPoint> seriesG = new LineGraphSeries<>();
        seriesG.setDrawBackground(true);
        seriesG.setBackgroundColor(0x33009900);
        for(int b = 0; b < 256; b++)
        {
            seriesG.appendData(new DataPoint(b,GCount[b]), true, 256);
        }
        seriesG.setColor(Color.GREEN);
        Ggraph.addSeries(seriesG);

        GraphView Bgraph = (GraphView) findViewById(R.id.Bgraph);
        LineGraphSeries<DataPoint> seriesB = new LineGraphSeries<>();
        seriesB.setDrawBackground(true);
        seriesB.setBackgroundColor(0x33000099);
        for(int c = 0; c < 256; c++)
        {
            seriesB.appendData(new DataPoint(c,BCount[c]), true, 256);
        }
        seriesB.setColor(Color.BLUE);
        Bgraph.addSeries(seriesB);

        GraphView BWgraph = (GraphView) findViewById(R.id.BWgraph);
        LineGraphSeries<DataPoint> seriesBW = new LineGraphSeries<>();
        seriesBW.setDrawBackground(true);
        seriesBW.setBackgroundColor(0x33999999);
        for(int d = 0; d < 256; d++)
        {
            seriesBW.appendData(new DataPoint(d,bwCount[d]), true, 256);
        }
        seriesBW.setColor(Color.GRAY);
        BWgraph.addSeries(seriesBW);

        //Histogram After
        GraphView RgraphAfter = (GraphView) findViewById(R.id.RgraphAfter);
        LineGraphSeries<DataPoint> seriesRA = new LineGraphSeries<>();
        seriesRA.setDrawBackground(true);
        seriesRA.setBackgroundColor(0x33990000);
        for(int a = 0; a < 256; a++)
        {
            seriesRA.appendData(new DataPoint(a,RNewCount[a]), true, 256);
        }
        seriesRA.setColor(Color.RED);
        RgraphAfter.addSeries(seriesRA);

        GraphView GgraphAfter = (GraphView) findViewById(R.id.GgraphAfter);
        LineGraphSeries<DataPoint> seriesGA = new LineGraphSeries<>();
        seriesGA.setDrawBackground(true);
        seriesGA.setBackgroundColor(0x33009900);
        for(int b = 0; b < 256; b++)
        {
            seriesGA.appendData(new DataPoint(b,GNewCount[b]), true, 256);
        }
        seriesGA.setColor(Color.GREEN);
        GgraphAfter.addSeries(seriesGA);

        GraphView BgraphAfter = (GraphView) findViewById(R.id.BgraphAfter);
        LineGraphSeries<DataPoint> seriesBA = new LineGraphSeries<>();
        seriesBA.setDrawBackground(true);
        seriesBA.setBackgroundColor(0x33000099);
        for(int c = 0; c < 256; c++)
        {
            seriesBA.appendData(new DataPoint(c,BNewCount[c]), true, 256);
        }
        seriesBA.setColor(Color.BLUE);
        BgraphAfter.addSeries(seriesBA);

        GraphView BWgraphAfter = (GraphView) findViewById(R.id.BWgraphAfter);
        LineGraphSeries<DataPoint> seriesBWA = new LineGraphSeries<>();
        seriesBWA.setDrawBackground(true);
        seriesBWA.setBackgroundColor(0x33999999);
        for(int d = 0; d < 256; d++)
        {
            seriesBWA.appendData(new DataPoint(d,bwNewCount[d]), true, 256);
        }
        seriesBWA.setColor(Color.GRAY);
        BWgraphAfter.addSeries(seriesBWA);
    }
}
