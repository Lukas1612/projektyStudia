package com.example.pong;

public class MLP{

    //W[0] = {n0, n2, ... nn}
    //W[1] = {n0, n2, ... nn}
    // .
    // .
    // .
    //W[n] = {n0, n2, ... nn}

   public float[] calculate(float[][] w, float[] X, int layers, int[] amountInLayers )
   {
       int INPUT_LAYER = 0;
       int OUTPUT_LAYER = layers-1;

       float[][] W = new float[layers][findMax(amountInLayers)];

       for(int i=0; i<layers; ++i)
       {
           for(int j=0; j<findMax(amountInLayers); ++j)
           {
               W[i][j]=w[i][j];
           }

       }


       float[] a;
       a = new float[layers];

       float[] finalResult;
       finalResult = new float[amountInLayers[OUTPUT_LAYER]];
       for(int i=0; i<amountInLayers[OUTPUT_LAYER]; ++i)
       {
           finalResult[i] = 0;
       }

       float yj=0;
       for(int J=0; J<layers; ++J)
       {
           a[J] = 0;
           for(int n=0; n<amountInLayers[J]; ++n)
           {
               if(J == INPUT_LAYER)
               {
                   a[J] = a[J] + W[J][n]*X[n];

               } else if(J == OUTPUT_LAYER)
               {
                   finalResult[n] = W[J][n]*a[J-1];
                   System.out.println(" -->> " +  finalResult[n]);
               }else
               {
                   if(n<(amountInLayers[J]-1))
                   {
                       yj = W[J][n] * a[J - 1];
                       a[J] = a[J] + yj;
                   }else
                   {
                       yj = 1;
                       a[J] = a[J] + yj;
                   }

               }

           }
       }

       return finalResult;
   }



   //N - learning constant
    public float[][] changeWeights(float[][] weights, float[] X, int layers, int[] amountInLayers, float[] t, float N)
    {
        for(int i=0; i<X.length; ++i)
        {
            System.out.println("X[" + i+ "] " +X[i]);
        }
        System.out.println("t " + t[0]);

            int INPUT_LAYER = 0;
            int OUTPUT_LAYER = layers - 1;
            int LAST_BUT_ONE_LAYER = 1;

            float[] a;
            float[][] D;
            float[] z;
            float bk = 0;
            float yj = 0;

            a = new float[layers];
            D = new float[layers][findMax(amountInLayers)];
            z = new float[amountInLayers[OUTPUT_LAYER]];

        float[][] W = new float[layers][findMax(amountInLayers)];

        for(int i=0; i<layers; ++i)
        {
            for(int j=0; j<findMax(amountInLayers); ++j)
            {
                W[i][j]=weights[i][j];
            }

        }



        for (int i = 0; i < layers; ++i) {
                a[i] = 0;
            }

            for (int i = 0; i < amountInLayers[OUTPUT_LAYER]; ++i) {
                z[i] = 0;
            }

            for (int i = 0; i < layers; ++i) {
                for (int j = 0; j < findMax(amountInLayers); ++j) {
                    D[i][j] = 0;
                }
            }


            for (int J = 0; J < layers; ++J) {
                a[J] = 0;
                for (int n = 0; n < amountInLayers[J]; ++n) {
                    if (J == INPUT_LAYER) {
                        a[J] = a[J] + W[J][n] * X[n];

                    } else if (J == OUTPUT_LAYER) {
                        bk = a[J - 1];
                        z[n] = W[J][n] * bk;
                    } else {

                        // bias
                        //W[i][j] = X[j] = 1
                        if(n<(amountInLayers[J]-1))
                        {
                            yj = W[J][n] * a[J - 1];
                            a[J] = a[J] + yj;
                        }else
                        {
                            yj = 1;
                            a[J] = a[J] + yj;
                        }
                    }

                }
            }


            for (int J = (OUTPUT_LAYER); J >= 0; --J) {

                for (int n = 0; n < amountInLayers[J]; ++n) {
                    if (J == INPUT_LAYER) {
                        for (int i = 0; i < amountInLayers[J + 1]; ++i) {
                            D[J][n] = D[J][n] + W[J][n] * D[J + 1][i];
                        }

                        yj = W[J][n] * X[n];
                        D[J][n] = D[J][n] * yj * (1F - yj);

                    } else if (J == OUTPUT_LAYER) {
                        D[J][n] = (z[n] - t[n]) * z[n] * (1F - z[n]);

                    } else {

                        for (int i = 0; i < amountInLayers[J + 1]; ++i) {
                            D[J][n] = D[J][n] + W[J][n] * D[J + 1][i];
                        }


                        // bias
                        //W[i][j]= 1
                        if(n<(amountInLayers[J]-1))
                        {
                            yj = W[J][n] * a[J - 1];
                            D[J][n] = D[J][n] * yj * (1F - yj);

                        }else
                        {
                            yj = 1;
                            D[J][n] = D[J][n] * yj * (1F - yj);

                        }
                    }

                }
            }







//update weights
            float xi=0;
            for (int J = (OUTPUT_LAYER); J >= 0; --J) {

                for (int n = 0; n < amountInLayers[J]; ++n) {
                    if (J == INPUT_LAYER) {


                        // bias
                        //W[i][j]= 1
                        if(n < (amountInLayers[J]-1))
                        {
                            W[J][n] = W[J][n] - N * D[J][n] * X[n];

                        }else
                        {
                            W[J][n] = 1;

                        }



                    } else if (J == OUTPUT_LAYER) {
                        for (int i = 0; i < amountInLayers[J - 1]; ++i) {

                            // bias
                            //W[i][j]= 1
                            if(i < (amountInLayers[J - 1]-1))
                            {
                                yj = W[J - 1][i] * a[J - 2];
                                W[J][n] = W[J][n] - N * D[J][n] * yj;

                            }else {
                                yj = 1;
                                W[J][n] = W[J][n] - N * D[J][n] * yj;

                            }


                        }

                    } else if (J == LAST_BUT_ONE_LAYER) {
                        for (int i = 0; i < amountInLayers[J - 1]; ++i) {


                            // bias
                            //W[i][j]= 1
                            if(n < (amountInLayers[J]-1))
                            {
                                W[J][n] = W[J][n] - N * D[J][n] * X[i] * W[J - 1][i];

                            }else
                            {
                                W[J][n] = 1;

                            }


                        }

                    } else {
                        for (int i = 0; i < amountInLayers[J - 1]; ++i) {

                            // bias
                            //W[i][j]= 1
                            if(n < (amountInLayers[J]-1))
                            {
                                xi = W[J - 1][i] * a[J - 2];
                                W[J][n] = W[J][n] - N * D[J][n] * xi;

                                if(i < (amountInLayers[J - 1]-1))
                                {
                                    xi = W[J - 1][i] * a[J - 2];
                                    W[J][n] = W[J][n] - N * D[J][n] * xi;

                                }else
                                {
                                    xi = 1;
                                    W[J][n] = W[J][n] - N * D[J][n] * xi;

                                }

                            }else
                            {
                                W[J][n] = 1;

                            }
                        }
                    }

                }
            }




        for(int I=0; I<layers; ++I)
        {
            for(int j=0; j<amountInLayers[I]; ++j)
            {
                System.out.println("[" + I+"][" +j+ "]" + W[I][j]);
            }
            System.out.println("z[0]" + z[0]);
            System.out.println("**************");
        }


        return W;

    }



    public int findMax( int[] amountInLayers)
    {
        int max = 0;

        for(int i = 0; i<amountInLayers.length; ++i)
        {
            if(amountInLayers[i]>max)
            {
                max = amountInLayers[i];
            }
        }

        return  max;
    }
}
