package com.benedetto.lars.lab3;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benedetto.lars.livemodqueue.R;import java.util.ArrayList;

public class ListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private Activity activity;
    private ArrayList<Manufacturer> manufacturers = new ArrayList<>();

    public ListAdapter(Activity act, ArrayList<Manufacturer> man) {
        activity = act;
        manufacturers = man;
    }

    public ArrayList<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return manufacturers.get(groupPosition).getModel(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //Maybe switch from parent to null??
            convertView = activity.getLayoutInflater().inflate(R.layout.child_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textViewChild);
        textView.setText(manufacturers.get(groupPosition).getModel(childPosition));
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        imageView.setTag(R.id.group_num, groupPosition);
        imageView.setTag(R.id.child_num, childPosition);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        final int groupPosition = (int) v.getTag(R.id.group_num);
        final int childPosition = (int) v.getTag(R.id.child_num);

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return manufacturers.get(groupPosition).getSize();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return manufacturers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return manufacturers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //Maybe switch from parent to null??
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.group_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.textViewGroup);
        textView.setText(manufacturers.get(groupPosition).getManufacturer());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}