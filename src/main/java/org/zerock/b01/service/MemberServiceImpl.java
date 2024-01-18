package org.zerock.b01.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.MemberRegisterDTO;
import org.zerock.b01.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.b01.domain.Member;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    /*------------------------------ ▼ 회원가입---------------------------------------------*/


    @Override
    public void join(Member member) throws MidExistException {
        String mid = member.getMid();
        boolean exist = memberRepository.existsById(mid);
        if (exist) {
            throw new MidExistException();
        }
        Member member1 = modelMapper.map(member, Member.class);
        member1.addRole(MemberRole.USER);
        member1.changePassword(passwordEncoder.encode(member.getMpw()));

        memberRepository.save(member1);
    }


    /*------------------------------ ▼ 로그인---------------------------------------------*/
    @Override
    public boolean Login(String mid, String mpw) {
        return memberRepository.findByMidAndMpw(mid, mpw).isPresent();

    }


    /*------------------------------ ▼ 마이페이지---------------------------------------------*/

    @Override
    public MemberRegisterDTO myPage(String mid) {
        Optional<Member> registerOptional = memberRepository.findByMid(mid);

        if (registerOptional.isPresent()) {
            Member registeredMember = registerOptional.get();

            // Create a DTO object from the entity and return
            MemberRegisterDTO memberInfo = new MemberRegisterDTO();
            memberInfo.setMid(registeredMember.getMid());
            memberInfo.setMpw(registeredMember.getMpw());
            memberInfo.setMname(registeredMember.getMname());
            memberInfo.setMage(registeredMember.getMage());
            memberInfo.setEmail(registeredMember.getEmail());
            memberInfo.setMaddress(registeredMember.getMaddress());
            memberInfo.setMsex(registeredMember.getMsex());  // Assuming the Register entity has a getSex() method

            return memberInfo;

        } else {
            throw new IllegalArgumentException("해당 ID를 가진 회원이 없습니다.");
        }
    }


    /*------------------------------ ▼ 회원정보수정---------------------------------------------*/
    public MemberRegisterDTO updateMemberInfo(MemberRegisterDTO member) {
        log.info("updateMemberInfo 들어가기!!");

        Optional<Member> registerOptional = memberRepository.findByMid(member.getMid());


        if (registerOptional.isPresent()) {
            log.info("리파지토리에 아이디가 존재한다면!!");
            Member memberToUpdate = registerOptional.get();

            // 엔터티의 값을 업데이트
            memberToUpdate.setMpw(member.getMpw());
            memberToUpdate.setMage(member.getMage());
            memberToUpdate.setMname(member.getMname());
            memberToUpdate.setMaddress(member.getMaddress());
            memberToUpdate.setMsex(member.getMsex());
            memberToUpdate.setEmail(member.getEmail());
            // 저장소에 변경된 정보 저장
            memberRepository.save(memberToUpdate);

            MemberRegisterDTO memberInfo = new MemberRegisterDTO();
            memberInfo.setMid(memberToUpdate.getMid());
            memberInfo.setMpw(memberToUpdate.getMpw());
            memberInfo.setMname(memberToUpdate.getMname());
            memberInfo.setMage(memberToUpdate.getMage());
            memberInfo.setEmail(memberToUpdate.getEmail());
            memberInfo.setMaddress(memberToUpdate.getMaddress());
            memberInfo.setMsex(memberToUpdate.getMsex());  // Assuming the Register entity has a getSex() method

            return memberInfo;


        } else {
            throw new RuntimeException("회원 정보를 찾을 수 없습니다.");
        }
    }


    /*---------------------------▼ 아이디 중복체크 -----------------------------------------------*/

    @Override
    public String duplicationId(String id) {
        Optional<Member> optionalRegister = memberRepository.findByMid(id);
        if (optionalRegister.isEmpty()) { //조회된 결과가 없으면 아이디 사용가능
            return "ok";

        } else {
            return "no";
        }
    }


    /*------------------------------ ▼ 회원탈퇴---------------------------------------------*/

    @Override
    public MemberRegisterDTO member(String id) {

        Optional<Member> registerOptional = memberRepository.findByMid(id);
        if (registerOptional.isPresent()) {
            return modelMapper.map(registerOptional, MemberRegisterDTO.class);
        } else {
            log.info("이 아이디엔 없어!");
            return null;
        }

    }


    @Override
    public boolean findMember(String id, String password) {

        Optional<Member> registerOptional = memberRepository.findByMid(id);

        if (registerOptional.isPresent()) {
            Member unregistermember = registerOptional.get();
            String dbPassword = unregistermember.getMpw();

            return passwordEncoder.matches(password, dbPassword);

        }
        return false;
    } //DB상에 있는 id가 존재하는지 확인 후 DB상의 비밀번호와 입력한 비밀번호가 맞는지 확인하기.

    @Override
    public void deleteMember(String id) {
        memberRepository.deleteById(id);
    }



    /*------------------------------ ▼ 비밀번호 수정---------------------------------------------*/

    public void updatePw(String id, String pw) {
        log.info("updatePw 들어가기!!");
        Optional<Member> registerOptional = memberRepository.getMid(id);

        if(registerOptional.isPresent()){
            Member member = modelMapper.map(registerOptional.get(), Member.class);
            member.changePassword(passwordEncoder.encode(pw));
        }
    }







    /*----------------------------------▼ localboardread, tradeboardread용 service ----------------------------*/


}


    //    /*------------------------------ ▼ 회원정보수정---------------------------------------------*/
//
//
//
//
//    //1. 아이디와 패스워드를 찾는다.
//    //2. 패드워드와 주소를 변경한다.
//    @Override
//    public void ModifyRegister(String id, String password,String newpassword, String newaddress){
//        Optional<Register> modifyoptional = memberRepository.findByIdAndPassword(id, password);
//
//        if(modifyoptional.isPresent()){
//            Register modifyregister = modifyoptional.get();
//            modifyregister.setPassword(newpassword);
//            modifyregister.setAddress(newaddress);
//            memberRepository.save(modifyregister);
//        }else{
//            throw new IllegalArgumentException("ID와 비밀번호에 해당하는 회원이 없습니다.");
//        }
//    }














