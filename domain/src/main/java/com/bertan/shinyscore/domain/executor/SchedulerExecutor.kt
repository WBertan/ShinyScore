package com.bertan.shinyscore.domain.executor

import io.reactivex.Scheduler

interface SchedulerExecutor {
    val scheduler: Scheduler
}