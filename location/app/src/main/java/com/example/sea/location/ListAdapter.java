package com.example.sea.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sea on 2017-10-07.
 */

public class ListAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<Listviewitem> data;
    private int layout;
    public ListAdapter(Context context, int layout, ArrayList<Listviewitem> data) {
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    public void remove(){
        data.remove(0);
    }

    @Override
    public Object getItem(int i) {
        return data.get(i).getText1();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = inflater.inflate(layout,viewGroup,false);
        }
        Listviewitem listviewitem = data.get(i);
        TextView text1 = (TextView)view.findViewById(R.id.textview1);
        text1.setText(listviewitem.getText1());
        TextView text2 = (TextView)view.findViewById(R.id.textview2);
        text2.setText(listviewitem.getText2());
        return view;
    }
}
