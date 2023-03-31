package com.d204.rumeet.badge.model.service;

import com.d204.rumeet.badge.model.dto.BadgeDto;

import java.util.List;

public interface BadgeService {

    BadgeDto getBadgeById(int id);

  //  void addDefaultBadge(int userId, int badgeId);

    void addBadge(int userId);

    List<BadgeDto> getAllBadgesByUserId(int userId);

    List<BadgeDto> getRecentBadgesByUserId(int userId);

}
