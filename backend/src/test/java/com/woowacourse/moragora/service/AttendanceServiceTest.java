package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.CoffeeStatResponse;
import com.woowacourse.moragora.dto.CoffeeStatsResponse;
import com.woowacourse.moragora.dto.EventRequest;
import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.dto.MeetingRequest;
import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.exception.meeting.ClosingTimeExcessException;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.support.ServerTimeManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AttendanceServiceTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ServerTimeManager serverTimeManager;

    @DisplayName("사용자들의 출석 여부를 변경한다.")
    @Test
    void updateAttendance() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);
        final Long meetingId = 1L;
        final Long userId = 1L;

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);
        serverTimeManager.refresh(dateTime);

        // when, then
        assertThatCode(() -> attendanceService.updateAttendance(meetingId, userId, request))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅방이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifMeetingNotFound() {
        // given
        final Long meetingId = 999L;
        final Long userId = 1L;
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(meetingId, userId, request))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @DisplayName("사용자의 출석 여부를 변경하려고 할 때, 미팅 참가자가 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifParticipantNotFound() {
        // given
        final MeetingRequest meetingRequest = new MeetingRequest("meeting",
                List.of(2L, 3L, 4L, 5L));
        final Long meetingId = meetingService.save(meetingRequest, 1L);
        final Long userId = 6L;

        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(meetingId, userId, request))
                .isInstanceOf(ParticipantNotFoundException.class);
    }

    @DisplayName("사용자 출석 제출 시간이 마감 시간을 초과할 경우 예외가 발생한다.")
    @Test
    void updateAttendance_throwsException_ifDeadlineTimeExcess() {
        // given
        final UserAttendanceRequest request = new UserAttendanceRequest(Status.PRESENT);
        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 10, 6);
        serverTimeManager.refresh(dateTime);

        // when, then
        assertThatThrownBy(() -> attendanceService.updateAttendance(1L, 1L, request))
                .isInstanceOf(ClosingTimeExcessException.class);
    }

    @DisplayName("유저별 다음에 사용될 커피스택을 조회한다.")
    @Test
    void countUsableCoffeeStack() {
        // given
        final Long meetingId = 1L;

        final EventRequest eventRequest = EventRequest.builder()
                .entranceTime(LocalTime.of(10, 0))
                .leaveTime(LocalTime.of(12, 0))
                .date(LocalDate.of(2022, 7, 16)).build();
        final List<Event> events = eventService.save(new EventsRequest(List.of(eventRequest)), meetingId);
        eventService.saveAttendances(events.get(0));

        // when
        final CoffeeStatsResponse response = attendanceService.countUsableCoffeeStack(meetingId);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(new CoffeeStatsResponse(
                        List.of(
                                new CoffeeStatResponse(1L, "아스피", 2L),
                                new CoffeeStatResponse(2L, "필즈", 3L),
                                new CoffeeStatResponse(3L, "포키", 1L),
                                new CoffeeStatResponse(4L, "썬", 1L)
                        ))
                );
    }

    @DisplayName("사용된 커피스택을 비활성화한다.")
    @Test
    void disableUsedTardy() {
        // given
        final Long meetingId = 1L;

        final EventRequest eventRequest = EventRequest.builder()
                .entranceTime(LocalTime.of(10, 0))
                .leaveTime(LocalTime.of(12, 0))
                .date(LocalDate.of(2022, 7, 16)).build();
        final List<Event> events = eventService.save(new EventsRequest(List.of(eventRequest)), meetingId);
        eventService.saveAttendances(events.get(0));

        // when, then
        assertThatCode(() -> attendanceService.disableUsedTardy(meetingId))
                .doesNotThrowAnyException();
    }
}
