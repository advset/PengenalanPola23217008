package com.example.android.pengenalanpola23217008;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Tugas1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas1);

        LayoutInflater controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.activity_tugas1, null);
        ImageView imageView =(ImageView) viewControl.findViewById(R.id.img);
        int width = imageView.getDrawable().getIntrinsicWidth();
        int height = imageView.getDrawable().getIntrinsicHeight();
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        int[] RCount = new int[256];
        int[] GCount = new int[256];
        int[] BCount = new int[256];
        int[] bwCount = new int[256];

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


                int pixel = bitmap.getPixel(i,j);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                int bwValue = (redValue+blueValue+greenValue)/3;

                RCount[redValue] += 1;
                GCount[greenValue] += 1;
                BCount[blueValue] += 1;
                bwCount[bwValue] += 1;

            }
        }


        GraphView Rgraph = (GraphView) findViewById(R.id.Rgraph);

        LineGraphSeries<DataPoint> seriesR = new LineGraphSeries<>();
        seriesR.setDrawBackground(true);
        //seriesR.setBackgroundColor(Color.RED);
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
    }
}
