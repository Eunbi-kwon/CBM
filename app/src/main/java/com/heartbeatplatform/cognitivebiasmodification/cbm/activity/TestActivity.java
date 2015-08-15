package com.heartbeatplatform.cognitivebiasmodification.cbm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heartbeatplatform.cognitivebiasmodification.cbm.R;
import com.heartbeatplatform.cognitivebiasmodification.cbm.dao.DBManager;
import com.heartbeatplatform.cognitivebiasmodification.cbm.model.QuestionInfo;
import com.heartbeatplatform.cognitivebiasmodification.cbm.model.QuestionResultInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestActivity extends Activity {

    ViewHandler handler;
    private static final int MAX_TEST_COUNT = 6;
    private DBManager dbManager;
    private GestureDetector gestureScanner;

    // view variable
    private TextView tViewCross;
    private ImageView iViewTop;
    private ImageView iViewBtm;
    private TextView tViewXTop;
    private TextView tViewXBtm;

    // for lock
    final Lock lock = new ReentrantLock();
    final Condition notComplete  = lock.newCondition();

    private List<Integer> smileFaceList = new ArrayList<Integer>();
    private List<Integer> angryFaceList = new ArrayList<Integer>();

    // information variable
    private QuestionInfo info;
    private QuestionResultInfo resultInfo ;
    private ArrayList<QuestionInfo> questions = new ArrayList <QuestionInfo>();

    // status variable
    private boolean isStart;
    private boolean isAvailableTouch;
    private long startTime;
    private long endTime;
    private float screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // ======  Set Handler ======
        gestureScanner = new GestureDetector(this, mGestureListener);
        handler = new ViewHandler();

        // ======  Get Data from DB ======
        dbManager = new DBManager(getApplicationContext());
        dbManager.onUpgrade();

        for ( int i = 1; i < MAX_TEST_COUNT ; i++ ) {
            QuestionInfo info = dbManager.selectQuestionInfo(i);
            questions.add(info);
            if ( info != null ){
                Log.d("TestActivity", " : TESTINFO " + info.toString());
            }
        }
        long seed = System.nanoTime();
        Collections.shuffle(questions, new Random(seed));


        // ======  Initialize test faces  ======

        String strSmile = "";
        String strAngry = "";
        int smileId = 0;
        int angryId = 0;
        for (int i = 1; i < MAX_TEST_COUNT; i++ )
        {
            Log.d("debug", "===== add face round : " + i + "======");
            if (i < 10) {
                strSmile = "face0" + i + "_" + "01";
                strAngry = "face0" + i + "_" + "05";
            }else if ( i < 20) {
                strSmile = "face" + i + "_" + "01";
                strAngry = "face" + i + "_" + "05";
            }else {
                strSmile = "face" + i + "_" + "01";
                strAngry = "face" + i + "_" + "05";
            }
            smileId = getApplicationContext().getResources().getIdentifier(strSmile, "drawable", getPackageName());
            angryId = getApplicationContext().getResources().getIdentifier(strAngry, "drawable", getPackageName());
            Log.d("debug", " smile id : " + smileId);
            Log.d("debug", " angry id : " + angryId);
            smileFaceList.add(smileId);
            angryFaceList.add(angryId);
        }


        // ======  Initialize variables  ======
        resultInfo = new QuestionResultInfo();
        startTime = 0L;
        endTime = 0L;
        isAvailableTouch = false;
        isStart = false;


        // ======= Initialize view =======

        tViewCross = (TextView)findViewById(R.id.test_text_view_cross);
        iViewTop = (ImageView)findViewById(R.id.test_image_view_top);
        iViewBtm = (ImageView)findViewById(R.id.test_image_view_btm);
        tViewXTop = (TextView)findViewById(R.id.test_text_view_x_top);
        tViewXBtm = (TextView)findViewById(R.id.test_text_view_x_btm);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;

    }



    public void onStartTest() {
        isStart = true;
        dbManager.removeQuestionResultInfo();

        Log.d("TestActivity", "on Start ~!!!! q.size = " + questions.size());
        // 2. Start Questions
        Thread questionThread = new Thread(new Runnable() {
            @Override
            public void run() {

                for(QuestionInfo q : questions) {

                    lock.lock();
                    info = q;

                    try {
                        // invoke main activity when doing a each question
                        Log.d("TestActivity", "Question = " + q.toString());
                        resultInfo.setQuestionNo(q.getQuestionNo());
                        resultInfo.setIsPositive(q.getIsPositive());
                        /*
                            step 1 : show a cross (visual target)
                            step 2 : show two pictures
                            step 3 : show a x
                            step 4 : show a blank page
                         */

                        if ( q.getQuestionNo() == 1) {
                            Thread.sleep(1000);
                            Log.d("TestActivity", "일초 쉰다");
                        }

                        Message msgStep1 = handler.obtainMessage(1);
                        handler.sendMessage(msgStep1);
                        Thread.sleep(500);

                        // 얼굴
                        Message msgStep2 = handler.obtainMessage(2, q.getPosSmile().equals("u") ? 1: 0, q.getQuestionNo());
                        handler.sendMessage(msgStep2);
                        Thread.sleep(1500);

                        if ( q.getPosX().equals("u") ) {
                            Log.d("TestActivity", "x의 위치가 위야!!!");
                        }
                        Message msgStep3 = handler.obtainMessage(3, q.getPosX().equals("u") ? 1: 0, q.getQuestionNo());
                        handler.sendMessage(msgStep3);
                        startTime = System.currentTimeMillis();
                        isAvailableTouch = true;

                        notComplete.await();
                        Log.d("TestActivity", "풀림!");

                        // Add onClickMethod for when user touch on screen
                        Message msgStep4 = handler.obtainMessage(4);
                        handler.sendMessage(msgStep4);
                        Thread.sleep(1000);

                    } catch (Exception e) {
                        Log.d("TestActivity", "Exception on thread");
                    } finally {
                        lock.unlock();
                    }
                }
                moveToResultPage();
            }
        });
        questionThread.start();
    }


    // This is handler that handle activity's view ( only main thread can handle view )
    public class ViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            /*
            step 1 : show a cross
            step 2 : show two pictures
            step 3 : show a dot
            step 4 : show a blank page

            */

            // 화면 동작 진행! case 문으로 분기해서 1,2,3 !!
            switch (msg.what) {
                case 1:
                    Log.d("TestActivity", "handleMessage (1)");
                    tViewCross.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    Log.d("TestActivity", "handleMessage (2)");
                    tViewCross.setVisibility(View.INVISIBLE);
                    if ( msg.arg1 == 1 ){
                        iViewTop.setImageResource(smileFaceList.get(msg.arg2 - 1 ));
                        iViewBtm.setImageResource(angryFaceList.get(msg.arg2 - 1));
                    }
                    else
                    {
                        iViewTop.setImageResource(angryFaceList.get(msg.arg2 - 1));
                        iViewBtm.setImageResource(smileFaceList.get(msg.arg2 - 1));
                    }
                    iViewTop.setVisibility(View.VISIBLE);
                    iViewBtm.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    Log.d("TestActivity", "handleMessage (3)");
                    iViewTop.setVisibility(View.INVISIBLE);
                    iViewBtm.setVisibility(View.INVISIBLE);

                    Log.d("TestActivity", "x의 위치 = " + msg.arg1);
                    if ( msg.arg1 == 1 ){
                        tViewXTop.setText(info.getArrow().equals("r") ? "▶" : "◀");
                        tViewXTop.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tViewXBtm.setText(info.getArrow().equals("r") ? "▶" : "◀");
                        tViewXBtm.setVisibility(View.VISIBLE);
                    }
                    break;
                case 4:
                    Log.d("TestActivity", "handleMessage (4)");
                    tViewXTop.setVisibility(View.INVISIBLE);
                    tViewXBtm.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( isStart == false ) {
            onStartTest();
        }
    }

    public void moveToResultPage() {
        isStart = false;
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureScanner.onTouchEvent(event);
    }

    GestureDetector.OnGestureListener mGestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY )
        {
            if (!isAvailableTouch)
            {
                return false;
            }
            Log.d("Error", "onFling");
            float startX = e1.getX();
            float startY = e1.getY();

            Log.d("TestActivity", "Action Down (1) X: " + startX + ", Y = " + startY + " /// screenHeight/2  = " + screenHeight / 2);
            Log.d("TestActivity", info.toString());
            if ( (screenHeight/2 < startY && info.getPosX().equals("u") ) || (screenHeight/2 >= startY && info.getPosX().equals("d"))) {
                Log.d("TestActivity", "x의 위치와 다른 곳을 눌러서 통과 못함");
                return false;
            }

            Log.d("TestActivity", "통과함");


            float xLorR = Math.abs(e1.getX()) - Math.abs(e2.getX());
            float yUorD = Math.abs(e1.getY()) - Math.abs(e2.getY());

            Log.d("TestActivity", "xLorR : " + xLorR + " / " + "yUorD" + yUorD);

            if ( Math.abs(xLorR) <= Math.abs(yUorD) ) {
                Log.d("TestActivity", "위아래는 아님요");
                return false;
            }

            if ( xLorR > 0 && info.getArrow().equals("r") || (xLorR <= 0 && info.getArrow().equals("l")))
            {
                Log.d("TestActivity", "방향이 틀렸음요");
                return false;
            }

            lock.lock();
            try {
                endTime = System.currentTimeMillis();
                long procTime = endTime - startTime;
                notComplete.signal();
                Log.d("TestActivity", "CLICK : procTime : " + procTime);

                // TODO : Result Insert
                resultInfo.setResultMillsec(procTime);
                Log.e("TestActivity", "RESULT : " + resultInfo.toString());
                dbManager.insertResultInfo(resultInfo);
                Log.e("TestActivity", dbManager.selectQuestionResultInfo(resultInfo.getQuestionNo()).toString());

            } finally {
                lock.unlock();
            }

            return true;
        }
        @Override
        public boolean onDown(MotionEvent e) {

            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
