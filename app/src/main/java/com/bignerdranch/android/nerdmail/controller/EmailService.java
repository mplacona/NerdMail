package com.bignerdranch.android.nerdmail.controller;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.bignerdranch.android.nerdmail.NerdMailApplication;
import com.bignerdranch.android.nerdmail.model.DataManager;
import com.bignerdranch.android.nerdmail.model.EmailNotifier;
import com.bignerdranch.android.nerdmailservice.Email;

import javax.inject.Inject;

/**
 * Created by mplacona on 18/08/2016.
 */
public class EmailService extends IntentService {
    private static final String TAG = "com.bignerdranch.android.nerdmail.EmailService";
    private static final String EXTRA_EMAIL = "com.bignerdranch.android.nerdmail.EMAIL_EXTRA";
    private static final String EXTRA_CLEAR = "com.bignerdranch.android.nerdmail.CLEAR_EXTRA";

    @Inject
    DataManager mDataManager;

    @Inject
    EmailNotifier mEmailNotifier;

    public EmailService() {
        super(TAG);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        NerdMailApplication.component(getApplicationContext()).inject(this);
    }

    public static Intent getNotifyIntent(Context context, Email email) {
        Intent intent = new Intent(context, EmailService.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

    public static Intent getClearintent(Context context){
        Intent intent = new Intent(context, EmailService.class);
        intent.putExtra(EXTRA_CLEAR, true);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean shouldClear = intent.getBooleanExtra(EXTRA_CLEAR, false);

        if(shouldClear){
            clearEmails();
        }else{
            Email email = (Email) intent.getSerializableExtra(EXTRA_EMAIL);
            mDataManager.insertEmail(email);

            mDataManager.getNotificationEmails().doOnNext(emails -> {
                mEmailNotifier.notifyOfEmails(emails);
            }).subscribe();
        }
    }

    private void clearEmails() {
        mDataManager.markEmailsAsNotified();
    }
}
