package com.josex2r.digitalheroes.controllers;


public interface AsyncTaskListener<T> {
	public void onTaskComplete(T param);
	public void onTaskFailed();
}
