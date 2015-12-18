package com.caocm.android.quiz;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.JsonReader;
import android.util.Log;

import com.caocm.android.quiz.db.QuizDatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class DBUpdateIntentService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPDATEDB = "com.caocm.android.quiz.action.UPDATEDB";

    private static String quizserverzURL = "https://dry-cove-9345.herokuapp.com/questions/questionlist";
    List<Question> questions = new ArrayList<>();

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionDBUpdate(Context context) {
        Intent intent = new Intent(context, DBUpdateIntentService.class);
        intent.setAction(ACTION_UPDATEDB);
        context.startService(intent);
    }

    public DBUpdateIntentService() {
        super("DBUpdateIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("onHandleIntent", "start");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATEDB.equals(action)) {
                Log.i("onHandleIntent", "");
                downloadQuestions();
                handleActionUpdateDB();
            }
        }
    }
    private boolean downloadQuestions(){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            Log.i("Download data:", "......");
            String ss = downloadData();
            Log.i("Download data:", ss);
            return true;
        }else{
            Log.i("Download data:", "network doesn't work.");
            return false;
        }
    }
    private String downloadData(){
        InputStream is = null;

        try {
            URL url = new URL(quizserverzURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int code = conn.getResponseCode();
            is = conn.getInputStream();
            JsonReader reader = new JsonReader(new InputStreamReader(is));

            reader.beginArray();
            while (reader.hasNext()){
                questions.add(readQuestion(reader));
            }
            reader.endArray();
            return readIt(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    private Question readQuestion(JsonReader reader){
        String question = null;
        String optionA = null;
        String optionB = null;
        String optionC = null;
        String optionD = null;
        String optionE = null;
        int answer = 0;
        try {
            reader.beginObject();
            while (reader.hasNext()){
                String name = reader.nextName();
                Log.i("name", name);
                if(name.equals("_id")) {
                    reader.nextString();
                }else if(name.equals("question")){
                    question = reader.nextString();
                }else if(name.equals("optionss")){
                    reader.beginArray();
                    optionA = reader.nextString();
                    optionB = reader.nextString();
                    optionC = reader.nextString();
                    optionD = reader.nextString();
                    optionE = reader.nextString();
                    reader.endArray();
                }else if(name.equals("answer")){
                    answer = reader.nextInt();
                    Log.i("answer", String.valueOf(answer));
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Question q = new Question(question);
        q.addOption(optionA);
        q.addOption(optionB);
        q.addOption(optionC);
        q.addOption(optionD);
        q.addOption(optionE);
        q.setAnswer(answer);
        Log.i("Question:", q.toString());
        return q;
    }
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) {
        Reader reader = null;
        String retString = "";
        int len = 128;
        try {
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            while(reader.read(buffer) > 0){
                retString += new String(buffer);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retString;
    }
    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdateDB() {
        QuizDatabaseHelper dbHelper = new QuizDatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.deleteQuestions(db);
        for (Question q : questions) {
            List<String> options = q.getOptions();
            dbHelper.insertQustion(db, q.getText()
                    , options.get(0)
                    , options.get(1)
                    , options.get(2)
                    , options.get(3)
                    , options.get(4)
                    , q.getAnswer());
        }

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
