package com.imejadevs.mastermind.Leaders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imejadevs.mastermind.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends AppCompatActivity {
    private ListView listView;
    MyDataAdapter freeAdapter;
    ArrayList<UsersList> arrayList;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        arrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        freeAdapter = new MyDataAdapter(this, arrayList);
        listView.setAdapter(freeAdapter);
        addData();
    }

    private void addData() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Leaders/Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    UsersList freeTips = new UsersList();
                    String username = (String) messageSnapshot.child("username").getValue();
                    String score = (String) messageSnapshot.child("score").getValue();
                    freeTips.setUsername("" + username);
                    freeTips.setScore("" + score);
                    arrayList.add(freeTips);
                    freeAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error..!!" + databaseError, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private class MyDataAdapter extends ArrayAdapter {
        ArrayList<UsersList> status;

        public MyDataAdapter(Context context, ArrayList<UsersList> status) {
            super(context, R.layout.members, R.id.bb, status);
            this.status = status;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.members, parent, false);
            TextView textView = (TextView) view.findViewById(R.id.bb);
            UsersList usersList = status.get(position);
            textView.setText("Name:" + usersList.getUsername() + "\nScore:" + usersList.getScore());
            return view;
        }
    }

    public void shareWhatsapp(View view) {
        Intent wp = new Intent(Intent.ACTION_SEND);
        wp.setType("text/plain");
        wp.setPackage("com.whatsapp");
        wp.putExtra(Intent.EXTRA_TEXT, "This is a nice game");
        try {
            startActivity(wp);
        } catch (android.content.ActivityNotFoundException e) {

            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
            startActivity(i);
        }
    }

    public void shareFacebook(View view) {
        Intent wp = new Intent(Intent.ACTION_SEND);
        wp.setType("text/plain");
        wp.setPackage("com.facebook.katana");
        wp.putExtra(Intent.EXTRA_TEXT, "This is a nice game");
        try {
            startActivity(wp);
        } catch (android.content.ActivityNotFoundException e) {

            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana"));
            startActivity(i);
        }
    }
}
