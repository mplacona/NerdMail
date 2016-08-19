package com.bignerdranch.android.nerdmail;

import android.app.Application;
import android.content.Context;

import com.bignerdranch.android.nerdmail.inject.DaggerNerdMailComponent;
import com.bignerdranch.android.nerdmail.inject.NerdMailComponent;
import com.bignerdranch.android.nerdmail.inject.NerdMailModule;

public class NerdMailApplication extends Application {
    public NerdMailComponent mComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        buildComponentAndInject();
    }

    private void buildComponentAndInject() {
        mComponent = DaggerComponentInitialiser.init(this);
    }

    public static NerdMailComponent component(Context context){
        return ((NerdMailApplication)
                context.getApplicationContext()).getComponent();
    }

    public NerdMailComponent getComponent(){
        return mComponent;
    }

    private final static class DaggerComponentInitialiser{
        public static NerdMailComponent init(NerdMailApplication app){
            return DaggerNerdMailComponent.builder()
                    .nerdMailModule(new NerdMailModule(app))
                    .build();
        }
    }
}
