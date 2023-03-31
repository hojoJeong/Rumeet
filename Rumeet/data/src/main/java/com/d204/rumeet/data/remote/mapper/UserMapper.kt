package com.d204.rumeet.data.remote.mapper

import com.d204.rumeet.data.remote.dto.request.user.ModifyUserDetailInfoRequestDto
import com.d204.rumeet.data.remote.dto.response.user.*
import com.d204.rumeet.domain.model.user.*

internal fun UserInfoResponse.toDomainModel() = UserInfoDomainModel(
    email,
    password,
    nickname,
    age,
    gender,
    profile,
    height,
    weight,
    date,
    state,
    oauth
)

internal fun ModifyUserDetailInfoDomainModel.toRequestDto() = ModifyUserDetailInfoRequestDto(
    age, gender, height, id, weight
)

internal fun AcquiredBadgeResponse.toDomainModel() = AcquiredBadgeListDomainModel(
    id = this.id,
    code = this.code,
    date = this.date
)

internal fun NotificationSettingStateResponseDto.toDomainModel() = NotificationStateDomainModel(
    friendAlarm = this.friendAlarm,
    matchingAlarm = this.matchingAlarm
)

internal fun RunningRecordRaceListResponseDto.toDomainModel() = RunningRecordActivityDomainModel(
    raceId = raceId,
    userId = userId,
    mode = mode,
    time = time,
    distance = km,
    pace = pace,
    heartRate = heartRate,
    calorie = kcal,
    success = success,
    polyLine = polyline,
    date = date
)

internal fun RunningRecordSummaryResponseDto.toDomainModel() = RunningRecordSummaryDomainModel(
    totalDistance = totalDistance,
    totalTime = totalTime,
    averagePace = averagePace
)

internal fun RunningRecordResponseDto.toDomainModel() = RunningRecordDomainModel(
    raceList = raceList.map { it.toDomainModel() },
    summaryData = summaryData.toDomainModel()
)

internal fun HomeDataResponseDto.toDomainModel() = HomeDataDomainModel(
    averagePace, nickname, totalCount, totalKm, userId
)