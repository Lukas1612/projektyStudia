package com.example.readyreadingkotlin.database

abstract class AbstractSerialDataInserter<T>(open var iterator: Iterator<T>?): IServerResponseHandler{

     fun startSerialInsertion()
    {

        if(iterator != null && iterator!!.hasNext())
        {
            insertNextSingleData()
        }

    }

   override fun onResponse(){

       if(iterator!!.hasNext())
       {
           insertNextSingleData()
       }

    }

    abstract fun insertNextSingleData()
}