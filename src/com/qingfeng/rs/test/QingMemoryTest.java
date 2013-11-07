package com.qingfeng.rs.test;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class QingMemoryTest {

	private static final String filename = "data/u1m.data";

	public static void main(String[] args) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		System.out.println("start: jvm used-memory= " + (runtime.totalMemory() - runtime.freeMemory())
				/ (double) (1024 * 1024) + "MB");

		DataModel dataModel = new FileDataModel(new File(filename));
		System.out.println("after dataModel: jvm used-memory= "
				+ (runtime.totalMemory() - runtime.freeMemory()) / (double) (1024 * 1024) + "MB");

		UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
		System.out.println("after similarity: jvm used-memory= "
				+ (runtime.totalMemory() - runtime.freeMemory()) / (double) (1024 * 1024) + "MB");

		UserNeighborhood neighborhood = new NearestNUserNeighborhood(5,
				similarity, dataModel);
		System.out.println("after neighborhood: jvm used-memory= "
				+ (runtime.totalMemory() - runtime.freeMemory()) / (double) (1024 * 1024) + "MB");

		Recommender recommender = new GenericUserBasedRecommender(dataModel,
				neighborhood, similarity);
		System.out.println("after recommender: jvm used-memory= "
				+ (runtime.totalMemory() - runtime.freeMemory()) / (double) (1024 * 1024) + "MB");

		System.out.println("recommend="
				+ recommender.recommend(42, 1).get(0).getItemID());
		System.out.println("after recommend first: jvm used-memory= "
				+ (runtime.totalMemory() - runtime.freeMemory()) / (double) (1024 * 1024) + "MB");
		System.gc();
		System.out.println("after gc: jvm used-memory= " + (runtime.totalMemory() - runtime.freeMemory())
				/ (double) (1024 * 1024) + "MB");

		// dataModel
		System.out.println("recommend="
				+ recommender.recommend(42, 1).get(0).getItemID());

		System.out.println("after recommend second: jvm used-memory= "
				+ (runtime.totalMemory() - runtime.freeMemory()) / (double) (1024 * 1024) + "MB");
	}

}
