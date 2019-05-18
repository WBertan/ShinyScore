package com.bertan.shinyscore.presentation.mapper

import com.bertan.shinyscore.presentation.mapper.UserMapper.asUserView
import com.bertan.shinyscore.presentation.model.UserView
import com.bertan.shinyscore.presentation.test.UserDomainFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperSpec {
    @Test
    fun `given domain when mapping to view it should map`() {
        val domain = UserDomainFactory.get()
        val expectedResult =
            UserView(
                domain.id,
                domain.name,
                "https://api.adorable.io/avatars/200/${domain.name}"
            )

        val result = domain.asUserView

        assertEquals(expectedResult, result)
    }
}