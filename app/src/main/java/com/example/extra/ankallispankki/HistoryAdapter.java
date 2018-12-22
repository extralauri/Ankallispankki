package com.example.extra.ankallispankki;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

 //Arraylist variables
 Context context;
 ArrayList<String> history_names = new ArrayList<>();
 ArrayList<String> history_from = new ArrayList<>();
 ArrayList<String> history_to = new ArrayList<>();
 ArrayList<String> history_money = new ArrayList<>();
 ArrayList<String> history_message = new ArrayList<>();
 ArrayList<String> history_time = new ArrayList<>();

 //HIstoryAdapter constructor. Adds parameter values to local variables
 public HistoryAdapter(Context context, ArrayList<String> Names, ArrayList<String> From, ArrayList<String> To, ArrayList<String> Money, ArrayList<String> Message,ArrayList<String> Time) {
  this.context = context;
  this.history_names = Names;
  this.history_from = From;
  this.history_to = To;
  this.history_money = Money;
  this.history_message = Message;
  this.history_time = Time;
}

 // RowLayout
 @Override
 public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
  // inflate the item Layout
  View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
  MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
  return vh;
 }

 @Override
 public void onBindViewHolder(MyViewHolder holder, final int position) {
  // set the data in items
  holder.name.setText(history_names.get(position));
  holder.from.setText(history_from.get(position));
  holder.to.setText(history_to.get(position));
  holder.money.setText(history_money.get(position));
  holder.message.setText(history_message.get(position));
  holder.time.setText(history_time.get(position));
 }

 @Override
 public int getItemCount() {
  return history_names.size();
 }

 public class MyViewHolder extends RecyclerView.ViewHolder {
  TextView name, from, to, money,message, time;// init the item view's

  public MyViewHolder(View itemView) {
   super(itemView);

   // get the reference of item view's
   name = (TextView) itemView.findViewById(R.id.name);
   from = (TextView) itemView.findViewById(R.id.from);
   to = (TextView) itemView.findViewById(R.id.to);
   money = (TextView) itemView.findViewById(R.id.money);
   message = (TextView) itemView.findViewById(R.id.message);
   time = (TextView) itemView.findViewById(R.id.time);
  }
 }
}

