package com.zan.silent_log.presentation.screen.pinsetup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.zan.silent_log.presentation.navigation.Screen
import kotlinx.coroutines.delay

// PIN의 길이
private const val PIN_LENGTH = 4

@Composable
fun PinSetupScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // PIN 상태
    var firstPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var isPinConfirmStep by remember { mutableStateOf(false) }
    var isPinMismatch by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    // 핀 입력 후 확인 단계로 전환
    fun proceedToConfirmPin() {
        isPinConfirmStep = true
    }
    
    // 핀 확인
    fun verifyPin() {
        if (firstPin == confirmPin) {
            // 핀 저장 로직
            savePin(context, firstPin)
            showSuccessDialog = true
        } else {
            // 핀 불일치
            isPinMismatch = true
            confirmPin = ""
        }
    }
    
    // 핀 리셋
    fun resetPinSetup() {
        firstPin = ""
        confirmPin = ""
        isPinConfirmStep = false
        isPinMismatch = false
    }
    
    // 숫자 입력 처리
    fun onNumberClick(number: Int) {
        if (!isPinConfirmStep) {
            // 첫 번째 PIN 입력 단계
            if (firstPin.length < PIN_LENGTH) {
                firstPin += number.toString()
                
                // PIN 길이가 PIN_LENGTH에 도달하면 확인 단계로 자동 전환
                if (firstPin.length == PIN_LENGTH) {
                    // 짧은 지연 후 확인 단계로 이동
                    isPinConfirmStep = true
                }
            }
        } else {
            // 확인 단계
            if (confirmPin.length < PIN_LENGTH) {
                confirmPin += number.toString()
                
                // 확인 PIN 입력이 완료되면 자동으로 검증
                if (confirmPin.length == PIN_LENGTH) {
                    verifyPin()
                }
            }
        }
    }
    
    // 백스페이스 처리
    fun onBackspaceClick() {
        if (!isPinConfirmStep) {
            if (firstPin.isNotEmpty()) {
                firstPin = firstPin.dropLast(1)
            }
        } else {
            if (confirmPin.isNotEmpty()) {
                confirmPin = confirmPin.dropLast(1)
                isPinMismatch = false // 수정 시 오류 메시지 숨김
            }
        }
    }
    
    // PIN 불일치 에러 메시지 표시 후 자동으로 지우기
    LaunchedEffect(isPinMismatch) {
        if (isPinMismatch) {
            delay(1500)
            isPinMismatch = false
        }
    }
    
    // 화면 UI
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = { 
                        if (isPinConfirmStep) {
                            // 확인 단계에서는 처음으로 돌아가기
                            resetPinSetup()
                        } else {
                            // 처음 단계에서는 이전 화면으로 돌아가기
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로 가기"
                    )
                }
                
                Text(
                    text = if (isPinConfirmStep) "PIN 확인" else "PIN 설정",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 아이콘 및 안내 텍스트
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (isPinConfirmStep) "PIN을 다시 입력해주세요" else "보안을 위한 PIN을 설정해주세요",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (isPinConfirmStep) "확인을 위해 동일한 PIN을 한번 더 입력하세요" 
                       else "앱 실행 시 PIN을 입력하여 앱에 접근할 수 있습니다",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // PIN 입력 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(PIN_LENGTH) { index ->
                    val isFilledFirst = index < firstPin.length && !isPinConfirmStep
                    val isFilledConfirm = index < confirmPin.length && isPinConfirmStep
                    
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                if (isFilledFirst || isFilledConfirm) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                    )
                }
            }
            
            // PIN 불일치 에러 메시지
            AnimatedVisibility(
                visible = isPinMismatch,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Text(
                    text = "PIN이 일치하지 않습니다. 다시 시도해주세요.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 숫자 키패드
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 숫자 키패드 행
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    NumberKey(number = 1, onClick = { onNumberClick(1) })
                    NumberKey(number = 2, onClick = { onNumberClick(2) })
                    NumberKey(number = 3, onClick = { onNumberClick(3) })
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    NumberKey(number = 4, onClick = { onNumberClick(4) })
                    NumberKey(number = 5, onClick = { onNumberClick(5) })
                    NumberKey(number = 6, onClick = { onNumberClick(6) })
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    NumberKey(number = 7, onClick = { onNumberClick(7) })
                    NumberKey(number = 8, onClick = { onNumberClick(8) })
                    NumberKey(number = 9, onClick = { onNumberClick(9) })
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 취소 버튼 (왼쪽)
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(72.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { 
                                if (isPinConfirmStep && confirmPin.isEmpty()) {
                                    // 확인 단계에서 PIN이 비어있으면 이전 단계로
                                    isPinConfirmStep = false
                                } else {
                                    // 그 외 경우 입력 초기화
                                    resetPinSetup()
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "취소",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    
                    // 숫자 0
                    NumberKey(number = 0, onClick = { onNumberClick(0) })
                    
                    // 백스페이스 버튼 (오른쪽)
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(72.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { onBackspaceClick() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Backspace,
                                contentDescription = "지우기"
                            )
                        }
                    }
                }
            }
        }
        
        // PIN 설정 완료 다이얼로그
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { /* 백버튼 무시 */ },
                icon = { 
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                title = { Text("PIN 설정 완료") },
                text = { 
                    Text(
                        "PIN이 성공적으로 설정되었습니다. 앱 실행 시 이 PIN을 사용하여 로그인할 수 있습니다."
                    ) 
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            // 메인 화면으로 이동
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(Screen.PinSetup.route, true)
                                .build()
                            navController.navigate(Screen.Main.route, navOptions)
                        }
                    ) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

@Composable
fun NumberKey(
    number: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(72.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// PIN 저장 함수 (SharedPreferences 사용)
private fun savePin(context: android.content.Context, pin: String) {
    val sharedPreferences = context.getSharedPreferences("app_preferences", android.content.Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
        putString("pin_code", pin)
        apply()
    }
} 