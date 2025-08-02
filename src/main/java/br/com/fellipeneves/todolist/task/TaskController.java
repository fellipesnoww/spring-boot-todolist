package br.com.fellipeneves.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

        var userId = request.getAttribute("idUser");

        var currentDate = LocalDate.now();
        if(currentDate.isAfter(taskModel.getStartAt().toLocalDate()) || currentDate.isAfter(taskModel.getEndAt().toLocalDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio e/ou fim, devem ser maior que a data atual");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio não pode ser maior que a data de término");
        }


        taskModel.setIdUser((UUID) userId);
        var newTask = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }

    @GetMapping("/")
    public ResponseEntity list(HttpServletRequest request) {
        var userId = request.getAttribute("idUser");

        var tasks = taskRepository.findByIdUser((UUID) userId);

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }
}
