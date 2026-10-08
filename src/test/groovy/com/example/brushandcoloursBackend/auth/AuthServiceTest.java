package com.example.brushandcoloursBackend.auth;

import com.example.brushandcoloursBackend.activities.Activity;
import com.example.brushandcoloursBackend.activities.ActivityRepository;
import com.example.brushandcoloursBackend.activities.ActivityService;
import com.example.brushandcoloursBackend.activities.dto.ActivityCreateRequest;
import com.example.brushandcoloursBackend.activities.dto.ActivityResponse;
import com.example.brushandcoloursBackend.activities.mapper.ActivityMapper;
import com.example.brushandcoloursBackend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityService activityService;

    @Test
    void getActivityById_ShouldReturnActivity(){

        //ARRANGE
        String activityId = "123";

        Activity activity = new Activity();
        activity.setId(activityId);
        activity.setTitle("Painting Workshop");

        ActivityResponse response = new ActivityResponse();
        response.setId(activityId);
        response.setTitle("Painting Workshop");

        when(activityRepository.findById(activityId))
                .thenReturn(Optional.of(activity));

        when(activityMapper.toResponse(activity))
                .thenReturn(response);

        //ACT
        ActivityResponse result = activityService.getActivityById(activityId);

        //ASSERT
        assertNotNull(result);
        assertEquals(activityId, result.getId());
        assertEquals("Painting Workshop", result.getTitle());

        verify(activityRepository).findById(activityId);
        verify(activityMapper).toResponse(activity);
    }

    @Test
    void getActivityById_ShouldReturnExceptionWhenNotFound(){
        //ARRANGE
        String activityId = "999";

        when(activityRepository.findById(activityId))
                .thenReturn(Optional.empty());

        //ACT + ASSERT
        assertThrows(ResourceNotFoundException.class,
                ()-> activityService.getActivityById(activityId));

        //VERIFY
        verify(activityRepository)
                .findById(activityId);

        verify(activityMapper, never()).toResponse(any());
    }

    @Test
    void createActivity_ShouldCreateAndSaveActivity(){
        //ARRANGE

        ActivityCreateRequest request = new ActivityCreateRequest();

        request.setTitle("Painting Workshop");
        request.setDescription("Beginner painting class");
        request.setBasePrice(new BigDecimal("500"));
        request.setCategory("Painting");
        request.setCity("Delhi");
        request.setActive(true);


        Activity activity = new Activity();

        activity.setTitle("Painting Workshop");
        activity.setDescription("Beginner painting class");
        activity.setBasePrice(new BigDecimal("500"));
        activity.setCategory("Painting");
        activity.setCity("Delhi");
        activity.setActive(true);


        Activity savedActivity = new Activity();

        savedActivity.setId("123");
        savedActivity.setTitle("Painting Workshop");
        savedActivity.setDescription("Beginner painting class");
        savedActivity.setBasePrice(new BigDecimal("500"));
        savedActivity.setCategory("Painting");
        savedActivity.setCity("Delhi");
        savedActivity.setActive(true);


        ActivityResponse response = new ActivityResponse();

        response.setId("123");
        response.setTitle("Painting Workshop");
        response.setBasePrice(new BigDecimal("500"));

        when(activityMapper.toEntity(request))
                .thenReturn(activity);

        when(activityRepository.save(activity))
                .thenReturn(savedActivity);

        when(activityMapper.toResponse(savedActivity))
                .thenReturn(response);

        //ACT
        ActivityResponse result =  activityService.createActivity(request);

        //ASSERT
        assertNotNull(response);

        assertEquals("123", result.getId());
        assertEquals("Painting Workshop", result.getTitle());
        assertEquals(new BigDecimal("500"), result.getBasePrice());

        //VERIFY
        verify(activityMapper).toEntity(request);
        verify(activityRepository).save(activity);
        verify(activityMapper).toResponse(savedActivity);

    }

    @Test
    void deleteActivity_ShouldDeleteActivity(){

        //ARRANGE
        String activityID = "123";

        Activity activity = new Activity();
        activity.setId(activityID);
        activity.setTitle("Painting Workshop");

        when(activityRepository.findById(activityID))
                .thenReturn(Optional.of(activity));

        //ACT
        activityService.deleteActivity(activityID);

        // ASSERT/VERIFY
        verify(activityRepository).findById(activityID);
        verify(activityRepository).delete(activity);
    }

    @Test
    void deleteActivity_ShouldThrowException(){
        //ARRANGE
        String activityId = "999";

        when(activityRepository.findById(activityId))
                .thenReturn(Optional.empty());

        // ACT+ASSERT
        assertThrows(ResourceNotFoundException.class,
                () -> activityService.deleteActivity(activityId));

        //VERIFY
        verify(activityRepository).findById(activityId);
        verify(activityRepository, never()).delete(any(Activity.class));
    }
}
