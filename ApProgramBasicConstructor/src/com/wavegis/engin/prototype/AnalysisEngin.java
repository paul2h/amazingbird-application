package com.wavegis.engin.prototype;

import java.util.List;

public interface AnalysisEngin<T> {

	/** 原始String轉成model bean */
	public List<T> analysisOriginalData(String originalMessage);

}
