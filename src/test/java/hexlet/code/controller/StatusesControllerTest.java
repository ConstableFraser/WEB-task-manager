package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.StatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StatusesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ObjectMapper om;

    private static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private TaskStatus testStatus;

    @Autowired
    private ModelGenerator modelGenerator;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testStatus = Instancio.of(modelGenerator.getStatusModel())
                .create();
        testStatus.setTasks(new ArrayList<>());
        statusRepository.save(testStatus);
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/task_statuses/{id}", testStatus.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testStatus.getName()),
                v -> v.node("slug").isEqualTo(testStatus.getSlug())
        );
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/task_statuses");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getStatusModel())
                .create();

        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var createdStatus = statusRepository.findBySlug(data.getSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        assertNotNull(createdStatus);
        assertThat(createdStatus.getName()).isEqualTo(data.getName());
        assertThat(createdStatus.getSlug()).isEqualTo(data.getSlug());
    }

    @Test
    void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("name", "new_status_name");

        var request = put("/api/task_statuses/" + testStatus.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedStatus = statusRepository.findById(testStatus.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        assertNotNull(updatedStatus);
        assertThat(updatedStatus.getName()).isEqualTo(data.get("name"));
        assertThat(updatedStatus.getSlug()).isEqualTo(testStatus.getSlug());
    }

    @Test
    void testDelete() throws Exception {
        var testDeleteStatus = Instancio.of(modelGenerator.getStatusModel())
                .create();
        testDeleteStatus.setTasks(new ArrayList<>());
        statusRepository.save(testDeleteStatus);

        var id = testDeleteStatus.getId();

        var request = delete("/api/task_statuses/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(statusRepository.findById(id)).isNotPresent();
    }
}
