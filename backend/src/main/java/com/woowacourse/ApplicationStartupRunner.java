package com.woowacourse;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.Participant;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.repository.AttendanceRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import com.woowacourse.moragora.repository.ParticipantRepository;
import com.woowacourse.moragora.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements ApplicationListener<ContextRefreshedEvent> {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final ParticipantRepository participantRepository;

    public ApplicationStartupRunner(final MeetingRepository meetingRepository,
                                    final UserRepository userRepository,
                                    final AttendanceRepository attendanceRepository,
                                    final ParticipantRepository participantRepository) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        final User user1 = new User("aaa111@foo.com", EncodedPassword.fromRawValue("1234smart!"), "아스피");
        final User user2 = new User("bbb222@foo.com", EncodedPassword.fromRawValue("1234smart!"), "필즈");
        final User user3 = new User("ccc333@foo.com", EncodedPassword.fromRawValue("1234smart!"), "포키");
        final User user4 = new User("ddd444@foo.com", EncodedPassword.fromRawValue("1234smart!"), "썬");
        final User user5 = new User("eee555@foo.com", EncodedPassword.fromRawValue("1234smart!"), "우디");
        final User user6 = new User("fff666@foo.com", EncodedPassword.fromRawValue("1234smart!"), "쿤");
        final User user7 = new User("ggg777@foo.com", EncodedPassword.fromRawValue("1234smart!"), "반듯");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        userRepository.save(user7);

        final Meeting meeting = new Meeting(
                "모임1",
                LocalDate.of(2022, 7, 10),
                LocalDate.of(2022, 8, 10),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0));
        meetingRepository.save(meeting);

        final Participant participant1 = new Participant(user1, meeting);
        final Participant participant2 = new Participant(user2, meeting);
        final Participant participant3 = new Participant(user3, meeting);
        final Participant participant4 = new Participant(user4, meeting);
        final Participant participant5 = new Participant(user5, meeting);
        final Participant participant6 = new Participant(user6, meeting);
        final Participant participant7 = new Participant(user7, meeting);

        participantRepository.save(participant1);
        participantRepository.save(participant2);
        participantRepository.save(participant3);
        participantRepository.save(participant4);
        participantRepository.save(participant5);
        participantRepository.save(participant6);
        participantRepository.save(participant7);

        final Attendance attendance1 = new Attendance(participant1, LocalDate.now(), Status.TARDY);
        final Attendance attendance2 = new Attendance(participant2, LocalDate.of(2022, 7, 12), Status.TARDY);
        final Attendance attendance3 = new Attendance(participant3, LocalDate.of(2022, 7, 12), Status.TARDY);
        final Attendance attendance4 = new Attendance(participant1, LocalDate.of(2022, 7, 13), Status.TARDY);
        final Attendance attendance5 = new Attendance(participant2, LocalDate.of(2022, 7, 13), Status.TARDY);
        final Attendance attendance6 = new Attendance(participant3, LocalDate.of(2022, 7, 13), Status.TARDY);
        final Attendance attendance7 = new Attendance(participant1, LocalDate.of(2022, 7, 14), Status.TARDY);
        final Attendance attendance8 = new Attendance(participant2, LocalDate.of(2022, 7, 14), Status.TARDY);
        final Attendance attendance9 = new Attendance(participant3, LocalDate.of(2022, 7, 14), Status.TARDY);

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);
        attendanceRepository.save(attendance3);
        attendanceRepository.save(attendance4);
        attendanceRepository.save(attendance5);
        attendanceRepository.save(attendance6);
        attendanceRepository.save(attendance7);
        attendanceRepository.save(attendance8);
        attendanceRepository.save(attendance9);
    }
}