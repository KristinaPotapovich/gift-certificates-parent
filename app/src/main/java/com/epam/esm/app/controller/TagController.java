package com.epam.esm.app.controller;


import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type Tag controller.
 */
@RestController
@RequestMapping(path = "/tags",
        produces = APPLICATION_JSON_VALUE)
public class TagController {

    private TagService tagServiceImpl;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagServiceImpl the tag service
     */
    @Autowired
    public TagController(TagService tagServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
    }

    /**
     * Find tag by name response entity.
     *
     * @param name the name
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping(value = "/query")
    public ResponseEntity<TagDto> findTagByName(@Valid @QueryParam("name") String name) throws ControllerException {
        try {
            return tagServiceImpl.findTagByName(name)
                    .map(tagDtoResponse -> new ResponseEntity<>(tagDtoResponse, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Find all tags response entity.
     *
     * @return the response entity
     * @throws ControllerException the controller exception
     */
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

    /**
     * Create tag response entity.
     *
     * @param tagDto the tag dto
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @PostMapping
    public @ResponseBody
    ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto tagDto) throws ControllerException {
        try {
            return tagServiceImpl
                    .create(tagDto)
                    .map(tagDtoResponse -> new ResponseEntity<>(tagDtoResponse, HttpStatus.CREATED))
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Update tag.
     *
     * @param id     the id
     * @param tagDto the tag dto
     * @throws ControllerException the controller exception
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateTag(@Valid @PathVariable("id") long id,
                          @Valid @RequestBody TagDto tagDto) throws ControllerException {
        try {
            tagDto.setId(id);
            tagServiceImpl.update(tagDto);
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Delete tag response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@Valid @PathVariable("id") long id) throws ControllerException {
        try {
            tagServiceImpl.delete(id);
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
    @GetMapping(value = "/queryPopularTag")
    public ResponseEntity<TagDto> findPopularTag(){
        return tagServiceImpl
                .findPopularTag()
                .map(tagDto -> new ResponseEntity<>(tagDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
