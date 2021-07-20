package com.atmanirbharrogar.company.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyExpandableListView extends BaseExpandableListAdapter {
    Context context;
    List<String> tt;
    Map<String,List<String>> stt;

    public MyExpandableListView(Context context, List<String> tt, Map<String, List<String>> stt) {
        this.context = context;
        this.tt = tt;
        this.stt = stt;
    }

    @Override
    public int getGroupCount() {
        return tt.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return stt.get(tt.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return tt.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return stt.get(tt.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title=(String)getGroup(groupPosition);

        if (convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.parent_list,null);
        }
        TextView te=(TextView)convertView.findViewById(R.id.txtParent);
        te.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String tit=(String)getChild(groupPosition,childPosition);

        if (convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.child_list,null);
        }
        TextView tet=(TextView)convertView.findViewById(R.id.txtChild);
        tet.setText(tit);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
