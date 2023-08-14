package br.com.compasso.posthistoryapi.controller.impl;

import br.com.compasso.posthistoryapi.client.PostClient;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.services.PostHistoryService;
import br.com.compasso.posthistoryapi.services.impl.PostHistoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class PostHistoryControllerImplTest {
    @InjectMocks
    private PostHistoryControllerImpl controller;

    @Mock
    private PostHistoryService service;
    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws IOException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void shouldTestProcess() throws Exception {

        doNothing().when(service).process(1L);
        this.mockMvc.perform(post("/posts/" + 1L)).andDo(print()).andExpect(status().is2xxSuccessful());

        verify(service, times(1)).process(anyLong());
    }

    @Test
    void shouldTestDisable() throws Exception {
        doNothing().when(service).disable(1L);
        this.mockMvc.perform(delete("/posts/" + 1L)).andDo(print()).andExpect(status().is2xxSuccessful());

        verify(service, times(1)).disable(anyLong());
    }

    @Test
    void shouldTestReprocess() throws Exception {
        doNothing().when(service).reprocess(1L);
        this.mockMvc.perform(put("/posts/" + 1L)).andDo(print()).andExpect(status().is2xxSuccessful());

        verify(service, times(1)).reprocess(anyLong());
    }

    @Test
    void shouldTestFindAll() throws Exception {
        doReturn(List.of(new PostDtoResponse(new Post(1L)))).when(service).findAll();

        this.mockMvc.perform(get("/posts")).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(service, times(1)).findAll();
    }
}
