package com.tsystems.javaschool.tasks.subsequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List inputX, List inputY) {
		
		if (inputX == null || inputY == null)
			throw new IllegalArgumentException();
		
		if (inputX.size() > inputY.size())
			return false;
		
		if (inputX.isEmpty())
			return true;
		
		List x = new ArrayList(inputX);
		List y = new ArrayList(inputY);
		
		Iterator iteratorX = x.iterator();
		Iterator iteratorY = y.iterator();
		
		String valueX = String.valueOf(iteratorX.next());
		
		while(!x.isEmpty() && !y.isEmpty()) {
			String valueY = String.valueOf(iteratorY.next());
			if (valueX.equals(valueY) && iteratorX.hasNext()) {
				iteratorX.remove();
				valueX = String.valueOf(iteratorX.next());
			}
			iteratorY.remove();
		}
		iteratorX.remove();
		if (x.isEmpty())
			return true;
		
		return false;
	}
}
