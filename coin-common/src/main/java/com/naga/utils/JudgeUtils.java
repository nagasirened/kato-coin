package com.naga.utils;

import java.util.Objects;

public class JudgeUtils {

    public static void checkNotNull(Object... args) {
        for (Object arg : args) {
            if (Objects.isNull(arg)) {
                throw new RuntimeException("内容不存在");
            }
        }
    }
}
