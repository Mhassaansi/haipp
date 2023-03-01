package com.appsnado.haippNew.Comman_Pacakges.retro

public class Genericdatapass<T> {

     internal var data: T? = null


    fun getData(): T? {
        return data
    }

    fun setData(data: T?) {
        this.data = data
    }






//
//     new AsyncTask<Void, Void, Void>() {
//
//          @Override
//          protected Void doInBackground( Void... voids ) {
//               //Do things...
//               return null;
//          }
//     }.execute();


}