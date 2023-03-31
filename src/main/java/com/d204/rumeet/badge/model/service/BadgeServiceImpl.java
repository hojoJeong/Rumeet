package com.d204.rumeet.badge.model.service;

import com.d204.rumeet.badge.model.dto.BadgeDto;
import com.d204.rumeet.badge.model.mapper.BadgeMapper;
import com.d204.rumeet.exception.NoBadgeDataException;
import com.d204.rumeet.record.model.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService{

    private final BadgeMapper badgeMapper;


    @Override
    public BadgeDto getBadgeById(int id) {
        BadgeDto badge = badgeMapper.getBadgeById(id);
        if (badge==null) {
            throw new NoBadgeDataException();
        }
        return badge;
    }


    @Override
    public void addBadge(int userId) {
//        RecordDto record = recordService.getRecord(userId);
//        double km = record.getTotalKm();
//        long time = record.getTotalTime();
//        int competitionCount = record.getCompetitionSuccessCount();
//        int teamCount = record.getTeamSuccessCount();
//        long date = System.currentTimeMillis();
//
//        // 경쟁 뱃지
//        int[] competitionCounts = {30, 20, 10};
//        int[] competitionBadges = {3, 2, 1};
//
//        if (competitionCount == 1){
//            MyBadgeDto myBadge = new MyBadgeDto(userId, 7, date); //경쟁 첫번째 승리
//            badgeMapper.addBadge(myBadge);
//        } else {
//            for (int i = 0; i < competitionCounts.length; i++) {
//                if (competitionCount >= competitionCounts[i]) {
//                    MyBadgeDto myBadge = new MyBadgeDto(userId, competitionBadges[i], date);
//                    badgeMapper.addBadge(myBadge);
//                    break;
//                }
//            }
//        }
//
//        // 협동 뱃지
//        int[] teamCounts = {30, 20, 10};
//        int[] teamBadges = {16, 15, 14};
//        for (int i = 0; i < teamCounts.length; i++) {
//            if (teamCount >= teamCounts[i]) {
//                MyBadgeDto myBadge = new MyBadgeDto(userId, teamBadges[i], date);
//                badgeMapper.addBadge(myBadge);
//                break;
//            }
//        }
//
//        // km 뱃지
//        int[] kmGoals = {1000, 500, 100};
//        int[] kmBadges = {5, 6, 4};
//        for (int i = 0; i < kmGoals.length; i++) {
//            if (km >= kmGoals[i]) {
//                MyBadgeDto myBadge = new MyBadgeDto(userId, kmBadges[i], date);
//                badgeMapper.addBadge(myBadge);
//                break;
//            }
//        }
//
//        // time 뱃지
//        int[] timeGoals = {180000, 72000, 36000}; //50시간, 20시간, 10시간
//        int[] timeBadges = {10, 9, 8};
//        for (int i = 0; i < timeGoals.length; i++) {
//            if (time >= timeGoals[i]) {
//                MyBadgeDto myBadge = new MyBadgeDto(userId, timeBadges[i], date);
//                badgeMapper.addBadge(myBadge);
//                break;
//            }
//        }
    }

    @Override
    public List<BadgeDto> getAllBadgesByUserId(int userId) {
        List<BadgeDto> badges = badgeMapper.getAllBadgesByUserId(userId);
        return badges;
    }

    @Override
    public List<BadgeDto> getRecentBadgesByUserId(int userId) {
        List<BadgeDto> badges = badgeMapper.getAllBadgesByUserId(userId);
        return badges;
    }
}
