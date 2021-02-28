package com.urise.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainStream {

    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));

        System.out.println(oddOrEven(List.of(5, 7, 3, 9, 11, 17, 2, 4, 6, 8)));
        System.out.println(oddOrEven(List.of(7, 3, 9, 11, 17, 2, 4, 6, 8)));
    }

    public static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (left, right) -> left * 10 + right);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        AtomicInteger sum = new AtomicInteger();
        return integers.stream()
                .peek(sum::addAndGet)
                .collect(Collectors.partitioningBy(element -> (element % 2 == 0)))
                .get(sum.intValue() % 2 != 0);
    }
}
