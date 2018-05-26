package com.zebrait;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyTest {
    public void quickSort(int[] nums, int start, int end) {
        if (nums == null) {
            throw new IllegalArgumentException();
        }
        if (end <= start) {
            return;
        }
        int border = start;
        int pivot = nums[start];
        for (int i = start; i <= end; ++i) {
            if (nums[i] < pivot) {
                swap(nums, i, ++border);
            }
        }
        swap(nums, start, border);
        quickSort(nums, start, border - 1);
        quickSort(nums, border + 1, end);

    }


    private void swap(int[] nums, int x, int y) {
        int n = nums[x];
        nums[x] = nums[y];
        nums[y] = n;
    }

    @Test
    public void test() {
        int[] nums = new int[]{4, 5, 2, 6, 1, 7, 9, 0};
        //quickSort(nums, 0, nums.length - 1);
        //mergeSort(nums);
        //qsort(nums, 0, nums.length - 1);
        //Arrays.stream(nums).forEach(System.out::println);
        System.out.println(longestSeries(nums));
        //System.out.println(lcs("a","ac"));
    }

    public void mergeSort(int[] nums) {
        int length = nums.length;
        if (length <= 1) return;
        int leftLength = length / 2;
        int rightLength = length - leftLength;
        int[] left = new int[leftLength];
        int[] right = new int[rightLength];
        for (int i = 0; i < leftLength; ++i) {
            left[i] = nums[i];
        }
        for (int i = 0; i < rightLength; ++i) {
            right[i] = nums[i + leftLength];
        }
        mergeSort(left);
        mergeSort(right);
        int leftIndex = 0;
        int rightIndex = 0;
        for (int i = 0; i < length; ++i) {
            if (leftIndex >= leftLength) {
                nums[i] = right[rightIndex++];
            } else if (rightIndex >= rightLength) {
                nums[i] = left[leftIndex++];
            } else if (left[leftIndex] > right[rightIndex]) {
                nums[i] = left[leftIndex++];
            } else {
                nums[i] = right[rightIndex++];
            }
        }
    }

    public void qsort(int[] nums, int start, int end) {
        if (nums == null || start < 0 || end >= nums.length) {
            throw new IllegalArgumentException();
        }
        if (end <= start) {
            return;
        }
        int pivot = nums[start];
        int border = start;
        for (int i = start; i <= end; ++i) {
            if (nums[i] < pivot) {
                swap(nums, i, ++border);
            }
        }
        swap(nums, border, start);
        qsort(nums, start, border - 1);
        qsort(nums, border + 1, end);
    }

    public int longestSeries(int[] nums) {
        int[] lens = new int[nums.length];
        for (int i = 0; i < lens.length; ++i) {
            lens[i] = 1;
        }
        int max = 0;
        for (int i = 0; i < nums.length; ++i) {
            for (int j = 0; j < i; ++j) {
                if (nums[j] < nums[i] && lens[j] + 1 > lens[i]) {
                    lens[i] = lens[j] + 1;
                    if (max < lens[i]) {
                        max = lens[i];
                    }
                }
            }
        }
        return max;
    }

    public int lcs(String a, String b) {
        int aLength = a.length();
        int bLength = b.length();
        int[][] lens = new int[aLength + 1][bLength + 1];
        for (int i = 1; i <= aLength; ++i) {
            for (int j = 1; j <= bLength; ++j) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    lens[i][j] = lens[i - 1][j - 1] + 1;
                } else if (lens[i - 1][j] > lens[i][j - 1]) {
                    lens[i][j] = lens[i - 1][j];
                } else {
                    lens[i][j] = lens[i][j - 1];
                }
            }
        }
        return lens[aLength][bLength];
    }

    @Test
    public void test4() {
    }
}
