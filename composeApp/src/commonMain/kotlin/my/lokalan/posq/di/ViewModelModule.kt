package my.lokalan.posq.di

import my.lokalan.posq.presentation.home.HomeViewModel
import my.lokalan.posq.presentation.home.member.MemberDetailViewModel
import my.lokalan.posq.presentation.login.LoginViewModel
import my.lokalan.posq.presentation.splash.SplashViewModel
import my.lokalan.posq.presentation.transaction.TransactionViewModel
import my.lokalan.posq.presentation.transaction.addtransaction.AddTransactionViewModel
import my.lokalan.posq.presentation.user.ListUserViewModel
import my.lokalan.posq.presentation.user.adduser.AddUserViewModel
import my.lokalan.posq.presentation.user.changepassword.ChangePasswordViewModel
import my.lokalan.posq.presentation.user.editprofile.EditProfileViewModel
import my.lokalan.posq.presentation.user.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ListUserViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AddUserViewModel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::MemberDetailViewModel)
    viewModelOf(::ChangePasswordViewModel)
    viewModelOf(::TransactionViewModel)
    viewModelOf(::AddTransactionViewModel)
}
