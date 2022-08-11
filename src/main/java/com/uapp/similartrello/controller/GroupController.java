package com.uapp.similartrello.controller;

import com.uapp.similartrello.exception.NotFoundException;
import com.uapp.similartrello.exception.ValidateException;
import com.uapp.similartrello.form.GroupForm;
import com.uapp.similartrello.model.Group;
import com.uapp.similartrello.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("group")
@Api("Groups REST controller")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation("Get all groups")
    public List<Group> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get group by id")
    public Group get(@PathVariable Integer id) {
        return service.get(List.of(id)).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Not found group by id " + id));
    }

    @PostMapping
    @ApiOperation("Save group")
    public Group save(@ModelAttribute("form") @Valid GroupForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.getAllErrors());
        }

        final Group group = getGroup(form);
        service.save(group);

        return group;
    }

    @DeleteMapping
    @ApiOperation("Delete group")
    public void delete(@ModelAttribute("form") @Valid GroupForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidateException(bindingResult.getAllErrors());
        }

        if (form.getId() == null) {
            throw new NotFoundException("Can't delete group without id");
        }

        service.delete(getGroup(form));
    }

    private Group getGroup(GroupForm form) {
        final Group group = new Group();
        group.setId(form.getId());
        group.setName(form.getName());
        group.setPosition(form.getPosition());

        return group;
    }
}
