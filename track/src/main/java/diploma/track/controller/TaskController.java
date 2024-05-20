package diploma.track.controller;

import diploma.track.dto.TaskAnswerDto;
import diploma.track.dto.TaskReadDto;
import diploma.track.service.TrackTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TrackTaskService trackTaskService;

    @GetMapping("/{trackId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskReadDto> createTask(@PathVariable String trackId) {
        return ResponseEntity.ok(trackTaskService.createTask(trackId));
    }

    @PostMapping("/handle")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> handleTaskAnswer(@RequestBody TaskAnswerDto taskAnswerDto) {
        return ResponseEntity.ok(trackTaskService.handleTask(taskAnswerDto));
    }
}
