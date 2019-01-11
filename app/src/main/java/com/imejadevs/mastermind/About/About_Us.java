package com.imejadevs.mastermind.About;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imejadevs.mastermind.R;

import java.util.List;

public class About_Us extends AppCompatActivity {
    private ListView listView;
    String [] groupmembers={"Team Leader\nStudent Name 1","Member\nStudent Name 2","Member\nStudent Name 3","Member\nStudent Name 4"};
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        listView=(ListView)findViewById(R.id.list);
        adapter=new MyAdapter(this,groupmembers);
        listView.setAdapter(adapter);
    }

    private class MyAdapter extends ArrayAdapter {


        Context context;

        String[] values;

        public MyAdapter(Context context, String[] values) {
            super(context,R.layout.members,R.id.bb,values);

            this.context = context;

            this.values = values;

        }

        @NonNull
        @Override
        public View getView(int position,  @NonNull View convertView,@NonNull ViewGroup parent) {

            LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row=inflater.inflate(R.layout.members,parent,false);

            TextView textView=(TextView)row.findViewById(R.id.bb);

            textView.setText(values[position]);

            return row;
        }
    }
}
