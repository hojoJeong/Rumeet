package com.d204.rumeet.badge.model.mapper;

import com.d204.rumeet.badge.model.dto.BadgeDto;
import com.d204.rumeet.badge.model.dto.MyBadgeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BadgeMapper {
    BadgeDto getBadgeById(int badgeId);

    void addBadge(MyBadgeDto myBadge);

    List<BadgeDto> getAllBadgesByUserId(int userId);

    List<BadgeDto> getRecentBadgesByUserId(int userId);

}
