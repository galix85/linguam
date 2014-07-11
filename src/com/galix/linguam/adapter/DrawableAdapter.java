package com.galix.linguam.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galix.linguam.R;
import com.galix.linguam.pojo.Language;

public class DrawableAdapter extends BaseAdapter {
    private Context context;

    private List<Language> listLanguage;

    public DrawableAdapter(Context context, List<Language> listLanguage) {
        this.context = context;
        this.listLanguage = listLanguage;
    }

    public int getCount() {
        return listLanguage.size();
    }

    public Object getItem(int position) {
        return listLanguage.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	
    	Language entry = listLanguage.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_listview_item, null);
        }
        TextView tvLanguageTitle = (TextView) convertView.findViewById(R.id.tvLanguageTitle);
        tvLanguageTitle.setText(entry.getTitle());

        TextView tvLanguageSubtitle = (TextView) convertView.findViewById(R.id.tvLanguageSubtitle);
        tvLanguageSubtitle.setText("("+entry.getSubtitle()+")");
    

        return convertView;
    }

}
