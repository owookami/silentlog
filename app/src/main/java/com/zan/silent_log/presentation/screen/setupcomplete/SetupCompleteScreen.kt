package com.zan.silent_log.presentation.screen.setupcomplete

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.zan.silent_log.presentation.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SetupCompleteScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // 애니메이션 상태
    var showIcon by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showDescription by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    
    // 순차적 애니메이션 실행
    LaunchedEffect(Unit) {
        // 아이콘 표시
        showIcon = true
        delay(400)
        
        // 제목 표시
        showTitle = true
        delay(300)
        
        // 설명 표시
        showDescription = true
        delay(300)
        
        // 버튼 표시
        showButton = true
    }
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            // 체크 아이콘 (성공 표시)
            AnimatedVisibility(
                visible = showIcon,
                enter = fadeIn(tween(500)) + 
                        slideInVertically(
                            animationSpec = tween(500),
                            initialOffsetY = { it / 2 }
                        )
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // 제목
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(tween(500)) + 
                        slideInVertically(
                            animationSpec = tween(500),
                            initialOffsetY = { it / 2 }
                        )
            ) {
                Text(
                    text = "설정 완료!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 설명
            AnimatedVisibility(
                visible = showDescription,
                enter = fadeIn(tween(500)) + 
                        slideInVertically(
                            animationSpec = tween(500),
                            initialOffsetY = { it / 2 }
                        )
            ) {
                Text(
                    text = "Silentlog 사용을 위한 모든 설정이 완료 되었습니다.\n이제 앱의 모든 기능을 7일간 무료로 사용할 수 있습니다.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 시작하기 버튼
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(tween(500)) + 
                        expandVertically(
                            animationSpec = tween(500),
                            expandFrom = Alignment.Top
                        )
            ) {
                Button(
                    onClick = {
                        // 메인 화면으로 이동
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(Screen.Splash.route, true)
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
                        text = "무료로 시작하기",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
} 