package com.ygdrasil.shared.di

import com.ygdrasil.shared.data.repository.PlatformRepositoryImpl
import com.ygdrasil.shared.domain.repository.PlatformRepository
import com.ygdrasil.shared.domain.usecase.GetPlatformInfoUseCase
import com.ygdrasil.shared.presentation.PlatformViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    // Data Layer
    singleOf(::PlatformRepositoryImpl) bind PlatformRepository::class

    // Domain Layer
    factoryOf(::GetPlatformInfoUseCase)

    // Presentation Layer (ViewModel & Scope)
    factory { CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) }
    factory { PlatformViewModel(get(), get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(sharedModule)
    }

// Raccourci pour iOS/Desktop pour démarrer Koin sans déclaration
fun initKoin() = initKoin {}
