package com.wym;

import java.util.Arrays;

/**
 * 将2个有序数组合并到一个新的数组中，同时保证升序
 */
public class MergeTwoSortedArrays {

    public static void main(String[] args) {
        int[] res = merge(new int[]{1, 3, 5, 7}, new int[]{1, 2, 6, 8});
        System.out.println(Arrays.toString(res));
    }

    public static int[] merge(int[] a, int[] b) {

        int[] res = new int[a.length + b.length];
        int i = 0, j = 0, index = 0;
        while (i < a.length && j < b.length) {
            res[index++] = a[i] < b[j] ? a[i++] : b[j++];
        }
        while (i < a.length) {
            res[index++] = a[i++];
        }
        while (j < b.length) {
            res[index++] = b[j++];
        }

        return res;
    }
}
