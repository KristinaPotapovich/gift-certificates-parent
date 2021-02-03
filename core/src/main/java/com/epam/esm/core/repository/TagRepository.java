package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;

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
     */
    Optional<Tag> findTagById(long id);

    /**
     * Find all tags by certificate id list.
     *
     * @param idCertificate the id certificate
     * @param page          the page
     * @param size          the size
     * @return the list
     */
    List<Tag> findAllTagsByCertificateId(long idCertificate, int page, int size);

    /**
     * Find popular tag tag.
     *
     * @return the tag
     */
    Tag findPopularTag();
    List<Tag> findAllTags(int page, int size);
}
