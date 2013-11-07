package com.qingfeng.rs.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class QingParaTest {

	private final String filename = "data/u.data";
	private double threshold = 0.95;
	private int neighborNum = 2;
	private ArrayList<UserSimilarity> userSims;
	private final int SIM_NUM = 4;
	private final int NEIGHBOR_NUM = 64;
	private final double THRESHOLD_LOW = 0.7;

	public static void main(String[] args) throws IOException, TasteException {

		new QingParaTest().valuate();

	}

	public QingParaTest() {
		super();
		this.userSims = new ArrayList<UserSimilarity>();
	}

	private void valuate() throws IOException, TasteException {
		DataModel dataModel = new FileDataModel(new File(filename));

		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();

		// populate Similarity
		populateUserSims(dataModel);

		int simBest = -1;
		double scoreBest = 5.0;
		int neighborBest = -1;
		double thresholdBest = -1;
		System.out.println("SIM\tNeighborNum\t\tThreshold\tscore");
		for (int i = 0; i < SIM_NUM; i++) {
			for (neighborNum = 2; neighborNum <= NEIGHBOR_NUM; neighborNum *= 2) {

				for (threshold = 0.75; threshold >= THRESHOLD_LOW; threshold -= 0.05) {
					double score = 5.0;
					QingRecommenderBuilder qRcommenderBuilder = new QingRecommenderBuilder(
							userSims.get(i), neighborNum, threshold);

					// Use 70% of the data to train; test using the other 30%.
					score = evaluator.evaluate(qRcommenderBuilder, null,
							dataModel, 0.7, 1.0);
					System.out.println((i + 1) + "\t" + neighborNum + "\t"
							+ threshold + "\t" + score);

					if (score < scoreBest) {
						scoreBest = score;
						simBest = i + 1;
						neighborBest = neighborNum;
						thresholdBest = threshold;
					}
				}
			}
		}
		System.out.println("The best parameter");
		System.out.println(simBest + "\t" + neighborBest + "\t" + thresholdBest
				+ "\t" + scoreBest);
	}

	private void populateUserSims(DataModel dataModel) throws TasteException {
		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(
				dataModel);
		userSims.add(userSimilarity);
		userSimilarity = new TanimotoCoefficientSimilarity(dataModel);
		userSims.add(userSimilarity);
		userSimilarity = new LogLikelihoodSimilarity(dataModel);
		userSims.add(userSimilarity);

		userSimilarity = new EuclideanDistanceSimilarity(dataModel);
		userSims.add(userSimilarity);

	}

}

class QingRecommenderBuilder implements RecommenderBuilder {

	private UserSimilarity userSimilarity;
	private int neighborNum;
	private double threshold;

	public QingRecommenderBuilder(UserSimilarity userSimilarity,
			int neighborNum, double threshold) {
		super();
		this.userSimilarity = userSimilarity;
		this.neighborNum = neighborNum;
		this.threshold = threshold;
	}

	@Override
	public Recommender buildRecommender(DataModel dataModel)
			throws TasteException {
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(
				neighborNum, threshold, userSimilarity, dataModel);
		return new GenericUserBasedRecommender(dataModel, neighborhood,
				userSimilarity);
	}

}
