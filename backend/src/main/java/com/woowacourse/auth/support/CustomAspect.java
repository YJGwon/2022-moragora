package com.woowacourse.auth.support;

import com.woowacourse.moragora.service.MeetingService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomAspect {
    /*
    * 여기서 repository를 사용하게 되면 controller test에서 repository까지 mocking해야 함
    * aop는 controller 로직의 일부로 봐야 하기 때문에 controller test에서도 적용 가능해야 하지 않을까?
    * 따라서 test 편의성 위해 MasterService.checkMaster()와 같이 service에 method를 작성한 후 여기서 호출하는 것이 좋을듯
    */
    private final MeetingService meetingService;

    public CustomAspect(final MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    // @Master annotation 가지고, meetingId와 masterId를 마지막 param으로 가지는 method에 대해 동작
    // 지금은 MeetingController.findOne()에 시범적용 해보았고, controller test 및 acceptance test에서 동작 확인
    @Before(value = "@annotation(Master) && args(.., meetingId, loginId)", argNames = "meetingId, loginId")
    public void logArgs(Long meetingId, Long loginId) {
        System.out.println("=========ids: " + meetingId + ", " + loginId);
        System.out.println("=========meeting name: " + meetingService.findById(meetingId, loginId).getName());
    }
}
