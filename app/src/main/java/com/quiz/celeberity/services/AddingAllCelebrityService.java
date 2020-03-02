package com.quiz.celeberity.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.quiz.celeberity.R;
import com.quiz.celeberity.model.QuizItem;
import com.quiz.celeberity.rooms.AppDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AddingAllCelebrityService extends IntentService {
    public static String ADD_ALL_CELEBRITY = "add-all-celebrity";


    public AddingAllCelebrityService() {
        super("AddingAllCelebrityService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            String action = intent.getAction();
            if (ADD_ALL_CELEBRITY.equals(action)) {
                AppDatabase db = AppDatabase.getAppDatabase(this);
                Context context = this;

                addCustomCelebrity(db, context, "Footballer", "Male", "celebrities_football_male.txt");
                addCustomCelebrity(db, context, "Acting", "Male", "celebrities_actors_male.txt");
                addCustomCelebrity(db, context, "Acting", "Female", "celebrities_actress_female.txt");
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, this.getString(R.string.re_open_the_app), Toast.LENGTH_LONG).show();
            stopService(intent);
        }
    }

    List<String> fullList = new ArrayList<>();

    private void addCustomCelebrity(final AppDatabase db, Context context, String category, String gender, String fileName) {
        int count = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                count++;
//                    mLine = reader.readLine();
                String[] myStrings = mLine.split("\\?");
                String celeberityName = myStrings[0];
                String celebrityImage = myStrings[1];

                QuizItem quizItem = new QuizItem();
                quizItem.setCeleberity_name(celeberityName);
                quizItem.setCeleberity_category(category);
                quizItem.setCeleberity_gender(gender);
                quizItem.setCeleberity_image(celebrityImage);
                db.gameDAO().insertAll(quizItem);
                if (fullList.contains(celebrityImage)) {
                    Log.d("Task: ", count + ", " + mLine);
                }
                fullList.add(celebrityImage);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }


}
