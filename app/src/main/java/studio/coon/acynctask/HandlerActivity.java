package studio.coon.acynctask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import studio.coon.acynctask.base.BaseActivity;

public class HandlerActivity extends BaseActivity {
    @BindView(R.id.et) EditText mEditText;
    @BindView(R.id.tvResult) TextView tvResult;
    @BindView(R.id.tvMilSec) TextView tvMilSec;
    @BindView(R.id.btnHandler) Button btnHandler;
    Handler mHandler;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        btnHandler.setText("AsyncTask");


        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                long timeS = bundle.getLong("TimeStart");
                long timeE = bundle.getLong("TimeEnd");
                tvResult.setText(bundle.getString("Pass"));
                long differenceMS = timeE - timeS;
                tvMilSec.setText(String.valueOf(differenceMS) + " мс");
            }
        };
    }



//    @Override
//    protected void onPause() {
//        // Удаляем Runnable-объект для прекращения задачи
//        mHandler.removeCallbacks(runnable);
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Добавляем Runnable-объект
//        mHandler.postDelayed(runnable, 100);
//    }

    // нажатие кнопки Start
    @OnClick(R.id.btnStart)
    public void onClickStart() {
        final String needToHackStr = mEditText.getText().toString();
        Runnable runnable = new Runnable() {
            public void run() {
                int indexHacking = 0;
                StringBuilder sbHackResult = new StringBuilder();
                long timeStart = System.currentTimeMillis();
                while(indexHacking < needToHackStr.length()){           // перебираем каждый символ в строке
                    for(char c = '\u0000'; c < 'z'; c++){               // сравниваем с исмволами из таблицы UNICODE
                        if(c == needToHackStr.charAt(indexHacking)){
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.wtf("sbHackResult", String.valueOf(sbHackResult));
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            long timeEnd = System.currentTimeMillis();
                            bundle.putString("Pass", sbHackResult.append(c).toString());
                            bundle.putLong("TimeStart", timeStart);
                            bundle.putLong("TimeEnd", timeEnd);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                            indexHacking++;
                            break;
                        }
                    }
                }

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    // нажатие кнопки AsyncTask
    @OnClick(R.id.btnHandler)
    public void onClickAsyncTask() {
        startActivity(new Intent(HandlerActivity.this, MainActivity.class));
    }
}
