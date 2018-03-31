package studio.coon.acynctask;

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

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText mEditText;
    Button btnStart;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.et);
        btnStart = (Button) findViewById(R.id.btnStart);
        mTextView1 = (TextView) findViewById(R.id.tvResult);
        mTextView2 = (TextView) findViewById(R.id.tvMilSec);
        mTextView3 = (TextView) findViewById(R.id.tvDone);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidAsyncTask validAsyncTask = new ValidAsyncTask();
                validAsyncTask.execute(mEditText.getText().toString());
                Log.wtf("editText", mEditText.getText().toString());
            }
        });


    }

    class ValidAsyncTask extends AsyncTask <String, String, String> {
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
            while(indexHacking < needToHackStr.length()){
                for(char c = '\u0000'; c < 'z'; c++){
                    if(c == needToHackStr.charAt(indexHacking)){
                        try { Thread.sleep(500);
                        } catch (InterruptedException e) {}
                        publishProgress(String.valueOf(c));          // следить за кол-вом объектов, которые уходят и приходят
                        indexHacking++;
                        break;
                    }
                }
            }
            return "Result String";
        }

        @Override
        protected void onProgressUpdate(String... values) {         // ... эти строчки говорят, что может прийти массив
            super.onProgressUpdate(values);
            Log.wtf("ValidAsyncTask", "onProgressUpdate" + values[0]);
            String data = values[0];
            //String completedString = mTextView1.getText().toString() + data;

            // более длинный вариант
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
            Log.wtf("ValidAsyncTask", "onPostExecute " + result);
            long differenceMS = System.currentTimeMillis() - startTime;
            mTextView2.setText(String.valueOf(differenceMS) + " мс");
            mTextView3.setText("МАЛАДЭС");
        }

    }
}
