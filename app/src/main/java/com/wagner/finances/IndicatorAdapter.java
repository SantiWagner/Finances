package com.wagner.finances;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.ViewHolder>{


    private Context context;
    private LayoutInflater inflater;
    List<Indicator> data= Collections.emptyList();

    public static int current_row=0;

    public IndicatorAdapter(Context context, List<Indicator> data){
        this.context=context;
        this.data=data;
    }

    public void clear(){
        this.data.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder myHolder= (ViewHolder) holder;
        Indicator current=data.get(position);
        myHolder.key.setText(current.key);
        myHolder.value.setText(current.value);
        if(current_row%2==0)
            myHolder.background.setBackgroundColor(context.getResources().getColor(R.color.lightBackground));
        current_row++;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView key;
        TextView value;
        LinearLayout background;

        // create constructor to get widget reference
        public ViewHolder(View itemView) {
            super(itemView);
            background = (LinearLayout) itemView.findViewById(R.id.row_background);
            key= (TextView) itemView.findViewById(R.id.key);
            value=(TextView)itemView.findViewById(R.id.value);
        }
    }
}
