package org.zerock.b01.service;

import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.MemberRegisterDTO;


public interface MemberService {


    boolean Login(String id, String password);
    MemberRegisterDTO updateMemberInfo(MemberRegisterDTO updatedMember);
    MemberRegisterDTO myPage(String id);
    String duplicationId(String id);
    boolean findMember(String id, String password);
    void deleteMember(String id);

    MemberRegisterDTO member(String id);



    void updatePw(String id, String pw);



    static class MidExistException extends Exception{

    }
    void join(Member member) throws MidExistException;

}
