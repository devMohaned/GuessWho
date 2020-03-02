package com.quiz.celeberity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.quiz.celeberity.R;
import com.quiz.celeberity.dialog.FailedDialog;
import com.quiz.celeberity.dialog.OutOfLivesDialog;
import com.quiz.celeberity.model.QuizItem;
import com.quiz.celeberity.rooms.AppExecuter;
import com.quiz.celeberity.viewModel.QuizViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.blurry.Blurry;

public class QuizActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private static final int LEVEL_MAX = 10;
    int level = 0;
    private boolean showedDialogBefore = false;


    int correctCount = 0;
    int liveCount = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        if (getIntent().getExtras() != null) {
            level = getIntent().getExtras().getInt("level");
            setupAnswers();
            loadAd();
            setupRewardedVideo();
        }

    }


    Bitmap currentBitmap;

    private void showImage(String url) {
        try {
            final ImageView imageView = findViewById(R.id.ID_image);
            final String pureBase64Encoded = url.substring(url.indexOf(",") + 1);
            final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imageView.setImageBitmap(bitmap);
            currentBitmap = bitmap;
            Blurry.with(QuizActivity.this).radius(LEVEL_MAX * level + LEVEL_MAX).from(bitmap).into(imageView);
        }catch (NullPointerException e)
        {
            getQuiz();
        }
    }

    String correctName = "";
    String correctCategory = "";
    String correctGender = "";

    List<String> alreadyObtained = new ArrayList<>();
    int reattempsCount = 0;
    private void getQuiz() {
        AppExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                QuizViewModel quizViewModel = new QuizViewModel(getApplication());
                final QuizItem correctQuiz = quizViewModel.getCorrectQuiz();
                if (!alreadyObtained.contains(correctQuiz.getCeleberity_name()))
                {
                    alreadyObtained.add(correctQuiz.getCeleberity_name());
                    correctName = correctQuiz.getCeleberity_name();
                    correctCategory = correctQuiz.getCeleberity_category();
                    correctGender = correctQuiz.getCeleberity_gender();
                    final List<String> randomQuiz = new ArrayList<>();
                    randomQuiz.add(correctName);
                    List<String> inCorrectQuizList = quizViewModel.getInCorrectQuiz(correctName);
                    randomQuiz.add(inCorrectQuizList.get(0));
                    randomQuiz.add(inCorrectQuizList.get(1));
                    randomQuiz.add(inCorrectQuizList.get(2));
                    Collections.shuffle(randomQuiz);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showImage(correctQuiz.getCeleberity_image());
                            mFirstAnswer.setText(randomQuiz.get(0));
                            mSecondAnswer.setText(randomQuiz.get(1));
                            mThirdAnswer.setText(randomQuiz.get(2));
                            mForthAnswer.setText(randomQuiz.get(3));
                        }
                    });
                }else{
                    if (reattempsCount < 30) {
                        reattempsCount++;
                        getQuiz();
                    }
                }

            }
        });


    }


    ImageView mFirstCheckBox, mSecondCheckBox, mThirdCheckBox, mForthCheckBox;
    TextView mFirstAnswer, mSecondAnswer, mThirdAnswer, mForthAnswer;

    private void setupAnswers() {


        mFirstCheckBox = findViewById(R.id.ID_first_checkbox);
        mSecondCheckBox = findViewById(R.id.ID_second_checkbox);
        mThirdCheckBox = findViewById(R.id.ID_third_checkbox);
        mForthCheckBox = findViewById(R.id.ID_forth_checkbox);

        mFirstAnswer = findViewById(R.id.ID_first_answer);
        mSecondAnswer = findViewById(R.id.ID_second_answer);
        mThirdAnswer = findViewById(R.id.ID_third_answer);
        mForthAnswer = findViewById(R.id.ID_forth_answer);


        mFirstCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    checkImage(mFirstCheckBox);
                    checkSolution(mFirstAnswer.getText().toString());
                }
            }
        });

        mFirstAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    checkImage(mFirstCheckBox);
                    checkSolution(mFirstAnswer.getText().toString());
                }
            }
        });


        mSecondCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    checkImage(mSecondCheckBox);
                    checkSolution(mSecondAnswer.getText().toString());
                }
            }
        });

        mSecondAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    checkImage(mSecondCheckBox);
                    checkSolution(mSecondAnswer.getText().toString());
                }
            }
        });


        mThirdCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    checkImage(mThirdCheckBox);
                    checkSolution(mThirdAnswer.getText().toString());
                }
            }
        });

        mThirdAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    checkImage(mThirdCheckBox);
                    checkSolution(mThirdAnswer.getText().toString());
                }
            }
        });


        mForthCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    checkImage(mForthCheckBox);
                    checkSolution(mForthAnswer.getText().toString());
                }
            }
        });

        mForthAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    checkImage(mForthCheckBox);
                    checkSolution(mForthAnswer.getText().toString());
                }
            }
        });


        getQuiz();
    }


    private void resetImages() {
        mFirstCheckBox.setImageResource(R.drawable.ic_check_box_empty);
        mSecondCheckBox.setImageResource(R.drawable.ic_check_box_empty);
        mThirdCheckBox.setImageResource(R.drawable.ic_check_box_empty);
        mForthCheckBox.setImageResource(R.drawable.ic_check_box_empty);
    }

    boolean clicked = false;

    private void checkImage(ImageView checkBox) {
        ImageView quizImage = findViewById(R.id.ID_image);
        quizImage.setImageBitmap(currentBitmap);
        clicked = true;
        checkBox.setImageResource(R.drawable.ic_check_box);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getQuiz();
                resetImages();
                clicked = false;
            }
        }, 700);
    }


    private void checkSolution(String solution) {
        if (correctName.equals(solution)) {
            correctCount++;
            updateFrameCounts();
        } else {
            if (liveCount > 0) {
                liveCount--;
                updateLives();
            } else {
                if (!showedDialogBefore) {
                    showedDialogBefore = true;
                    showOutOfLiveDialog();
                } else {
                    showFailedDialog();
                }
            }
        }
    }


    int hintCount = 1;

    private void setupHint() {
        final ImageView hintImage = findViewById(R.id.ID_lamp);
        final TextView hintText = findViewById(R.id.ID_hint_text);
        hintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintImage.setVisibility(View.GONE);
                hintText.setText(getString(R.string.person_is) + " " + correctCategory);
                hintCount--;
            }
        });
    }


    private void updateLives() {
        RelativeLayout root = findViewById(R.id.root);
        ImageView heart = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(48, 48);
        heart.setPadding(8, 8, 8, 8);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.START_OF, R.id.counter);
        heart.setLayoutParams(params);
        heart.setImageResource(R.drawable.ic_like);
        root.addView(heart);
        animateGoToBottom(heart);


        TextView liveCountText = findViewById(R.id.ID_live_count);
        liveCountText.setText("" + liveCount);

    }

    int timeCounter = 44;
    int delay = 5000; // delay for 5 sec.
    int period = 1000; // repeat every sec.

    private void setupCounter() {
        final TextView timerText = findViewById(R.id.ID_timer);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timeCounter < 45) {
                    handler.postDelayed(this, 1000L);
                    timeCounter--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerText.setText("" + timeCounter);
                        }
                    });
                    return;
                }

                handler.removeCallbacks(this);
            }
        }, 1000L);

    }

    private void updateFrameCounts() {
        TextView textView = findViewById(R.id.ID_frame_count);
        textView.setText("" + correctCount);
    }

    private void animateGoToBottom(final ImageView imageView) {
        // load the animation
        Animation animMoveToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.go_to_bottom);

        // set animation listener
        animMoveToTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(animMoveToTop);
    }

    private void showOutOfLiveDialog() {
        final OutOfLivesDialog cdd = new OutOfLivesDialog(QuizActivity.this);
        cdd.setCancelable(false);
        cdd.show();
        if (isNewHighScore()) {
            cdd.highscore.setText(getString(R.string.new_high_score) + " " + correctCount);
            cdd.highscore.setVisibility(View.VISIBLE);
        }

        cdd.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                    cdd.dismiss();
                } else {
                    Toast.makeText(QuizActivity.this, getString(R.string.no_ads_found), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cdd.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdd.dismiss();
                finish();
            }
        });
    }

    private void showFailedDialog() {
        isNewHighScore();
        final FailedDialog cdd = new FailedDialog(QuizActivity.this);
        cdd.setCancelable(false);
        cdd.show();

        cdd.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                cdd.dismiss();
            }
        });

        cdd.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdd.dismiss();
                finish();
            }
        });

    }

    private boolean isNewHighScore() {
        SharedPreferences pref = getSharedPreferences("score", MODE_PRIVATE);
        int highestScore = pref.getInt("score", 0);

        if (correctCount > highestScore) {
            SharedPreferences.Editor editor = getSharedPreferences("score", MODE_PRIVATE).edit();
            editor.putInt("score", correctCount);
            editor.apply();
            return true;
        }
        return false;

    }

    private void resetGame() {
        alreadyObtained.clear();
        showedDialogBefore = false;
        correctCount = 0;
        liveCount = 5;
        updateFrameCounts();
        updateLives();
        getQuiz();
    }


    private void loadAd() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private RewardedVideoAd mRewardedVideoAd;

    private void setupRewardedVideo() {
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-7003413723788424/2980310030",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
    }

    @Override
    public void onRewardedVideoAdLoaded() {
    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }

    @Override
    public void onResume() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.resume(this);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.pause(this);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.destroy(this);
        }
        super.onDestroy();
    }

}