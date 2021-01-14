package com.epam.esm.core.repository;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;


public interface TagRepository extends BaseRepository<Tag> {
    Tag findTagByName(String name) throws RepositoryException;

    boolean isTagExistByName(String name) throws RepositoryException;
}
