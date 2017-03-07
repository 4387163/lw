package com.winthesky.bo.ktbg;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

import com.winthesky.bo.BaseBusinessBO;

@Service
public class RecommendBO extends BaseBusinessBO {

	public Map<String, Double> recommendUsers(String userID) {
		Map<String, Double> pre_similars = new LinkedHashMap<String, Double>();

		// 查出所有用户
		List<Map<String, Object>> users = getJdbcTemplate().queryForList("select id user_id from user");

		// 再循环计算这些相似用户的相似度
		for (int i = 0; i < users.size(); i++) {
			Map<String, Object> u = users.get(i);
			String tid = (String) u.get("user_id");
			if (!tid.equals(userID)) {
				double d = sim_pearson(userID, tid);
				if (!Double.isNaN(d) && d > 0d) {
					pre_similars.put(tid, d);
				}
			}
		}

		Map<String, Double> similars = sortByValue(pre_similars, true);
		return similars;
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
		String sql = "SELECT nr.news_id FROM news_rating nr where nr.user_id = ? "
				+ "and exists (select 1 from news_rating nr2 where nr2.user_id=? and nr2.news_id = nr.news_id)";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, new Object[] { person1, person2 });

		double sumX = 0.0;
		double sumY = 0.0;
		double sumX_Sq = 0.0;
		double sumY_Sq = 0.0;
		double sumXY = 0.0;
		int N = list.size();

		List<Map<String, Object>> p1list = getJdbcTemplate().queryForList(
				"select nr.news_id, nr.score from news_rating nr where nr.user_id = ?", new Object[] { person1 });
		List<Map<String, Object>> p2list = getJdbcTemplate().queryForList(
				"select nr.news_id, nr.score from news_rating nr where nr.user_id = ?", new Object[] { person2 });

		Map<String, Integer> p1Map = new HashMap<String, Integer>();
		for (Map<String, Object> map : p1list) {
			String newsID = (String) map.get("news_id");
			Integer score = (Integer) map.get("score");
			p1Map.put(newsID, score);
		}

		Map<String, Integer> p2Map = new HashMap<String, Integer>();
		for (Map<String, Object> map : p2list) {
			String newsID = (String) map.get("news_id");
			Integer score = (Integer) map.get("score");
			p2Map.put(newsID, score);
		}

		for (Map<String, Object> map : list) {
			String name = (String) map.get("news_id");
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

	public List recommendNews(final String userID) {
		Map<String, Double> users = recommendUsers(userID);
		if (!users.isEmpty()) {
			getJdbcTemplate().update("delete from temp_user_similar");
			final List<Map.Entry<String, Double>> userList = new ArrayList<Map.Entry<String, Double>>(users.entrySet());
			getJdbcTemplate().batchUpdate("insert into temp_user_similar values(?,?,?)",
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							Map.Entry<String, Double> e = userList.get(i);
							ps.setString(1, userID);
							ps.setString(2, e.getKey());
							ps.setDouble(3, e.getValue());
						}

						@Override
						public int getBatchSize() {
							return userList.size();
						}
					});

			StringBuffer sb = new StringBuffer();
			sb.append("select * from ( ");
			sb.append("select a.news_id, sum(a.s1 * a.s2) / sum(a.s1) as score ");
			sb.append("from ( ");
			sb.append("select nr1.news_id, tus.similar_score as s1, ifnull(nr1.score, 0) as s2 ");
			sb.append("from news_rating nr1 ");
			sb.append("inner join temp_user_similar tus on nr1.user_id = tus.user2_id ");
			sb.append("where not exists (select 1 from news_rating nr2 where nr2.user_id = tus.user1_id and nr2.news_id = nr1.news_id) ");
			sb.append("and tus.user1_id = ?) a ");
			sb.append("group by a.news_id) b ");
			sb.append("order by b.score desc ");
			List list = getJdbcTemplate().queryForList(sb.toString(), new Object[] { userID });
			return list;
		}

		return null;
	}

}
