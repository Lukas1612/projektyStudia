package com.example.pong;

//N przykladow ((x^1,t^1),...,(x^N,t^N)).
//x^n= (1,(x^n)_1,..,(x^n)_i..,(x^n)_I) - wejscie
//t^n= (1,(t^n)_1,..,(t^n)_k..,(t^n)_K) - wyjscie
public class ERROR {

    public double calculate(int N, int K)
    {
        double result = 0.0;

        for(int i=0; i<N; ++i)
        {
            for(int j=0; j<K; ++j)
            {
                result = result + 1;
            }
        }
        return 0.0;
    }
}
