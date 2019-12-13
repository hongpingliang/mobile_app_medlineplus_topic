package com.bim.medlinetopic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.bim.core.ActivityBase;
import com.bim.core.Util;

public class ActivityMain extends ActivityBase {
	private EditText mTerm;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.main);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon);

		mTerm = (EditText) findViewById(R.id.search_code_text);
		Button searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				doSearch();
			}
		});
		
		if ( 1 != getPreferenceInt(Setting.KEY_Disclaimer)) {
			showDisclaimer();
		}		

	}

	private void showDisclaimer() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.disclaimer_title);
		alert.setIcon(R.drawable.app_icon);
		alert.setMessage(R.string.disclaimer_message);

		alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				savePreference(Setting.KEY_Disclaimer, 1);
				return;
			}
		});

		alert.setNegativeButton("Deny",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		alert.show();
	}		
	
	private void doSearch() {
		String term = Util.trim(mTerm.getText().toString());
		if (Util.isNull(term)) {
			showMessage("Please enter search keywords");
			return;
		}
		if ( term.indexOf(" ") > -1 ) {
			//term = term.replaceAll(" ", "+");
			term = "%22" + Util.encode(term) + "%22";
		} 
		ActivityListArticle.startActivity(this, term, 0);
	}	

}