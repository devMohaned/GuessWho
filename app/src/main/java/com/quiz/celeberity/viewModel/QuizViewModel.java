package com.quiz.celeberity.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.quiz.celeberity.model.QuizItem;
import com.quiz.celeberity.rooms.AppDatabase;

import java.util.List;


public class QuizViewModel extends AndroidViewModel {
    private QuizItem correctQuizList;
    private List<String> inCorrectQuizList;

    public QuizViewModel(@NonNull Application application) {
        super(application);
    }


    public QuizItem getCorrectQuiz() {
        correctQuizList =  AppDatabase.getAppDatabase(this.getApplication()).gameDAO().getRandomQuiz();
        return correctQuizList;
    }
    public List<String> getInCorrectQuiz(String celebrityName) {
        inCorrectQuizList =  AppDatabase.getAppDatabase(this.getApplication()).gameDAO().getInCorrectRandomQuiz(celebrityName);
        return inCorrectQuizList;
    }

}
