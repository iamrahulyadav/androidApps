package com.latesttrendingapps.template;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder>{

    private Context context;
    private String[] genderList;
    public ResultAdapter(Context context, String[] genderList) {
        this.context = context;
        this.genderList = genderList;
    }

    @NonNull
    @Override
    public ResultAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ResultAdapter.ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ResultViewHolder holder, int position) {
//        String text = genderList.toString();
//        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return genderList.length;
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ResultViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
