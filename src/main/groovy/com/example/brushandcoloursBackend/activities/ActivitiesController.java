package com.example.brushandcoloursBackend.activities;

import com.example.brushandcoloursBackend.activities.dto.ActivityCreateRequest;
import com.example.brushandcoloursBackend.activities.dto.ActivityResponse;
import com.example.brushandcoloursBackend.activities.dto.ActivityUpdateRequest;
import com.example.brushandcoloursBackend.auth.dto.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivitiesController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody ActivityCreateRequest request){
        ActivityResponse response = activityService.createActivity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<PagedResponse<ActivityResponse>> getAllActivities(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "direction", defaultValue = "desc") String direction
    ){
        return ResponseEntity.ok(activityService.getAllActivities(page, size, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable("id") String id){
        ActivityResponse response = activityService.getActivityById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable("id") String id){
         activityService.deleteActivity(id);
         return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> updateActivity(
            @PathVariable("id") String id, @Valid @RequestBody ActivityUpdateRequest request
            ){
        ActivityResponse response = activityService.updateActivity(id,request);
        return ResponseEntity.ok(response);
    }
}
