package com.guavabot.algo1;

import java.util.List;

public class Utils {

	static int[] listToIntArray(List<Integer> list)  {
	    int[] array = new int[list.size()];
	    for (int i = 0; i < list.size(); i++) {
	    	array[i] = list.get(i);
	    }
	    return array;
	}

}
