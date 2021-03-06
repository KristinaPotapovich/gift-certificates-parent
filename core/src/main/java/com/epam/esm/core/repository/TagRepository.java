package com.epam.esm.core.repository;

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
     * Find popular tag tag.
     *
     * @return the tag
     */
    Tag findPopularTag();

    List<Tag> findAllTags(int page, int size);
}
