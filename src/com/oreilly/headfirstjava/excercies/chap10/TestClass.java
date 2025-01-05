package com.oreilly.headfirstjava.excercies.chap10;

import com.hackerrank.challenge.Test;

public class TestClass {

    public static void main(String[] args) {
        int[] nums = {6,6,2};
        //System.out.println(TestClass.array667(nums));
        System.out.println(TestClass.countXX("abcxxx"));

    }

    public static int array667(int[] nums) {

        int arrSize = nums.length;
        int count = 0;

        if( arrSize < 2){
            return count;
        }

        int num1 = nums[0];
        int num2 = nums[1];

        if( num1 == 6 && num2 == 6){
            count = 1;
            return count;
        } else if( num1 == 6 && num2 == 7){
            count = 1;
            return count;
        }


        for( int idx = 2 ; idx < arrSize ; idx++){
            num1 = nums[idx-1];
            num2 = nums[idx];

            if( num1 == 6 && num2 == 6){
                count+= 1;

            } else if( num1 == 6 && num2 == 7){
                count+= 1;

            }
        }

        return count;
    }


    static int countXX(String str) {


        int finalCnt = 0;
        int strLen = str.length();

        for( int idx = 0 ; idx < strLen ; ){
            int countX = 0;
            for( int idx2 = idx; idx2 < strLen ; idx2++){

                if( str.charAt(idx2) == 'x' ){
                    countX++;
                } else {
                    idx = idx2+1;
                    break;
                }

            }

            finalCnt = countX > 2 ? finalCnt + (countX -1 ) : finalCnt + 1 ;

        }

        return finalCnt;
    }


}
