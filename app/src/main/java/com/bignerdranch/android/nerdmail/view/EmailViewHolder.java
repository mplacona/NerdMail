package com.bignerdranch.android.nerdmail.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.nerdmail.R;
import com.bignerdranch.android.nerdmailservice.Email;

public class EmailViewHolder extends RecyclerView.ViewHolder {

    private EmailListItemView mEmailListItemView;

    public EmailViewHolder(View itemView) {
        super(itemView);
        mEmailListItemView = (EmailListItemView) itemView;
    }

    public void bindEmail(Email email) {
       mEmailListItemView.setEmail(email);
    }
}
