package com.d204.rumeet.badge.model.service;

import com.d204.rumeet.badge.model.dto.BadgeDto;
import com.d204.rumeet.badge.model.mapper.BadgeMapper;
import com.d204.rumeet.exception.NoBadgeDataException;
import com.d204.rumeet.exception.NoUserDataException;
import com.d204.rumeet.user.model.dto.UserDto;
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
    public void addBadge(BadgeDto badge) {

    }

    @Override
    public List<BadgeDto> getAllBadgesByUserId(int userId) {
        return null;
    }
}
