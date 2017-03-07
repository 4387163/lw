package com.winthesky.ktbg.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class UserRecommend {

	private Map<String, Map<String, Integer>> user_ratings_data;

	private List<String> users;

	public UserRecommend() {
		initData();
	}

	private void initData() {
		user_ratings_data = new HashMap<String, Map<String, Integer>>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("./data/user_news_rating.txt"));
			String str = "";
			while (StringUtils.isNotBlank(str = br.readLine())) {
				String[] strs = str.split(",");
				int rating = Integer.parseInt(strs[2]);

				Map<String, Integer> user_rating_list = user_ratings_data.get(strs[0]);
				if (user_rating_list == null) {
					user_rating_list = new HashMap<String, Integer>();
					user_ratings_data.put(strs[0], user_rating_list);
				}
				user_rating_list.put(strs[1], rating);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}

		users = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader("./data/user.txt"));
			String str = "";
			while (StringUtils.isNotBlank(str = br.readLine())) {
				String[] strs = str.split(",");
				users.add(strs[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public Map<String, Double> recommendUsers(String userID) {
		Map<String, Double> pre_similars = new LinkedHashMap<String, Double>();
		// 再循环计算这些相似用户的相似度
		for (int i = 0; i < users.size(); i++) {
			String u = users.get(i);
			if (!u.equals(userID)) {
				double d = sim_pearson(userID, u);
				if (!Double.isNaN(d) && d > 0d) {
					pre_similars.put(u, d);
				}
			}
		}

		Map<String, Double> similars = sortByValue(pre_similars, true);
		return similars;
	}

	public void recommendNews(String userID, Map<String, Double> similarUsers) {
		Map<String, Integer> userRatings = user_ratings_data.get(userID);
		
		// 先得到该用户未看过，但是相似用户看过的新闻列表
		Map<String, Double> similar_sum = new LinkedHashMap<String, Double>();
		Map<String, Double> similar_pro = new LinkedHashMap<String, Double>();

		Double similarSum = 0d;
		for (Iterator<Map.Entry<String, Double>> iterator = similarUsers.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Double> entry = iterator.next();
			similarSum += entry.getValue();
			Map<String, Integer> tuserRatings = user_ratings_data.get(entry.getKey());
			for (Iterator<Map.Entry<String, Integer>> iterator2 = tuserRatings.entrySet().iterator(); iterator2
					.hasNext();) {
				Map.Entry<String, Integer> entry2 = iterator2.next();
				if(!userRatings.containsKey(entry2.getKey())) { // 如果没看过
					// 相似度总和
					Double sum = similar_sum.get(entry2.getKey());
					if (sum == null) {
						sum = 0d;
					}
					sum += (entry.getValue());
					similar_sum.put(entry2.getKey(), sum);
					
					// 相似度*评分 的总和
					Double pro = similar_pro.get(entry2.getKey());
					if (pro == null) {
						pro = 0d;
					}
					pro += (entry.getValue() * entry2.getValue());
					similar_pro.put(entry2.getKey(), pro);
					
				}
			}
		}

	}

	public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean desc) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		if (desc) {
			Collections.reverse(list);
		}

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
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
		Map<String, Integer> p1ratings = user_ratings_data.get(person1);
		if (p1ratings != null) {
			for (Entry<String, Integer> p1 : p1ratings.entrySet()) {
				Map<String, Integer> p2ratings = user_ratings_data.get(person2);
				if (p2ratings != null && p2ratings.containsKey(p1.getKey())) {
					list.add(p1.getKey());
				}
			}
		}

		double sumX = 0.0;
		double sumY = 0.0;
		double sumX_Sq = 0.0;
		double sumY_Sq = 0.0;
		double sumXY = 0.0;
		int N = list.size();

		for (String name : list) {
			Map<String, Integer> p1Map = user_ratings_data.get(person1);
			Map<String, Integer> p2Map = user_ratings_data.get(person2);

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
		String userID = "00002";
		UserRecommend ur = new UserRecommend();
		Map<String, Double> recommends = ur.recommendUsers(userID);
		for (Iterator<Map.Entry<String, Double>> iterator = recommends.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Double> entry = iterator.next();
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
}
