package com.d204.rumeet.friend.model.service;
import com.d204.rumeet.exception.DuplicateFriendRequestException;
import com.d204.rumeet.exception.ExistingFriendException;
import com.d204.rumeet.exception.NoFriendDataException;
import com.d204.rumeet.exception.NoRequestException;
import com.d204.rumeet.fcm.model.service.FcmMessageService;
import com.d204.rumeet.friend.model.dao.FriendDao;
import com.d204.rumeet.friend.model.dao.FriendRequestDao;
import com.d204.rumeet.friend.model.dto.*;
import com.d204.rumeet.friend.model.mapper.FriendMapper;
import com.d204.rumeet.user.model.dto.SimpleUserDto;
import com.d204.rumeet.user.model.dto.SimpleUserFcmDto;
import com.d204.rumeet.user.model.dto.UserDto;
import com.d204.rumeet.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {

    private final MongoTemplate mongoTemplate;

    private final UserService userService;

    private final FcmMessageService fcmMessageService;

    private final FriendMapper friendMapper;

    @Override
    public List<FriendListDto> getFilteredFriendsByUserId(int userId, int type){
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<FriendDao> friends = mongoTemplate.find(query, FriendDao.class, "friend");

        Set<Integer> friendIds = new HashSet<>();
        for (FriendDao friend : friends) {
            friendIds.add(friend.getFriendId());
        }
        System.out.println(friendIds);
        List<FriendListDto> friendList = new ArrayList<>();
        for (int friendId : friendIds) {
            int cnt = friendMapper.getMatchCount(userId, friendId);
            if (cnt > 0) {
                System.out.println(friendId+"cnt있음"+ cnt);
                FriendListDto friend = friendMapper.getRunningFriend(userId, friendId);
                if (friend != null) {
                    friendList.add(friend);
                }
            } else {
                System.out.println(friendId+"cnt없음"+ cnt);
                FriendListDto friend = friendMapper.getFriend(friendId);
                System.out.println(friend);
                if (friend != null) {
                    friendList.add(friend);
                }
            }
        }
            if (type == 2) {
                Collections.sort(friendList, (x, y) -> -Long.compare(x.getLatestDate(), y.getLatestDate()));
            } else if (type == 3) {
                Collections.sort(friendList, (x, y) -> -Integer.compare(x.getMatchCount(), y.getMatchCount()));
            }

            return friendList;
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

        SimpleUserFcmDto toUser = userService.getSimpleUserFcmInfoById(toId);
        SimpleUserFcmDto fromUser = userService.getSimpleUserFcmInfoById(fromId);

        log.warn("##################fromUser:"+fromUser.toString());
        log.warn("##################toUser"+toUser.toString());


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
                    .fromUserName(fromUser.getNickname())
                    .date(current)
                    .build();
            mongoTemplate.insert(friendRequest);
        }

        log.warn("##################fromUser:"+fromUser.toString());
        log.warn("##################toUser"+toUser.toString());
        // toUserId인 유저의 친구 요청 알림 수신 여부 확인
        try{
            if (toUser.getFriendAlarm() == 1) { // toUserId인 유저에게 FCM 전송
                fcmMessageService.sendMessageTo(toUser.getFcmToken(),
                        "친구 요청",
                        fromUser.getNickname()+"님으로부터 친구요청이 왔습니다.",
                        1);
            }
        } catch (IOException e) {

        }
    }


    @Override
    public List<FriendRequestInfoDto> getReceiveRequests(int toUserId) {

        List<FriendRequestDao> requests = mongoTemplate.find(
                Query.query(Criteria.where("toUserId").is(toUserId)),
                FriendRequestDao.class
        );
        List<FriendRequestInfoDto> list = new ArrayList<>();
        for (FriendRequestDao dto : requests){
            FriendRequestInfoDto tmp = new FriendRequestInfoDto();
            UserDto user = userService.getUserById(dto.getFromUserId());
            tmp.setId(dto.getId());
            tmp.setFromUserId(user.getId());
            tmp.setDate(dto.getDate());
            tmp.setFromUserName(user.getNickname());
            tmp.setFromUserProfile(user.getProfile());
            tmp.setToUserId(toUserId);
        }
        return list;
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

