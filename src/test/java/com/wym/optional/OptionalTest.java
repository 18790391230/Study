package com.wym.optional;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
public class OptionalTest {


    @Test
    public void test1() {

        Optional<String> abc = Optional.of("abc");

        Map<Integer, Optional<String>> map = new HashMap<>();
        map.put(1, abc);
        map.put(2, Optional.empty());

        System.out.println(map.get(1).orElse(null));
        System.out.println(map.get(2).orElse(null));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(LocalDateTime.now().format(dateTimeFormatter));

        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        System.out.println(localDateTime.format(dateTimeFormatter));

        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        Date from = Date.from(zonedDateTime.toInstant());
        System.out.println(from);

    }



}
