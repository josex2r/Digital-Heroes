package com.josex2r.digitalheroes.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.Post;

public class RssXmlPullParser {

	private URL rssUrl;
	
	public RssXmlPullParser(String url){
		try {
			this.rssUrl=new URL(url);
		}catch (MalformedURLException  e) {
			throw new RuntimeException(e);
		}
	}
	
	private InputStream getInputStream(){
        try{
            return rssUrl.openConnection().getInputStream();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
	
	private String truncate(final String content, final int lastIndex) {
	    String result = content.substring(0, lastIndex);
	    if (content.charAt(lastIndex) != ' ') {
	        result = result.substring(0, result.lastIndexOf(" "));
	    }
	    return result+"...";
	}
	
	public List<Post> getNews(){
        List<Post> news=null;
        XmlPullParser parser=Xml.newPullParser();
        try{
            parser.setInput(this.getInputStream(), null);
            int event=parser.getEventType();
            Post noticiaActual = null;
            while(event!=XmlPullParser.END_DOCUMENT){
                String tag = null;
                switch (event){
                    case XmlPullParser.START_DOCUMENT:
                    	news=new ArrayList<Post>();
                        break;
                    case XmlPullParser.START_TAG:
                        tag=parser.getName();
                        if (tag.equals("item")){
                            noticiaActual = new Post();
                        }else if (noticiaActual != null){
                            if (tag.equals("link"))
                            	noticiaActual.setLink(parser.nextText());
                            else if (tag.equals("description")){
                            	String description=parser.nextText();
                            	description=truncate(description, 115);
                            	description=description.replaceAll("(^.)*\\[...\\].*$", "$1 ...");
                            	description=description.replace("&#8220;", "¿");
                            	description=description.replace("&#8221;", "?");
                            	description=description.replace("&#8230; ", "\n");
                            	description=description.replaceAll("<[^>]+>", "");
                            	noticiaActual.setDescription(description);
                            }else if (tag.equals("pubDate"))
                                noticiaActual.setDate(parser.nextText());
                            else if (tag.equals("title"))
                                noticiaActual.setTitle(parser.nextText());
                            else if (tag.equals("guid"))
                                noticiaActual.setGuid(parser.nextText());
                            else if (tag.equals("creator"))
                                noticiaActual.setCreator(parser.nextText());
                            else if (tag.equals("category")){
                            	String categoryName=parser.nextText();
                            	
                            	if(categoryName.equals("Advertising")){
                            		noticiaActual.setCategory(Blog.FILTER_ADVERSITING);
                            	}else if(categoryName.equals("Creatividad")){
                            		noticiaActual.setCategory(Blog.FILTER_CREATIVIDAD);
                            	}else if(categoryName.equals("Inside Góbalo")){
                            		noticiaActual.setCategory(Blog.FILTER_INSIDE);
                            	}else if(categoryName.equals("Marketing Digital y Social Media")){
                            		noticiaActual.setCategory(Blog.FILTER_MARKETING);
                            	}else if(categoryName.equals("Negocios")){
                            		noticiaActual.setCategory(Blog.FILTER_NEGOCIOS);
                            	}else if(categoryName.equals("SEO y SEM")){
                            		noticiaActual.setCategory(Blog.FILTER_SEO);
                            	}else if(categoryName.equals("Web y Programación")){
                            		noticiaActual.setCategory(Blog.FILTER_WEB);
                            	}
                                
                            }else if (tag.equals("image"))
                                noticiaActual.setImageLink(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tag=parser.getName();
                        if (tag.equals("item") && noticiaActual != null){
                            news.add(noticiaActual);
                            //Log.d("MyApp",noticiaActual.getTitle());
                            if(noticiaActual.getImageLink().equals(""))
                            	noticiaActual.setImageLink("NO-IMAGE");
                            //Log.d("MyApp",noticiaActual.getImageLink());
                        }
                        break;
            	}
                event = parser.next();
        	}
        }catch (Exception e){
        	 return null;
        }
        return news;
    }

	
	
	
}
