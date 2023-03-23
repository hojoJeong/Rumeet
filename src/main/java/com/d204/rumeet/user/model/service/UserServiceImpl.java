package com.d204.rumeet.user.model.service;

import com.d204.rumeet.exception.DuplicateException;
import com.d204.rumeet.exception.NoUserDataException;
import com.d204.rumeet.tools.JwtTool;
import com.d204.rumeet.tools.OSUpload;
import com.d204.rumeet.tools.OkhttpUtils;
import com.d204.rumeet.tools.SHA256;
import com.d204.rumeet.user.model.dto.*;
import com.d204.rumeet.user.model.mapper.UserMapper;
import com.google.gson.Gson;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final JwtTool jwtTool;
    private final UserMapper userMapper;
    private final OSUpload osUpload;
    private final JavaMailSender emailSender;
    private final SHA256 sha256;
    private final OkhttpUtils okhttpUtils;
    final String bucketName = "rumeet";

    @Override
    public LoginUserDto doLogin(LoginDto loginDto) {
        UserDto user = userMapper.doLogin(loginDto);
        if(user == null) {
            throw new NoUserDataException();
        }
        return this.generateUser(user.getId());
    }

    @Override
    public LoginUserDto generateUser(int id) {
        String accessToken = "Bearer " + jwtTool.createAccessToken(id);
        String refreshToken = "Bearer " + jwtTool.createRefreshToken(id);
        LoginUserDto user = new LoginUserDto(id,accessToken,refreshToken);
        return user;
    }

    @Override
    public UserDto getUserById(int id) {
        UserDto user = userMapper.getUserById(id);
        if(user == null) {
            throw new NoUserDataException();
        }
        return user;
    }

    @Override
    public void modifyUser(ModifyUserDto user) {
        int flag = userMapper.modifyUser(user);
        if(flag == 0) {
            throw new NoUserDataException();
        }
    }

    @Override
    public void delUser(int id) {
        int flag = userMapper.delUser(id);
        if(flag == 0) {
            throw new NoUserDataException();
        }
    }
    public String uploadFile(MultipartFile profile, String baseUrl) {
        String url = baseUrl;

        if(profile != null && !profile.isEmpty()) {
            String [] formats = {".jpeg", ".png", ".bmp", ".jpg", ".PNG", ".JPEG"};
            // 원래 파일 이름 추출
            String origName = profile.getOriginalFilename();

            // 확장자 추출(ex : .png)
            String extension = origName.substring(origName.lastIndexOf("."));

            String folderName = "profile";
            for(int i = 0; i < formats.length; i++) {
                if (extension.equals(formats[i])){
                    // user email과 확장자 결합
                    File uploadFile = null;
                    try {
                        uploadFile = osUpload.convert(profile)        // 파일 생성
                                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String fileName = folderName + "/" + System.nanoTime() + extension;
                    osUpload.put(bucketName, fileName, uploadFile);

                    url = "https://kr.object.ncloudstorage.com/"+bucketName+"/"+fileName;
                    break;
                }
            }
        }
        return url;
    }
    @Override
    public void joinUser(JoinUserDto user, MultipartFile profile) {
        String url = this.uploadFile(profile,"https://kr.object.ncloudstorage.com/rumeet/base_profile.png");
        user.setProfile(url);
        user.setDate(System.currentTimeMillis());
        userMapper.joinUser(user);
    }
    @Override
    public void joinKakaoUser(JoinKakaoUserDto user, MultipartFile profile) {
        String url = this.uploadFile(profile, user.getProfile());
        user.setProfile(url);
        String email = createKey();
        String pwd = createKey();
        try {
            email = sha256.encrypt(email);
            pwd = sha256.encrypt(pwd);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        user.setEmail(email);
        user.setPassword(pwd);
        user.setDate(System.currentTimeMillis());
        userMapper.joinKakaoUser(user);
    }

    @Override
    public void checkDuplication(int type, String value) {
        int count = userMapper.checkDuplication(new CheckDto(type, value));
        if(count == 1) {
            throw new DuplicateException();
        }
    }

    @Override
    public String sendSimpleMessage(String email) throws MailException {
        MimeMessage message = null;
        String ePw = createKey();
        try {
            message = createMessage(email, ePw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //예외처리
        emailSender.send(message);
        try {
            ePw = sha256.encrypt(ePw);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ePw;
    }

    @Override
    public List<SimpleUserDto> searchUsersByNickname(String nickname) {
        List<SimpleUserDto> users = userMapper.searchUsersByNickname("%" + nickname + "%");
        return users;
    }

    @Override
    public void modifyPwd(ModifyPwdDto dto) {
        int count = userMapper.modifyPwd(dto);
        if(count == 0) {
            throw new NoUserDataException();
        }
    }

    @Override
    public UserDto getUserOauth(String tokenId) {
        return userMapper.getUserOauth(tokenId);
    }

    @Override
    public void modifyUserProfile(ProfileUserDto user, MultipartFile profile) {
        String url = this.uploadFile(profile, user.getProfile());
        user.setProfile(url);
        int cnt = userMapper.modifyUserProfile(user);
        if(cnt == 0) {
            throw new NoUserDataException();
        }
    }

    @Override
    public KakaoUserDto kakaoOauth(String code) {
        String REST_API_KEY = "b653f0b908ff838fb24e6e7cce632a38";
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = null;
        Response response = null;
        String responseBody = "";

        String access_token = code;
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + access_token)
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        responseBody = "";
        if (response.isSuccessful()) {
            try {
                responseBody = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        KakaoUserDto kakaoUser = new Gson().fromJson(responseBody, KakaoUserDto.class);
        return kakaoUser;
    }
//
    @Override
    public NaverUserDto naverOauth(String code) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://nid.naver.com/oauth2.0/token?client_id=swueBQKH3TyR0BispyA3&client_secret=c_360FcNCo&grant_type=authorization_code&state=rumeet&code="+code)
                .get()
                .build();
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String responseBody = "";
        if (response.isSuccessful()) {
            try {
                responseBody = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new Gson();
        AccessTokenResponse accessTokenResponse = gson.fromJson(responseBody,AccessTokenResponse.class);
        String access_token = accessTokenResponse.access_token;
        System.out.println(accessTokenResponse.id_token);
        request = new Request.Builder()
                .url("https://openapi.naver.com/v1/nid/me")
                .header("Authorization", "Bearer " + access_token)
                .get()
                .build();

        call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        responseBody = "";
        if (response.isSuccessful()) {
            try {
                responseBody = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        NaverUserDto naverUserDto = new Gson().fromJson(responseBody, NaverUserDto.class);
        return naverUserDto;
    }

    private MimeMessage createMessage(String to, String ePw)throws Exception{
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상
        message.setSubject("Vilez 이메일 인증");//제목

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 rumeet입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("gch03944@gmail.com","rummmet"));//보내는 사람

        return message;
    }

    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    @Data
    public class AccessTokenResponse {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private String id_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;

        // getters and setters
    }

    @Data
    public class KakaoUser {
        private long expiresInMillis;
        private String id;
        private int expires_in;
        private int app_id;
        private int appId;
    }

}
