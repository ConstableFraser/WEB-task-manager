package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import hexlet.code.model.User;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) {
        // Create default user
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);

        // Create default statuses
        var statuses = List.of("draft", "to_review", "to_be_fixed", "to_publish", "published");
        for (String s : statuses) {
            var taskStatus = new TaskStatus(s, s);
            taskStatus.setCreatedAt(LocalDate.now());
            statusRepository.save(taskStatus);
        }

        //Create default labels
        var labelNames = List.of("bug", "feature");
        for (String name : labelNames) {
            var label = new Label();
            label.setName(name);
            label.setCreatedAt(LocalDate.now());
            labelRepository.save(label);
        }
    }
}
