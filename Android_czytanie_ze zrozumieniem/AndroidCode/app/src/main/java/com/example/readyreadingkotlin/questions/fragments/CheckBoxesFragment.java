package com.example.readyreadingkotlin.questions.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.readyreadingkotlin.R;
import com.example.readyreadingkotlin.questions.QuestionFragmentKotlin;
import com.example.readyreadingkotlin.questions.database.AppDatabase;
import com.example.readyreadingkotlin.questions.questionmodels.AnswerOptions;
import com.example.readyreadingkotlin.questions.questionmodels.QuestionsItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * This fragment provide the Checkbox/Multiple related Options/Choices.
 */
public class CheckBoxesFragment extends Fragment
{
    private final ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();
    private final BottomNavigationView navView;
    private int atLeastOneChecked = 0;
    private FragmentActivity mContext;
    private Button nextOrFinishButton;
    //private Button previousButton;
    private TextView questionCBTypeTextView;
    private LinearLayout checkboxesLinearLayout;
    private AppDatabase appDatabase;
    private String questionId = "";
    private int currentPagePosition = 0;
    private int clickedCheckBoxPosition = 0;
    private String qState = "0";
    private QuestionFragmentKotlin questionFragment;

    public CheckBoxesFragment(QuestionFragmentKotlin questionFragment, BottomNavigationView navView)
    {
        this.questionFragment = questionFragment;
        this.navView = navView;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_check_boxes, container, false);

        appDatabase = AppDatabase.getAppDatabase(getActivity());

        nextOrFinishButton = rootView.findViewById(R.id.nextOrFinishButton);
        //previousButton = rootView.findViewById(R.id.previousButton);
        questionCBTypeTextView = rootView.findViewById(R.id.questionCBTypeTextView);
        checkboxesLinearLayout = rootView.findViewById(R.id.checkboxesLinearLayout);

        nextOrFinishButton.setOnClickListener(v -> {
            if (currentPagePosition == this.questionFragment.getTotalQuestionsSize())
            {
                /* Here, You go back from where you started OR If you want to go next Activity just change the Intent*/

              //  this.navView.setSelectedItemId(R.id.navigation_answers);

            } else
            {
                this.questionFragment.nextQuestion();
            }
        });
        //previousButton.setOnClickListener(view -> mContext.onBackPressed());

        return rootView;
    }


    /*This method get called only when the fragment get visible, and here states of checkbox(s) retained*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        atLeastOneChecked = 0;

        if (isVisibleToUser)
        {
            for (int i = 0; i < checkBoxArrayList.size(); i++)
            {
                CheckBox checkBox = checkBoxArrayList.get(i);
                String cbPosition = String.valueOf(i);

                String[] data = new String[]{questionId, cbPosition};
                Observable.just(data)
                        .map(this::getTheStateOfCheckBox)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>()
                        {
                            @Override
                            public void onSubscribe(Disposable d)
                            {

                            }

                            @Override
                            public void onNext(String s)
                            {
                                qState = s;
                            }

                            @Override
                            public void onError(Throwable e)
                            {

                            }

                            @Override
                            public void onComplete()
                            {
                                if (qState.equals("1"))
                                {
                                    checkBox.setChecked(true);
                                    atLeastOneChecked = atLeastOneChecked + 1;

                                    if (!nextOrFinishButton.isEnabled())
                                    {
                                        nextOrFinishButton.setEnabled(true);
                                    }
                                } else
                                {
                                    checkBox.setChecked(false);
                                }
                            }
                        });
            }
        }
    }


    private String getTheStateOfCheckBox(String[] data)
    {
        return appDatabase.getQuestionChoicesDao().isChecked(data[0], data[1]);
    }

//  !!
    private void saveActionsOfCheckBox()
    {
        for (int i = 0; i < checkBoxArrayList.size(); i++)
        {
            if (i == clickedCheckBoxPosition)
            {
                CheckBox checkBox = checkBoxArrayList.get(i);
                if (checkBox.isChecked())
                {
                    atLeastOneChecked = atLeastOneChecked + 1;

                    String cbPosition = String.valueOf(checkBoxArrayList.indexOf(checkBox));

                    String[] data = new String[]{"1", questionId, cbPosition};
                    insertAnswerInDatabase(data);

                } else
                {
                    atLeastOneChecked = atLeastOneChecked - 1;
                    if (atLeastOneChecked <= 0)
                        atLeastOneChecked = 0;

                    String cbPosition = String.valueOf(checkBoxArrayList.indexOf(checkBox));

                    String[] data = new String[]{"0", questionId, cbPosition};
                    insertAnswerInDatabase(data);
                }
            }
        }

        if (atLeastOneChecked != 0)
        {
            nextOrFinishButton.setEnabled(true);
        } else
        {
            nextOrFinishButton.setEnabled(false);
        }
    }


    private void insertAnswerInDatabase(String[] data)
    {
        Observable.just(data)
                .map(this::insertingInDb)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private String insertingInDb(String[] data)
    {
        appDatabase.getQuestionChoicesDao().updateQuestionWithChoice(data[0], data[1], data[2]);
        return "";
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        QuestionsItem checkBoxTypeQuestion = null;

        if (getArguments() != null)
        {
            checkBoxTypeQuestion = getArguments().getParcelable("question");
            questionId = String.valueOf(checkBoxTypeQuestion != null ? checkBoxTypeQuestion.getId() : 0);
            currentPagePosition = getArguments().getInt("page_position") + 1;
        }

        questionCBTypeTextView.setText(checkBoxTypeQuestion != null ? checkBoxTypeQuestion.getQuestionName() : "");

        /*Disable the button until any choice got selected*/
        nextOrFinishButton.setEnabled(false);

        List<AnswerOptions> checkBoxChoices = Objects.requireNonNull(checkBoxTypeQuestion).getAnswerOptions();

        checkBoxArrayList.clear();

        for (AnswerOptions choice : checkBoxChoices)
        {
            CheckBox checkBox = new CheckBox(mContext);

            checkBox.setText(choice.getName());
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            checkBox.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            checkBox.setPadding(10, 40, 10, 40);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 25;

            View view = new View(mContext);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.divider));
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));

            checkboxesLinearLayout.addView(checkBox, params);
            checkboxesLinearLayout.addView(view);
            checkBoxArrayList.add(checkBox);

            checkBox.setOnClickListener(view1 -> {
                CheckBox buttonView = (CheckBox) view1;
                clickedCheckBoxPosition = checkBoxArrayList.indexOf(buttonView);
                saveActionsOfCheckBox();
            });


        }


        if (currentPagePosition == this.questionFragment.getTotalQuestionsSize())
        {
            nextOrFinishButton.setText(R.string.finish);
        } else
        {
            nextOrFinishButton.setText(R.string.next);
        }
    }
}