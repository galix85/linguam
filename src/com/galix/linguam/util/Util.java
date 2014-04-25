package com.galix.linguam.util;

import java.util.ArrayList;
import java.util.Random;

public class Util {

	public static int randomNumber(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min + 1) + min;
	}

	public static ArrayList<Integer> generateRandomNumber(int min, int max) {
		
		int rnd;
		int[] randNo = new int[max+1];
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int k = min; k <= max; k++) {
			rnd = randomNumber(min,max);

			if (k == min) {
				randNo[min] = rnd;
				numbers.add(randNo[min]);
			} else {
				while (numbers.contains(Integer.valueOf(rnd))) {
					rnd = randomNumber(min,max);
				}
				randNo[k] = rnd;
				numbers.add(randNo[k]);
			}
		}
		return numbers;
	}

}
