package com.qingfeng.rs;

import java.util.List;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.qingfeng.rs.intro.RecommenderIntro;

public class QingRS {

	private RecommenderIntro recommender = null;
	private static final String filename = "data/u.data";

	public static void main(String[] args) throws Exception {

		int userId = 1;
		int rankNum = 2;
		
		QingRS qingRS = new QingRS();
		for(int neighberNum = 2; neighberNum < 10; neighberNum++) {
			System.out.println("neigherNum=" + neighberNum);
			qingRS.initRecommenderIntro(filename, neighberNum);
			String resultStr = qingRS.getRecommender(userId, rankNum);
			System.out.println(resultStr);
		}

	}

	private void initRecommenderIntro(String filename, int num) throws Exception {
		recommender = new RecommenderIntro(filename, num);
	}
	private String getRecommender(int userId, int num) throws Exception {
			
		List<RecommendedItem> recommendedList = recommender.SimpleRecommend(
				userId, num);
		String resultStr = "Recommend=" + recommendedList.get(0).getItemID() + " "
				+ recommendedList.get(0).getValue();

		return resultStr;
	}

}