package hexlet.code.specification;

import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    @Autowired
    private LabelRepository labelRepository;

    public Specification<Task> build(TaskParamsDTO params) {
        return withTitle(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task> withTitle(String title) {
        var pattern = ("%" + title + "%").toLowerCase();
        return (root, query, cb) -> title == null ? cb.conjunction() : cb.like(cb.lower(root.get("name")), pattern);
    }

    private Specification<Task> withAssigneeId(Long id) {
        return (root, query, cb) -> id == null ? cb.conjunction() : cb.equal(root.get("assignee").get("id"), id);
    }

    private Specification<Task> withStatus(String slug) {
        return (root, query, cb) ->
                slug == null ? cb.conjunction() : cb.equal(root.get("taskStatus").get("slug"), slug);
    }

    private Specification<Task> withLabelId(Long id) {
        return (root, query, cb) ->
                id == null ? cb.conjunction() : cb.isMember(labelRepository.findById(id).orElseThrow(),
                        root.get("labels"));
    }
}
