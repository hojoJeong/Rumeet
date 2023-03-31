package com.d204.rumeet.friend.model.service;
import com.d204.rumeet.exception.DuplicateFriendRequestException;
import com.d204.rumeet.exception.ExistingFriendException;
import com.d204.rumeet.exception.NoFriendDataException;
import com.d204.rumeet.exception.NoRequestException;
import com.d204.rumeet.fcm.model.service.FcmMessageService;
import com.d204.rumeet.friend.model.dao.FriendDao;
import com.d204.rumeet.friend.model.dao.FriendRequestDao;
import com.d204.rumeet.friend.model.dto.FriendRequestDto;
import com.d204.rumeet.user.model.dto.SimpleUserDto;
import com.d204.rumeet.user.model.dto.SimpleUserFcmDto;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final MongoTemplate mongoTemplate;

    private final UserService userService;

    private final FcmMessageService fcmMessageService;


    @Override
    public List<SimpleUserDto> getFriendsByUserId(int userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<FriendDao> friends = mongoTemplate.find(query, FriendDao.class, "friend");

        Set<Integer> friendIds = new HashSet<>();
        for (FriendDao friend : friends) {
            friendIds.add(friend.getFriendId());
        }

        List<SimpleUserDto> filteredFriends = new ArrayList<>();
        for (int friendId : friendIds) {
            filteredFriends.add(userService.getSimpleUserById(friendId));
        }
        return filteredFriends;
    }


    @Override
    public void deleteFriend(int userId, int friendId) {
        Query query1 = new Query(Criteria.where("userId").is(userId)
                .and("friendId").is(friendId));
        Query query2 = new Query(Criteria.where("userId").is(friendId)
                .and("friendId").is(userId));
        FriendDao existingFriend = mongoTemplate.findOne(query1, FriendDao.class);
        FriendDao existingFriend2 = mongoTemplate.findOne(query1, FriendDao.class);
        if (existingFriend ==null && existingFriend2 == null){
            throw new NoFriendDataException();
        }
        else{
            mongoTemplate.remove(query1, FriendDao.class);
            mongoTemplate.remove(query2, FriendDao.class);
        }
    }

    @Override
    public void requestFriend(FriendRequestDto friendRequestDto) {
        int fromId = friendRequestDto.getFromUserId();
        int toId = friendRequestDto.getToUserId();

        Query query1 = new Query(Criteria.where("fromUserId").is(fromId)
                .and("toUserId").is(toId));

        Query query2 = new Query(Criteria.where("userId").is(fromId)
                .and("friendId").is(toId));

        FriendRequestDao existingRequest = mongoTemplate.findOne(query1, FriendRequestDao.class);

        FriendDao existingFriend = mongoTemplate.findOne(query2, FriendDao.class);

        Long current = System.currentTimeMillis();

        if (existingRequest != null) {
            throw new DuplicateFriendRequestException();
        } else if (existingFriend!=null){
            throw new ExistingFriendException();
        }else {
            FriendRequestDao friendRequest = FriendRequestDao.builder()
                    .fromUserId(fromId)
                    .toUserId(toId)
                    .date(current)
                    .build();
            mongoTemplate.insert(friendRequest);
        }
        // toUserId인 유저의 친구 요청 알림 수신 여부 확인
        SimpleUserFcmDto me = userService.getSimpleUserFcmInfoById(fromId);
        SimpleUserFcmDto friend = userService.getSimpleUserFcmInfoById(toId);
        try{
            if (friend.getFriendAlarm() == 1) { // toUserId인 유저에게 FCM 전송
                fcmMessageService.sendMessageTo(friend.getFcmToken(),
                        "친구 요청",
                        me.getNickname()+"님으로부터 친구요청이 왔습니다.",
                        fromId, -1, current);
            }
        } catch (IOException e) {

        }
    }


    @Override
    public List<FriendRequestDao> getReceiveRequests(int toUserId) {
        List<FriendRequestDao> requests = mongoTemplate.find(
                Query.query(Criteria.where("toUserId").is(toUserId)),
                FriendRequestDao.class
        );
        return requests;
    }

    @Override
    public List<FriendRequestDao> getSendRequests(int fromUserId) {
        List<FriendRequestDao> requests = mongoTemplate.find(
                Query.query(Criteria.where("fromUserId").is(fromUserId)),
                FriendRequestDao.class
        );
        return requests;
    }


    @Override
    public void acceptRequests(FriendRequestDto friendRequestDto) {
        int fromId = friendRequestDto.getFromUserId();
        int toId = friendRequestDto.getToUserId();

        Query query = new Query(Criteria.where("fromUserId").is(fromId)
                .and("toUserId").is(toId));
        FriendRequestDao existingRequest = mongoTemplate.findOne(query, FriendRequestDao.class);

        if (existingRequest == null) {
            throw new NoRequestException();}

        else{
            mongoTemplate.remove(query, FriendRequestDao.class);
            FriendDao friend = FriendDao.builder()
                    .userId(toId)
                    .friendId(fromId)
                    .date(System.currentTimeMillis())
                    .build();
            mongoTemplate.insert(friend);

            FriendDao friend2 = FriendDao.builder()
                    .userId(fromId)
                    .friendId(toId)
                    .date(System.currentTimeMillis())
                    .build();
            mongoTemplate.insert(friend2);
            }
        }

    @Override
    public void rejectRequests(FriendRequestDto friendRequestDto) {
        int fromId = friendRequestDto.getFromUserId();
        int toId = friendRequestDto.getToUserId();

        Query query = new Query(Criteria.where("fromUserId").is(fromId)
                .and("toUserId").is(toId));

        FriendRequestDao existingRequest = mongoTemplate.findOne(query, FriendRequestDao.class);

        if (existingRequest == null) {
            throw new NoRequestException();}
        else {
            mongoTemplate.remove(query, FriendRequestDao.class);
        }
    }

    @Override
    public List<SimpleUserDto> searchFriend(int userId, String nickname) {
        List<SimpleUserDto> users = userService.searchUsersByNickname(nickname);

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<FriendDao> friends = mongoTemplate.find(query, FriendDao.class, "friend");

        Set<Integer> friendId = new HashSet<>();
        for (FriendDao friend : friends) {
            friendId.add(friend.getFriendId());
        }

        List<SimpleUserDto> filteredFriends = new ArrayList<>();
        for (SimpleUserDto user : users) {
            if (friendId.contains(user.getId())) {
                filteredFriends.add(user);
            }
        }
        return filteredFriends;

    }
}

