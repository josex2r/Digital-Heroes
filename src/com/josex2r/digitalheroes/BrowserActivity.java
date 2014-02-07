package com.josex2r.digitalheroes;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.josex2r.digitalheoroes.R;

public class BrowserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		
		Uri link = getIntent().getData();
		
		WebView navegador = (WebView)findViewById(R.id.wvPost);
		
		WebSettings settings = navegador.getSettings();
		settings.setJavaScriptEnabled(true);

		
		navegador.setHorizontalScrollBarEnabled(false);
		
		navegador.setWebViewClient(new Callback());
		navegador.loadUrl(link.toString());
		
	}

	
	private class Callback extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}
	}

}
