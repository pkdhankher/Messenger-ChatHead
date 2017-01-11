package com.dhankher.chathead;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dhankher on 12/23/2016.
 */

public class ChatHeadService extends Service {

    String TAG = "PAWAN";
    WindowManager windowManager;
    LayoutInflater inflater;
    FrameLayout chatHeadLayout, removeLayout, chatLayout, imgframe;
    TableLayout tableLayout;
    RecyclerView recyclerView;
    ImageView circleImage, removeImage;
    double radius;
    int x;
    int y;
    int statusBarHeight, screenWidth, screenHeight;
    int lastPositionX, lastPositionY;
    //    int removeViewX = (int) (screenWidth / 2 - radius), removeViewY = (int) (screenHeight * 9 / 10 + radius);
    int removeViewX = 295, removeViewY = 1068;
    boolean onClick = false;
    WindowManager.LayoutParams headparams, removeparams, chatviewparams;
    ObjectAnimator objectAnimatorTowardsRemoveView, objectAnimatorAwayFromRemoveView;
    AdapterClass adapterclass;
    List<String> list;
    String title, text,string;
    TextView textView;
    private boolean isNearToRemoveView = false;


//            TableRow tr = new TableRow(getApplicationContext());
//            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//            TextView textview = new TextView(getApplicationContext());
//            textview.setLayoutParams(new TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
//            textview.setTextSize(20);
//            textview.setTextColor(Color.parseColor("#0B0719"));
//            textview.setText(Html.fromHtml(pack + "<br><b>" + title + " : </b>" + text));
//            ImageView imageView = new ImageView(getApplicationContext());
//            imageView.setImageResource(image);
//            tr.addView(imageView);
//            tr.addView(textview);
//            tableLayout.addView(tr);

//        }
//    };

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
        circleImage = (ImageView) chatHeadLayout.findViewById(R.id.circle);
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
        imgframe = (FrameLayout) removeLayout.findViewById(R.id.imgframe);
        removeImage = (ImageView) removeLayout.findViewById(R.id.removeimg);

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
        Log.i("removeX: " + removeparams.x, "removeparmsY" + removeparams.y);

        chatLayout = (FrameLayout) inflater.inflate(R.layout.chatview, null);
        recyclerView = (RecyclerView) chatLayout.findViewById(R.id.rvView);
        // tableLayout = (TableLayout) chatLayout.findViewById(R.id.tab);



        chatviewparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        //  FLAG_ACTIVITY_NEW_TASK
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
                        if ((x > (removeViewX - 120) && x < (removeViewX + 120 + (radius * 2))) && (y > (removeViewY - 120) && y < (removeViewY + 120))) {

                            if (isNearToRemoveView == false) {

                                final int initialX = headparams.x;
                                final int initialY = headparams.y;
                                final int finalX = removeViewX;
                                final int finalY = removeViewY;
                                final float diffX = finalX - initialX;
                                final float diffY = finalY - initialY;
                                objectAnimatorTowardsRemoveView = new ObjectAnimator();
                                objectAnimatorTowardsRemoveView.setDuration(100);
                                objectAnimatorTowardsRemoveView.setFloatValues(0, 100);
                                objectAnimatorTowardsRemoveView.setInterpolator(new OvershootInterpolator());
                                objectAnimatorTowardsRemoveView.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        float value = (float) animation.getAnimatedValue();
                                        float currentX = initialX + (diffX * value / 100);
                                        float currentY = initialY + (diffY * value / 100);
                                        headparams.x = (int) currentX;
                                        headparams.y = (int) currentY;
                                        windowManager.updateViewLayout(chatHeadLayout, headparams);
                                    }
                                });
                                objectAnimatorTowardsRemoveView.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        removeImage.animate().scaleX(1.3f).scaleY(1.3f).setDuration(5);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                                objectAnimatorTowardsRemoveView.start();
                                isNearToRemoveView = true;
                            }

                        } else {
                            if (isNearToRemoveView == true) {
                                final int initialX = removeViewX;
                                final int initialY = removeViewY;

                                final int finalX = headparams.x;
                                final int finalY = headparams.y;

                                final float diffX = finalX - initialX;
                                final float diffY = finalY - initialY;

                                objectAnimatorAwayFromRemoveView = new ObjectAnimator();
                                objectAnimatorAwayFromRemoveView.setDuration(100);
                                objectAnimatorAwayFromRemoveView.setFloatValues(0, 100);
                                objectAnimatorAwayFromRemoveView.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        float value = (float) animation.getAnimatedValue();
                                        float currentX = initialX + (diffX * value / 100);
                                        float currentY = initialY + (diffY * value / 100);
                                        headparams.x = (int) currentX;
                                        headparams.y = (int) currentY;
                                        windowManager.updateViewLayout(chatHeadLayout, headparams);
                                    }
                                });
                                removeImage.animate().scaleX(1f).scaleY(1f).setDuration(5);
                                objectAnimatorAwayFromRemoveView.start();
                                isNearToRemoveView = false;
                            } else {
                                windowManager.updateViewLayout(chatHeadLayout, headparams);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        isLongClick = false;

                        if ((x > (removeViewX - 120) && x < (removeViewX + 120 + (radius * 2))) && (y > (removeViewY - 120) && y < (removeViewY + 120))) {
                            objectAnimatorTowardsRemoveView.removeAllUpdateListeners();
                            objectAnimatorTowardsRemoveView.removeAllListeners();
                            objectAnimatorAwayFromRemoveView.removeAllUpdateListeners();


                            windowManager.removeView(chatHeadLayout);
                            windowManager.removeView(chatLayout);
                            windowManager.removeView(removeLayout);
                            stopSelf();
                            return false;
                        }

                        if (x < (screenWidth / 2)) {
                            ObjectAnimator objectAnimator = new ObjectAnimator();
                            objectAnimator.setDuration(500);
                            float init = headparams.x;
                            objectAnimator.setFloatValues(init, 0f);
                            objectAnimator.setInterpolator(new OvershootInterpolator());
                            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    float current = (int) value;
                                    headparams.x = (int) current;
                                    headparams.y = (int) (y - statusBarHeight - radius);
                                    windowManager.updateViewLayout(chatHeadLayout, headparams);
                                }
                            });
                            objectAnimator.start();
                        } else {
                            ObjectAnimator objectAnimator = new ObjectAnimator();
                            objectAnimator.setDuration(500);
                            float initial = headparams.x;
                            float finalV = (float) (screenWidth - 2 * radius);
                            objectAnimator.setFloatValues(initial, finalV);
                            objectAnimator.setInterpolator(new OvershootInterpolator());
                            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    float current = (int) value;
                                    headparams.x = (int) current;
                                    headparams.y = (int) (y - statusBarHeight - radius);
                                    windowManager.updateViewLayout(chatHeadLayout, headparams);

                                }
                            });

                            objectAnimator.start();
                        }

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

//                removeViewX = (int) (screenWidth / 2 - radius);
//                removeViewY = (int) (screenHeight * 9 / 10 + radius);
            }
        });
        BroadcastReceiver onNotice = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String pack = intent.getStringExtra("package");
                title = intent.getStringExtra("title");
                text = intent.getStringExtra("text");
                Integer image = intent.getIntExtra("image", 0);
                Log.d("onReceive: " + title, "text" + text);
                list = new ArrayList<>();
                for (int i = 1; i <= 7; i++) {
                    list.add(title+"    "+text);

                }
                Log.d("listTitle " + title, "listText" + text);
                adapterclass = new AdapterClass(getApplicationContext(), list);
                recyclerView.setAdapter(adapterclass);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));







        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    list.remove(position);
                                    adapterclass.notifyItemRemoved(position);
                                }
                                adapterclass.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    list.remove(position);
                                    adapterclass.notifyItemRemoved(position);
                                }
                                adapterclass.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }


    private void chatHead_click() {
        //    boolean onClick = false;
        if (onClick) {
            final int intialX = headparams.x;
            final int intialY = headparams.y;

            final int finalX = lastPositionX;
            final int finalY = lastPositionY;

            final float diffX = finalX - intialX;
            final float diffY = finalY - intialY;

            ObjectAnimator objectAnimator = new ObjectAnimator();
            objectAnimator.setDuration(500);
            objectAnimator.setInterpolator(new OvershootInterpolator());
            objectAnimator.setFloatValues(0, 100);

            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    float currentX = intialX + (diffX * value / 100);
                    float currentY = intialY + (diffY * value / 100);
                    headparams.x = (int) currentX;
                    headparams.y = (int) currentY;

                    windowManager.updateViewLayout(chatHeadLayout, headparams);
                }
            });

            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    chatLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {


                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
            onClick = false;
        } else {
            lastPositionX = headparams.x;
            lastPositionY = headparams.y;

            final int intialX = headparams.x;
            final int intialY = headparams.y;

            final int finalX = (int) (screenWidth - (2 * radius));
            final int finalY = 10;


            final int diffX = finalX - intialX;
            final int diffY = finalY - intialY;

            ObjectAnimator objectAnimator = new ObjectAnimator();
            objectAnimator.setDuration(500);
            objectAnimator.setInterpolator(new OvershootInterpolator());
            objectAnimator.setFloatValues(0, 100);

            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    float curruntX = intialX + (diffX * value / 100);
                    float curruntY = intialY + (diffY * value / 100);
                    headparams.x = (int) curruntX;
                    headparams.y = (int) curruntY;

                    windowManager.updateViewLayout(chatHeadLayout, headparams);
                }
            });

            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    chatLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
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


//            ObjectAnimator animX = ObjectAnimator.ofFloat(headparams.x, "headparams.x", finalX);
//            ObjectAnimator animY = ObjectAnimator.ofFloat(headparams.y, "headparams.y", finalY);
//            AnimatorSet animSetXY = new AnimatorSet();
//            animSetXY.playTogether(animX, animY);
//            animSetXY.start();
//
//            windowManager.updateViewLayout(chatHeadLayout, headparams);