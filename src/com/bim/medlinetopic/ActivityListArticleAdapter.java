package com.bim.medlinetopic;

import java.util.ArrayList;
import java.util.List;

import com.bim.core.Util;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ActivityListArticleAdapter extends BaseAdapter {
	private ActivityListArticle activity;
	protected LayoutInflater inflater;
	private List<Article> articleList;

	public ActivityListArticleAdapter(ActivityListArticle activity) {
		this.activity = activity;
		this.articleList = new ArrayList<Article>();
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addArticleList(List<Article> list) {
		if (list != null) {
			articleList.addAll(list);
		}
	}

	public int getCount() {
		if (articleList.size() >= activity.getAvailabeArticleCount()) {
			return articleList.size();
		} else {
			return articleList.size() + 1;
		}
	}

	public Article getArticle(int position) {
		if (position < articleList.size()) {
			return articleList.get(position);
		} else {
			return null;
		}
	}

	public Object getItem(int position) {
		return getArticle(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.list_article_row, parent, false);
			final ListItemCache cache = new ListItemCache();
			cache.mLoading = (TextView) view
					.findViewById(R.id.list_row_loading);
			cache.mDataLayout = view.findViewById(R.id.list_row_data_layout);
			cache.mTitle = (TextView) view.findViewById(R.id.list_row_title);
			cache.mSummary = (TextView) view
					.findViewById(R.id.list_row_summary);
			view.setTag(cache);
		}

		final ListItemCache cache = (ListItemCache) view.getTag();
		final Article article = getArticle(position);
		if (article == null) {
			cache.mLoading.setVisibility(View.VISIBLE);
			cache.mDataLayout.setVisibility(View.GONE);
			activity.chekAndLoadMore();
			return view;
		}

		cache.mLoading.setVisibility(View.GONE);
		cache.mDataLayout.setVisibility(View.VISIBLE);

		String num = (position + 1) + ".  ";
		String title = num + Setting.removeHTMLTag(article.getTitle());;
		cache.mTitle.setText(Html.fromHtml(title));
		cache.mTitle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				openURL(article);
			}
		});

		cache.mSummary.setText(Html.fromHtml(article.getSummary()));

		return view;
	}

	private void openURL(Article article) {
		if (Util.isNull(article.getUrl())) {
			return;
		}
/*
		String fileName = Setting.parseUrlFileName(article.getUrl());
		String url;
		if (Util.isNotNull(fileName) && fileName.endsWith(".html")) {
			url = Setting.URL_MOBILE
					+ fileName.substring(0, fileName.length() - 1);
		} else {
			url = article.getUrl();
		}
*/		
		activity.openURL(article.getUrl());
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	private final static class ListItemCache {
		public TextView mLoading;
		public View mDataLayout;
		public TextView mTitle;
		public TextView mSummary;

	}

}
