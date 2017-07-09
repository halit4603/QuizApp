package com.example.android.quizapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {


    private final Context context;
    private List<Category> list;
    SQLiteDatabase db;
    private List<Question> questionList = new ArrayList<>();

    public QuizListAdapter(Context context, List<Category> list, SQLiteDatabase db) {
        this.context = context;
        this.list = list;
        this.db = db;

    }

    @Override
    public QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_listitem, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, int position) {
        Category category = list.get(position);
        holder.topicName.setText(category.categoryName);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView topicName;

        public QuizViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            topicName = (TextView) itemView.findViewById(R.id.quiz_topic);

        }

        @Override
        public void onClick(View view) {
            String TAG = "ye raha";

            Cursor res = db.rawQuery("select * from " + DBContract.DBEntry.TABLE_NAME_2 + " where " +
                    DBContract.DBEntry.COLUMN_ID + "=" + list.get(getAdapterPosition()).id + ";", null);
            questionList.clear();

            for(res.moveToFirst(); !res.isAfterLast(); res.moveToNext()){
                Question ques = new Question(res.getString(res.getColumnIndex("question")), res.getString(res.getColumnIndex("options")),
                        res.getString(res.getColumnIndex("answer")));
                questionList.add(ques);
            }

            res.close();
            Intent intent = new Intent(context, QuizDetails.class);
            intent.putExtra("list", (Serializable) questionList);
            intent.putExtra("topic", list.get(getAdapterPosition()).categoryName);
            context.startActivity(intent);
        }
    }
}