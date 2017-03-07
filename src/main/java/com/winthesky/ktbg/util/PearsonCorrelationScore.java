package com.winthesky.ktbg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author MXJ
 * 计算两个用户的相似度
 */
public class PearsonCorrelationScore {
	private Map<String, Map<String, Double>> dataset = null;

	public PearsonCorrelationScore() {
		initDataSet();
	}

	/**
	 * 初始化数据集
	 */
	private void initDataSet() {
		dataset = new HashMap<String, Map<String, Double>>();

		// 初始化Lisa Rose 数据集
		Map<String, Double> roseMap = new HashMap<String, Double>();
		roseMap.put("Lady in the water", 2.5);
		roseMap.put("Snakes on a Plane", 3.5);
		roseMap.put("Just My Luck", 3.0);
		roseMap.put("Superman Returns", 3.5);
		roseMap.put("You, Me and Dupree", 2.5);
		roseMap.put("The Night Listener", 3.0);
		dataset.put("Lisa Rose", roseMap);

		// 初始化Jack Matthews 数据集
		Map<String, Double> jackMap = new HashMap<String, Double>();
		jackMap.put("Lady in the water", 3.0);
		jackMap.put("Snakes on a Plane", 4.0);
		jackMap.put("Superman Returns", 5.0);
		jackMap.put("You, Me and Dupree", 3.5);
		jackMap.put("The Night Listener", 3.0);
		dataset.put("Jack Matthews", jackMap);

		// 初始化Jack Matthews 数据集
		Map<String, Double> geneMap = new HashMap<String, Double>();
		geneMap.put("Lady in the water", 3.0);
		geneMap.put("Snakes on a Plane", 3.5);
		geneMap.put("Just My Luck", 1.5);
		geneMap.put("Superman Returns", 5.0);
		geneMap.put("You, Me and Dupree", 3.5);
		geneMap.put("The Night Listener", 3.0);
		dataset.put("Gene Seymour", geneMap);
	}

	public Map<String, Map<String, Double>> getDataSet() {
		return dataset;
	}

	/**
	 * @param person1
	 *            name
	 * @param person2
	 *            name
	 * @return 皮尔逊相关度值
	 */
	public double sim_pearson(String person1, String person2) {
		// 找出双方都评论过的电影,（皮尔逊算法要求）
		List<String> list = new ArrayList<String>();
		for (Entry<String, Double> p1 : dataset.get(person1).entrySet()) {
			if (dataset.get(person2).containsKey(p1.getKey())) {
				list.add(p1.getKey());
			}
		}

		double sumX = 0.0;
		double sumY = 0.0;
		double sumX_Sq = 0.0;
		double sumY_Sq = 0.0;
		double sumXY = 0.0;
		int N = list.size();

		for (String name : list) {
			Map<String, Double> p1Map = dataset.get(person1);
			Map<String, Double> p2Map = dataset.get(person2);

			sumX += p1Map.get(name);
			sumY += p2Map.get(name);
			sumX_Sq += Math.pow(p1Map.get(name), 2);
			sumY_Sq += Math.pow(p2Map.get(name), 2);
			sumXY += p1Map.get(name) * p2Map.get(name);
		}

		double numerator = sumXY - sumX * sumY / N;
		double denominator = Math.sqrt((sumX_Sq - sumX * sumX / N) * (sumY_Sq - sumY * sumY / N));

		// 分母不能为0
		if (denominator == 0) {
			return 0;
		}

		return numerator / denominator;
	}

	public static void main(String[] args) {
		PearsonCorrelationScore pearsonCorrelationScore = new PearsonCorrelationScore();
		//Gene Seymour 
		System.out.println(pearsonCorrelationScore.sim_pearson("Jack Matthews", "Lisa Rose"));
	}

}
