package com.example.nongdam.api.scedule.dto.request;

import com.example.nongdam.entity.member.entity.Member;
import com.example.nongdam.entity.schedule.entity.Schedule;
import com.example.nongdam.global.FinalValue;
import com.example.nongdam.entity.crop.repository.CropRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequestDto {
    private int cropId;
    private String startTime;
    private String endTime;
    private String toDo;

    public Schedule build(Member member, CropRepository repository) throws IllegalArgumentException {
        LocalDateTime startTime = LocalDateTime.parse(this.startTime, FinalValue.DAYTIME_FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(this.endTime, FinalValue.DAYTIME_FORMATTER);
        if(startTime.isAfter(endTime))
            throw new IllegalArgumentException("잘못된 입력입니다. 시작/끝 시간.");
        return Schedule.builder()
                .crop(repository.findById(this.cropId).get())
                .startTime(startTime)
                .endTime(endTime)
                .toDo(this.toDo)
                .member(member)
                .build();
    }
}
