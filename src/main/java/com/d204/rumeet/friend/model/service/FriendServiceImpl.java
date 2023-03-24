package com.d204.rumeet.friend.model.service;
import com.d204.rumeet.data.RespData;
import com.d204.rumeet.exception.NoRequestException;
import com.d204.rumeet.friend.model.dao.FriendDao;
import com.d204.rumeet.friend.model.dao.FriendRequestDao;
import com.d204.rumeet.friend.model.dto.FriendRequestDto;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class FriendServiceImpl implements FriendService {

    private final MongoTemplate mongoTemplate;

    public FriendServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<FriendDao> getFriendsByUserId(int userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, FriendDao.class, "friend");
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        Query query1 = new Query(Criteria.where("userId").is(userId)
                .and("friendId").is(friendId));
        Query query2 = new Query(Criteria.where("userId").is(friendId)
                .and("friendId").is(userId));

        mongoTemplate.remove(query1, FriendDao.class);
        mongoTemplate.remove(query2, FriendDao.class);
    }

    @Override
    public void requestFriend(FriendRequestDto friendRequestDto) {
        int fromId = friendRequestDto.getFromUserId();
        int toId = friendRequestDto.getToUserId();

        Query query = new Query(Criteria.where("fromUserId").is(fromId)
                .and("toUserId").is(toId));
        FriendRequestDao existingRequest = mongoTemplate.findOne(query, FriendRequestDao.class);

        if (existingRequest != null) {
            throw new DuplicateRequestException();

        } else {
            FriendRequestDao friendRequest = FriendRequestDao.builder()
                    .fromUserId(fromId)
                    .toUserId(toId)
                    .date(System.currentTimeMillis())
                    .build();
            mongoTemplate.insert(friendRequest);
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
        RespData<List> data = new RespData<>();

        // 요청이 없을떄
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
        RespData<List> data = new RespData<>();

        Query query = new Query(Criteria.where("fromUserId").is(fromId)
                .and("toUserId").is(toId));

        FriendRequestDao existingRequest = mongoTemplate.findOne(query, FriendRequestDao.class);

        if (existingRequest == null) {
            throw new NoRequestException();}
        else {
            mongoTemplate.remove(query, FriendRequestDao.class);
        }
    }


}

