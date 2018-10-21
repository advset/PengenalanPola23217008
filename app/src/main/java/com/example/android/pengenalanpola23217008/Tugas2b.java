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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Tugas2b extends AppCompatActivity {

    int[] RCount = new int[256];
    int[] GCount = new int[256];
    int[] BCount = new int[256];
    int[] bwCount = new int[256];
    double weight=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas2b);

        ImageView img = findViewById(R.id.img);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fabiola_s1_cropped);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap bitmapBW = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        for(int k=0; k<256; k++)
        {
            RCount[k] = 0;
            GCount[k] = 0;
            BCount[k] = 0;
            bwCount[k] = 0;
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

        for(int i=0; i<height; i++)
        {
            for(int j=0; j<width; j++)
            {


                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                int bwValue = (redValue+blueValue+greenValue)/3;
                int bwColour = 0xFF000000 | (bwValue<<16 | bwValue<<8 | bwValue);
                bitmapBW.setPixel(j,i,bwColour);
            }
        }

        img.setImageBitmap(bitmapBW);

    }

    public void applyChanges(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fabiola_s1_cropped);
        TextView weightTV = (TextView) findViewById(R.id.weightTV);
        String weightTVstr = weightTV.getText().toString();
        double weightTVint = Double.parseDouble(weightTVstr);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        secondImage(weightTVint, height, width);
        Toast.makeText(Tugas2b.this, "Weight " + weightTVint + " has applied",
                Toast.LENGTH_SHORT).show();
    }

    public void increment(View view){
        weight = weight+0.1;
        display(weight);
    }

    /**
     * This method is called when - button is clicked
     */
    public void decrement(View view){
        if(weight>=0.2){
            weight = weight-0.1;
        }
        display(weight);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(double number) {
        TextView weightTV = (TextView) findViewById(R.id.weightTV);
        weightTV.setText("" + number);
    }

    private void secondImage (double weight, int height, int width){
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

        /*
        //Calculating CDF - not scaled
        for(int i=0; i<256; i++)
        {
            RCDF[i] += RCount[0];
            GCDF[i] += GCount[0];
            BCDF[i] += BCount[0];
            bwCDF[i] += bwCount[0];
            for(int j=1; j<=i; j++)
            {
                RCDF[i] += 10*RCount[j];
                GCDF[i] += 10*GCount[j];
                BCDF[i] += 10*BCount[j];
                bwCDF[i] += 10*bwCount[j];
            }
        }
        */
        //Calculating CDF - not scaled
        for(int i=0; i<256; i++)
        {
            for(int j=0; j<i; j++)
            {
                RCDF[i] += weight*RCount[j];
                GCDF[i] += weight*GCount[j];
                BCDF[i] += weight*BCount[j];
                bwCDF[i] += weight*bwCount[j];
            }
            RCDF[i] += RCount[i];
            GCDF[i] += GCount[i];
            BCDF[i] += BCount[i];
            bwCDF[i] += bwCount[i];
        }

        //Calculating Level Mapping
        for(int i=0; i<256; i++)
        {
            RMap[i] = RCDF[i]*255/(RCDF[255]);
            GMap[i] = GCDF[i]*255/(GCDF[255]);
            BMap[i] = BCDF[i]*255/(BCDF[255]);
            bwMap[i] = bwCDF[i]*255/(bwCDF[255]);
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fabiola_s1_cropped);
        Bitmap bitmapAfter = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bitmapAfterBW = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        //apply the map it to new bitmap
        for(int i=0; i<height; i++)
        {
            for(int j=0; j<width; j++)
            {


                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);

                int bwValue = (redValue+blueValue+greenValue)/3;

                int currentColor = 0xFF000000 | RMap[redValue]<<16 | GMap[greenValue]<<8 | BMap[blueValue];

                int currentBWColor = 0xFF000000 | (bwMap[bwValue]<<16 | bwMap[bwValue]<<8 | bwMap[bwValue]);

                bitmapAfter.setPixel(j,i,currentColor);
                bitmapAfterBW.setPixel(j,i,currentBWColor);

                RNewCount[RMap[redValue]] += 1;
                GNewCount[GMap[greenValue]] += 1;
                BNewCount[BMap[blueValue]] += 1;
                bwNewCount[bwMap[bwValue]] += 1;

            }
        }
        //untuk image setelah pemrosesan
        ImageView img2 = findViewById(R.id.img2);
        img2.setImageBitmap(bitmapAfterBW);
        //img2.setImageBitmap(Bitmap.createScaledBitmap(bitmapAfter, width, height, false));

        GraphView Rgraph = (GraphView) findViewById(R.id.Rgraph);
        Rgraph.removeAllSeries();
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
        Ggraph.removeAllSeries();
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
        Bgraph.removeAllSeries();
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
        BWgraph.removeAllSeries();
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
        RgraphAfter.removeAllSeries();
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
        GgraphAfter.removeAllSeries();
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
        BgraphAfter.removeAllSeries();
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
        BWgraphAfter.removeAllSeries();
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
