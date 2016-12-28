package com.dhankher.chathead;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.print.PrintAttributes;
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
    FrameLayout chatHead, removeView;
    View circle, removeImg;
    double radius;
    int x;
    int y;
    int statusBarHeight, screenWidth;

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

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        chatHead = (FrameLayout) inflater.inflate(R.layout.chatheadview, null);
        circle = chatHead.findViewById(R.id.circle);
        final WindowManager.LayoutParams headparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        headparams.gravity = Gravity.TOP | Gravity.LEFT;
        headparams.x = 0;
        headparams.y = 100;
        windowManager.addView(chatHead, headparams);

        removeView = (FrameLayout) inflater.inflate(R.layout.removeview, null);
        removeImg = removeView.findViewById(R.id.removeimg);

        WindowManager.LayoutParams removeparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        removeparams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        windowManager.addView(removeView, removeparams);

        chatHead.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch: ");
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        headparams.x = (int) (x - radius);
                        headparams.y = (int) (y - statusBarHeight - radius);
                        windowManager.updateViewLayout(chatHead, headparams);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: " + screenWidth);
                        if (x < (screenWidth / 2)) {
                            x = 0;
                        } else {
                            x = (int) (screenWidth - 2 * radius);
                        }
                        headparams.x = x;
                        windowManager.updateViewLayout(chatHead, headparams);
                        removeView.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        chatHead.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        chatHead.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    chatHead.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    chatHead.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                radius = chatHead.getWidth() / 2;
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


//    WindowManager windowManager;
//    View view;
//    private String TAG = ChatHeadService.class.getCanonicalName();


//
//    double defaultOpenPositionX;
//    double defaultOpenPositionY;
//    Float lastPositionX;
//    Float lastPositionY;
//    int x;
//    int y;
//
//    boolean isOpen = true;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    private int radius;
//    private int halfScreen;
//    int statusBarHeight;
//    WindowManager.LayoutParams params;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//
//        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.chatheadview, null);
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_TOAST,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.TOP | Gravity.LEFT;
//        windowManager.addView(view, params);
//
//        statusBarHeight = getStatusBarHeight();
//
//        halfScreen = windowManager.getDefaultDisplay().getWidth();
//
//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//
//                radius = view.getWidth() / 2;
//                defaultOpenPositionY = view.getHeight() / 10;
//                defaultOpenPositionX = view.getWidth();
//            }
//        });
//
//
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(TAG, "onTouch: ");
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        x = (int) (event.getRawX() - radius);
//                        y = (int) (event.getRawY() - radius - statusBarHeight);
//
//                        params.x = x;
//                        params.y = y;
//                        windowManager.updateViewLayout(view, params);
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//
//                        if (event.getRawX() <= halfScreen) {
//                            x = 0;
//                        } else if (event.getRawX() > halfScreen) {
//                            x = 2 * halfScreen - (radius * 2);
//                        }
//                        y = (int) event.getRawY();
//
//
//                        params.x = x;
//                        params.y = y;
//
//                        windowManager.updateViewLayout(view, params);
//                        break;
//                }
//                return false;
//            }
//        });
//    }
//
//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//}
