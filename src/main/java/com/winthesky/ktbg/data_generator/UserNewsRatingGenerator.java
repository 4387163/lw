package com.winthesky.ktbg.data_generator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class UserNewsRatingGenerator {

	public String randomUserID() {
		Random random = new Random();
		int r = random.nextInt(20) + 1;
		return StringUtils.leftPad(r + "", 5, "0");
	}

	public String randomNewsID() {
		Random random = new Random();
		int r = random.nextInt(100) + 1;
		return StringUtils.leftPad(r + "", 5, "0");
	}

	public int randomRating() {
		Random random = new Random();
		int r = random.nextInt(10) + 1;
		return r;
	}

	public static void main(String[] args) throws Exception {
		File file = new File("./data/user_news_rating.txt");
		FileOutputStream fos = new FileOutputStream(file);
		UserNewsRatingGenerator g = new UserNewsRatingGenerator();
		Map<String, Map<String, Integer>> filter = new HashMap<String, Map<String, Integer>>();
		for (int i = 0; i < 500; i++) {
			String userID = g.randomUserID();
			String newsID = g.randomNewsID();
			int rating = g.randomRating();
			Map<String, Integer> userratings = filter.get(userID);
			if (userratings == null) {
				userratings = new HashMap<String, Integer>();
				filter.put(userID, userratings);
			}

			if (userratings.containsKey(newsID)) {
				continue;
			}

			userratings.put(newsID, rating);
			String str = userID + "," + newsID + "," + rating;
			fos.write((str + "\n").getBytes());
		}
		fos.close();
	}
}
