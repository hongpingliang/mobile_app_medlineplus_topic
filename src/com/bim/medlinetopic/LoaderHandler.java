package com.bim.medlinetopic;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bim.core.Util;

public class LoaderHandler extends DefaultHandler {

	private Loader loader;
	private Article article;

	private boolean is_file = false;
	private boolean is_server = false;
	private boolean is_count = false;
	private boolean is_retstart = false;
	private boolean is_retmax = false;
	private boolean is_document = false;
	private boolean is_content = false;
	private boolean is_title = false;
	private boolean is_organizationName = false;
	private boolean is_FullSummary = false;

	private StringBuffer mSb;

	public LoaderHandler(Loader loader) {
		this.loader = loader;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		mSb = new StringBuffer();
		String tag = Util.trim(localName);

		if ("file".equals(tag)) {
			is_file = true;
		} else if ("server".equals(tag)) {
			is_server = true;
		} else if ("count".equals(tag)) {
			is_count = true;
		} else if ("retstart".equals(tag)) {
			is_retstart = true;
		} else if ("retmax".equals(tag)) {
			is_retmax = true;
		} else if ("document".equals(tag)) {
			is_document = true;
			article = new Article();
			if (attributes != null) {
				article.setUrl(attributes.getValue("url"));
			}
		} else if ("content".equals(tag)) {
			if (is_document) {
				is_content = true;
				if (attributes != null) {
					String val = attributes.getValue("name");
					if ("title".equals(val)) {
						is_title = true;
					} else if ("organizationName".equals(val)) {
						is_organizationName = true;
					}
					if ("FullSummary".equals(val)) {
						is_FullSummary = true;
					}
				}
			}
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		String tag = Util.trim(localName);

		String value = null;
		if (mSb != null) {
			value = Util.trim(mSb.toString());
		}

		if ("file".equals(tag)) {
			loader.setFile(value);
			is_file = false;
		} else if ("server".equals(tag)) {
			loader.setServer(value);
			is_server = false;
		} else if ("count".equals(tag)) {
			loader.setCount(Util.toInt(value));
			is_count = false;
		} else if ("retstart".equals(tag)) {
			is_retstart = false;
		} else if ("retmax".equals(tag)) {
			is_retmax = false;
		} else if ("document".equals(tag)) {
			is_document = false;
			loader.getArticleList().add(article);
			article = null;
		} else if ("content".equals(tag)) {
			if (is_document) {
				if (is_content) {
					if (is_title) {
						article.setTitle(value);
						is_title = false;
					} else if (is_organizationName) {
						article.setOrganization(value);
						is_organizationName = false;
					} else if (is_FullSummary) {
						article.setSummary(value);
						is_FullSummary = false;
					}
				}
				is_content = false;
			}
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (is_file || is_server || is_count || is_retstart || is_retmax
				|| is_title || is_organizationName || is_FullSummary) {
			mSb.append(new String(ch, start, length));
		}
	}

	public void startDocument() throws SAXException {
	}

	public void endDocument() throws SAXException {
	}
}
