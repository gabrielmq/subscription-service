package io.github.gabrmsouza.subscription.domain.utils;

import java.util.UUID;

public interface IDUtils {
    static String uuid() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }
}
