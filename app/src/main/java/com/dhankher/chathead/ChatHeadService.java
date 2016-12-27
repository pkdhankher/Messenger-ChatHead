package com.dhankher.chathead;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Dhankher on 12/23/2016.
 */

public class ChatHeadService extends Service {
    WindowManager windowManager;
    View view;
    private String TAG = ChatHeadService.class.getCanonicalName();

    Float defaultOpenPositionX;
    Float defaultOpenPositionY;
    Float lastPositionX;
    Float lastPositionY;
    Float x;
    Float y;

    boolean isOpen = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private int radius;
    private int halfScreen;
    int statusBarHeight;


    @Override
    public void onCreate() {
        super.onCreate();


        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.messenger_views, null);
        final View circle = view.findViewById(R.id.circle);
        final View chatView = view.findViewById(R.id.chat_view);
        final View closeView = view.findViewById(R.id.close_view);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT);
        // params.gravity = Gravity.TOP | Gravity.RIGHT;
        // params.x = 0;
        // params.y = 100;
        windowManager.addView(view, params);

        statusBarHeight = getStatusBarHeight();
        circle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                radius = circle.getWidth() / 2;
            }
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                halfScreen = view.getWidth() / 2;
            }
        });


        circle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View chatHead) {

                if (isOpen) {
                    chatHead.setX(600);
                    chatHead.setY(120);
//                    lastPositionX = chatHead.getX();
//                    lastPositionY = chatHead.getY();
                      chatView.setVisibility(View.VISIBLE);
                    isOpen = false;
                } else {

//                    chatHead.setX(lastPositionX);
//                    chatHead.setY(lastPositionY);
                        chatView.setVisibility(View.GONE);
                    isOpen = true;

                }
            }
        });

        circle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: ");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        closeView.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = (event.getRawX() - radius);
                        circle.setX(x);
                        y = (event.getRawY() - radius - statusBarHeight);
                        circle.setY(y);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (x <= halfScreen) {
                            circle.setX(0f);
                            circle.setY(y);
                        } else if (x > halfScreen) {
                            circle.setX(2 * halfScreen - (radius * 2));
                            circle.setY(y);
                        }

                        closeView.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
