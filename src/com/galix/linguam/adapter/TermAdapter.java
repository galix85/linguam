package com.galix.linguam.adapter;

import java.util.List;

import com.galix.linguam.R;
import com.galix.linguam.util.WordReferenceUtil.Term;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TermAdapter extends BaseAdapter implements OnClickListener {
    private Context context;

    private List<Term> listTerm;

    public TermAdapter(Context context, List<Term> listPhonebook) {
        this.context = context;
        this.listTerm = listPhonebook;
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

        //TextView tvType = (TextView) convertView.findViewById(R.id.tvType);
        //tvType.setText(entry.getPOS());
        
        /*
        // Set the onClick Listener on this button
        Button btnRemove = (Button) convertView.findViewById(R.id.btnRemove);
        btnRemove.setFocusableInTouchMode(false);
        btnRemove.setFocusable(false);
        btnRemove.setOnClickListener(this);
        // Set the entry, so that you can capture which item was clicked and
        // then remove it
        // As an alternative, you can use the id/position of the item to capture
        // the item
        // that was clicked.
        btnRemove.setTag(entry);

        // btnRemove.setId(position);*/
        

        return convertView;
    }

    @Override
    public void onClick(View view) {
        Term entry = (Term) view.getTag();
        listTerm.remove(entry);
        // listPhonebook.remove(view.getId());
        notifyDataSetChanged();

    }


}
