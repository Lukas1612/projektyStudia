package projektTrzy;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


public class App extends Application {

    private  AI ai_poket;
    private float maxValue = 0F;

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series;
        //series.setName("My portfolio");

        Scene scene  = new Scene(lineChart, 1600,1200);


        series = doAi(1000f, "series 1000");
        lineChart.getData().add(series);
        series = doAi(100f, "series 100");
        lineChart.getData().add(series);
        series = doAi(10f, "series 10");
        lineChart.getData().add(series);
        series = doAi(1f, "series 1");
        lineChart.getData().add(series);
        series = doAi(0.1f, "series 0.1");
        lineChart.getData().add(series);
        series = doAi(0.01f, "series 0.01");
        lineChart.getData().add(series);
        series = doAi(0.001f, "series 0.001");
        lineChart.getData().add(series);
        series = doAi(0.0001f, "series 0.0001");
        lineChart.getData().add(series);
        series = doAi(0.00001f, "series 0.00001");
        lineChart.getData().add(series);
        series = doAi(0.000001F, "series 0.000001");
        lineChart.getData().add(series);
        series = doAi(0.0000001f, "series 0.0000001");
        lineChart.getData().add(series);
        series = doAi_pocket(0.00001f, "series final");
        lineChart.getData().add(series);
        series = doAi_pocket(0.000001f, "series final");
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();

    }

    XYChart.Series doAi(float lRate, String name) throws Exception
    {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        //28x28 = 784
        float[][] train = new float[10000][784];
        float[][] res = new float[10000][10];


        MnistMatrix[] mnistMatrix = new MnistDataReader().readData("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte");

        for(int i=0 ; i<mnistMatrix.length; ++i)
        {
            Arrays.fill(res[i], 0);
            res[i][mnistMatrix[i].getLabel()] = 1;

            for (int r = 0; r < mnistMatrix[i].getNumberOfRows(); r++ ) {
                for (int c = 0; c < mnistMatrix[i].getNumberOfColumns(); c++) {
                    train[i][(r*28) + c] = mnistMatrix[i].getValue(r, c);
                }
            }
        }


        AI ai = new AI(784, 10);

        float hit = 0F;
        float miss = 0F;
        Random r = new Random();
        int en = 50;



        for (int e = 0; e < en; e++) {

            hit = 0;
            miss = 0;

            for (int i = 0; i < res.length; i++) {
                int idx = r.nextInt(res.length);
                ai.train(train[idx], res[idx], lRate);
            }

            //  System.out.printf("%d epoch\n", e + 1);
            for (int i = 0; i < res.length; i++) {
                float[] rr = res[i];
                float[] t = train[i];

                if(findMax(rr) == findMax(ai.run(t)))
                {
                    hit = hit+1F;
                }else
                {
                    miss = miss +1F;
                }
            }


            if(((hit*100)/(hit+miss))>maxValue)
            {
                maxValue = ((hit*100)/(hit+miss));
                ai_poket = ai;
                System.out.println("pocket " + name + ": hit/miss = " + (hit*100)/(hit+miss) + "%");
            }

            series.getData().add(new XYChart.Data(e, (hit*100)/(hit+miss)));


        }

        System.out.println(name + ": hit/miss = " + (hit*100)/(hit+miss) + "%");

        return series;

    }

    XYChart.Series doAi_pocket(float lRate, String name) throws Exception
    {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        //28x28 = 784
        float[][] train = new float[10000][784];
        float[][] res = new float[10000][10];


        MnistMatrix[] mnistMatrix = new MnistDataReader().readData("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte");

        for(int i=0 ; i<mnistMatrix.length; ++i)
        {
            Arrays.fill(res[i], 0);
            res[i][mnistMatrix[i].getLabel()] = 1;

            for (int r = 0; r < mnistMatrix[i].getNumberOfRows(); r++ ) {
                for (int c = 0; c < mnistMatrix[i].getNumberOfColumns(); c++) {
                    train[i][(r*28) + c] = mnistMatrix[i].getValue(r, c);
                }
            }
        }



        float hit = 0F;
        float miss = 0F;
        Random r = new Random();
        int en = 50;

        for (int i = 0; i < res.length; i++) {
            float[] rr = res[i];
            float[] t = train[i];

            if(findMax(rr) == findMax(ai_poket.run(t)))
            {
                hit = hit+1F;
            }else
            {
                miss = miss +1F;
            }
        }

        series.getData().add(new XYChart.Data(0, (hit*100)/(hit+miss)));

        System.out.println("pocket start: " + (hit*100)/(hit+miss));


        for (int e = 1; e < en; e++) {

            hit = 0;
            miss = 0;

            for (int i = 0; i < res.length; i++) {
                int idx = r.nextInt(res.length);
                ai_poket.train(train[idx], res[idx], lRate);
            }

            //  System.out.printf("%d epoch\n", e + 1);
            for (int i = 0; i < res.length; i++) {
                float[] rr = res[i];
                float[] t = train[i];

                if(findMax(rr) == findMax(ai_poket.run(t)))
                {
                    hit = hit+1F;
                }else
                {
                    miss = miss +1F;
                }
            }

            series.getData().add(new XYChart.Data(e, (hit*100)/(hit+miss)));

        }

        System.out.println(name + ": hit/miss = " + (hit*100)/(hit+miss) + "%");

        return series;

    }

    private static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.println("label: " + matrix.getLabel());
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                System.out.print(matrix.getValue(r, c) + " ");
            }
            System.out.println();
        }
    }

    public static int findMax(float[] r)
    {
        float max = 0;
        int index = 0;

        for(int i=0; i<r.length; ++i)
        {
            if(r[i]>max)
            {
                max = r[i];
                index = i;
            }
        }
        return index;
    }


    public static void main(String[] args) throws IOException {

        launch();


    }


}