package com.example.nongdam.entity.member.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.nongdam.entity.member.entity.Member;
import com.example.nongdam.global.annotation.DeleteMemberCache;
import com.example.nongdam.api.member.dto.request.JoinMemberRequestDto;
import com.example.nongdam.api.member.dto.request.LoginRequestDto;
import com.example.nongdam.api.member.dto.request.MemberInfoRequestDto;
import com.example.nongdam.api.member.dto.response.JwtResponseDto;
import com.example.nongdam.api.member.dto.response.MemberInfoResponseDto;
import com.example.nongdam.entity.crop.repository.CropRepository;
import com.example.nongdam.entity.member.repository.MemberRepository;
import com.example.nongdam.global.security.JwtProvider;
import com.example.nongdam.entity.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private final CropRepository cropRepository;
    private final JwtProvider provider;
    private final AwsS3Service s3Service;



    public void checkEmail (String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    public void saveMember(JoinMemberRequestDto dto) {
        checkEmail(dto.getEmail());
        memberRepository.save(dto.build(encoder));
    }

    @Transactional
    public JwtResponseDto login(LoginRequestDto dto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
        if (encoder.matches(dto.getPassword(), member.getPassword())) {
            return provider.generateToken(member, response);
        } else throw new IllegalArgumentException("계정 혹은 비밀번호가 다릅니다.");
    }


    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(Member member) {
        return new MemberInfoResponseDto(member);
    }



    @Transactional
    @DeleteMemberCache(memberIdArg = "memberid")
    public ResponseEntity<?> updateMember(String image, int memberid, MultipartFile profileImage, MemberInfoRequestDto requestDto, String username) {
        Member member = memberRepository.findById(memberid).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않습니다."));
        String memberEmail = member.getEmail();
        if (Objects.equals(memberEmail, username)) {
            if (profileImage != null) {
                try {
                    String[] urlArr = member.getProfileImage().split("/");
                    String fileKey = urlArr[urlArr.length - 1];
                    s3Service.deleteFile(fileKey);
                } catch (AmazonS3Exception e) {
                    log.warn("삭제할 파일 없음");
                }
                member.updateMember(requestDto, s3Service.uploadFile(profileImage), cropRepository);
            } else {
                member.updateMember(requestDto, image, cropRepository);
            }
            memberRepository.save(member);
            return new ResponseEntity<>("회원정보가 수정되었습니다.", HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>("회원정보 접근권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
