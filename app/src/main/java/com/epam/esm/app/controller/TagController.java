package com.epam.esm.app.controller;


import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.services.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.ws.rs.QueryParam;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/tags",
        produces = APPLICATION_JSON_VALUE)
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/query")
    public ResponseEntity<TagDto> findTagByParam(@QueryParam("name") String name) {
        return tagService.findTagByName(name)
                .map(tagDto -> new ResponseEntity<>(tagDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<TagDto>> findAllTags() {
        return tagService.findAllTags().map(tagDto -> new ResponseEntity<>(tagDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        return tagService.create(tagDto).map(tagDto1 -> new ResponseEntity<>(tagDto1, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping(value = "/{id}")
    public @ResponseBody
    ResponseEntity<TagDto> updateTag(@PathVariable("id") long id, @RequestBody TagDto tagDto) {
        tagDto.setId(id);
        return tagService.update(tagDto).map(tagDto1 -> new ResponseEntity<>(tagDto1, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteTag(@PathVariable("id") long id) {
        boolean delete = tagService.delete(id);
        if (!delete) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
