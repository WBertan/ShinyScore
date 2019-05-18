package com.bertan.shinyscore.data.mapper

import com.bertan.shinyscore.data.model.UserEntity
import com.bertan.shinyscore.domain.model.User

object UserMapper {
    val UserEntity.asUser: User
        get() =
            User(
                id,
                name
            )

    val User.asUserEntity: UserEntity
        get() =
            UserEntity(
                id,
                name
            )
}