package com.curtidosbadia.badiapp.app;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class AppController extends AppCompatActivity {
    private Scheduler scheduler;

    @Override
    public void onCreate(Bundle savedInstanceState){ super.onCreate(savedInstanceState);}

    private static AppController get(Context context){
        return (AppController) context.getApplicationContext();
    }

    public static AppController create(Context context){ return AppController.get(context); }

    public Scheduler subscribeScheduler() {
        if(scheduler == null){
            scheduler = Schedulers.io();
        }
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler){ this.scheduler = scheduler; }
}
