package com.aqoong.lib.expandabletextviewsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aqoong.lib.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ExpandableTextView textView;
    RecyclerView recyclerView;
    ArrayList<ExpandableTextView> testList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setText("ABCDEFGHIJKfdfddf\n\n\nbyebye", "더보기");

        recyclerView = findViewById(R.id.listView);


        testList = new ArrayList<>();
        for(int i = 0  ; i < 20 ; i++){
            ExpandableTextView view = new ExpandableTextView(this);
            view.setText(i, "This is Test Message.\n\nbyebye\nIndex : " + i, "show more");
            testList.add(view);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new TempAdapter());

        recyclerView.getAdapter().notifyDataSetChanged();
    }


    public class TempAdapter extends RecyclerView.Adapter<TempAdapter.ItemHolder>{
        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item, viewGroup, false);

            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder itemHolder, final int i) {
            itemHolder.title.setText("Index : " + i);
            itemHolder.textView.setExpandableTextView(testList.get(i));
        }

        @Override
        public void onViewRecycled(@NonNull ItemHolder holder) {
            testList.get(holder.textView.getIndex()).setExpandableTextView(holder.textView);
        }

        @Override
        public int getItemCount() {
            return testList.size();
        }

        public class ItemHolder extends RecyclerView.ViewHolder{
            ExpandableTextView textView;
            TextView title;

            public ItemHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.index);
                textView = itemView.findViewById(R.id.expand_text);
            }
        }
    }
}
