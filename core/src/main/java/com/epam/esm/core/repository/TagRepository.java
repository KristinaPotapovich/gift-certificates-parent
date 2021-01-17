package com.epam.esm.core.repository;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;

import java.util.List;
import java.util.Optional;


/**
 * The interface Tag repository.
 */
public interface TagRepository extends BaseRepository<Tag> {
    /**
     * Find tag by name optional.
     *
     * @param name the name
     * @return the optional
     * @throws RepositoryException the repository exception
     */
    Optional<Tag> findTagByName(String name) throws RepositoryException;

    /**
     * Find all tags by certificate id list.
     *
     * @param idCertificate the id certificate
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<Tag> findAllTagsByCertificateId(long idCertificate) throws RepositoryException;
}
