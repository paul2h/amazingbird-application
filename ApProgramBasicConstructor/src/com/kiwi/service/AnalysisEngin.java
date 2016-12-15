package com.kiwi.service;

import java.util.List;

public interface AnalysisEngin<T> {

	public List<T> analysisOriginalData(String originalMessage);
	
}
