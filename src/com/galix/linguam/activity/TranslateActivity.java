package com.galix.linguam.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.db.OriginalWordDBAdapter;
import com.galix.linguam.db.TranslationDBAdapter;
import com.galix.linguam.util.RequestWR;
import com.galix.linguam.util.WordReferenceUtil.Term;

public class TranslateActivity extends ListActivity {

	private ImageButton search_button;
	private ListView listview;
	ArrayList<Term> translateList;

	private static HashMap<String, List<Term>> hashmapResponse;
	private static OriginalWordDBAdapter originalWordDB;
	private static TranslationDBAdapter translatedWordDB;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate_main_layout);

		originalWordDB = new OriginalWordDBAdapter(
				LinguamApplication.getContext());
		translatedWordDB = new TranslationDBAdapter(
				LinguamApplication.getContext());

		// List & listview
		translateList = new ArrayList<Term>();
		listview = (ListView) findViewById(android.R.id.list);

		// Get the message from the intent
		Intent intent = getIntent();

		search_button = (ImageButton) findViewById(R.id.translate);
		search_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				EditText translate_caption = (EditText) (findViewById(R.id.search));
				String word_to_translate = translate_caption.getText()
						.toString();
				if (!word_to_translate.equals("")) {
					// hashmapResponse = RequestWR.callWR(word_to_translate);
					showResults(RequestWR.callWR(word_to_translate));
					translate_caption.setText("");
				}
			}
		});
	}

	private void saveOriginalWord(Term originalWord) {
		originalWordDB.createOriginalWord(originalWord);
	}

	private void saveTranslateWord(Term translateWord, Term originalWord) {
		translatedWordDB.createTranslation(translateWord, true,
				originalWord.getTerm());
	}

	private void showResults(final HashMap<String, List<Term>> hashmapResponse) {

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.id.list, translateList);

		listview.setAdapter(adapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {

				final Term item = (Term) parent.getItemAtPosition(position);
				saveOriginalWord(hashmapResponse.get("originalTerm").get(
						hashmapResponse.get("originalTerm").size() - 1));
				saveTranslateWord(item, hashmapResponse.get("originalTerm")
						.get(hashmapResponse.get("originalTerm").size() - 1));

			}

		});

	}

	private class StableArrayAdapter extends ArrayAdapter<Term> {

		HashMap<Term, Integer> mIdMap = new HashMap<Term, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				ArrayList<Term> translateList) {
			super(LinguamApplication.getContext(), textViewResourceId,
					translateList);
			for (int i = 0; i < translateList.size(); ++i) {
				mIdMap.put(translateList.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			Term item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}
}