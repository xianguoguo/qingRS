package com.qingfeng.rs.test;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

public class QingDataModelTest {

	private final static String filename = "data/u.data";
	private final static int threshold = 20;
	public static void main(String[] args) throws IOException, TasteException {

		DataModel dataModel = new FileDataModel(new File(filename));
		//compute the max and min value
		//计算最大最小值
		float maxValue = dataModel.getMaxPreference();
		float minValue = dataModel.getMinPreference();
		
		//compute the number of usersNum and itemsNum
		//计算用户和物品总数
		int usersNum = dataModel.getNumUsers();
		int itemsNum = dataModel.getNumItems();
		
		
		int[] itemsNumForUsers = new int[usersNum];
		int[] usersNumForItems = new int[itemsNum];
		
		LongPrimitiveIterator userIDs = dataModel.getUserIDs();
		int i = 0;
		while (userIDs.hasNext()) {
			itemsNumForUsers[i++] = dataModel.getPreferencesFromUser(userIDs.next()).length();
		}
		assert (i == usersNum);
		
		LongPrimitiveIterator itemIDs = dataModel.getItemIDs();
		i = 0;
		while (itemIDs.hasNext()) {
			usersNumForItems[i++] = dataModel.getPreferencesForItem(itemIDs.next()).length();
		}
		assert (i == itemsNum);
		
		//compute mean and variance
		//计算平均值和方差
		double usersMean;
		double usersVar;
		
		int sum = 0;
		int sqSum = 0;
int countLower = 0;
		for (int num : itemsNumForUsers) {
			sum += num;
			if (num < threshold) {
				countLower++;
		//		System.out.println("user warning(" + countLower + ")=" + num);
			}
			sqSum += num * num;
		}
System.out.println("user warning(" + countLower + ")");
		usersMean = (double)sum / usersNum;
		double userSqMean = (double)sqSum / usersNum;
		usersVar = Math.sqrt(userSqMean - usersMean * usersMean);
		
		double itemsMean;
		double itemsVar;
		sum = 0;
		sqSum = 0;
		for (int num : usersNumForItems) {
			sum += num;
			if (num < threshold) {
				countLower++;
				//System.out.println("item warning(" + countLower + ")=" + num);
			}
			sqSum += num * num;
		}
System.out.println("item warning(" + countLower + ")");
		itemsMean = (double)sum / itemsNum;
		double itemsSqMean = (double)sqSum / itemsNum;
		itemsVar = Math.sqrt(itemsSqMean - itemsMean * itemsMean);
		
		
		System.out.println("Preference=(" + minValue + ", " + maxValue + ")");
		System.out.println("usersNum=" + usersNum + ", userMean=" + usersMean + ", userVar=" + usersVar);
		System.out.println("itemsNum=" + itemsNum + ", itemsMean=" + itemsMean + ", itemsVar=" + itemsVar);
	}

}
