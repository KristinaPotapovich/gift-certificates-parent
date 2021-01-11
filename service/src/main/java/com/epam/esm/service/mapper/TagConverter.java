package com.epam.esm.service.mapper;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.core.entity.Tag;

public class TagConverter {
    public static TagDto mapToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    public static Tag mapToTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        tag.setName(tagDto.getName());
        return tag;
    }
}
