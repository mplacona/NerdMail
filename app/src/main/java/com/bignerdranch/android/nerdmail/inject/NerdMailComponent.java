package com.bignerdranch.android.nerdmail.inject;

import com.bignerdranch.android.nerdmail.controller.DrawerActivity;
import com.bignerdranch.android.nerdmail.controller.EmailListFragment;
import com.bignerdranch.android.nerdmail.controller.EmailService;
import com.bignerdranch.android.nerdmail.view.EmailListItemView;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NerdMailModule.class})
public interface NerdMailComponent {
    void inject(EmailListFragment fragment);
    void inject(EmailListItemView emailListItemView);
    void inject(EmailService emailService);
    void inject(DrawerActivity drawerActivity);
}
