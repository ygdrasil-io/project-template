package com.ygdrasil.shared.domain.usecase

import com.ygdrasil.shared.domain.model.PlatformInfo
import com.ygdrasil.shared.domain.repository.PlatformRepository
import kotlinx.coroutines.flow.Flow

class GetPlatformInfoUseCase(
    private val repository: PlatformRepository
) {
    operator fun invoke(): Flow<Result<PlatformInfo>> {
        return repository.getPlatformInfo()
    }
}
