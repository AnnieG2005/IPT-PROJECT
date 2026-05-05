package com.shruti.lofo.ui.Navigation;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shruti.lofo.R;
import com.shruti.lofo.utils.SessionManager;

public class DrawerManipulator {

    public static void updateDrawerHeader(Context context, View headerView) {
        TextView usernameTextView = headerView.findViewById(R.id.usernameTextView);
        SessionManager sessionManager = new SessionManager(context);

        if (sessionManager.isLoggedIn()) {
            usernameTextView.setText(sessionManager.getEmail());
        } else  {
            usernameTextView.setText(context.getString(R.string.nav_header_default_text));
        }
    }
}