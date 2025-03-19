package com.zan.silent_log.presentation.screen.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.zan.silent_log.R
import com.zan.silent_log.presentation.navigation.Screen
import com.zan.silent_log.util.AppDebugSettings
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // 페이드인 애니메이션을 위한 알파값
    val alpha = remember { Animatable(0f) }
    
    // 애니메이션 실행 효과
    LaunchedEffect(key1 = true) {
        // 디버깅 상태 로깅
        AppDebugSettings.logDebugStatus()
        
        // 알파값을 0에서 1로 애니메이션 (페이드인)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        
        // 스플래시 화면을 일정 시간 보여준 후 다음 화면으로 이동
        delay(1000)
        
        // 디버깅 모드에서 온보딩 화면 건너뛰기가 활성화되어 있으면 바로 메인 화면으로 이동
        if (AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_ONBOARDING)) {
            // 메인 화면으로 직접 이동
            val navOptions = NavOptions.Builder()
                .setPopUpTo(Screen.Splash.route, true)
                .build()
            navController.navigate(Screen.Main.route, navOptions)
        } else {
            // 온보딩 화면으로 이동 (기존 플로우)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(Screen.Splash.route, true)
                .build()
            navController.navigate(Screen.Onboarding.route, navOptions)
        }
    }
    
    // 스플래시 화면 UI
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alpha.value)
                .padding(16.dp)
        ) {
            // 앱 로고 또는 아이콘
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "앱 로고",
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 앱 이름
            Text(
                text = "Silent Log",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
} 