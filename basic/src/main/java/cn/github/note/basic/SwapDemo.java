package cn.github.note.basic;

public class SwapDemo {

    public static void main(String[] args) {
//        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
//        swap(arr, 2, 5);

        int[] arr2 = {1, 2, 2, 1, 1, 3, 1, 3, 3};
        oddNumber(arr2);
    }

    /**
     * 数组内两值交换
     *
     * @param arr
     * @param i
     * @param j
     */
    static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    /**
     * 从数组获取唯一的数出现奇数次
     *
     * @param arr
     */
    static void oddNumber(int[] arr) {
        int i = 0;
        for (int cur : arr) {
            i ^= cur;
        }
        System.out.println(i);
    }
}

