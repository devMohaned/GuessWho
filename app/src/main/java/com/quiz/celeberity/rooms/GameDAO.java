package com.quiz.celeberity.rooms;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.quiz.celeberity.model.QuizItem;

import java.util.List;


@Dao
public interface GameDAO {

    @Query("SELECT * FROM celebrities_table")
    LiveData<List<QuizItem>> getAll();


    @Query("SELECT * FROM celebrities_table ORDER BY RANDOM() LIMIT 1")
    QuizItem getRandomQuiz();

    @Query("SELECT celeberity_name FROM celebrities_table WHERE celeberity_name != :celeberityName ORDER BY RANDOM() LIMIT 3")
    List<String> getInCorrectRandomQuiz(String celeberityName);


    @Query("SELECT COUNT(celeberity_name) FROM celebrities_table")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(QuizItem... quiz);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIfFoundReplace(QuizItem... quiz);

}
