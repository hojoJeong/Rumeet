package com.d204.rumeet.friend.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.friend.model.dao.FriendDao;
import com.d204.rumeet.friend.model.dao.FriendRequestDao;
import com.d204.rumeet.friend.model.dto.FriendRequestDto;
import com.d204.rumeet.friend.model.service.FriendService;
import com.d204.rumeet.user.model.dto.SimpleUserDto;
import com.d204.rumeet.user.model.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {


    private final MongoTemplate mongoTemplate;
    private final UserService userService;
    private final FriendService friendService;



    // 달리기 한 뒤에 정렬 추가하기
    @Operation(summary = "내 친구 조회", description = "친구 조회 (전체 (닉네임순), 최근 같이 뛴 친구, 함께 많이 달린 친구) ... 아직 전체로 조회만 됨")
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> searchByUserId(@PathVariable int userId) {
        List<FriendDao> list = friendService.getFriendsByUserId(userId);
        RespData<List> data = new RespData<>();
        data.setData(list);
        return data.builder();
    }

    @Operation(summary = "친구 삭제", description = "userId = 내 id, friendId = 삭제할 친구의 id")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFriend(@RequestParam("userId") int userId, @RequestParam("friendId") int friendId) {
        friendService.deleteFriend(userId,friendId);
        RespData<Void> data = new RespData<>();
        data.setMsg("친구 삭제");
        return data.builder();
    }

    @Operation(summary = "친구 요청")
    @PostMapping("/request")
    public ResponseEntity<?> requestFriend(@RequestBody FriendRequestDto friendRequestDto) {
        friendService.requestFriend(friendRequestDto);
        RespData<Void> data = new RespData<>();
        return data.builder();
    }

    @Operation(summary = "받은 친구 요청 조회", description = "나에게 친구요청한 목록 조회")
    @GetMapping("/to-request")
    public ResponseEntity<?> getFriendRequests(@RequestParam("userId") int toUserId) {
        List<FriendRequestDao> requests = friendService.getReceiveRequests(toUserId);
        RespData<List> data = new RespData<>();
        data.setData(requests);
        return data.builder();
    }

    @Operation(summary = "보낸 친구 요청 조회", description = "내가 친구요청한 목록 조회")
    @GetMapping("/from-request")
    public ResponseEntity<?> searchFriendRequests(@RequestParam("userId") int fromUserId) {
        List<FriendRequestDao> requests = friendService.getSendRequests(fromUserId);
        RespData<List> data = new RespData<>();
        data.setData(requests);
        return data.builder();
    }

    //친구요청 수락
    @Operation(summary = "친구 요청 수락")
    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        friendService.acceptRequests(friendRequestDto);
        RespData<Void> data = new RespData<>();
        data.setMsg("친구 요청 수락");
        return data.builder();
    }

    // 친구요청 거절
    @Operation(summary = "친구 요청 거절", description = "요청이 삭제됩니다.")
    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        friendService.rejectRequests(friendRequestDto);
        RespData<Void> data = new RespData<>();
        data.setMsg("친구 요청 거절");
        return data.builder();
    }


    // 친구목록에서 닉네임 검색
    @Operation(summary = "내 친구 목록에서 닉네임으로 조회", description = "친구 닉네임 조회")
    @GetMapping("/search")
    public ResponseEntity<?> searchFriend(@RequestParam("userId") int userId, @RequestParam("nickname") String nickname) {
        List<SimpleUserDto> friends = friendService.searchFriend(userId, nickname);
        RespData<List> data = new RespData<>();
        data.setData(friends);
        return data.builder();
    }

}
