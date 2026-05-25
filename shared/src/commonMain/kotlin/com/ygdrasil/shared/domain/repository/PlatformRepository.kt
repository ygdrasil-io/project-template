package com.ygdrasil.shared.domain.repository

import com.ygdrasil.shared.domain.model.PlatformInfo
import kotlinx.coroutines.flow.Flow

interface PlatformRepository {
    fun getPlatformInfo(): Flow<Result<PlatformInfo>>
}
