package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.MeetingAttendances;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Getter;

@Getter
public class MeetingResponse {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final String name;
    private final long attendanceCount;
    private final String entranceTime;
    private final String leaveTime;
    private final Boolean isMaster;
    private final Boolean isCoffeeTime;
    private final List<ParticipantResponse> users;

    public MeetingResponse(final Long id,
                           final String name,
                           final long attendanceCount,
                           final LocalTime entranceTime,
                           final LocalTime leaveTime,
                           final boolean isMaster,
                           final Boolean isCoffeeTime,
                           final List<ParticipantResponse> usersResponse) {
        this.id = id;
        this.name = name;
        this.attendanceCount = attendanceCount;
        this.entranceTime = entranceTime.format(TIME_FORMATTER);
        this.leaveTime = leaveTime.format(TIME_FORMATTER);
        this.isMaster = isMaster;
        this.isCoffeeTime = isCoffeeTime;
        this.users = usersResponse;
    }

    public static MeetingResponse of(final Meeting meeting,
                                     final boolean isMaster,
                                     final List<ParticipantResponse> participantResponses,
                                     final MeetingAttendances meetingAttendances,
                                     final Event event,
                                     final int attendanceCount) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getName(),
                attendanceCount,
                event.getEntranceTime(),
                event.getLeaveTime(),
                isMaster,
                meetingAttendances.isTardyStackFull(),
                participantResponses
        );
    }
}
