package com.example.brushandcoloursBackend.activities;

import com.example.brushandcoloursBackend.activities.dto.ActivityCreateRequest;
import com.example.brushandcoloursBackend.activities.dto.ActivityResponse;
import com.example.brushandcoloursBackend.activities.dto.ActivityUpdateRequest;
import com.example.brushandcoloursBackend.activities.mapper.ActivityMapper;
import com.example.brushandcoloursBackend.auth.dto.PagedResponse;
import com.example.brushandcoloursBackend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActivityService {

    // here we need to create -> CREATE, GETALL, GETBYID, UPDATE, DELETE, FINFBYID

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    @Transactional
    public ActivityResponse createActivity(ActivityCreateRequest request){
        Activity activity = activityMapper.toEntity(request);
        Activity savedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(savedActivity);
    }

    @Transactional(readOnly = true)
    public PagedResponse<ActivityResponse> getAllActivities(int page, int size, String sortBy, String direction){

        if(page < 0) page = 0;
        if(size <= 0) size = 10;
        if(size > 100) size = 100;

        Set<String> allowedSortFields = Set.of(
                "title", "basePrice", "category", "city", "createdAt", "updatedAt"
        );

        if(!allowedSortFields.contains(sortBy)) sortBy = "createdAt";

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC;

        Sort sort = Sort.by(sortDirection, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Activity> activityPage = activityRepository.findAll(pageable);

        List<ActivityResponse> content = activityPage.getContent()
                .stream()
                .map(activityMapper::toResponse)
                .toList();

        return new PagedResponse<>(
                content,
                activityPage.getNumber(),
                activityPage.getSize(),
                activityPage.getTotalElements(),
                activityPage.getTotalPages(),
                activityPage.isFirst(),
                activityPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public ActivityResponse getActivityById(String id){
        Activity activity = findActivityById(id);
        return activityMapper.toResponse(activity);
    }

    @Transactional
    public ActivityResponse updateActivity(String id, ActivityUpdateRequest request){
        Activity activity = findActivityById(id);
        Activity updateActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updateActivity);
    }

    @Transactional
    public void deleteActivity(String id){
        Activity activity = findActivityById(id);
        activityRepository.delete(activity);
    }

    private Activity findActivityById(String id){
        return activityRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Activity not found with id: " + id));
    }
}
