package com.quiz.celeberity.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "celebrities_table")
public class QuizItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "celeberity_name")
    private String celeberity_name;

    @ColumnInfo(name = "celeberity_image")
    private String celeberity_image;

    @ColumnInfo(name = "celeberity_gender")
    private String celeberity_gender;


    @ColumnInfo(name = "celeberity_category")
    private String celeberity_category;


    public QuizItem()
    {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCeleberity_name() {
        return celeberity_name;
    }

    public void setCeleberity_name(String celeberity_name) {
        this.celeberity_name = celeberity_name;
    }



    public String getCeleberity_image() {
        return celeberity_image;
    }

    public void setCeleberity_image(String celeberity_image) {
        this.celeberity_image = celeberity_image;
    }

    public String getCeleberity_category() {
        return celeberity_category;
    }

    public void setCeleberity_category(String celeberity_category) {
        this.celeberity_category = celeberity_category;
    }

    public String getCeleberity_gender() {
        return celeberity_gender;
    }

    public void setCeleberity_gender(String celeberity_gender) {
        this.celeberity_gender = celeberity_gender;
    }
}
