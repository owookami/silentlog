package com.zan.silent_log

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zan.silent_log.presentation.navigation.Screen
import com.zan.silent_log.presentation.screen.login.LoginScreen
import com.zan.silent_log.presentation.screen.main.MainScreen
import com.zan.silent_log.presentation.screen.onboarding.OnboardingScreen
import com.zan.silent_log.presentation.screen.permissions.PermissionsScreen
import com.zan.silent_log.presentation.screen.pinsetup.PinSetupScreen
import com.zan.silent_log.presentation.screen.splash.SplashScreen
import com.zan.silent_log.presentation.screen.terms.TermsAgreementScreen
import com.zan.silent_log.ui.theme.Silent_logTheme
import com.zan.silent_log.util.PermissionManager

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Silent_logTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SilentLogNavHost()
                }
            }
        }
        
        // 개발 모드에서 권한 상태 로깅
        if (PermissionManager.DEV_MODE) {
            PermissionManager.logPermissionStatus(this)
            Log.d(TAG, "앱 시작: 개발 모드")
        }
    }

    override fun onStop() {
        super.onStop()
        if (PermissionManager.DEV_MODE && isFinishing) {
            // 애플리케이션이 완전히 종료될 때 권한 상태 저장
            PermissionManager.savePermissionResetTime(this)
            PermissionManager.logPermissionStatus(this)
            Log.d(TAG, "앱 종료 시 권한 상태 기록 완료 (개발 모드)")
        }
    }
    
    override fun onResume() {
        super.onResume()
        // 개발 모드에서 권한 상태 로깅
        if (PermissionManager.DEV_MODE) {
            PermissionManager.logPermissionStatus(this)
        }
    }
}

@Composable
fun SilentLogNavHost() {
    val navController = rememberNavController()
    
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Splash.route) {
                SplashScreen(navController = navController)
            }
            
            composable(route = Screen.Onboarding.route) {
                OnboardingScreen(navController = navController)
            }
            
            composable(route = Screen.TermsAgreement.route) {
                TermsAgreementScreen(navController = navController)
            }
            
            composable(route = Screen.Permissions.route) {
                PermissionsScreen(navController = navController)
            }
            
            composable(route = Screen.PinSetup.route) {
                PinSetupScreen(navController = navController)
            }
            
            composable(route = Screen.Login.route) {
                LoginScreen(navController = navController)
            }
            
            composable(route = Screen.Main.route) {
                MainScreen(navController = navController)
            }
            
            // 추가 화면은 필요할 때 구현할 예정입니다.
        }
    }
}