package com.epam.esm.controller;

import com.epam.esm.config.RootConfig;
import com.epam.esm.entity.Tag;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/tag",
        produces = APPLICATION_JSON_VALUE)
public class TagController {
    ApplicationContext javaConfigContext =
            new AnnotationConfigApplicationContext(RootConfig.class);
    @GetMapping(value = "/{id}")
    public Tag findTag(@PathVariable("id") long id) {
        return javaConfigContext.getBean(Tag.class);
    }

    @GetMapping
    public ResponseEntity<Tag> findAllTags() {
        Tag tag = javaConfigContext.getBean(Tag.class);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    Tag createTag(@RequestBody Tag tag) {
        return tag;
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTag(@PathVariable("id") long id) {
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable("id") long id) {

    }

}
