package com.example.readyreadingkotlin.questions;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.readyreadingkotlin.R;
import com.example.readyreadingkotlin.questions.database.AppDatabase;
import com.example.readyreadingkotlin.questions.qdb.QuestionEntity;
import com.example.readyreadingkotlin.questions.qdb.QuestionWithChoicesEntity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnswerFragment extends Fragment {

    private AnswerViewModel mViewModel;

    Context context;
    LinearLayout resultLinearLayout;
    List<QuestionEntity> questionsList = new ArrayList<>();
    List<QuestionWithChoicesEntity> questionsWithAllChoicesList = new ArrayList<>();
    private AppDatabase appDatabase;

    BottomNavigationView navView;

    public AnswerFragment(BottomNavigationView navView)
    {
        this.navView = navView;
    }

    public BottomNavigationView getNavView() {
        return navView;
    }

    public void setNavView(BottomNavigationView navView) {
        this.navView = navView;
    }

  /*  public static AnswerFragment newInstance() {
        return new AnswerFragment();
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.answer_fragment, container, false);

        context = this.getContext();
        appDatabase = AppDatabase.getAppDatabase(this.getContext());

        resultLinearLayout = root.findViewById(R.id.resultLinearLayout);
        toolBarInit(root);

        getResultFromDatabase();


        return root;
    }

    private void toolBarInit(View root)
    {
        Toolbar answerToolBar = root.findViewById(R.id.answerToolbar);
        answerToolBar.setNavigationIcon(R.drawable.ic_arrow_back);
        answerToolBar.setNavigationOnClickListener(v -> onBackPressed());
    }


    public void onBackPressed()
    {
     //   this.navView.setSelectedItemId(R.id.navigation_questions);
    }

    /*After, getting all result you can/must delete the saved results
    although we are clearing the Tables as soon we start the QuestionActivity.*/
    private void getResultFromDatabase()
    {
        Completable.fromAction(() -> {
            questionsList = appDatabase.getQuestionDao().getAllQuestions();

            questionsWithAllChoicesList = appDatabase.getQuestionChoicesDao().getAllQuestionsWithChoices("1");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onComplete()
                    {
                        makeJsonDataToMakeResultView();
                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }
                });
    }

    /*Here, JSON got created and send to make Result View as per Project requirement.
     * Alternatively, in your case, you make Network-call to send the result to back-end.*/
    private void makeJsonDataToMakeResultView()
    {
        try
        {
            JSONArray questionAndAnswerArray = new JSONArray();
            int questionsSize = questionsList.size();
            if (questionsSize > 0)
            {
                for (int i = 0; i < questionsSize; i++)
                {
                    JSONObject questionName = new JSONObject();
                    questionName.put("question", questionsList.get(i).getQuestion());
                    //questionName.put("question_id", String.valueOf(questionsList.get(i).getQuestionId()));
                    String questionId = String.valueOf(questionsList.get(i).getQuestionId());

                    JSONArray answerChoicesList = new JSONArray();
                    int selectedChoicesSize = questionsWithAllChoicesList.size();
                    for (int k = 0; k < selectedChoicesSize; k++)
                    {
                        String questionIdOfChoice = questionsWithAllChoicesList.get(k).getQuestionId();
                        if (questionId.equals(questionIdOfChoice))
                        {
                            JSONObject selectedChoice = new JSONObject();
                            selectedChoice.put("answer_choice", questionsWithAllChoicesList.get(k).getAnswerChoice());
                            //selectedChoice.put("answer_id", questionsWithAllChoicesList.get(k).getAnswerChoiceId());
                            answerChoicesList.put(selectedChoice);
                        }
                    }
                    questionName.put("selected_answer", answerChoicesList);

                    questionAndAnswerArray.put(questionName);
                }
            }

            questionsAnswerView(questionAndAnswerArray);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void questionsAnswerView(JSONArray questionsWithAnswerArray)
    {
        if (questionsWithAnswerArray.length() > 0)
        {
            try
            {
                for (int i = 0; i < questionsWithAnswerArray.length(); i++)
                {
                    String question = questionsWithAnswerArray.getJSONObject(i).getString("question");

                    TextView questionTextView = new TextView(context);
                    questionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    questionTextView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    questionTextView.setPadding(40, 30, 16, 30);
                    questionTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    questionTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    questionTextView.setTypeface(null, Typeface.BOLD);
                    questionTextView.setText(question);

                    resultLinearLayout.addView(questionTextView);

                    JSONArray selectedAnswerJSONArray = questionsWithAnswerArray.getJSONObject(i).getJSONArray("selected_answer");

                    for (int j = 0; j < selectedAnswerJSONArray.length(); j++)
                    {
                        String answer = selectedAnswerJSONArray.getJSONObject(j).getString("answer_choice");
                        String formattedAnswer = "• " + answer; // alt + 7 --> •

                        TextView answerTextView = new TextView(context);
                        answerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        answerTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        answerTextView.setPadding(60, 30, 16, 30);
                        answerTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        answerTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                        answerTextView.setText(formattedAnswer);

                        View view = new View(context);
                        view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));

                        resultLinearLayout.addView(answerTextView);
                        resultLinearLayout.addView(view);
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}