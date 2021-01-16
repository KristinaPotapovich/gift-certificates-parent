package com.epam.esm.core.repository;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;

import java.util.List;
import java.util.Optional;


public interface TagRepository extends BaseRepository<Tag> {
    Optional<Tag> findTagByName(String name) throws RepositoryException;

    List<Tag> findAllTagsByCertificateId(long idCertificate) throws RepositoryException;
}
