package com.epam.esm.controller;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Tag tag) {
        if(!tagService.create(tag)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Tag with this name already exists");
        }
    }

    @GetMapping(value = "/{id}")
    public Tag findById(@PathVariable(name = "id") long id) {
        return tagService.find(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "There is no tag with id " + id)
        );
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") long id) {
        if(!tagService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete tag with id" + id);
        }
    }
}
