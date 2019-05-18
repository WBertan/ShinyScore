package com.bertan.shinyscore.data.mapper

import com.bertan.shinyscore.data.mapper.UserMapper.asUser
import com.bertan.shinyscore.data.mapper.UserMapper.asUserEntity
import com.bertan.shinyscore.data.model.UserEntity
import com.bertan.shinyscore.data.test.UserDataFactory
import com.bertan.shinyscore.data.test.UserDomainFactory
import com.bertan.shinyscore.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperSpec {
    @Test
    fun `given domain when mapping to entity it should map`() {
        val domain = UserDomainFactory.get()
        val expectedResult =
            UserEntity(
                domain.id,
                domain.name
            )

        val result = domain.asUserEntity

        assertEquals(expectedResult, result)
    }

    @Test
    fun `given entity when mapping to domain it should map`() {
        val entity = UserDataFactory.get()
        val expectedResult =
            User(
                entity.id,
                entity.name
            )

        val result = entity.asUser

        assertEquals(expectedResult, result)
    }
}