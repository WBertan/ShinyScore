package com.bertan.shinyscore.presentation.mapper

import com.bertan.shinyscore.domain.model.User
import com.bertan.shinyscore.presentation.model.UserView

object UserMapper {
    val User.asUserView: UserView
        get() =
            UserView(
                id,
                name,
                "https://api.adorable.io/avatars/200/$name"
            )

    val UserView.asUser: User
        get() =
            User(
                id,
                name
            )
}