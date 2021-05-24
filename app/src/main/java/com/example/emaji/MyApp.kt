package com.example.emaji

import android.app.Application
import com.example.emaji.repositories.*
import com.example.emaji.ui.cycle.CycleViewModel
import com.example.emaji.ui.login.LoginViewModel
import com.example.emaji.ui.main.file.FileViewModel
import com.example.emaji.ui.main.history.HistoryViewModel
import com.example.emaji.ui.main.home.HomeViewModel
import com.example.emaji.ui.main.profile.ProfileViewModel
import com.example.emaji.ui.task.TaskViewModel
import com.example.emaji.ui.update_password.UpdatePasswordViewModel
import com.example.emaji.ui.update_profile.UpdateProfileState
import com.example.emaji.ui.update_profile.UpdateProfileViewModel
import com.example.emaji.webservices.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.logger.Level

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module {
    single { ApiClient.instance() }
//    single { FirebaseRepository() }
}

val repositoryModules = module {
    factory { CycleRepository(get()) }
    factory { TaskRepository(get()) }
    factory { ToolRepository(get()) }
    factory { UserRepository(get()) }
    factory { HistoryRepository(get()) }
    factory { FileRepository(get()) }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { CycleViewModel(get()) }
    viewModel { TaskViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { FileViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UpdateProfileViewModel(get()) }
    viewModel { UpdatePasswordViewModel(get()) }
}