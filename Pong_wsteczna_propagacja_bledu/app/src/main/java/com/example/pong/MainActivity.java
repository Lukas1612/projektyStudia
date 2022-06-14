package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements DataPreparationListener {

    private static final int MENU_NEW_GAME = 1;
    private static final int MENU_RESUME = 2;
    private static final int MENU_EXIT = 3;
    ArrayList<GameFrameData> data;

    private PongThread mGameThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pong_layout);

        final PongView mPongView = (PongView) findViewById(R.id.main);
        mPongView.setStatusView((TextView) findViewById(R.id.status));
        mPongView.setScoreView((TextView) findViewById(R.id.score));

        mGameThread = mPongView.getGameThread();
        mGameThread.setDataListener(this);

        if (savedInstanceState == null) {
            mGameThread.setState(PongThread.STATE_READY);
        } else {
            mGameThread.restoreState(savedInstanceState);
        }

       /* MLP mlp = new MLP();
        //float[][] W, float[] X, int layers, int[] amountInLayers, float[] t, float N
        float[][] W = {{ 1, 2, 3, 4 }, { 5, 6, 7, 8} , { 9, 10, 0, 0}};
        float[] X = {1, 2, 1, 2};
        int[] amountInLayers = {4, 4, 2};
        int layers = 3;
        float[] t = { 5, 6};
        float N = 0.3F;

        W = mlp.changeWeights(W, X,  layers, amountInLayers, t,  N);*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGameThread.pause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGameThread.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_NEW_GAME, 0, R.string.menu_new_game);
        menu.add(0, MENU_RESUME, 0, R.string.menu_resume);
        menu.add(0, MENU_EXIT, 0, R.string.menu_exit);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_NEW_GAME:
                mGameThread.startNewGame();
                return true;
            case MENU_EXIT:
                finish();
                return true;
            case MENU_RESUME:
                mGameThread.unPause();
                return true;
        }
        return false;
    }

   /* @Override
    public void setData(ArrayList<GameFrameData> data) {
        this.data = data;
        mGameThread.pause();


        MLP mlp = new MLP();

        float[][] W = new float[4][33];

        Random r = new Random();

        int[] amountInLayers = {4, 33, 33, 1};
        int layers = 4;

        for(int i=0; i<layers; ++i)
        {
            for(int j=0; j<amountInLayers[i]; ++j)
            {
                if(j==(amountInLayers[i]-1) && i<(layers-1))
                {
                    W[i][j] = 1;
                }else
                {
                    float random = 0.01F + r.nextFloat() * (0.02F - 0F);
                    W[i][j] = random;
                }

            }
        }

        float[] X = new float[amountInLayers[0]];
        X[amountInLayers[0]-1] = 1;
        float[] t = new float[amountInLayers[layers-1]];
        float N = 0.0000001F;

        for(int I=0; I<layers; ++I)
        {
            for(int j=0; j<amountInLayers[I]; ++j)
            {
                System.out.println("[" + I+"][" +j+ "]" + W[I][j]);
            }
            System.out.println("**************");
        }

        for(int i=0; i<100; ++i)
        {
            System.out.println(i);

            int random = 0 + (int)(Math.random() * ((898 - 0) + 1));


            X[0] = data.get(random).ballCy;
            X[1] = data.get(random).computerPlayerBoundsTop;
            X[2] = data.get(random).computerPlayerPaddleHeight;
            X[3] = 0;
            t[0] =  data.get(random).computerPlayerOutputPosition;


                W = mlp.changeWeights(W, X,  layers, amountInLayers, t,  N);

               /* for(int I=0; I<layers; ++I)
                {
                    for(int j=0; j<amountInLayers[I]; ++j)
                    {
                        System.out.println("[" + I+"][" +j+ "]" + W[I][j]);
                    }
                    System.out.println("**************");
                }*/


        /*}

        for(int I=0; I<layers; ++I)
        {
            for(int j=0; j<amountInLayers[I]; ++j)
            {
                System.out.println("[" + I+"][" +j+ "]" + W[I][j]);
            }
            System.out.println("**************");
        }


        MLP mlp2 = new MLP();
        System.out.println(" " +  X[0]+ " " +  X[1]+ " " +  X[2] + " " + t[0]);
        System.out.println(mlp2.calculate(W, X,  layers,  amountInLayers)[0]);

        mGameThread.startNewGame();

    }*/



    @Override
    public void setData(ArrayList<GameFrameData> data) {
        this.data = data;
        mGameThread.pause();



        mGameThread.startNewGame();

    }
}