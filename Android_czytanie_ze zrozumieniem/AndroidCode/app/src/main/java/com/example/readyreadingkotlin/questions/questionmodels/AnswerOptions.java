package com.example.readyreadingkotlin.questions.questionmodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AnswerOptions implements Parcelable
{
    public static final Creator<AnswerOptions> CREATOR = new Creator<AnswerOptions>()
    {
        @Override
        public AnswerOptions createFromParcel(Parcel in)
        {
            return new AnswerOptions(in);
        }

        @Override
        public AnswerOptions[] newArray(int size)
        {
            return new AnswerOptions[size];
        }
    };
    @SerializedName("answer_id")
    private String answerId;
    @SerializedName("name")
    private String name;
    @SerializedName("correct_or_false")
    private String correct_or_false;

    public AnswerOptions(Parcel in)
    {
        answerId = in.readString();
        name = in.readString();
        correct_or_false = in.readString();
    }


    public String getCorrect_or_false() {
        return correct_or_false;
    }

    public void setCorrect_or_false(String correct_or_false) {
        this.correct_or_false = correct_or_false;
    }

    public String getAnswerId()
    {
        return answerId;
    }

    public void setAnswerId(String answerId)
    {
        this.answerId = answerId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(answerId);
        dest.writeString(name);
        dest.writeString(correct_or_false);
    }

    public AnswerOptions() {
    }

    @Override
    public String toString() {
        return "AnswerOptions{" +
                "answerId='" + answerId + '\'' +
                ", name='" + name + '\'' +
                ", correct_or_false='" + correct_or_false + '\'' +
                '}';
    }
}