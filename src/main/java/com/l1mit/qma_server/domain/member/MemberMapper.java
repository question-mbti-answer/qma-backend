package com.l1mit.qma_server.domain.member;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.dto.MemberInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    MemberInfoResponse entityToMemberInfoResponse(Member member) {
        return MemberInfoResponse.builder()
                .provider(member.getOauth2Entity().getSocialProvider())
                .mbtiEntity(member.getMbtiEntity())
                .build();
    }
}
