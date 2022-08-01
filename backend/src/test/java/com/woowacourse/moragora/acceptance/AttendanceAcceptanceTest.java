package com.woowacourse.moragora.acceptance;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.dto.UserAttendanceRequest;
import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.support.ServerTimeManager;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AttendanceAcceptanceTest extends AcceptanceTest {

    @MockBean
    private ServerTimeManager serverTimeManager;

    @DisplayName("모임의 출석을 마감하면 총 모임 횟수와 결석한 참가자들의 결일을 증가시키고 상태코드 204을 반환한다.")
    @Test
    void endAttendance() {
        // given
        final int meetingId = 1;
        final int userId = 1;
        final UserAttendanceRequest userAttendanceRequest = new UserAttendanceRequest(Status.PRESENT);

        final LocalDateTime dateTime = LocalDateTime.of(2022, 7, 14, 0, 0);

        given(serverTimeManager.isOverClosingTime(any(LocalTime.class)))
                .willReturn(false);
        given(serverTimeManager.getDate())
                .willReturn(dateTime.toLocalDate());

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .auth().oauth2(signUpAndGetToken())
                .body(userAttendanceRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/meetings/" + meetingId + "/users/" + userId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}