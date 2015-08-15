package com.heartbeatplatform.cognitivebiasmodification.cbm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.heartbeatplatform.cognitivebiasmodification.cbm.R;
import com.heartbeatplatform.cognitivebiasmodification.cbm.dao.DBManager;
import com.heartbeatplatform.cognitivebiasmodification.cbm.model.QuestionResultInfo;

import java.util.List;

public class ResultActivity extends Activity {

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        dbManager = new DBManager(getApplicationContext());

        long reactionTimeSum = 0L;
        long reactionTimePosSum = 0L;
        long reactionTimeNegSum = 0L;

        List<QuestionResultInfo> resultList = dbManager.selectQuestionResultInfoAll();
        Log.e("ResultActivity", "RESULT ALL : " + resultList.size());

        for ( QuestionResultInfo resultInfo : resultList )
        {
            if ( resultInfo != null ){
                Log.e("ResultActivity", " : TESTINFO " + resultInfo.toString());
                reactionTimeSum += resultInfo.getResultMillsec();

                if (resultInfo.getIsPositive().equals("p")) {
                    reactionTimePosSum += resultInfo.getResultMillsec();
                }
                else
                {
                    reactionTimeNegSum += resultInfo.getResultMillsec();
                }
            }
        }

        Log.e("ResultActivity", "RESULT :::: TOTAL : " + reactionTimeSum +", AVG TIME : " + (reactionTimeSum/20));
        Log.e("ResultActivity", "RESULT :::: POSITIVE " + (reactionTimePosSum));
        Log.e("ResultActivity", "RESULT :::: NEGATIVE " + (reactionTimeNegSum));

        TextView pValue = (TextView)findViewById(R.id.positiveValue);
        TextView nValue = (TextView)findViewById(R.id.negativeValue);
        pValue.setText((reactionTimePosSum) + " Millisecond" );
        nValue.setText((reactionTimeNegSum) + " Millisecond" );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
