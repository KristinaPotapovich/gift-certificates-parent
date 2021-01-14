package com.epam.esm.service.services;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService extends BaseService<GiftCertificateDto> {
    Optional<GiftCertificateDto> findCertificateById(long id) throws ServiceException;

    Optional<List<GiftCertificateDto>> findCertificateByParam(String param) throws ServiceException;
}
