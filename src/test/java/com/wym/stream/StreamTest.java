package com.wym.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wym.common.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.interfaces.PBEKey;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {


    private ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void toArrayTest() throws JsonProcessingException {

        User user1 = User.builder().age(10).name("张三").build();
        User user2 = User.builder().age(20).name("李四").build();

//        User[] users = Stream.of(user1, user2).toArray(length -> new User[length]);
        User[] users = Stream.of(user1, user2).toArray(User[]::new);

        System.out.println(objectMapper.writeValueAsString(users));
    }

    @Test
    public void toCollectTest() throws JsonProcessingException {

        User user1 = User.builder().age(10).name("张三").build();
        User user2 = User.builder().age(20).name("李四").build();
        List<User> collect = Stream.of(user1, user2).collect(Collectors.toList());
        collect.remove(0);

        System.out.println(objectMapper.writeValueAsString(collect));
    }

    @Test
    public void toCollectTest2() throws Exception {

        Stream<String> stream = Stream.of("aaa", "bbb", "ccc");
//        List<String> list2 = Stream.of("000").collect(Collectors.toList());

        Stream<String> parallel = stream.parallel();
        ArrayList<Object> collect1 = stream.parallel().collect(ArrayList::new, (list, item) -> {
            list.add(item);
        }, (list, list2) -> {

            try {
                System.out.println("list=" + objectMapper.writeValueAsString(list));
                System.out.println("list2=" + objectMapper.writeValueAsString(list2));
            } catch (JsonProcessingException e) {
            }
//            list.addAll(list2);
        });
        System.out.println(objectMapper.writeValueAsString(collect1));

//        ArrayList<String> collect = stream.collect(ArrayList::new, (list, item) -> {
//            list.add(item);
//        }, (list, list2) -> {
//            try {
//                System.out.println("list=" + objectMapper.writeValueAsString(list));
//                System.out.println("list2=" + objectMapper.writeValueAsString(list2));
//            } catch (JsonProcessingException e) {}
//            list.addAll(list2);
//        });
//        System.out.println(objectMapper.writeValueAsString(collect));
//        ArrayList<String> collect = stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.in.read();
    }

    @Test
    public void toCollectTest3() {

        Stream<String> stringStream = Stream.of("111", "222");

        String concat = stringStream.collect(StringBuilder::new, StringBuilder::append,
                StringBuilder::append)
                .toString();
        System.out.println(concat);
    }

    @Test
    public void toCollectTest4() {
        User user1 = User.builder().age(10).name("张三").build();
        User user2 = User.builder().age(20).name("李四").build();
        List<User> collect = Stream.of(user1, user2).collect(Collectors.toCollection(LinkedList::new));
    }

    @Test
    public void toCollectTest5() throws JsonProcessingException {
        User user1 = User.builder().age(10).name("zhangsan").build();
        User user2 = User.builder().age(20).name("lisi").build();
        User user3 = User.builder().age(20).name("wangwu").build();

        //set
        Set<String> set = Stream.of(user1, user2, user3).map(User::getName).collect(Collectors.toCollection(TreeSet::new));
        System.out.println("set=" + objectMapper.writeValueAsString(set));

        //拼接字符串
        String concatString = Stream.of(user1, user2, user3).map(User::getName).collect(Collectors.joining(","));
        System.out.println("concatString=" + concatString);

        //求总和
        int total = Stream.of(user1, user2, user3).mapToInt(User::getAge).sum();
        System.out.println("total=" + total);

        //求整数平均值
        int avg = Stream.of(user1, user2, user3).collect(Collectors.averagingInt(User::getAge)).intValue();
        System.out.println("avg=" + avg);

        //求小数平均值
        double doubleAvg = Stream.of(user1, user2, user3).collect(Collectors.averagingDouble(User::getAge));
        System.out.println("doubleAvg=" + doubleAvg);

        //分组
        Map<Integer, List<User>> map1 = Stream.of(user1, user2, user3).collect(Collectors.groupingBy(User::getAge));
        System.out.println("map1=" + objectMapper.writeValueAsString(map1));

        //分组后再分组
        Map<Integer, Map<Integer, List<User>>> map2 = Stream.of(user1, user2, user3).collect(Collectors.groupingBy(User::getAge, Collectors.groupingBy(User::getAge)));
        System.out.println("map2=" + objectMapper.writeValueAsString(map2));

    }

    /**
     * 拼接2个list
     * @throws JsonProcessingException
     */
    @Test
    public void combineTest() throws JsonProcessingException {

//        List<String> list1 = new ArrayList<>();
//        list1.add("111");
//        list1.add("222");
        List<String> list1 = null;

//        List<String> list2 = new ArrayList<>();
        List<String> list2 = null;

        Stream<List<String>> stream = Stream.of(list1, list2);

        List<String> collect = stream.collect(ArrayList::new, (list, item)->{
            if (item != null) {
                list.addAll(item);
            }
        }, ArrayList::addAll);

        System.out.println(objectMapper.writeValueAsString(collect));

    }

    @Test
    public void mapAndReduceTest() throws JsonProcessingException {
        User user1 = User.builder().age(10).name("张三").build();
        User user2 = User.builder().age(20).name("李四").build();

        Integer reduce = Stream.of(user1, user2).map(User::getAge).reduce(0, Integer::sum);

        System.out.println("reduce=" + reduce);
    }

    @Test
    public void intStreamRangeTest() throws JsonProcessingException {

        int[] ints = IntStream.range(3, 10).filter(e -> e % 2 == 0).toArray();

        System.out.println(Arrays.toString(ints));
    }

    @Test
    public void flatMapTest() throws JsonProcessingException {

        Stream<List<Integer>> stream = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4, 5));

        List<Integer> collect = stream.flatMap(list -> list.stream()).map(e -> e * e).collect(Collectors.toList());

        System.out.println(objectMapper.writeValueAsString(collect));

    }

    @Test
    public void iterate() {

        //后面的每一个操作执行： e -> e + 2
        Stream.iterate(1, e -> e + 2).limit(10).forEach(System.out::println);

    }

    @Test
    public void skipAndLimitTest() {

        Stream<String> stream = Stream.of("111", "222", "333", "444");

        stream.filter(e -> e.length() > 2).map(e -> e + " EOF").skip(2).limit(3).collect(Collectors.toList()).forEach(System.out::println);

    }

    /**
     * 统计信息
     * @throws JsonProcessingException
     */
    @Test
    public void summaryStatistics() throws JsonProcessingException {
        Stream<Integer> stream = Stream.of(111, 222, 333, 444);

        IntSummaryStatistics statistics = stream.filter(e -> e > 2).mapToInt(e -> e * e).skip(2).limit(3).summaryStatistics();

        System.out.println(objectMapper.writeValueAsString(statistics));
    }

    /**
     * 转换大小写并排序
     */
    @Test
    public void practice1() {

        List<String> list = Stream.of("aaa", "bbb", "ccc").collect(Collectors.toList());

        list.stream().map(String::toUpperCase).sorted(Comparator.reverseOrder()).collect(Collectors.toList()).forEach(System.out::println);

    }

    @Test
    public void practice2() {

        System.out.println(Stream.of(111, 222, 333, 444).allMatch(e -> e > 100));

        System.out.println(Stream.of(111, 222, 333, 444).anyMatch(e -> e > 100));

        Stream<Integer> stream = Stream.of(111, 222, 333, 444, 111);

        Integer[] intArr = Stream.of(111, 222, 333, 444, 111).toArray(Integer[]::new);
        System.out.println(Arrays.toString(intArr));

        System.out.println(Stream.of(111, 222, 333, 444, 111).count());

        List<Integer> list = stream.distinct().collect(Collectors.toList());
        System.out.println(list.stream().map(String::valueOf).collect(Collectors.joining(",")));

        Stream.of(111, 222, 333, 444, 111).filter(e -> e > 200).findFirst().ifPresent(System.out::println);

        Stream.of(Arrays.asList(111, 222, 333), Arrays.asList(444, 111)).flatMap(Collection::stream).distinct().collect(Collectors.toList()).forEach(System.out::println);

        List<Double> collect = Stream.of(111, 222, 333, 444, 111).mapToDouble(Integer::doubleValue).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        System.out.println(Stream.of(111, 222, 333, 444, 111).noneMatch(e -> e > 1000));
        System.out.println(Stream.of(111, 222, 333, 444, 111).noneMatch(e -> e < 1000));
    }

    @Test
    public void reduceTest() {

        System.out.println(Stream.of(111, 222, 333, 444, 111).reduce(0, Integer::sum));

        Stream.of(111, 222, 333, 444, 111).reduce(Integer::sum).ifPresent(System.out::println);

    }













}
