package com.epam.esm.app.controller;


import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.services.TagService;
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

    private TagService tagServiceImpl;

    @Autowired
    public TagController(TagService tagServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
    }

    @GetMapping(value = "/query")
    public ResponseEntity<TagDto> findTagByName(@QueryParam("name") String name) throws ControllerException {
        try {
            return tagServiceImpl.findTagByName(name)
                    .map(tagDtoResponse -> new ResponseEntity<>(tagDtoResponse, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<TagDto>> findAllTags() throws ControllerException {
        try {
            return tagServiceImpl
                    .findAll()
                    .map(tagDto -> new ResponseEntity<>(tagDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) throws ControllerException {
        try {
            return tagServiceImpl
                    .create(tagDto)
                    .map(tagDtoResponse -> new ResponseEntity<>(tagDtoResponse, HttpStatus.CREATED))
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateTag(@PathVariable("id") long id,
                          @RequestBody TagDto tagDto) throws ControllerException {
        try {
            tagDto.setId(id);
            tagServiceImpl.update(tagDto);
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteTag(@PathVariable("id") long id) throws ControllerException {
        try {
            if (tagServiceImpl.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }

    }
}
