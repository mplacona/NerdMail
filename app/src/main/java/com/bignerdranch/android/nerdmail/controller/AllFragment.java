package com.bignerdranch.android.nerdmail.controller;

import com.bignerdranch.android.nerdmailservice.Email;

import rx.Observable;

public class AllFragment extends EmailListFragment {

    @Override
    protected Observable<Email> getEmailList() {
        return mDataManager.getEmails();
    }
}
