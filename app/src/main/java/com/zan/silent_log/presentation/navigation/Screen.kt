package com.zan.silent_log.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Onboarding : Screen("onboarding_screen")
    object TermsAgreement : Screen("terms_agreement_screen")
    object Permissions : Screen("permissions_screen")
    object PinSetup : Screen("pin_setup_screen")
    object Login : Screen("login_screen")
    object Main : Screen("main_screen")
    object Settings : Screen("settings_screen") 
    object PhoneNumber : Screen("phone_number_screen")
    object CallLog : Screen("call_log_screen")
    
    // 파라미터가 필요한 경우 추가
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
} 