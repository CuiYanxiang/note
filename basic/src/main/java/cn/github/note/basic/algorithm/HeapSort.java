package cn.github.note.basic.algorithm;

import java.util.Arrays;

public class HeapSort {
    public static void main(String[] args) {
        int[] arr = {6, 1, 5, 3, 2, 5, 7, 9};
        headSort(arr);
        Arrays.stream(arr).forEach(System.out::println);
    }

    static void headSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i);
        }
        int heapSize = arr.length;
        swap(arr, 0, --heapSize);
        while (heapSize > 0) {
            heapify(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
        }
    }

    static void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1;
        while (left < heapSize) {
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);
            index = largest;
            left = index * 2 + 1;
        }

    }

    static void heapInsert(int[] arr, int index) {
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    static void swap(int[] arr, int j, int k) {
        int tmp = arr[j];
        arr[j] = arr[k];
        arr[k] = tmp;
    }
}
