package com.example.sea.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sea on 2017-11-19.
 */

public class ListviewAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private ArrayList<Listviewi> data;
    private int layout;
    public ListviewAdapter(Context context, int layout, ArrayList<Listviewi> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i).getName();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=inflater.inflate(layout,viewGroup,false);
        }
        Listviewi listviewitem=data.get(i);

        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(listviewitem.getName());
        TextView hp = (TextView)view.findViewById(R.id.hp);
        hp.setText(listviewitem.getHp());
        return view;
    }
}
