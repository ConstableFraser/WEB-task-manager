package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Status;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private ObjectMapper om;

    private Task testTask;
    private User anotherUser;
    private Status testStatus;

    private static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        var user = Instancio.of(modelGenerator.getUserModel()).create();
        anotherUser = Instancio.of(modelGenerator.getUserModel()).create();
        testStatus = Instancio.of(modelGenerator.getStatusModel())
                .create();

        user.setTasks(new ArrayList<>());
        anotherUser.setTasks(new ArrayList<>());
        testStatus.setTasks(new ArrayList<>());

        userRepository.save(user);
        userRepository.save(anotherUser);
        statusRepository.save(testStatus);

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setAssignee(user);
        testTask.setStatus(testStatus);
    }

    @Test
    public void testShow() throws Exception {
        taskRepository.save(testTask);
        var request = get("/api/tasks/{id}", testTask.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("assigneeId").isEqualTo(testTask.getAssignee().getId())
        );
    }

    @Test
    public void testIndex() throws Exception {
        taskRepository.save(testTask);
        var request = get("/api/tasks")
                .with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getTaskModel())
                .create();
        var taskDTO = mapper.map(data);
        taskDTO.setStatusId(testStatus.getId());
        taskDTO.setAssigneeId(anotherUser.getId());

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(taskDTO));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName(data.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo(taskDTO.getTitle());
        assertThat(task.getDescription()).isEqualTo(taskDTO.getContent());
        assertThat(task.getAssignee().getId()).isEqualTo(taskDTO.getAssigneeId());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = Instancio.of(modelGenerator.getTaskModel())
                .create();
        data.setName("new_title");
        data.setStatus(testStatus);
        data.setAssignee(anotherUser);
        var taskDTO = mapper.map(taskRepository.save(data));

        var request = put("/api/tasks/{id}", taskDTO.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(taskDTO));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(taskDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo(taskDTO.getTitle());
        assertThat(task.getDescription()).isEqualTo(taskDTO.getContent());
        assertThat(task.getAssignee().getId()).isEqualTo(taskDTO.getAssigneeId());
    }

    @Test
    public void testDestroy() throws Exception {
        taskRepository.save(testTask);

        var request = delete("/api/tasks/" + testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskRepository.findById(testTask.getId())).isNotPresent();
    }
}
