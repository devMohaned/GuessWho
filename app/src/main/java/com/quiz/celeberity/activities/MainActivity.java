package com.quiz.celeberity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.quiz.celeberity.R;
import com.quiz.celeberity.services.AddingAllCelebrityService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import jp.wasabeef.blurry.Blurry;

import static com.quiz.celeberity.services.AddingAllCelebrityService.ADD_ALL_CELEBRITY;

public class MainActivity extends AppCompatActivity {

    // 0 > Easy 1 > Medium 2 > Hard
    private int currentLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showImage();
        setupViews();
        startService();
      /*  String base64 = getByteArrayFromImageURL("https://www.myagecalculator.com/images/cristiano-ronaldo.jpg");
        Log.e("Tag base64", base64);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("score", MODE_PRIVATE);
        int highestScore = pref.getInt("score", 0);
        TextView highestScoreTextView = findViewById(R.id.ID_highest_score);
        highestScoreTextView.setText("" + highestScore);
    }

    private void startService()
    {
        Intent intent = new Intent(this, AddingAllCelebrityService.class);
        intent.setAction(ADD_ALL_CELEBRITY);
        startService(intent);
    }



    private void showImage()
    {
        ImageView mainImage = findViewById(R.id.ID_main_image);
        String url =
                "data:image/jpeg;base64,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";
        final String pureBase64Encoded = url.substring(url.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        mainImage.setImageBitmap(bitmap);

    }


    private void blurImage()
    {
        ImageView mainImage = findViewById(R.id.ID_main_image);
        String url =
                "data:image/jpeg;base64,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";
        final String pureBase64Encoded = url.substring(url.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        mainImage.setImageBitmap(bitmap);
        Blurry.with(MainActivity.this).radius(7 * currentLevel + 7).animate(500).from(bitmap).into(mainImage);

    }

    private void setupViews()
    {
        final Button mEasy = findViewById(R.id.ID_easy_cv);
        final Button mMedium = findViewById(R.id.ID_medium_cv);
        final Button mHard = findViewById(R.id.ID_hard_cv);

        CardView mStartQuiz = findViewById(R.id.ID_start_quiz_cv);


        mEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLevel = 0;
                resetAllCardViewsExcept(mEasy);
            }
        });

        mMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLevel = 1;
                resetAllCardViewsExcept(mMedium);
            }
        });

        mHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLevel = 2;
                resetAllCardViewsExcept(mHard);
            }
        });

        mStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizActivity.class);
                intent.putExtra("level",currentLevel);
                startActivity(intent);
            }
        });

    }


    private void resetAllCardViewsExcept(Button cardView)
    {
        final Button mEasy = findViewById(R.id.ID_easy_cv);
        final Button mMedium = findViewById(R.id.ID_medium_cv);
        final Button mHard = findViewById(R.id.ID_hard_cv);

        mEasy.setEnabled(true);
        mMedium.setEnabled(true);
        mHard.setEnabled(true);
        cardView.setEnabled(false);
        blurImage();
    }

    private String getByteArrayFromImageURL(String url) {

        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        return null;
    }

    private void initializeAds()
    {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

}
