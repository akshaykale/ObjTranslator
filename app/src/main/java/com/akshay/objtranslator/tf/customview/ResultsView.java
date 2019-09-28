package com.akshay.objtranslator.tf.customview;

import com.akshay.objtranslator.tf.tflite.Classifier;

import java.util.List;

public interface ResultsView {
    public void setResults(final List<Classifier.Recognition> results);
}