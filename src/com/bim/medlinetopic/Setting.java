package com.bim.medlinetopic;

import com.bim.core.SettingBase;
import com.bim.core.Util;

public class Setting extends SettingBase {
	public static final String KEY_Disclaimer = "pref_Disclaimer";
	
	public static final String URL_BASE = "http://wsearch.nlm.nih.gov/ws/query";
	public static final String URL_MOBILE = "http://m.medlineplus.gov/topic/";
	public static final int SEARCH_retmax = 5;

	public static String getURL(String term, String server, String file,
			int retstart) {
		String t = URL_BASE;
		t += "?retmax=" + SEARCH_retmax;
		if (Util.isNull(server) || Util.isNull(file)) {
			t += "&db=healthTopics&term=" + term;
		} else {
			t += "&server=" + server;
			t += "&file=" + file;
			t += "&retstart=" + retstart;
		}

		return t;
	}

	public static String removeHTMLTag(String str) {
		if ( Util.isNull(str)) {
			return str;
		}
		return str.replaceAll("\\<.*?\\>", "");
	}
	
	public static String parseUrlFileName(String url) {
		try {
			 return url.substring(url.lastIndexOf('/')+1);
		} catch (Exception e) {
		}
		return null;

	}

}
