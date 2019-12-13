package com.bim.medlinetopic;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Handler;

import com.bim.core.Log;

public class Loader implements Runnable {
	private ActivityListArticle activity;
	private String term;
	private String server;
	private String file;
	private int count;
	private int retstart;
	
	private List<Article> articleList;

	private final Handler mLoadHandler = new Handler();
	final Runnable mLoadCallback = new Runnable() {
		public void run() {
			activity.onLoadReady(articleList);
		}
	};

	public Loader(ActivityListArticle activity) {
		this.activity = activity;
		
	}

	public void run() {
		this.articleList = new ArrayList<Article>();
		String url = Setting.getURL(term, server, file, retstart);
		Log.d(url);
		try {
			LoaderHandler handler = new LoaderHandler(this);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(handler);

			URL u = new URL(url);
			InputSource ip = new InputSource(u.openStream());
			xr.parse(ip);
		} catch (Exception e) {
			Log.d(e);
		}

		mLoadHandler.post(mLoadCallback);
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getRetstart() {
		return retstart;
	}

	public void setRetstart(int retstart) {
		this.retstart = retstart;
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}