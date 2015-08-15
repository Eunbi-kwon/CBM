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
import com.heartbeatplatform.cognitivebiasmodification.cbm.model.QuestionInfo;
import com.heartbeatplatform.cognitivebiasmodification.cbm.model.QuestionResultInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestDescriptionActivityStep2 extends Activity {

    ViewHandler handler;
    private GestureDetector gestureScanner;

    // view variable
    private TextView tViewCross;
    private ImageView iViewTop;
    private ImageView iViewBtm;
    private TextView tViewXTop;
    private TextView tViewXBtm;
    private TextView clickResultText;

    // for lock
    final Lock lock = new ReentrantLock();
    final Condition notComplete  = lock.newCondition();
    private List<Integer> smileFaceList = new ArrayList<Integer>();
    private List<Integer> angryFaceList = new ArrayList<Integer>();

    // information variable
    private QuestionInfo info;
    private QuestionResultInfo resultInfo;
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
        setContentView(R.layout.activity_test_description_step2);

        // ======  Set Handler ======
        gestureScanner = new GestureDetector(this, mGestureListener);
        handler = new ViewHandler();

        questions.add(new QuestionInfo(21, "u", "d", "u", "p", "r"));
        questions.add(new QuestionInfo(22, "d", "u", "d", "n", "r"));


        // ======  Initialize variables  ======
        resultInfo = new QuestionResultInfo();
        String strSmile = "";
        String strAngry = "";
        int smileId = 0;
        int angryId = 0;
        for (int i = 21; i < 23; i++ )
        {
            Log.e("debug", "===== add face round : " + i + "======");
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
            Log.e("debug", " smile id : " + smileId);
            Log.e("debug", " angry id : " + angryId);
            smileFaceList.add(smileId);
            angryFaceList.add(angryId);
        }


        // ======  Initialize variables  ======
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
        clickResultText = (TextView)findViewById(R.id.clickResultText);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
    }

    public void onStartTest() {
        isStart = true;

        // ======= Start Questions =======
        Thread questionThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.e("TestActivity", "Questions : " + questions.size());
                for (QuestionInfo q : questions) {
                    lock.lock();
                    info = q;

                    try {
                        Log.e("TestActivity" , "Questions: " + q.toString());

                        // invoke main activity when doing a each question
                        resultInfo.setQuestionNo(q.getQuestionNo());
                        resultInfo.setIsPositive(q.getIsPositive());

                        Log.e("TestActivity", "step 1 ");

                        if (q.getQuestionNo() == 1) {
                            Thread.sleep(1000);
                            Log.e("TestActivity", "일초 쉰다");
                        }

                        Log.e("TestActivity", "step 1 ");
                        Message msgStep1 = handler.obtainMessage(1);
                        handler.sendMessage(msgStep1);
                        Thread.sleep(500);

                        // 얼굴
                        Log.e("TestActivity", "step 2 ");
                        Message msgStep2 = handler.obtainMessage(2, q.getPosSmile().equals("u") ? 1 : 0, q.getQuestionNo());
                        handler.sendMessage(msgStep2);
                        Thread.sleep(1500);

                        if (q.getPosX().equals("u")) {
                            Log.e("TestActivity", "x의 위치가 위야!!!");
                        }

                        Log.e("TestActivity", "step 3 ");
                        Message msgStep3 = handler.obtainMessage(3, q.getPosX().equals("u") ? 1 : 0, q.getQuestionNo());
                        handler.sendMessage(msgStep3);
                        startTime = System.currentTimeMillis();
                        isAvailableTouch = true;

                        notComplete.await();
                        Log.e("TestActivity", "풀림!");


                        Log.e("TestActivity", "step 4 ");
                        // Add onClickMethod for when user touch on screen
                        Message msgStep4 = handler.obtainMessage(4);
                        handler.sendMessage(msgStep4);
                        Thread.sleep(1000);

                    } catch (Exception e) {
                        Log.e("TestActivity", "Exception on thread");
                        Log.e("TestActivity", e.toString());
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }

                }

                moveToResultPage();

            }
        });

        questionThread.start();
    }

/*


    public void onClickTestStartButton(View v) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_test_description_step1, menu);
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
                    Log.e("TestActivity", "handleMessage (1)");
                    clickResultText.setVisibility(View.INVISIBLE);
                    tViewCross.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    Log.e("TestActivity", "handleMessage (2)");
                    tViewCross.setVisibility(View.INVISIBLE);
                    if ( msg.arg1 == 1 ){
                        iViewTop.setImageResource(smileFaceList.get(msg.arg2 - 1 - 20 ));
                        iViewBtm.setImageResource(angryFaceList.get(msg.arg2 - 1 - 20));
                    }
                    else
                    {
                        iViewTop.setImageResource(angryFaceList.get(msg.arg2 - 1 - 20));
                        iViewBtm.setImageResource(smileFaceList.get(msg.arg2 - 1 - 20));
                    }
                    iViewTop.setVisibility(View.VISIBLE);
                    iViewBtm.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    Log.e("TestActivity", "handleMessage (3)");
                    iViewTop.setVisibility(View.INVISIBLE);
                    iViewBtm.setVisibility(View.INVISIBLE);

                    Log.e("TestActivity", "x의 위치 = " + msg.arg1);
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
                    Log.e("TestActivity", "handleMessage (4)");
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
        Intent intent = new Intent(this, TestDescriptionActivityStep3.class);
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
            Log.e("Error", "onFling");
            float startX = e1.getX();
            float startY = e1.getY();

            Log.e("TestActivity", "Action Down (1) X: " + startX + ", Y = " + startY + " /// screenHeight/2  = " + screenHeight / 2);
            Log.e("TestActivity", info.toString());
            if ( (screenHeight/2 < startY && info.getPosX().equals("u") ) || (screenHeight/2 >= startY && info.getPosX().equals("d"))) {
                Log.e("TestActivity", "x의 위치와 다른 곳을 눌러서 통과 못함");
                return false;
            }



            float xLorR = Math.abs(e1.getX()) - Math.abs(e2.getX());
            float yUorD = Math.abs(e1.getY()) - Math.abs(e2.getY());

            Log.e("TestActivity", "xLorR : " + xLorR + " / " + "yUorD" + yUorD);

            if ( Math.abs(xLorR) <= Math.abs(yUorD) ) {
                Log.e("TestActivity", "위아래는 아님요");
                return false;
            }


            if ( xLorR > 0 && info.getArrow().equals("r") || (xLorR <= 0 && info.getArrow().equals("l")))
            {
                Log.e("TestActivity", "방향이 틀렸음요");

                clickResultText.setText("이 바보야!!방향이 틀렸잖아");
                clickResultText.setVisibility(View.VISIBLE);
                return false;
            }

            clickResultText.setText("Great~~!!!");
            clickResultText.setVisibility(View.VISIBLE);


            lock.lock();
            try {
                endTime = System.currentTimeMillis();
                long procTime = endTime - startTime;
                notComplete.signal();
                Log.e("TestActivity", "CLICK : procTime : " + procTime);
                resultInfo.setResultMillsec(procTime);
                Log.e("TestActivity", "RESULT : " + resultInfo.toString());
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
}
