package com.caocm.android.quiz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.caocm.android.quiz.db.QuizDatabaseHelper;


public class MainActivity extends FragmentActivity implements FeedbackDialogFragment.ResultDialogListener {

    private Question q;
    private int selectedAnswer;
    SQLiteDatabase db;
    private Cursor queryCursor;
    private View selectedItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        q = Question.getNextQuestion();

        TextView questionText = (TextView)findViewById(R.id.qustiontext);
        questionText.setText(q.getText());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, q.getOptions());
        final ListView listView = (ListView)findViewById(R.id.options);
        //listView.setBackgroundColor(Color.GRAY);
        listView.setAdapter(adapter);

        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < adapter.getCount(); size++) {
            View listItem = adapter.getView(size, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        Log.i("totalHeight", String.valueOf(totalHeight));
        //setting listview item in adapter
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight*6/5;
        listView.setLayoutParams(params);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedItemView != null)
                    selectedItemView.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
                selectedItemView = view;
                selectedItemView.setBackgroundColor(Color.rgb(0xA5, 0x2A, 0x2A));
                selectedAnswer = position;
            }
        });
    }

    @Override
    public void onStart(){
        QuizDatabaseHelper dbHelper= new QuizDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        queryCursor = db.query("QUESTIONS", new String[]{"QUSTIONBODY", "OPTIONA", "OPTIONB", "OPTIONC", "OPTIOND", "OPTIONE", "ANSWER"},
                null, null, null, null, null );

        if(queryCursor.moveToFirst()){
            q = new Question(queryCursor.getString(0));
            for(int i = 1; i < 5; i ++){
                q.addOption(queryCursor.getString(i));
            }
            q.setAnswer(queryCursor.getInt(6));
        }
        updateView();
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
        queryCursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void nextQuestion(){
        nextQuestion(null);
    }
    public void nextQuestion(View view){
        if(queryCursor.moveToNext() ) {
            selectedAnswer = -1;
            q = new Question(queryCursor.getString(0));
            for(int i = 1; i < 5; i ++){
                q.addOption(queryCursor.getString(i));
            }
            q.setAnswer(queryCursor.getInt(6));
        }

        updateView();
    }
    public void preQuestion(View view){
        if(queryCursor.moveToPrevious() ) {
            selectedAnswer = -1;
            q = new Question(queryCursor.getString(0));
            for(int i = 1; i < 5; i ++){
                q.addOption(queryCursor.getString(i));
            }
            q.setAnswer(queryCursor.getInt(6));
        }

        updateView();
    }

    private void updateView(){
        TextView questionText = (TextView)findViewById(R.id.qustiontext);
        questionText.setText(q.getText());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, q.getOptions());
        ListView listView = (ListView)findViewById(R.id.options);
        listView.setAdapter(adapter);
        ImageButton nextBtn = (ImageButton)findViewById(R.id.nextQ);
        ImageButton preBtn = (ImageButton)findViewById(R.id.preQ);
        nextBtn.setVisibility(View.VISIBLE);
        preBtn.setVisibility(View.VISIBLE);
        if(queryCursor.isLast()){
            nextBtn.setVisibility(View.INVISIBLE);
        }
        if(queryCursor.isFirst()){
            preBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void submitAnswer(View view){
        FeedbackDialogFragment feedback = new FeedbackDialogFragment();
        Bundle bundle = new Bundle();
        if(selectedAnswer == q.getAnswer()){
            bundle.putInt("RESULT", 1);
        }else if(selectedAnswer == -1){
            bundle.putInt("RESULT", -1);
        }else{
            bundle.putInt("RESULT", 0);
        }
        if(queryCursor.isLast()){
            bundle.putBoolean("ISLAST", true);
        }
        feedback.setArguments(bundle);
        feedback.show(getFragmentManager(), "missiles");
    }
}
