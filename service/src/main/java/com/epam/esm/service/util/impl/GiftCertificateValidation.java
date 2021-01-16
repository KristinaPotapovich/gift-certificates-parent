package com.epam.esm.service.util.impl;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.util.Validation;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidation implements Validation<GiftCertificateDto> {
    private static final String WRONG_NAME = "validation_certificate_wrong_name";
    private static final String WRONG_DESCRIPTION = "validation_certificate_wrong_description";
    private static final String WRONG_PRICE = "validation_certificate_wrong_price";
    private static final String WRONG_DURATION = "validation_certificate_wrong_duration";
    private static String CERTIFICATE_NAME_AND_DESCRIPTION_REGEX =
            "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$";
    private static int MAX_SIZE_NAME = 50;
    private static int MAX_SIZE_DESCRIPTION = 250;

    @Override
    public void validate(GiftCertificateDto giftCertificateDto) throws ValidationException {
        if (giftCertificateDto.getName() == null || giftCertificateDto.getName().trim().isEmpty()
                || !giftCertificateDto.getName().matches(CERTIFICATE_NAME_AND_DESCRIPTION_REGEX)
                || giftCertificateDto.getName().length() > MAX_SIZE_NAME) {
            throw new ValidationException(WRONG_NAME);
        }
        if (giftCertificateDto.getDescription() == null || giftCertificateDto.getDescription().trim().isEmpty()
                || !giftCertificateDto.getDescription().matches(CERTIFICATE_NAME_AND_DESCRIPTION_REGEX)
                || giftCertificateDto.getDescription().length() > MAX_SIZE_DESCRIPTION) {
            throw new ValidationException(WRONG_DESCRIPTION);
        }
        if (giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(WRONG_PRICE);
        }
        if (giftCertificateDto.getDurationInDays() <= 0) {
            throw new ValidationException(WRONG_DURATION);
        }
    }
}
