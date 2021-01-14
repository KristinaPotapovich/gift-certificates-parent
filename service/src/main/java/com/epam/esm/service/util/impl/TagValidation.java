package com.epam.esm.service.util.impl;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.util.Validation;
import org.springframework.stereotype.Component;

@Component("tagValidation")
public class TagValidation implements Validation<TagDto> {
    private static String TAG_NAME_REGEX = "[a-zA-Zа-яА-Я]+";
    private static int MAX_SIZE_NAME = 50;

    @Override
    public void validate(TagDto tagDto) throws ValidationException {
        if (tagDto.getName() == null || tagDto.getName().trim().isEmpty() ||
                !tagDto.getName().matches(TAG_NAME_REGEX) || tagDto.getName().length() > MAX_SIZE_NAME) {
            throw new ValidationException("Tag with wrong name");
        }
    }
}
