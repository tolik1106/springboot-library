package com.zhitar.library.util;

import com.zhitar.library.domain.User;
import com.zhitar.library.dto.BookDto;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtil {

    private ControllerUtil() {}

    public static Map<String, String> getErrorsMap(Class<?> clazz, BindingResult result) {
        Collector<FieldError, ?, Map<String, String>> fieldErrorMapCollector = Collectors.toMap(
                fieldError -> clazz.getSimpleName() + fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return result.getFieldErrors().stream().collect(fieldErrorMapCollector);
    }

    public static boolean checkIsDebtor(Collection<BookDto> books, User owner) {
        for (BookDto bookDto : books) {
            if (bookDto.getExpiredDays() != null
                    && bookDto.getExpiredDays() >= 30
                    && bookDto.getOwnerId() == owner.getId()
            ) {
                return true;
            }
        }
        return false;
    }
}
