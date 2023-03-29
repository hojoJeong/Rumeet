package com.d204.rumeet.badge.model.service;

import com.d204.rumeet.badge.model.dto.BadgeDto;

import java.util.List;

public interface BadgeService {

    BadgeDto getBadgeById(int id);

    void addBadge(int userId, int badgeId);

    List<BadgeDto> getAllBadgesByUserId(int userId);
}
