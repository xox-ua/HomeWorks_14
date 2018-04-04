package studio.coon.acynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText mEditText;
    static TextView mTextView1;
    static TextView mTextView2;
    static TextView mTextView3;
    static long startTime;
    ValidAsyncTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.et);
        Button btnStart = (Button) findViewById(R.id.btnStart);
        mTextView1 = (TextView) findViewById(R.id.tvResult);
        mTextView2 = (TextView) findViewById(R.id.tvMilSec);
        mTextView3 = (TextView) findViewById(R.id.tvDone);
        Button btnHandler = (Button) findViewById(R.id.btnHandler);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidAsyncTask validAsyncTask = new ValidAsyncTask();
                validAsyncTask.execute(mEditText.getText().toString());
                Log.wtf("editText", mEditText.getText().toString());
            }
        });

        btnHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HandlerActivity.class));
            }
        });

        myTask = (ValidAsyncTask) getLastCustomNonConfigurationInstance();
        if (myTask == null) {
            myTask = new ValidAsyncTask();      // создаём асинтаск
            myTask.execute("VOVAN");          // со следующими параметрами - Stroka0
        }
        // передаем в MyTask ссылку на текущее MainActivity
        myTask.link(this);

    }

    public Object onRetainCustomNonConfigurationInstance() {
        Log.wtf("onRetainNonConfigurationInstance", " done");
        // удаляем из MyTask ссылку на старое MainActivity
        myTask.unLink();
        return myTask;
    }


    static class ValidAsyncTask extends AsyncTask <String, String, String> {
        WeakReference<MainActivity> activity;

        // получаем ссылку на MainActivity
        void link(MainActivity act) {
            activity = new WeakReference<>(act);
        }

        void unLink() {
            activity.clear();
        }

        // выполняется перед doInBackground, имеет доступ к UI
        @Override
        protected void onPreExecute() {
            Log.wtf("ValidAsyncTask", "onPreExecute");
            startTime = System.currentTimeMillis();
        }

        // новый поток, здесь решаем все свои тяжелые задачи. Т.к. поток не основной - не имеет доступа к UI.
        @Override
        protected String doInBackground(String... strings) {
            Log.wtf("ValidAsyncTask", "doInBackground " + strings[0]);
            int indexHacking = 0;
            String needToHackStr = strings[0];
            while(indexHacking < needToHackStr.length()){           // перебираем каждый символ в строке
                for(char c = '\u0000'; c < 'z'; c++){               // сравниваем с исмволами из таблицы UNICODE
                    if(c == needToHackStr.charAt(indexHacking)){
                        try { Thread.sleep(500);
                        } catch (InterruptedException e) {}
                        publishProgress(String.valueOf(c));          // следить за кол-вом объектов, которые уходят и приходят
                        indexHacking++;
                        break;
                    }
                }
            }
            return null;    // тут можно передавать результат = return "Stroka 77";
        }

        @Override
        protected void onProgressUpdate(String... values) {         // ... эти строчки говорят, что может прийти массив
            super.onProgressUpdate(values);
            if (activity == null) {
                Log.wtf("onProgressUpdate", "activity == null");
            } else {
                Toast.makeText(activity.get().getApplicationContext(), "onProgressUpdate", Toast.LENGTH_SHORT).show();
            }
            Log.wtf("ValidAsyncTask", "onProgressUpdate" + values[0]);
            String data = values[0];

            // соединяем полученные символы вместе
            StringBuilder builder = new StringBuilder();
            builder.append(mTextView1.getText().toString());    // а можно добавить готовую строку
            builder.append(data);                               // можно добавить один символ
            String completedString = builder.toString();        // результирующая строка
            mTextView1.setText(completedString);
        }

        // выполняется после doInBackground имеет доступ к UI
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
//            Toast.makeText(activity.get().getApplicationContext(), "onPostExecute", Toast.LENGTH_SHORT).show();
            Log.wtf("ValidAsyncTask", "onPostExecute " + result);
            long differenceMS = System.currentTimeMillis() - startTime;
            mTextView2.setText(String.valueOf(differenceMS) + " мс");
            mTextView3.setText("МАЛАДЭС");
        }

    }
}
