package com.uapp.similartrello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uapp.similartrello.exception.NotFoundException;
import com.uapp.similartrello.form.GroupForm;
import com.uapp.similartrello.model.Group;
import com.uapp.similartrello.service.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GroupControllerTest extends IntegrationTest {

    private static final int GROUP_ID = 1;

    @MockBean
    private GroupService service;

    static Stream<Arguments> arguments4ValidInput() {
        GroupForm form = createForm();
        return Stream.of(
                Arguments.arguments(get("/group")),
                Arguments.arguments(get("/group/{id}", GROUP_ID)),
                Arguments.arguments(
                        post("/group")
                                .param("id", form.getId().toString())
                                .param("name", form.getName())
                                .param("position", String.valueOf(form.getPosition()))),
                Arguments.arguments(
                        delete("/group")
                                .param("id", form.getId().toString())
                                .param("name", form.getName())
                                .param("position", String.valueOf(form.getPosition())))
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4ValidInput")
    void pathCheck_WhenValidInput_ThenReturn200(RequestBuilder builder) throws Exception {
        when(service.get(any())).thenReturn(List.of(createGroup()));

        doNothing().when(service).save(any());
        doNothing().when(service).delete(any());

        mvc.perform(builder).andExpect(status().isOk());
    }

    static Stream<Arguments> arguments4NotValidInput() {
        return Stream.of(
                Arguments.arguments(post("/group"), status().isBadRequest()),
                Arguments.arguments(delete("/group"), status().isBadRequest())
        );
    }

    @ParameterizedTest
    @MethodSource("arguments4NotValidInput")
    void pathCheck_WhenNotValidInput_ThenReturn400(RequestBuilder builder) throws Exception {
        mvc.perform(builder).andExpect(status().isBadRequest());
    }

    @Test
    void get_WhenValidInput_ThenReturnActualResult() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        when(service.get(List.of(GROUP_ID))).thenReturn(List.of(createGroup()));

        MvcResult mvcResult = mvc.perform(get("/group/{id}", GROUP_ID))
                .andExpect(status().isOk())
                .andReturn();

        Group expectedResult = createGroup();
        String actualResult = mvcResult.getResponse().getContentAsString();

        assertEquals(actualResult, objectMapper.writeValueAsString(expectedResult));
    }

    @Test
    void get_WhenNotExistInput_ThenReturnActualException() throws Exception {
        when(service.get(List.of(GROUP_ID))).thenThrow(NotFoundException.class);

        mvc.perform(get("/group/{id}", GROUP_ID))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
    }

    private static GroupForm createForm() {
        GroupForm form = new GroupForm();
        form.setId(1);
        form.setName("aaa");
        form.setPosition(1);

        return form;
    }

    private static Group createGroup() {
        GroupForm form = createForm();
        Group group = new Group();
        group.setId(form.getId());
        group.setName(form.getName());
        group.setPosition(form.getPosition());

        return group;
    }
}