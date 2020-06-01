/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgc.svd.mahout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.ParallelSGDFactorizer;


import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
/**
 *
 * @author subhasish
 */
public class SVDVideoRecommender {
    public static final String EEG_INTEREST_LEVEL_DATASET_FILE = "ratings.dat";

	
	public static DataModel generateDataModel(int dataset) throws Exception {			
		String data = EEG_INTEREST_LEVEL_DATASET_FILE;
		File dataFile = new File(data);
		return new FileDataModel(dataFile);
	}
        
        public static DataModel getPrefDataByFileFast() throws Exception {		
		String data = EEG_INTEREST_LEVEL_DATASET_FILE;
		FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();
		BufferedReader br = new BufferedReader(new FileReader(data));

		ArrayList<Preference> list = new ArrayList<Preference>();
		String line = br.readLine();
		long uid_init = Long.parseLong(line.split("::")[0]);
		while (line != null) {
			String[] arr = line.split("::");
			long uid = Long.parseLong(arr[0]);
			if (uid_init == uid) {
				list.add(new GenericPreference(uid, Long.parseLong(arr[1]),
						Float.parseFloat(arr[2])));
			} else {
				preferences.put(uid_init, new GenericUserPreferenceArray(list));
				list = new ArrayList<Preference>();
				list.add(new GenericPreference(uid, Long.parseLong(arr[1]),
						Float.parseFloat(arr[2])));
				uid_init = uid;
			}
			line = br.readLine();

			if (line == null) {
				preferences.put(uid_init, new GenericUserPreferenceArray(list));
			}
		}
		br.close();
		return new GenericDataModel(preferences);
	}
        
        public static RecommenderBuilder buildSVDRecommender() throws TasteException {
		System.out.println("Recommendation is ALSWR-based and SVDRecommender");
		return new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model)
					throws TasteException {
				return new SVDRecommender(model, new ALSWRFactorizer(model, 10,
						0.05, 10));
			}
		};
	}
        
        // SVDRecommender ExpectationMaximizationSVDFactorizer 20, 50
	public static RecommenderBuilder svd_recommender_13(DataModel model) {
		 
			//RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
			return new RecommenderBuilder() { 
				@Override 
				public Recommender buildRecommender(DataModel model) throws TasteException {
                                    System.out.println("Builing recommender...");
                                    Factorizer f = new SVDPlusPlusFactorizer(model, 3, 2);//new ParallelSGDFactorizer(model,3,0.05, 2); 
                                    return new SVDRecommender(model, f);
				}
			};
	}
        
        private static void runALSSVDRecommender(DataModel dataModel)
			throws TasteException {
                       
		System.out.println("Start of Running an SVD Recommendation");
		RecommenderBuilder recommenderBuilder = svd_recommender_13(dataModel);
		SVDRecommender recommender = (SVDRecommender) recommenderBuilder.buildRecommender(dataModel);
                System.out.println("SVD++");
		RunningAverage runningAverage = new FullRunningAverage();
		LongPrimitiveIterator userIDs = dataModel.getUserIDs();
                System.out.println("looping");
		while (userIDs.hasNext()) {
			long userID = userIDs.nextLong();
                        //System.out.println(userID);
			for (Preference pref : dataModel.getPreferencesFromUser(userID)) {

				double ratingValue = pref.getValue();
				double preferenceEstimate = recommender.estimatePreference(
						userID, pref.getItemID());

				//System.out.println(userID + "," + pref.getItemID() + ","
				//		+ ratingValue);
				double errorValue = ratingValue - preferenceEstimate;
				runningAverage.addDatum(errorValue * errorValue);
			}
		}

		double rmse = Math.sqrt(runningAverage.getAverage());
		System.out.println(rmse);

		// Recommender Evaluation -- Average Absolute Difference Evaluator
		//RecommenderEvaluator absoluteDifferenceEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		//double score = absoluteDifferenceEvaluator.evaluate(recommenderBuilder,
		//		null, dataModel, 0.9, 1.0);
		//System.out.println("ALS-based Recommender Average Score is: " + score);

		// Recommender Evaluation -- RMS Evaluator
		RecommenderEvaluator rmsEvaluator = new RMSRecommenderEvaluator();
		double rmsscore = rmsEvaluator.evaluate(recommenderBuilder, null,
				dataModel, 0.9, 1.0);
		System.out.println("SVD-based Recommender RMS Score is:" + rmsscore);

		// Recommender Evaluation -- IRStats Evaluator
		//RecommenderIRStatsEvaluator irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
		//IRStatistics stats = irStatsEvaluator.evaluate(recommenderBuilder,
		//		null, dataModel, null, 2,
		//		GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);

		//System.out.println("Precision Valus is : " + stats.getPrecision());
		//System.out.println("Recall Value is : " + stats.getRecall());

		System.out.println("End of Running an SVD Recommendation");
	}
        
        
        public static void main(String[] args) throws Exception {

		/********** Uncomment the line that matches the data set under test **********/

		DataModel dataModel = getPrefDataByFileFast();
                runALSSVDRecommender(dataModel);
                
                
        }

}
