package com.github.dylmeadows.common.util;

import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Optional;

public interface LocalizedEnum {

    @NonNull
    static <T extends Enum<T> & LocalizedEnum> Optional<T> findByLocalizedValue(@NonNull Class<T> enumType, @NonNull String i18n) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter($enum -> Optional.of($enum)
                        .map(LocalizedEnum::getLocalizedValue)
                        .filter(i18n::equals)
                        .isPresent())
                .findFirst();
    }

    @NonNull
    String getLocalizedValue();
}
