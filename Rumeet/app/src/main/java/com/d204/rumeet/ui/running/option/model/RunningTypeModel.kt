package com.d204.rumeet.ui.running.option.model

data class RunningTypeModel(
    // 거리 or 협동의 난이도
    var distance: RunningDistance = RunningDistance.ONE,
    // 싱글, 싱글(고스트), 멀티(경쟁), 멀티(협동)
    var runningType: RunningType = RunningType.SINGLE,
    // 누구와 달릴지
    var runningDetailType: RunningDetailType = RunningDetailType.SINGLE,
    // 협동의 난이도
    var runningDifficulty : RunningDifficulty = RunningDifficulty.DEFAULT,
)

enum class RunningType {
    SINGLE, MULTI_COLLABORATION, SINGLE_GHOST, MULTI_COMPETITIVE
}

enum class RunningDistance {
    ONE, TWO, THREE, FIVE
}

enum class RunningDifficulty {
    EASY, NORMAL, HARD, DEFAULT
}

enum class RunningDetailType {
    FRIEND, RANDOM, GHOST_SINGLE, SINGLE, GHOST_FRIEND
}

