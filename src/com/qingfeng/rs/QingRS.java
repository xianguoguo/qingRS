package com.qingfeng.rs;

import java.util.List;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.qingfeng.rs.intro.RecommenderIntro;

public class QingRS {

	private RecommenderIntro recommender = null;
	private final String filename = "intro.csv";

	public static void main(String[] args) throws Exception {

		int userId = 1;
		int rankNum = 1;
		
		QingRS qingRS = new QingRS();
		String resultStr = qingRS.getRecommender(userId, rankNum);
		
		System.out.println(resultStr);
	}

	private String getRecommender(int userId, int num) throws Exception {
		if (recommender == null)
			recommender = new RecommenderIntro(filename);

		List<RecommendedItem> recommendedList = recommender.SimpleRecommend(
				userId, num);
		String resultStr = "Result=" + recommendedList.get(0).getItemID() + " "
				+ recommendedList.get(0).getValue();

		return resultStr;
	}

}