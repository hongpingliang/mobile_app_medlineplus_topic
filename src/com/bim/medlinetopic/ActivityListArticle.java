package com.bim.medlinetopic;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.bim.core.ActivityBase;
import com.bim.core.Log;
import com.bim.core.Util;

public class ActivityListArticle extends ActivityBase {
	private ActivityListArticleAdapter mListAdapter;
	private String term;
	private Loader loader;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.list_article);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon);

		Intent intent = getIntent();
		if (intent != null) {
			term = intent.getStringExtra("term");
		}
		if ( Util.isNull(term)) {
			Log.d("No term: in ActivityListArticle");
			finish();
			return;
		}

		loader = new Loader(this);
		loader.setCount(1);
		loader.setTerm(term.replaceAll(" ", "+"));

		mListAdapter = new ActivityListArticleAdapter(this);
		ListView mListView = (ListView) findViewById(R.id.list_article_list);
		mListView.setAdapter(mListAdapter);
		
		chekAndLoadMore();
	}

	public int getAvailabeArticleCount() {
		return loader.getCount();
	}

	public void chekAndLoadMore() {
		if (mListAdapter.getArticleList().size() >= getAvailabeArticleCount()) {
			return;
		}

		if (httpThread != null && httpThread.isAlive()) {
			return;
		}
		
		httpThread = new Thread(loader);
		httpThread.start();
		setProgressBarIndeterminateVisibility(true);
	}

	public void onLoadReady(List<Article> articleList) {
		setProgressBarIndeterminateVisibility(false);
		//closeDialog();
		if (articleList == null) {
			return;
		}
		
		if (articleList != null && articleList.size() > 0) {
			Log.d("load: " + articleList.size());
			mListAdapter.addArticleList(articleList);
			mListAdapter.notifyDataSetChanged();
			int newStart = loader.getRetstart() + Setting.SEARCH_retmax;
			loader.setRetstart(newStart);
		}
		
		setTitle("Found: " + loader.getCount());
	}

	
	public static void startActivity(Activity activity, String term, int requestCode) {
		Intent intent = new Intent(activity, ActivityListArticle.class);
		intent.putExtra("term", term);

		activity.startActivityForResult(intent, requestCode);
	}	

}
