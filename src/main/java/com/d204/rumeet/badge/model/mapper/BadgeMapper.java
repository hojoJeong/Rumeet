package com.d204.rumeet.badge.model.mapper;

import com.d204.rumeet.badge.model.dto.BadgeDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BadgeMapper {
    BadgeDto getBadgeById(int badgeId);

    void addBadge(int badgeId, int userId, long date);
}
