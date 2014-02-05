package com.josex2r.digitalheroes.controllers;

import java.util.List;

import com.josex2r.digitalheroes.model.Post;

public interface AsyncTaskListener {
	public void onTaskComplete(List<Post> loadedPosts);
	public void onTaskFailed();
}
