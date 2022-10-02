package cn.github.note.basic.algorithm;

public class SwapDemo {

    public static void main(String[] args) {
//        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
//        swap(arr, 2, 5);

        int[] arr2 = {1, 2, 2, 1, 1, 3, 1, 3, 3};
//        oddNumber(arr2);

        int[] arr3 = {1, 2, 2, 1, 3, 3, 3, 5, 5, 5};

        printOddTimeNumber2(arr3);
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

    /**
     * 从数组获取两个数出现奇数次
     *
     * @param arr
     */
    static void printOddTimeNumber2(int[] arr) {
        int eor = 0;
        for (int i : arr) {
            eor ^= i;
        }
        // eor = a ^ b
        // eor != 0
        // eor 必然有一个位置上是1
        int rightOne = eor & (~eor + 1); //取出最右边的1
        int onlyOne = 0;
        for (int cur : arr) {
            if ((cur & rightOne) == rightOne) {
                onlyOne ^= cur;
            }
        }
        System.out.println(onlyOne + " " + (eor ^ onlyOne));
    }
}

