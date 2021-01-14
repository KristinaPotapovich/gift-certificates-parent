package com.epam.esm.service.util.impl;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.util.Validation;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidation implements Validation<GiftCertificateDto> {
    private static String CERTIFICATE_NAME_AND_DESCRIPTION_REGEX =
            "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$";
    private static int MAX_SIZE_NAME = 50;
    private static int MAX_SIZE_DESCRIPTION = 250;

    @Override
    public void validate(GiftCertificateDto giftCertificateDto) throws ValidationException {
        if (giftCertificateDto.getName() == null || giftCertificateDto.getName().trim().isEmpty()
                || !giftCertificateDto.getName().matches(CERTIFICATE_NAME_AND_DESCRIPTION_REGEX)
                || giftCertificateDto.getName().length() > MAX_SIZE_NAME) {
            throw new ValidationException("Gift Certificate with wrong name");
        }
        if (giftCertificateDto.getDescription() == null || giftCertificateDto.getDescription().trim().isEmpty()
                || !giftCertificateDto.getDescription().matches(CERTIFICATE_NAME_AND_DESCRIPTION_REGEX)
                || giftCertificateDto.getDescription().length() > MAX_SIZE_DESCRIPTION) {
            throw new ValidationException("Gift Certificate with wrong description");
        }
        if (giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException("Gift Certificate with wrong price");
        }
        if (giftCertificateDto.getDurationInDays() <= 0) {
            throw new ValidationException("Gift Certificate with wrong duration");
        }
    }
}
