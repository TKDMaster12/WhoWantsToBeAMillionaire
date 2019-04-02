package com.example.whowantstobeamillionaire;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class Game {

    private final int questionDelay = 2500;
    private final int nextQuestionTimer = 2500;
    private static Activity activity;
    private final ArrayList<Questions> easyQuestions;
    private final ArrayList<Questions> hardQuestions;
    private final ArrayList<Questions> mediumQuestions;
    private final Button answerA;
    private final Button answerB;
    private final Button answerC;
    private final Button answerD;

    private static int timeCounter = 0;
    public static Timer timer = new Timer();

    private Button exitButton;
    private static String winningAmount;
    private final TextView questionContainer;
    private Questions questions;
    private TextView TimeCounter;
    private ImageButton FiftyFifty;
    private ImageButton Phone;
    private ImageButton Audience;
    private static String audienceAnswer;
    private Button[] moneyArray;
    public static CountDownTimer countDownTimer;
    private int index;
    private int questionPieces = 0;
    private final int questionCounter = 0;
    private final boolean fiftyFiftyLifeLine = true;
    boolean audienceLifeLine = true;
    private boolean fiftyFiftyAnswer = true;
    boolean phoneLifeLine = true;
    public static MediaPlayer mediaPlayer;
    private static String withdrawal = "0";
    private static int exitStatus = 0;
    private static String phoneAnswer;

    private Button getExitButton() {
        return exitButton;
    }

    public void setExitButton(Button exitButton) {
        this.exitButton = exitButton;
    }

    private ImageButton getAudience() {
        return Audience;
    }

    public void setAudience(ImageButton audience) {
        Audience = audience;
    }

    private ImageButton getPhone() {
        return Phone;
    }

    public void setPhone(ImageButton phone) {
        Phone = phone;
    }

    private ImageButton getFiftyFifty() {
        return FiftyFifty;
    }

    public void setFiftyFifty(ImageButton fiftyFifty) {
        FiftyFifty = fiftyFifty;
    }

    private TextView getTimeCounter() {
        return TimeCounter;
    }

    public void setTimeCounter(TextView timeCounter) {
        TimeCounter = timeCounter;
    }

    private Button[] getMoneyArray() {
        return moneyArray;
    }

    public void setMoneyArray(Button[] moneyArray) {
        this.moneyArray = moneyArray;
    }

    private int getQuestionPieces() {
        return questionPieces;
    }

    public void setQuestionPieces(int questionPieces) {
        this.questionPieces = questionPieces;
    }

    public Game(final Activity activity, Button answerA, Button answerB, Button answerC, Button answerD, TextView questionContainer) {
        this.activity = activity;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.questionContainer = questionContainer;
        hardQuestions = nextHardQuestion();
        easyQuestions = nextEasyQuestion();
        mediumQuestions = nextMediumQuestion();
        mediaPlayer = MediaPlayer.create(activity, R.raw.game_start);
        SharedPreferences sharedPreferences=activity.getSharedPreferences("sound", Context.MODE_PRIVATE);

        if(Objects.equals(sharedPreferences.getString("sound", ""), "ok")) {
            mediaPlayer.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(activity, R.raw.display_question);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(0.3f, 0.3f);
                }
            }, 2500);
        }
    }

    public void playGame() {

        getExitButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitGame();
            }
        });
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                timeCounter++;
                Log.e("Time:::", "" + timeCounter);
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(t, 1000, 1000);
        nextQuestion(questionCounter, new response() {
            @Override
            public void answerTrue() {

            }

            @Override
            public void answerFalse() {
                FinishGame();
            }
        });
    }

    private ArrayList<Questions> nextEasyQuestion() {
        ArrayList<Questions> easyQuestions = new ArrayList<>();
        XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            InputStream in_s = activity.getApplicationContext().getAssets().open("easy_questions.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            easyQuestions = questionParser(parser);

            for (Questions questions : easyQuestions) {
                Log.e("Incoming Questions:", questions.getAskedQuestion());
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(easyQuestions);
        return easyQuestions;
    }

    private ArrayList<Questions> nextMediumQuestion() {
        ArrayList<Questions> mediumQuestions = new ArrayList<>();
        XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            InputStream in_s = activity.getApplicationContext().getAssets().open("medium_questions.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            mediumQuestions = questionParser(parser);
            for (Questions questions : mediumQuestions) {
                Log.e("Incoming Questions:", questions.getCorrectAnswer());
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(mediumQuestions);
        return mediumQuestions;
    }

    private ArrayList<Questions> nextHardQuestion() {
        ArrayList<Questions> hardQuestions = new ArrayList<>();
        XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            InputStream in_s = activity.getApplicationContext().getAssets().open("hard_questions.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            hardQuestions = questionParser(parser);

            for (Questions questions : hardQuestions) {
                Log.e("Incoming Questions:", questions.getCorrectAnswer());
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(hardQuestions);
        return hardQuestions;
    }

    private void nextQuestion(int questionOrder, final response response) {
        index = questionOrder;

        if (index < 5)
            questions = easyQuestions.get(index);
        else if (index < 9)
            questions = mediumQuestions.get(index - 5);
        else
            questions = hardQuestions.get(index - 9);

        Map<String, String> stringMapAnswer;

        stringMapAnswer = questions.getAnswers();
        FiftyFiftyLifeLine(getFiftyFifty(), questions.getCorrectAnswer());
        phoneLifeLineResponse(questions.getCorrectAnswer(), getPhone());
        CallTheAudience(getAudience(), questions.getCorrectAnswer());

        answerA.setVisibility(View.VISIBLE);
        answerB.setVisibility(View.VISIBLE);
        answerC.setVisibility(View.VISIBLE);
        answerD.setVisibility(View.VISIBLE);
        String a = "A:  " + stringMapAnswer.get("A");
        String b = "B:  " + stringMapAnswer.get("B");
        String c = "C:  " + stringMapAnswer.get("C");
        String d = "D:  " + stringMapAnswer.get("D");
        answerA.setText(a);
        answerB.setText(b);
        answerC.setText(c);
        answerD.setText(d);
        answerA.setEnabled(true);
        answerB.setEnabled(true);
        answerC.setEnabled(true);
        answerD.setEnabled(true);
        final CountDownTimer ct = backward();
        ct.start();
        winningAmount = setEarnedMoney(index);
        answerA.setBackground(activity.getDrawable(R.drawable.default_question));
        answerA.setTextColor(Color.WHITE);
        answerB.setBackground(activity.getDrawable(R.drawable.default_question));
        answerB.setTextColor(Color.WHITE);
        answerC.setBackground(activity.getDrawable(R.drawable.default_question));
        answerC.setTextColor(Color.WHITE);
        answerD.setBackground(activity.getDrawable(R.drawable.default_question));
        answerD.setTextColor(Color.WHITE);
        questionContainer.setText(questions.getAskedQuestion());
        answerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound2Seconds(R.raw.wait_for_question);
                RightButtonGreen(questions.getCorrectAnswer());
                ct.cancel();
                answerA.setEnabled(false);
                answerB.setEnabled(false);
                answerC.setEnabled(false);
                answerD.setEnabled(false);
                answerA.setBackground(activity.getDrawable(R.drawable.question_active));
                answerA.setTextColor(Color.BLACK);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (String.valueOf(answerA.getText().charAt(0)).equals(questions.getCorrectAnswer())) {
                            PlaySound2Seconds(R.raw.right_answer);
                            answerA.setBackground(activity.getDrawable(R.drawable.correct_answer));
                            answerA.setTextColor(Color.WHITE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    response.answerTrue();
                                    if (index < (getQuestionPieces() - 1)) {
                                        index++;
                                        nextQuestion(index, new response() {
                                            @Override
                                            public void answerTrue() {

                                            }

                                            @Override
                                            public void answerFalse() {
                                                FinishGame();
                                            }
                                        });
                                    } else {
                                        FinishGameFinal();
                                    }
                                }
                            }, nextQuestionTimer);
                        } else {
                            PlaySound(R.raw.wrong_answer);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    response.answerFalse();

                                }
                            }, nextQuestionTimer);
                            answerA.setBackground(activity.getDrawable(R.drawable.wrong_answer));
                            answerA.setTextColor(Color.WHITE);
                        }
                    }
                }, questionDelay);
            }
        });
        answerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound2Seconds(R.raw.wait_for_question);

                RightButtonGreen(questions.getCorrectAnswer());
                ct.cancel();
                answerB.setEnabled(false);
                answerA.setEnabled(false);
                answerC.setEnabled(false);
                answerD.setEnabled(false);
                answerB.setBackground(activity.getDrawable(R.drawable.question_active));
                answerB.setTextColor(Color.BLACK);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (String.valueOf(answerB.getText().charAt(0)).equals(questions.getCorrectAnswer())) {
                            PlaySound2Seconds(R.raw.right_answer);
                            answerB.setBackground(activity.getDrawable(R.drawable.correct_answer));
                            answerB.setTextColor(Color.WHITE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    response.answerTrue();
                                    if (index < (getQuestionPieces() - 1)) {
                                        index++;
                                        nextQuestion(index, new response() {
                                            @Override
                                            public void answerTrue() {

                                            }

                                            @Override
                                            public void answerFalse() {
                                                FinishGame();
                                            }
                                        });
                                    } else {
                                        FinishGameFinal();
                                    }
                                }
                            }, nextQuestionTimer);
                        } else {
                            PlaySound(R.raw.wrong_answer);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    response.answerFalse();
                                }
                            }, nextQuestionTimer);
                            answerB.setBackground(activity.getDrawable(R.drawable.wrong_answer));
                            answerB.setTextColor(Color.WHITE);
                        }
                    }
                }, questionDelay);
            }
        });
        answerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound2Seconds(R.raw.wait_for_question);

                RightButtonGreen(questions.getCorrectAnswer());
                ct.cancel();
                answerC.setEnabled(false);
                answerB.setEnabled(false);
                answerA.setEnabled(false);
                answerD.setEnabled(false);
                answerC.setBackground(activity.getDrawable(R.drawable.question_active));
                answerC.setTextColor(Color.BLACK);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (String.valueOf(answerC.getText().charAt(0)).equals(questions.getCorrectAnswer())) {
                            PlaySound2Seconds(R.raw.right_answer);
                            answerC.setBackground(activity.getDrawable(R.drawable.correct_answer));
                            answerC.setTextColor(Color.WHITE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    response.answerTrue();
                                    if (index < (getQuestionPieces() - 1)) {
                                        index++;
                                        nextQuestion(index, new response() {
                                            @Override
                                            public void answerTrue() {

                                            }

                                            @Override
                                            public void answerFalse() {
                                                FinishGame();
                                                Log.e("Output Command", "C came from the wrong answer");
                                            }
                                        });
                                    } else {
                                        FinishGameFinal();
                                    }
                                }
                            }, nextQuestionTimer);
                        } else {
                            PlaySound(R.raw.wrong_answer);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    response.answerFalse();
                                }
                            }, nextQuestionTimer);
                            answerC.setBackground(activity.getDrawable(R.drawable.wrong_answer));
                            answerC.setTextColor(Color.WHITE);
                        }
                    }
                }, questionDelay);
            }
        });
        answerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound2Seconds(R.raw.wait_for_question);
                RightButtonGreen(questions.getCorrectAnswer());
                ct.cancel();
                answerD.setEnabled(false);
                answerB.setEnabled(false);
                answerC.setEnabled(false);
                answerA.setEnabled(false);
                answerD.setBackground(activity.getDrawable(R.drawable.question_active));
                answerD.setTextColor(Color.BLACK);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (String.valueOf(answerD.getText().charAt(0)).equals(questions.getCorrectAnswer())) {
                            PlaySound2Seconds(R.raw.right_answer);
                            answerD.setBackground(activity.getDrawable(R.drawable.correct_answer));
                            answerD.setTextColor(Color.WHITE);
                            final Handler handler = new Handler();
                            response.answerTrue();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (index < (getQuestionPieces() - 1)) {
                                        index++;
                                        nextQuestion(index, new response() {
                                            @Override
                                            public void answerTrue() {

                                            }

                                            @Override
                                            public void answerFalse() {
                                                FinishGame();
                                                Log.e("Output Command", "D came from the wrong answer");
                                            }
                                        });
                                    } else {
                                        FinishGameFinal();
                                    }
                                }
                            }, nextQuestionTimer);
                        } else {
                            PlaySound(R.raw.wrong_answer);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    response.answerFalse();
                                }
                            }, nextQuestionTimer);
                            answerD.setBackground(activity.getDrawable(R.drawable.wrong_answer));
                            answerD.setTextColor(Color.WHITE);
                        }
                    }
                }, questionDelay);
            }
        });
    }

    private ArrayList<Questions> questionParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        String text = "";
        ArrayList<Questions> questionsArrayList = new ArrayList<>();
        int eventType = parser.getEventType();
        Questions questions = null;
        Map<String, String> stringMapAnswer = new HashMap<>();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagName.equalsIgnoreCase("questions")) {
                        questions = new Questions();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagName.equalsIgnoreCase("questions")) {
                        questions.setAnswers(stringMapAnswer);
                        stringMapAnswer = new HashMap<>();
                        questionsArrayList.add(questions);
                    } else if (tagName.equalsIgnoreCase("question")) {
                        questions.setAskedQuestion(text);
                    } else if (tagName.equalsIgnoreCase("a")) {
                        stringMapAnswer.put("A", text);
                    } else if (tagName.equalsIgnoreCase("b")) {
                        stringMapAnswer.put("B", text);
                    } else if (tagName.equalsIgnoreCase("c")) {
                        stringMapAnswer.put("C", text);
                    } else if (tagName.equalsIgnoreCase("d")) {
                        stringMapAnswer.put("D", text);
                    } else if (tagName.equalsIgnoreCase("response")) {
                        questions.setCorrectAnswer(text);
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }
        return questionsArrayList;
    }

    interface response {
        void answerTrue();

        void answerFalse();
    }

    private String setEarnedMoney(int QQ) {

        String earnedAmountQuestion = "0/0";

        for (int i = 0; i < getMoneyArray().length; i++) {
            if (i == QQ) {
                getMoneyArray()[i].setBackground(activity.getDrawable(R.drawable.money_active));
            } else if (i == 4 || i == 9 || i == 14) {
                getMoneyArray()[i].setBackground(activity.getDrawable(R.drawable.money_queue));
            } else {
                getMoneyArray()[i].setBackground(activity.getDrawable(R.drawable.default_money));
            }
        }

        switch (QQ) {
            case 0:
                earnedAmountQuestion = "0";
                withdrawal = "0";
                break;
            case 1:
                earnedAmountQuestion = "100";
                withdrawal = "0";
                break;
            case 2:
                earnedAmountQuestion = "200";
                withdrawal = "0";
                break;
            case 3:
                earnedAmountQuestion = "300";
                withdrawal = "0";
                break;
            case 4:
                withdrawal = "0";
                earnedAmountQuestion = "500";
                break;
            case 5:
                withdrawal = "1,000";
                earnedAmountQuestion = "1,000";
                break;
            case 6:
                withdrawal = "1,000";
                earnedAmountQuestion = "2,000";
                break;
            case 7:
                withdrawal = "1,000";
                earnedAmountQuestion = "4,000";
                break;
            case 8:
                withdrawal = "1,000";
                earnedAmountQuestion = "8,000";
                break;
            case 9:
                withdrawal = "1,000";
                earnedAmountQuestion = "16,000";
                break;
            case 10:
                withdrawal = "32,000";
                earnedAmountQuestion = "32,000";
                break;
            case 11:
                withdrawal = "32,000";
                earnedAmountQuestion = "64,000";
                break;
            case 12:
                withdrawal = "32,000";
                earnedAmountQuestion = "125,000";
                break;
            case 13:
                withdrawal = "32,000";
                earnedAmountQuestion = "250,000";
                break;
            case 14:
                withdrawal = "32,000";
                earnedAmountQuestion = "500,000";
                break;
        }

        return earnedAmountQuestion;
    }

    private CountDownTimer backward() {
        countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                String time = String.valueOf(millisUntilFinished / 1000);
                getTimeCounter().setText(time);
                if (getTimeCounter().getText().equals("0")) {
                    FinishGame();
                }
            }

            public void onFinish() {
            }
        };
        return countDownTimer;
    }

    private static void FinishGame() {
        FragmentManager fm = activity.getFragmentManager();
        GameOver dialogFragment = new GameOver();
        dialogFragment.show(fm, "Sample Fragment");
    }

    private static void FinishGameFinal() {
        FragmentManager fm = activity.getFragmentManager();
        GameOverFinal dialogFragment = new GameOverFinal();
        dialogFragment.show(fm, "Sample Fragment");
    }

    private void RightButtonGreen(final String rightAnswer) {
        final Button rightAnswerButton;

        if (String.valueOf(answerA.getText().charAt(0)).equals(rightAnswer)) {
            rightAnswerButton = answerA;
        } else if (String.valueOf(answerB.getText().charAt(0)).equals(rightAnswer)) {
            rightAnswerButton = answerB;
        } else if (String.valueOf(answerC.getText().charAt(0)).equals(rightAnswer)) {
            rightAnswerButton = answerC;
        } else {
            rightAnswerButton = answerD;
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rightAnswerButton.setBackground(activity.getDrawable(R.drawable.correct_answer));
            }
        }, questionDelay);
    }

    private void FiftyFiftyLifeLine(final ImageButton fiftyFiftyButton, final String FiftyFiftyCorrectAnswer) {

        fiftyFiftyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fiftyFiftyLifeLine) {
                    fiftyFiftyAnswer = false;
                    fiftyFiftyButton.setEnabled(false);
                    fiftyFiftyButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.fiftyfifty_life_line_inactive, view.getContext().getTheme()));

                    ArrayList<Button> buttonArrayList = new ArrayList<>();
                    buttonArrayList.add(answerA);
                    buttonArrayList.add(answerB);
                    buttonArrayList.add(answerC);
                    buttonArrayList.add(answerD);

                    ArrayList<Button> eraseButtonArrayList = new ArrayList<>();
                    for (int i = 0; i < buttonArrayList.size(); i++) {
                        if (!String.valueOf(buttonArrayList.get(i).getText().charAt(0)).equals(FiftyFiftyCorrectAnswer)) {
                            eraseButtonArrayList.add(buttonArrayList.get(i));
                        }
                    }
                    Collections.shuffle(eraseButtonArrayList);
                    eraseButtonArrayList.get(0).setVisibility(View.INVISIBLE);
                    eraseButtonArrayList.get(1).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public static class AudienceLifeLine extends DialogFragment {
        ImageView ia, ib, ic, id;
        ImageView imageViewAnswer;
        Button dismiss;
        int total = 10;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.audience_lifeline, container, false);
            getDialog().setTitle("Audience LifeLine");

            dismiss = rootView.findViewById(R.id.btn_dismiss);
            ia = rootView.findViewById(R.id.ia);
            ib =  rootView.findViewById(R.id.ib);
            ic = rootView.findViewById(R.id.ic);
            id = rootView.findViewById(R.id.id);

            ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
            imageViewArrayList.add(ia);
            imageViewArrayList.add(ib);
            imageViewArrayList.add(ic);
            imageViewArrayList.add(id);
            ArrayList<ImageView> xd = new ArrayList<>();
            for (int i = 0; i < imageViewArrayList.size(); i++) {
                if (imageViewArrayList.get(i).getTag().equals(audienceAnswer)) {
                    imageViewAnswer = imageViewArrayList.get(i);
                    Random r = new Random();
                    int Low = 4;
                    int High = 8;
                    int Result = r.nextInt(High - Low) + Low;
                    total = total - Result;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                            imageViewAnswer.getLayoutParams();
                    params.weight = (float) Result;
                    imageViewAnswer.setLayoutParams(params);
                } else {
                    xd.add(imageViewArrayList.get(i));
                }
            }

            int[] gg = AudienceResults(total, 3);
            for (int i = 0; i < xd.size(); i++) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                        xd.get(i).getLayoutParams();
                params.weight = (float) gg[i];
                xd.get(i).setLayoutParams(params);
            }

            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });

            return rootView;
        }
    }

    public static class PhoneLifeLine extends DialogFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.telephone_lifeline, container, false);
            getDialog().setTitle("Phone Lifeline");
            Button okay = rootView.findViewById(R.id.btn_thankYou);
            TextView telephone = rootView.findViewById(R.id.phoneAnswer);
            telephone.setText(phoneAnswer);
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });
            return rootView;
        }
    }

    private void exitGame() {
        FragmentManager fm = activity.getFragmentManager();
        Dialog dialogFragment = new Dialog();
        dialogFragment.show(fm, "Sample Fragment");
    }

    public static class Dialog extends DialogFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dialog, container, false);
            getDialog().setTitle("Are you Sure you want to Quit");
            Button buttonYes = rootView.findViewById(R.id.buttonYes);
            Button buttonNo = rootView.findViewById(R.id.buttonNo);

            buttonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exitStatus = 1;
                    FinishGame();
                }
            });

            buttonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            return rootView;
        }
    }

    private void phoneLifeLineResponse(final String answered, final ImageButton phoneButton) {
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String talk = "....";
                String appendix;

                switch (answered) {
                    case "1":
                        appendix = "A";
                        break;
                    case "2":
                        appendix = "B";
                        break;
                    case "3":
                        appendix = "C";
                        break;
                    default:
                        appendix = "D";
                        break;
                }

                Random random = new Random();
                int i = random.nextInt(5);

                switch (i) {
                    case 0:
                        talk = "I don't know the answer sorry :(. Good luck.";
                        break;
                    case 1:
                        talk = "I searched it on Google ^^ definitely " + appendix;
                        break;
                    case 2:
                        talk = "I am not sure but seems to be " + appendix + ".";
                        break;
                    case 3:
                        talk = "If I were you I would say " + appendix + ".";
                        break;
                    case 4:
                        talk = "Hi buddy good luck. I will say " + appendix + ".";
                        break;
                }

                phoneAnswer = talk;
                FragmentManager fm = activity.getFragmentManager();
                PhoneLifeLine dialogFragment = new PhoneLifeLine();
                dialogFragment.show(fm, "LifeLine");
                phoneButton.setEnabled(false);
                phoneButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.phone_life_line_inactive, view.getContext().getTheme()));
            }
        });
    }

    private static int[] AudienceResults(int number, int number_of_parts) {
        Random r = new Random();
        HashSet<Integer> uniqueInts = new HashSet<>();
        uniqueInts.add(0);
        uniqueInts.add(number);
        int array_size = number_of_parts + 1;
        while (uniqueInts.size() < array_size) {
            uniqueInts.add(1 + r.nextInt(number - 1));
        }
        Integer[] dividers = uniqueInts.toArray(new Integer[array_size]);
        Arrays.sort(dividers);
        int[] results = new int[number_of_parts];
        for (int i = 1, j = 0; i < dividers.length; ++i, ++j) {
            results[j] = dividers[i] - dividers[j];
        }
        return results;
    }

    private void CallTheAudience(final ImageButton audience, String reply) {
        audienceAnswer = reply;
        audience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = activity.getFragmentManager();
                AudienceLifeLine dialogFragment = new AudienceLifeLine();
                dialogFragment.show(fm, "LifeLine");
                audience.setEnabled(false);
                audience.setImageDrawable(activity.getResources().getDrawable(R.drawable.audience_inactive, view.getContext().getTheme()));
            }
        });
    }

    public static class GameOver extends DialogFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.game_score, container, false);
            getDialog().setTitle("The Amount You Earned");

            Button mainMenu = rootView.findViewById(R.id.homeButton);
            TextView moneyWon = rootView.findViewById(R.id.moneyWon);
            TextView explanation = rootView.findViewById(R.id.explanation);
            String giveMoney = "";

            if (exitStatus == 1)
                giveMoney = winningAmount;
            else if (exitStatus == 0)
                giveMoney = withdrawal;

            countDownTimer.cancel();
            mediaPlayer.stop();
            timer.cancel();

            String money = getActivity().getResources().getString(R.string.currency) + " " + giveMoney;
            moneyWon.setText(money);
            explanation.setText(getActivity().getResources().getString(R.string.game_over));

            Success success = new Success(giveMoney, timeCounter);
            this.setCancelable(false);

            SQLiteScore sqLiteScore = new SQLiteScore(activity);
            sqLiteScore.addScore(success);

            mainMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });

            return rootView;
        }
    }

    public static class GameOverFinal extends DialogFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.game_score, container, false);
            getDialog().setTitle("The Amount You Earned");

            Button mainMenu = rootView.findViewById(R.id.homeButton);
            TextView moneyWon = rootView.findViewById(R.id.moneyWon);
            TextView explanation = rootView.findViewById(R.id.explanation);
            String giveMoney = "1,000,000";
            String money = getActivity().getResources().getString(R.string.currency) + " " + giveMoney;

            countDownTimer.cancel();
            mediaPlayer.stop();
            timer.cancel();

            moneyWon.setText(money);
            explanation.setText(getActivity().getResources().getString(R.string.game_over_final));

            Success success = new Success(giveMoney, timeCounter);
            this.setCancelable(false);

            SQLiteScore sqLiteScore = new SQLiteScore(activity);
            sqLiteScore.addScore(success);

            mainMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });

            return rootView;
        }
    }

    private void PlaySound(int i) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("sound", Context.MODE_PRIVATE);
        if (Objects.equals(sharedPreferences.getString("sound", ""), "ok")) {
            final MediaPlayer mp = MediaPlayer.create(activity, i);
            mp.start();
        }
    }

    private void PlaySound2Seconds(int i) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("sound", Context.MODE_PRIVATE);
        if (Objects.equals(sharedPreferences.getString("sound", ""), "ok")) {
            final MediaPlayer mp = MediaPlayer.create(activity, i);
            mp.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.stop();
                }
            }, 2200);
        }
    }
}