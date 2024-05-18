package hexlet.code.util;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.Task;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hexlet.code.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;

@Getter
@Component
public class ModelGenerator {
    private Model<User> userModel;
    private Model<TaskStatus> statusModel;
    private Model<Task> taskModel;
    private Model<Label> labelModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().name().split(" ")[0])
                .supply(Select.field(User::getLastName), () -> faker.name().name().split(" ")[1])
                .toModel();

        statusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.lorem().
                        maxLengthSentence(faker.random().nextInt(2, 12)))
                .supply(Select.field(TaskStatus::getSlug), () -> faker.lorem().
                        maxLengthSentence(faker.random().nextInt(2, 12)))
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getLabels))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getTaskStatus))
                .supply(Select.field(Task::getName), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.gameOfThrones().quote())
                .toModel();

        labelModel = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getTasks))
                .supply(Select.field(Label::getName), () -> faker.lorem().
                        maxLengthSentence(faker.random().nextInt(3, 12)))
                .toModel();
    }
}
