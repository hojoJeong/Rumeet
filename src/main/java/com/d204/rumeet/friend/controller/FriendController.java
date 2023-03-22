package com.d204.rumeet.friend.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.friend.model.dao.FriendDao;
import com.d204.rumeet.friend.model.dao.FriendRequestDao;
import com.d204.rumeet.friend.model.dto.FriendRequestDto;
import com.d204.rumeet.user.model.dto.SimpleUserDto;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {


    private final MongoTemplate mongoTemplate;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> selectAll() {
        RespData<List> data = new RespData<>();
        List<FriendDao> list = mongoTemplate.findAll(FriendDao.class,"friend");
        data.setData(list);
        return data.builder();
    }

    // 친구 조회 (전체 (닉네임순), 최근 같이 뛴 친구, 함께 많이 달린 친구)
    // 달리기 한 뒤에 정렬 추가하기
    @GetMapping("/{userId}")
    public ResponseEntity<?> selectByUserId(@PathVariable int userId) {
        RespData<List> data = new RespData<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<FriendDao> list = mongoTemplate.find(query, FriendDao.class,"friend");
        data.setData(list);
        return data.builder();
    }

    // 친구 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFriend(@RequestParam("userId") int userId, @RequestParam("friendId") int friendId) {
        Query query1 = new Query(Criteria.where("userId").is(userId)
                .and("friendId").is(friendId));
        Query query2 = new Query(Criteria.where("userId").is(friendId)
                .and("friendId").is(userId));

        mongoTemplate.remove(query1, FriendDao.class);
        mongoTemplate.remove(query2, FriendDao.class);

        RespData<List> data = new RespData<>();
        data.setMsg("친구 삭제");
        return data.builder();
    }

    //친구요청
    @PostMapping("/request")
    public ResponseEntity<?> requestFriend(@RequestBody FriendRequestDto friendRequestDto) {
        int fromId = friendRequestDto.getFromUserId();
        int toId = friendRequestDto.getToUserId();

        Query query = new Query(Criteria.where("fromUserId").is(fromId)
                .and("toUserId").is(toId));
        FriendRequestDao existingRequest = mongoTemplate.findOne(query, FriendRequestDao.class);

        // 친구요청 존재할 때
        if (existingRequest != null) {
            RespData<List> data = new RespData<>();
            data.setMsg("이미 친구요청 보낸 상태입니다.");
            return data.builder();

        // 존재하지 않으면 친구요청
        } else {
            FriendRequestDao friendRequest = FriendRequestDao.builder()
                    .fromUserId(fromId)
                    .toUserId(toId)
                    .date(System.currentTimeMillis())
                    .build();
            mongoTemplate.insert(friendRequest);

            RespData<Void> data = new RespData<>();
            return data.builder();
        }
    }

    // 친구요청 조회
    @GetMapping("/request")
    public ResponseEntity<?> getFriendRequests(@RequestParam("userId") int toUserId) {
        List<FriendRequestDao> requests = mongoTemplate.find(
                Query.query(Criteria.where("toUserId").is(toUserId)),
                FriendRequestDao.class
        );

        RespData<List> data = new RespData<>();
        data.setData(requests);
        return data.builder();
    }

    //친구요청 수락
    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        int fromId = friendRequestDto.getFromUserId();
        int toId = friendRequestDto.getToUserId();

        // 요청 삭제
        Query query = new Query(Criteria.where("fromUserId").is(fromId)
                .and("toUserId").is(toId));
        mongoTemplate.remove(query, FriendRequestDao.class);

        // 친구 추가
        FriendDao friend = FriendDao.builder()
                .userId(toId)
                .friendId(fromId)
                .date(System.currentTimeMillis())
                .build();
        mongoTemplate.insert(friend);

        RespData<List> data = new RespData<>();
        data.setMsg("친구 요청 수락");
        return data.builder();
    }

    // 친구요청 거절
    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        int fromId = friendRequestDto.getFromUserId();
        int toId = friendRequestDto.getToUserId();

        Query query = new Query(Criteria.where("fromUserId").is(fromId)
                .and("toUserId").is(toId));
        mongoTemplate.remove(query, FriendRequestDao.class);

        RespData<List> data = new RespData<>();
        data.setMsg("친구 요청 거절");
        return data.builder();
    }


    // 닉네임으로 유저 검색


}
