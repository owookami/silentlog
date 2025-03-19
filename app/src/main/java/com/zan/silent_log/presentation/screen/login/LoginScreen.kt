package com.zan.silent_log.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.zan.silent_log.presentation.navigation.Screen
import com.zan.silent_log.util.AppDebugSettings
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // 디버깅 모드에서 로그인 건너뛰기 처리
    LaunchedEffect(Unit) {
        if (AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_LOGIN)) {
            // 잠시 대기 후 바로 메인 화면으로 이동
            delay(300)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(Screen.Login.route, true)
                .build()
            navController.navigate(Screen.Main.route, navOptions)
        }
    }
    
    // 로그인 화면 UI
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "로그인",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "설정한 PIN을 입력하여 로그인하세요",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // PIN 입력 구현 필요
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(Screen.Login.route, true)
                    .build()
                navController.navigate(Screen.Main.route, navOptions)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "로그인",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 