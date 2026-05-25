package com.ygdrasil.shared.data.repository

import com.ygdrasil.shared.Platform
import com.ygdrasil.shared.domain.model.PlatformInfo
import com.ygdrasil.shared.domain.model.PlatformName
import com.ygdrasil.shared.domain.repository.PlatformRepository
import com.ygdrasil.shared.getPlatform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlatformRepositoryImpl : PlatformRepository {
    override fun getPlatformInfo(): Flow<Result<PlatformInfo>> = flow {
        val result = try {
            val platform: Platform = getPlatform()
            Result.success(
                PlatformInfo(
                    name = PlatformName(platform.name),
                    osVersion = platform.osVersion,
                    isSupported = true
                )
            )
        } catch (e: Exception) {
            if (e is kotlinx.coroutines.CancellationException) throw e
            Result.failure(e)
        }
        emit(result)
    }
}
