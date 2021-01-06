package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepository implements BaseRepository<Tag> {
    @Override
    public Tag create(Tag tag) {
        return null;
    }

    @Override
    public Tag update(Tag tag) {
        return null;
    }

    @Override
    public void delete(Tag tag) {

    }
    public Tag findTag(Tag tag) {
        return null;
    }
}
