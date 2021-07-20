package com.atmanirbharrogar.company.work;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowDetails extends AppCompatActivity {
    ExpandableListView expandableListView;
    List<String> topics;
    Map<String,List<String>> subtopics;
    ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        expandableListView=(ExpandableListView)findViewById(R.id.expandablelistview);
        fill();

        listAdapter=new MyExpandableListView(this,topics,subtopics);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String ii = subtopics.get(topics.get(groupPosition)).get(childPosition);
                return false;
            }
        });

            }


    public void fill() {
        topics = new ArrayList<String>();
        subtopics=new HashMap<>();

        topics.add("1.Operator");
        topics.add("2.If and If else");
        topics.add("3.Switch");
        topics.add("4.For loop");
        topics.add("5.While loop");
        topics.add("6.Pointer");


        List<String>Operator=new ArrayList<>();
        List<String>If=new ArrayList<>();
        List<String>Switch=new ArrayList<>();
        List<String>For=new ArrayList<>();
        List<String>While=new ArrayList<>();
        List<String>Pointer=new ArrayList<>();


        subtopics.put(topics.get(0),Operator);
        subtopics.put(topics.get(1),If);
        subtopics.put(topics.get(2),Switch);
        subtopics.put(topics.get(3),For);
        subtopics.put(topics.get(4),While);
        subtopics.put(topics.get(5),Pointer);}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}