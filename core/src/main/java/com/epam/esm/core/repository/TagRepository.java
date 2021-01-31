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
     * @param id the id
     * @return the optional
     * @throws RepositoryException the repository exception
     */
    Optional<Tag> findTagById(long id) throws RepositoryException;

    /**
     * Find all tags by certificate id list.
     *
     * @param idCertificate the id certificate
     * @param page          the page
     * @param size          the size
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<Tag> findAllTagsByCertificateId(long idCertificate, int page, int size) throws RepositoryException;

    /**
     * Find popular tag tag.
     *
     * @return the tag
     */
    Tag findPopularTag();
}
