package com.galix.linguam.util;

import java.util.ArrayList;

import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import android.graphics.Point;
import java.util.Random;

import android.annotation.TargetApi;
import android.content.Context;

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

	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getWidth(Context ctxt){

		int width=0;
		
	    WindowManager wm = (WindowManager) ctxt.getSystemService(Context.WINDOW_SERVICE);
	    Display display = wm.getDefaultDisplay();
	    
	    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB){                   
	        Point size = new Point();
	        display.getSize(size);
	        width = size.x;
	    }
	    else{
	        width = display.getWidth();  // deprecated
	    }
	    return width;
		
	}
}
