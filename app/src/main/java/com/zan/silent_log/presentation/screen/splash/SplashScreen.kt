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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.zan.silent_log.R
import com.zan.silent_log.presentation.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // 페이드인 애니메이션을 위한 알파값
    val alpha = remember { Animatable(0f) }
    
    // 애니메이션 실행 효과
    LaunchedEffect(key1 = true) {
        // 알파값을 0에서 1로 애니메이션 (페이드인)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        
        // 스플래시 화면을 일정 시간 보여준 후 온보딩 화면으로 이동
        delay(1000)
        
        // 온보딩 화면으로 네비게이션
        val navOptions = NavOptions.Builder()
            .setPopUpTo(Screen.Splash.route, true)
            .build()
        navController.navigate(Screen.Onboarding.route, navOptions)
    }
    
    // 스플래시 화면 UI
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alpha.value)
                .padding(16.dp)
        ) {
            // 앱 로고
            Image(
                painter = painterResource(id = R.drawable.img_app_logo),
                contentDescription = "앱 로고",
                modifier = Modifier.size(150.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 앱 타이틀
            Text(
                text = "SilentLog",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 