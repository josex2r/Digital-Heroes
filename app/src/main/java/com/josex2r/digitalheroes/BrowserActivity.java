package com.josex2r.digitalheroes;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.MalformedURLException;
import java.net.URL;

@SuppressLint("SetJavaScriptEnabled") public class BrowserActivity extends Activity {
	
	private ProgressBar pbWebLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		overridePendingTransition(R.anim.activity_slide_in, R.anim.no_anim);
		
		setContentView(R.layout.activity_browser);
		 
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		pbWebLoader = (ProgressBar) findViewById(R.id.pbWebLoader);
		pbWebLoader.setVisibility(View.VISIBLE);
		
		Bundle data = getIntent().getExtras();

		try {
			String title = data.getString("title");
			actionBar.setTitle(title);
			
			URL link = new URL( data.getString("uri") );
			
			WebView navegador = (WebView)findViewById(R.id.wvPost);
			
			WebSettings settings = navegador.getSettings();
			settings.setJavaScriptEnabled(true);
			
			navegador.setHorizontalScrollBarEnabled(false);
			
			navegador.setWebViewClient(new WebViewClient(){
		        public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        	/*if (url != null && url.matches("/gobalo/g")){
	        			return false;
		        	}else  {*/
		        		view.getContext().startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(url)) );
		        		return true;
		        	//}
		        }
		        
		        @Override
		        public void onPageFinished(WebView view, String url) {
		        	// TODO Auto-generated method stub
		        	pbWebLoader.setVisibility(View.GONE);
		        	super.onPageFinished(view, url);
		        }
		    });
			
			navegador.loadUrl(link.toString());
			
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		overridePendingTransition(R.anim.no_anim, R.anim.activity_slide_out);
		super.onPause();
	}

}
