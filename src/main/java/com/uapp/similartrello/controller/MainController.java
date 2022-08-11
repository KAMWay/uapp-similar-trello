package com.uapp.similartrello.controller;

import com.uapp.similartrello.model.Group;
import com.uapp.similartrello.model.Task;
import com.uapp.similartrello.service.logic.GroupingTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@Api("Main controller")
public class MainController {

    private final GroupingTaskService groupingTaskService;

    public MainController(GroupingTaskService groupingTaskService) {
        this.groupingTaskService = groupingTaskService;
    }

    @GetMapping("/")
    @ApiOperation("Main page")
    public ModelAndView index() {
        final Map<Group, List<Task>> groups = groupingTaskService.getGroupingTaskMap();

        return new ModelAndView("index")
                .addObject("groups", groupingTaskService.getSortedGroupingTaskMap(groups));
    }
}
