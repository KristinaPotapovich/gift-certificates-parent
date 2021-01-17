package com.epam.esm.service.mapper;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.core.entity.Tag;

/**
 * The type Tag converter.
 */
public class TagConverter {
    /**
     * Map to tag dto tag dto.
     *
     * @param tag the tag
     * @return the tag dto
     */
    public static TagDto mapToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    /**
     * Map to tag tag.
     *
     * @param tagDto the tag dto
     * @return the tag
     */
    public static Tag mapToTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        tag.setName(tagDto.getName());
        return tag;
    }
}
