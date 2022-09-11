package ru.ylab.alekseev_sergei.second_lectire;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }
    }

    private static Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia"),
    };

    public static void main(String[] args) {
        int[] array = {3, 4, 2, 7};
        int target = 10;

        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println("\n**************************************************");
        System.out.println("Remove duplicates, sort by id, group by name:\n");

        newRowData(RAW_DATA);

        System.out.println("\n**************************************************");
        System.out.println("Sum of two:\n");

        System.out.println(Arrays.toString(sumOfTwo(array, target)));

        System.out.println("\n**************************************************");
        System.out.println("Fuzzy search:\n");

        System.out.println(fuzzySearch("car", "ca6$$#_rtwheel")); // true
        System.out.println(fuzzySearch("cwhl", "cartwheel")); // true
        System.out.println(fuzzySearch("cwhee", "cartwheel")); // true
        System.out.println(fuzzySearch("cartwheel", "cartwheel")); // true
        System.out.println(fuzzySearch("cwheeel", "cartwheel")); // false
        System.out.println(fuzzySearch("lw", "cartwheel")); // false
    }

    private static void newRowData(Person[] data) {
        Arrays.stream(data)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(Person::getName).thenComparing(Person::getId))
                .collect(Collectors.groupingBy(Person::getName, LinkedHashMap::new, Collectors.counting()))
                .forEach((key, value) -> System.out.println("Key: " + key + "\n Value: " + value));
    }

    //   O(n^2)
//    private static int[] sumOfTwo(int[] array, int target) {
//        int[] result = new int[2];
//        if (Objects.nonNull(array)) {
//            for (int i = 0; i < array.length; i++) {
//                for (int j = i + 1; j < array.length; j++) {
//                    if (array[i] + array[j] == target) {
//                        result[0] = array[i];
//                        result[1] = array[j];
//                    }
//                }
//            }
//        }
//        return result;
//    }

    //  O(n)
    private static int[] sumOfTwo(int[] array, int target) {
        if (Objects.nonNull(array)) {
            Map<Integer, Integer> map = new HashMap<>();

            for (int i = 0; i < array.length; i++) {
                map.put(array[i], i);
            }

            for (int i = 0; i < array.length; i++) {
                int temp = target - array[i];
                if (map.containsKey(temp) && map.get(temp) != i) {
                    return new int[]{array[i], temp};
                }
            }
        }
        return new int[0];
    }

    private static boolean fuzzySearch(String target, String str) {
        if (target.length() > str.length() || target.isBlank()) {
            return false;
        }

        for (char ch : target.toCharArray()) {
            int i = str.indexOf(ch);
            if (i == -1) {
                return false;
            }
            str = str.substring(str.indexOf(ch) + 1);
        }
        return true;
    }
}
