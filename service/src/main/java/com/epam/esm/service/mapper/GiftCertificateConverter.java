package com.epam.esm.service.mapper;


import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.service.dto.TagDto;

import java.util.List;
import java.util.stream.Collectors;


public class GiftCertificateConverter {
    public static GiftCertificateDto mapToGiftCertificateDto(GiftCertificate giftCertificate) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setId(giftCertificate.getId());
        giftCertificateDto.setName(giftCertificate.getName());
        giftCertificateDto.setDescription(giftCertificate.getDescription());
        giftCertificateDto.setPrice(giftCertificate.getPrice());
        giftCertificateDto.setDurationInDays(giftCertificate.getDurationInDays());
        giftCertificateDto.setCreateDate(giftCertificate.getCreateDate());
        giftCertificateDto.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        List<TagDto> tagDto = giftCertificate.getTags()
                .stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList());
        giftCertificateDto.setTags(tagDto);
        return giftCertificateDto;
    }

    public static GiftCertificate mapToGiftCertificate(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(giftCertificateDto.getId());
        giftCertificate.setName(giftCertificateDto.getName());
        giftCertificate.setDescription(giftCertificateDto.getDescription());
        giftCertificate.setPrice(giftCertificateDto.getPrice());
        giftCertificate.setDurationInDays(giftCertificateDto.getDurationInDays());
        giftCertificate.setCreateDate(giftCertificateDto.getCreateDate());
        giftCertificate.setLastUpdateDate(giftCertificateDto.getLastUpdateDate());
        giftCertificate.setTags(giftCertificateDto.getTags()
                .stream()
                .map(TagConverter::mapToTag)
                .collect(Collectors.toList()));
        return giftCertificate;
    }
}
