package com.bignerdranch.android.nerdmail.inject;

import android.content.Context;

import com.bignerdranch.android.nerdmail.model.DataManager;
import com.bignerdranch.android.nerdmail.model.EmailNotifier;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NerdMailModule {

    private Context mApplicationContext;

    public NerdMailModule(Context context){
        mApplicationContext = context;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(){
        return new DataManager(mApplicationContext);
    }

    @Provides
    @Singleton
    EmailNotifier provideEmailnotifier(){
        return new EmailNotifier(mApplicationContext);
    }
}
