package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.impl.TagRepository;
import com.epam.esm.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService implements BaseService<Tag> {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Optional<Tag> create(Tag tag) {
        return Optional.empty();
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        return Optional.empty();
    }

    @Override
    public Optional<Tag> delete(Tag tag) {
        return Optional.empty();
    }
}
