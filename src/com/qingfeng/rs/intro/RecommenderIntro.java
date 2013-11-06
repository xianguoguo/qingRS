package com.qingfeng.rs.intro;

import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;
import java.io.*;
import java.util.*;

public class RecommenderIntro {

	private FileDataModel model;
	private PearsonCorrelationSimilarity similarity;
	private NearestNUserNeighborhood neighborhood;
	private GenericUserBasedRecommender recommender;

	public RecommenderIntro(String filename) throws Exception {
		model = new FileDataModel(new File(filename));

		similarity = new PearsonCorrelationSimilarity(model);
		neighborhood = new NearestNUserNeighborhood(2, similarity, model);

		recommender = new GenericUserBasedRecommender(model, neighborhood,
				similarity);
	}

	public List<RecommendedItem> SimpleRecommend(int userid, int num)
			throws Exception {

		List<RecommendedItem> recommendations = recommender.recommend(userid,
				num);

		return recommendations;

	}

}
