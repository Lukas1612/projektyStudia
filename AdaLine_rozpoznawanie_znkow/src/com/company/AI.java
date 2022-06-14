package com.company;

import java.util.Arrays;
import java.util.Random;

public class AI{

    public static class Adaline {

        float output;
        float[] input;
        float[] weights;


        public Adaline(int inputSize, Random r) {
            input = new float[inputSize + 1];
            weights = new float[(1 + inputSize)];
            initWeights(r);
        }


        public void initWeights(Random r) {
            for (int i = 0; i < weights.length; i++) {
                weights[i] = (r.nextFloat() - 0.5f) * 4f;
            }
        }

        public float run(float[] in) {
            System.arraycopy(in, 0, input, 0, in.length);
            input[input.length - 1] = 1;
            output = 0;

           for (int j = 0; j < input.length; j++) {
               output += weights[j] * input[j];
           }

            output = (float) (1 / (1 + Math.exp(-output)));

            return output;
        }

        public void train(float error, float learningRate) {

            for(int j = 0; j < weights.length; j++)
            {
                weights[j] = weights[j] + (learningRate * error * input[j]);
            }
        }
    }


    Adaline[] units;

    public AI(int inputSize, int numberOfUnits) {
        units = new Adaline[numberOfUnits];
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < numberOfUnits; i++) {
            units[i] = new Adaline(inputSize, r);
        }
    }

    public float[] run(float[] input) {
        float[] output = new float[units.length];
        for (int i = 0; i < units.length; i++) {
            output[i] = units[i].run(input);
        }
        return output;
    }

    public void train(float[] input, float[] targetOutput, float learningRate) {
        float[] calcOut = run(input);
        float[] error = new float[calcOut.length];
        for (int i = 0; i < units.length; i++) {
            error[i] = targetOutput[i] - calcOut[i];
        }
        for (int i = units.length - 1; i >= 0; i--) {
            units[i].train(error[i], learningRate);
        }
    }

}