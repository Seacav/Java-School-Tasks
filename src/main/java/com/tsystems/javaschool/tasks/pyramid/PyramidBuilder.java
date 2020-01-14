package com.tsystems.javaschool.tasks.pyramid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
    	if (inputNumbers.contains(null))
			throw new CannotBuildPyramidException();
		
		double rowNum = getNumberOfRows(inputNumbers.size());
		
		//rowNum should be integer otherwise it's not triangular number
		if (rowNum % 1 == 0) {
			
			Collections.sort(inputNumbers);
			List<Integer> input = new ArrayList<Integer>(inputNumbers);
			
		    int colNum = (int) (rowNum * 2 - 1);
		    int[][] pyramid = new int[(int) rowNum][colNum];
		    
		    for (int i=0; i < rowNum; i++) {
		    	int start = colNum / 2 - i;
		    	for (int col = 0; col < i+1; col++) {
		    		pyramid[i][start] = input.remove(0);
		    		start += 2;
		    	}
		    }
		    return pyramid;
		} else {
			throw new CannotBuildPyramidException();
		}
	}
	
	private double getNumberOfRows(int num) {
		//Triangular number equation is Tn = n*(n+1)/2
		//leads us to equation n^2+n+2*Tn=0
		//discriminant = 1+4*(2*Tn)
		//
		double rowNum = (Math.sqrt(1 + 8*num) - 1) / 2;
		return rowNum;
	}


}
