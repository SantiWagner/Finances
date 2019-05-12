package com.wagner.finances;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Santiago on 4/22/2019.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{


    private Context context;
    private LayoutInflater inflater;
    List<Item> data= Collections.emptyList();

    public ItemAdapter(Context context, List<Item> data){
        this.context=context;
        this.data=data;
    }

    public void clear(){
        this.data.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder myHolder= (ViewHolder) holder;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        Item current=data.get(position);
        myHolder.name.setText(current.name);
        myHolder.amount.setText(""+current.amount+" "+current.currency);
        myHolder.last_updated.setText("Last updated: "+df.format(new Date(current.last_updated)));
        myHolder.edit_button.setId(current.id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        TextView name;
        TextView amount;
        TextView last_updated;
        Button edit_button;

        // create constructor to get widget reference
        public ViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.item_name);
            amount=(TextView)itemView.findViewById(R.id.item_amount);
            last_updated=itemView.findViewById(R.id.last_updated);
            edit_button = itemView.findViewById(R.id.editButton);
        }
    }
}