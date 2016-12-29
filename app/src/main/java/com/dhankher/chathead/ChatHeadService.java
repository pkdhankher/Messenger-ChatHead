package com.dhankher.chathead;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * Created by Dhankher on 12/23/2016.
 */

public class ChatHeadService extends Service {

    String TAG = "PAWAN";
    WindowManager windowManager;
    LayoutInflater inflater;
    FrameLayout chatHeadLayout, removeLayout, chatLayout;
    View circleView, removeView, chatView;
    double radius;
    int x;
    int y;
    int statusBarHeight, screenWidth, screenHeight;
    int lastPositionX, lastPositionY;
    int removeViewX,removeViewY;
    boolean onClick = false;
    WindowManager.LayoutParams headparams, removeparams, chatviewparams;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        statusBarHeight = getStatusBarHeight();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        chatHeadLayout = (FrameLayout) inflater.inflate(R.layout.chatheadview, null);
        circleView = chatHeadLayout.findViewById(R.id.circle);
        headparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        headparams.gravity = Gravity.TOP | Gravity.LEFT;
        headparams.x = 0;
        headparams.y = 10;
        windowManager.addView(chatHeadLayout, headparams);

        removeLayout = (FrameLayout) inflater.inflate(R.layout.removeview, null);
        removeView = removeLayout.findViewById(R.id.removeimg);

        removeparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        removeparams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        windowManager.addView(removeLayout, removeparams);

        chatLayout = (FrameLayout) inflater.inflate(R.layout.chatview, null);
        chatView = chatLayout.findViewById(R.id.chat);
        chatviewparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        chatviewparams.gravity = Gravity.BOTTOM;
        windowManager.addView(chatLayout, chatviewparams);

        chatHeadLayout.setOnTouchListener(new View.OnTouchListener() {
            long startTime, endTime;
            boolean isLongClick = false;
            Handler handler_longclick = new Handler();
            Runnable runnable_longClick = new Runnable() {
                @Override
                public void run() {
                    isLongClick = true;
                    removeLayout.setVisibility(View.VISIBLE);

                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        handler_longclick.postDelayed(runnable_longClick, 600);
                        chatLayout.setVisibility(View.GONE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch: ");
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        headparams.x = (int) (x - radius);
                        headparams.y = (int) (y - statusBarHeight - radius);
                        windowManager.updateViewLayout(chatHeadLayout, headparams);

                        removeViewX = (int) (screenWidth / 2 - radius);
                        removeViewY = (int) (screenHeight*9/10+radius);

//                        removeViewY = removeparams.y;
//                        removeViewX = removeparams.x;

                        Log.d(TAG, "onTouch: x: " + x + ", removeViewX: " + removeViewX);
                        if ((x > (removeViewX - 50) && x < (removeViewX + 50 + (radius * 2)))&&(y>(removeViewY-50) && y<(removeViewY+50))) {
                            circleView.setBackgroundResource(R.drawable.circle_view_red);
                        } else {
                            circleView.setBackgroundResource(R.drawable.circle_view);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongClick = false;
                        if ((x > (removeViewX - 50) && x < (removeViewX + 50 + (radius * 2)))&&(y>(removeViewY-50) && y<(removeViewY+50))) {
                            windowManager.removeView(chatHeadLayout);
                            windowManager.removeView(chatLayout);
                            windowManager.removeView(removeLayout);
                            stopSelf();
                        }
                        Log.d(TAG, "onTouch: " + screenWidth);

                        if (x < (screenWidth / 2)) {
                            x = 0;
                        } else {
                            x = (int) (screenWidth - 2 * radius);
                        }
                        headparams.x = x;
                        windowManager.updateViewLayout(chatHeadLayout, headparams);

                        isLongClick = false;
                        removeLayout.setVisibility(View.GONE);
                        handler_longclick.removeCallbacks(runnable_longClick);
                        endTime = System.currentTimeMillis();
                        if (endTime - startTime < 100) {
                            chatHead_click();
                        }
                        break;
                }
                return false;
            }
        });


        chatHeadLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    chatHeadLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    chatHeadLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                radius = chatHeadLayout.getWidth() / 2;
            }
        });


    }

    private void chatHead_click() {
        //    boolean onClick = false;
        if (onClick) {
            headparams.x = lastPositionX;
            headparams.y = lastPositionY;
            windowManager.updateViewLayout(chatHeadLayout, headparams);
            chatLayout.setVisibility(View.GONE);
            onClick = false;
        } else {
            lastPositionX = headparams.x;
            lastPositionY = headparams.y;

            headparams.x = (int) (screenWidth - (2 * radius));
            headparams.y = 10;

            windowManager.updateViewLayout(chatHeadLayout, headparams);
            chatLayout.setVisibility(View.VISIBLE);
            onClick = true;
        }

        Log.d(TAG, "chatHead_click: x: " + headparams.x + ", y: " + headparams.y);
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

