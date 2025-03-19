package com.zan.silent_log.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // 상태 관리
    var selectedNavItem by remember { mutableIntStateOf(0) }
    var isServiceRunning by remember { mutableStateOf(false) }
    
    // 애니메이션 관련 상태
    var showSummary by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }
    
    // 전화번호 입력 관련 상태
    var phoneNumber by remember { mutableStateOf("") }
    var selectedCountryCode by remember { mutableStateOf("+82") }
    var isCountryCodeMenuExpanded by remember { mutableStateOf(false) }
    
    // 국가 코드 리스트 (예시)
    val countryCodes = listOf("+82", "+1", "+44", "+81", "+86")
    
    // 무료 체험 기간 (임의로 7일 중 5일 남은 것으로 설정)
    val totalTrialDays = 7
    val remainingDays = 5
    
    // 애니메이션 실행
    LaunchedEffect(Unit) {
        showSummary = true
        delay(300)
        showButtons = true
    }
    
    // 샘플 통화 데이터 (통계 표시용)
    val callLogs = remember {
        listOf(
            CallLog("010-1234-5678", "홍길동", CallType.INCOMING, LocalDateTime.now().minusHours(1), 125),
            CallLog("010-9876-5432", "김철수", CallType.OUTGOING, LocalDateTime.now().minusHours(3), 45),
            CallLog("010-1111-2222", "이영희", CallType.MISSED, LocalDateTime.now().minusHours(5), 0)
        )
    }
    
    // 통화 통계 데이터
    val callStats = remember {
        CallStats(
            totalCalls = callLogs.size,
            incomingCalls = callLogs.count { it.type == CallType.INCOMING },
            outgoingCalls = callLogs.count { it.type == CallType.OUTGOING },
            deletedCalls = 2, // 삭제된 통화 수 (임의의 데이터)
            totalDuration = callLogs.sumOf { it.durationInSeconds },
            silencedCalls = 1 // 임의의 데이터
        )
    }
    
    // 서비스 시작/중지 함수
    fun toggleService() {
        isServiceRunning = !isServiceRunning
        // TODO: 실제 서비스 시작/중지 로직 구현
    }
    
    // 번호 추가 함수
    fun addPhoneNumber() {
        if (phoneNumber.isNotEmpty()) {
            // TODO: 번호 추가 로직 구현
            phoneNumber = "" // 입력 필드 초기화
        }
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "SilentLog",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedNavItem == 0,
                    onClick = { selectedNavItem = 0 },
                    icon = { 
                        Icon(
                            imageVector = Icons.Default.Home, 
                            contentDescription = "홈"
                        ) 
                    },
                    label = { Text("홈") }
                )
                NavigationBarItem(
                    selected = selectedNavItem == 1,
                    onClick = { selectedNavItem = 1 },
                    icon = { 
                        Icon(
                            imageVector = Icons.Default.Person, 
                            contentDescription = "프로필"
                        ) 
                    },
                    label = { Text("프로필") }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                
                // 무료 체험 기간 카운터 배너
                AnimatedVisibility(
                    visible = showSummary,
                    enter = fadeIn(tween(500)) + 
                            slideInVertically(
                                animationSpec = tween(500),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    TrialPeriodBanner(
                        totalDays = totalTrialDays, 
                        remainingDays = remainingDays,
                        onUpgradeClick = { /* 구독 화면으로 이동 */ }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 통화 통계 요약 섹션
                AnimatedVisibility(
                    visible = showSummary,
                    enter = fadeIn(tween(500)) + 
                            slideInVertically(
                                animationSpec = tween(500),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    CallStatsSummary(callStats = callStats)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 번호 관리 섹션
                AnimatedVisibility(
                    visible = showButtons,
                    enter = fadeIn(tween(500)) + 
                            slideInVertically(
                                animationSpec = tween(500),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // 섹션 제목
                        Text(
                            text = "관리 번호 추가하기",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // 전화번호 입력 필드
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 국가 코드 선택 드롭다운
                            Box {
                                OutlinedTextField(
                                    value = selectedCountryCode,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.width(80.dp),
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "국가 코드 선택",
                                            modifier = Modifier.clickable { isCountryCodeMenuExpanded = true }
                                        )
                                    },
                                    shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp, topEnd = 0.dp, bottomEnd = 0.dp)
                                )
                                
                                DropdownMenu(
                                    expanded = isCountryCodeMenuExpanded,
                                    onDismissRequest = { isCountryCodeMenuExpanded = false }
                                ) {
                                    countryCodes.forEach { code ->
                                        DropdownMenuItem(
                                            text = { Text(code) },
                                            onClick = {
                                                selectedCountryCode = code
                                                isCountryCodeMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            
                            // 전화번호 입력 필드
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("전화번호 추가") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            // 추가 버튼
                            IconButton(
                                onClick = { addPhoneNumber() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "번호 추가",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // 서비스 시작/중지 버튼
                        Button(
                            onClick = { toggleService() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isServiceRunning) 
                                    MaterialTheme.colorScheme.error 
                                else 
                                    MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isServiceRunning) 
                                    Icons.Default.Close
                                else 
                                    Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isServiceRunning) "서비스 중지하기" else "서비스 시작하기",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// 무료 체험 기간 배너
@Composable
fun TrialPeriodBanner(
    totalDays: Int,
    remainingDays: Int,
    onUpgradeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 타이머 아이콘
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 무료 체험 기간 텍스트
                Text(
                    text = "무료 체험 기간",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // 남은 일수 표시
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "D-$remainingDays",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 진행률 표시 바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((remainingDays.toFloat() / totalDays))
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = remainingDays.toString() + "일 남았습니다",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                TextButton(
                    onClick = onUpgradeClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "구독",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// 통화 통계 요약 컴포넌트
@Composable
fun CallStatsSummary(callStats: CallStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "통화 통계",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Call,
                    value = callStats.totalCalls.toString(),
                    label = "전체",
                    color = MaterialTheme.colorScheme.primary
                )
                
                StatItem(
                    icon = Icons.Default.KeyboardArrowDown,
                    value = callStats.incomingCalls.toString(),
                    label = "수신",
                    color = MaterialTheme.colorScheme.tertiary
                )
                
                StatItem(
                    icon = Icons.Default.KeyboardArrowUp,
                    value = callStats.outgoingCalls.toString(),
                    label = "발신",
                    color = MaterialTheme.colorScheme.secondary
                )
                
                StatItem(
                    icon = Icons.Default.Delete,
                    value = callStats.deletedCalls.toString(),
                    label = "삭제",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// 통계 아이템 컴포넌트
@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// 데이터 클래스들
data class CallLog(
    val phoneNumber: String,
    val name: String? = null,
    val type: CallType,
    val timestamp: LocalDateTime,
    val durationInSeconds: Int
)

enum class CallType {
    INCOMING, OUTGOING, MISSED
}

data class CallStats(
    val totalCalls: Int,
    val incomingCalls: Int,
    val outgoingCalls: Int,
    val deletedCalls: Int,
    val totalDuration: Int,
    val silencedCalls: Int
)

// 유틸리티 함수
fun formatDuration(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    
    return when {
        hours > 0 -> String.format("%d시간 %d분 %d초", hours, minutes, remainingSeconds)
        minutes > 0 -> String.format("%d분 %d초", minutes, remainingSeconds)
        else -> String.format("%d초", remainingSeconds)
    }
} 