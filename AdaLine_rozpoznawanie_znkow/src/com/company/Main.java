package com.company;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {



    public static void main(String[] args) throws IOException {


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


       /* mnistMatrix = new MnistDataReader().readData("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte");
        printMnistMatrix(mnistMatrix[3]);*/

        AI ai = new AI(784, 10);

        float hit = 0F;
        float miss = 0F;
        Random r = new Random();
        int en = 200;
        float lRate = 0.00001f;
        boolean[] tr = {false, false, false};
        for (int e = 0; e < en; e++) {

            for (int i = 0; i < res.length; i++) {
                int idx = r.nextInt(res.length);
                ai.train(train[idx], res[idx], lRate);

                //*****************
              /*  for (int J = 0; J < 100; J++) {
                    int n = r.nextInt(10000);
                    float[] rr = res[n];
                    float[] t = train[n];
                    // System.out.printf("%d epoch\n", e + 1);
                    //System.out.printf("R %d --> AI %d\n", findMax(rr), findMax(ai.run(t)));
                    if(findMax(rr) == findMax(ai.run(t)))
                    {
                        hit = hit+1F;
                    }else
                    {
                        miss = miss +1F;
                    }
                }
                System.out.println(hit + "   " + miss + " = " + (hit*100)/(hit+miss));
                hit = 0;
                miss = 0;
*/
                //*****************
            }

            if ((e + 1) % 100 == 0) {
                System.out.printf("%d epoch\n", e + 1);
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

                System.out.println("       ");
                System.out.println(hit + "/" + miss + " = " + (hit*100)/(hit+miss));
                System.out.println("       ");
                hit = 0;
                miss = 0;
            }
        }

      /*  int n = r.nextInt(train.length);
        for(int y=0; y<28; ++y)
        {
            for(int x=0; x<28; ++x)
            {
                System.out.printf(" %.0f ", train[n][(y*x)+x]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println(" result " + findMax(ai.run(ai.run(train[n]))));
        System.out.println(" result " + findMax(res[n]));*/


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


}
