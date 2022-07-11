package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Meeting;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MeetingRequest {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime entranceTime;
    private LocalTime leaveTime;

    private List<Long> userIds;

    public MeetingRequest(String name, LocalDate startDate, LocalDate endDate, LocalTime entranceTime,
                          LocalTime leaveTime,
                          List<Long> userIds) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
        this.userIds = userIds;
    }

    public Meeting toEntity() {
        return new Meeting(name, startDate, endDate, entranceTime, leaveTime);
    }
}
