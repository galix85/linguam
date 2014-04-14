package com.galix.linguam.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galix.linguam.R;
import com.galix.linguam.pojo.Term;

public class TermAdapter extends BaseAdapter {
    private Context context;

    private List<Term> listTerm;

    public TermAdapter(Context context, List<Term> listTerm) {
        this.context = context;
        this.listTerm = listTerm;
    }

    public int getCount() {
        return listTerm.size();
    }

    public Object getItem(int position) {
        return listTerm.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	
    	Term entry = listTerm.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.translate_row, null);
        }
        TextView tvTerm = (TextView) convertView.findViewById(R.id.tvTerm);
        tvTerm.setText(entry.getTerm());

        TextView tvType = (TextView) convertView.findViewById(R.id.tvType);
        if (!entry.getSense().equals("")){
        	tvType.setText(entry.getPOS() + " ("+ entry.getSense() +")");
        }else{
        	tvType.setText(entry.getPOS());
        }

        return convertView;
    }

}
