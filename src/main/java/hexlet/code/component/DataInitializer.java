package hexlet.code.component;

import hexlet.code.model.Status;
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
            Status status = new Status(s, s);
            status.setCreatedAt(LocalDate.now());
            statusRepository.save(status);
        }
    }
}
