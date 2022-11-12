package com.example.nongdam.dto.response;

import com.example.nongdam.FinalValue;
import com.example.nongdam.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {
    private long id;
    private String startTime;
    private String endTime;
    private CropDto crop;
    private int cropId;
    private String toDo;

    public ScheduleResponseDto(Schedule schedule){
        this.id = schedule.getId();
        this.startTime = schedule.getStartTime().format(FinalValue.DAYTIME_FORMATTER);
        this.endTime = schedule.getEndTime().format(FinalValue.DAYTIME_FORMATTER);
        this.crop = new CropDto(schedule.getCrop());
        this.cropId = schedule.getCrop().getId();
        this.toDo = schedule.getToDo();
    }
}
