package com.defi.auth.permission.controler;

import com.defi.auth.permission.dto.ActionRequest;
import com.defi.auth.permission.entity.Action;
import com.defi.auth.permission.service.ActionService;
import com.defi.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/permissions/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<Action>>> getAll() {
        List<Action> actions = actionService.findAll();
        return ResponseEntity.ok(BaseResponse.of(actions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Action>> getById(@PathVariable Long id) {
        return actionService.findById(id)
                .map(action -> ResponseEntity.ok(BaseResponse.of(action)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Action>> create(@RequestBody @Valid ActionRequest request) {
        Action action = actionService.create(request);
        return ResponseEntity.ok(BaseResponse.of(action));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Action>> update(@PathVariable Long id, @RequestBody @Valid ActionRequest request) {
        Action updated = actionService.update(id, request);
        return ResponseEntity.ok(BaseResponse.of(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        actionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
