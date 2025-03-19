package com.zan.silent_log.presentation.screen.permissions

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.zan.silent_log.presentation.navigation.Screen
import com.zan.silent_log.util.AppDebugSettings
import com.zan.silent_log.util.PermissionManager
import kotlinx.coroutines.launch

private const val TAG = "PermissionsScreen"

@Composable
fun PermissionsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // 권한 상태
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showDevModeDialog by remember { mutableStateOf(false) }
    
    // 디버깅 모드에서 권한 검사 건너뛰기 처리
    LaunchedEffect(Unit) {
        if (AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_PERMISSION_CHECK)) {
            Log.d(TAG, "디버깅 모드: 권한 검사 건너뛰기 활성화됨")
            // PIN 설정 화면 건너뛰기 옵션도 활성화되어 있으면 메인 화면으로 바로 이동
            if (AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_PIN_SETUP)) {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(Screen.Permissions.route, true)
                    .build()
                navController.navigate(Screen.Main.route, navOptions)
            } else {
                // PIN 설정 화면으로만 이동
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(Screen.Permissions.route, true)
                    .build()
                navController.navigate(Screen.PinSetup.route, navOptions)
            }
        }
    }
    
    // 권한 상태 확인 함수
    fun checkPermissions(): Boolean {
        // 디버깅 모드에서 권한 검사 건너뛰기 옵션이 활성화되어 있으면 항상 true 반환
        if (AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_PERMISSION_CHECK)) {
            return true
        }
        return PermissionManager.checkAllPermissionsGranted(context)
    }
    
    // 권한 요청 런처
    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        Log.d(TAG, "권한 요청 결과: $permissionsResult")
        
        val allGranted = permissionsResult.values.all { it } || 
                        AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_PERMISSION_CHECK)
        
        if (allGranted) {
            // 모든 권한이 허용되었을 때 또는 디버깅 모드에서 권한 검사 건너뛰기가 활성화되었을 때
            Log.d(TAG, "모든 권한이 허용됨")
            
            // PIN 설정 화면 건너뛰기 옵션이 활성화되어 있으면 메인 화면으로 바로 이동
            if (AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_PIN_SETUP)) {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(Screen.Permissions.route, true)
                    .build()
                navController.navigate(Screen.Main.route, navOptions)
            } else {
                // PIN 설정 화면으로 이동
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(Screen.Permissions.route, true)
                    .build()
                navController.navigate(Screen.PinSetup.route, navOptions)
            }
        } else {
            // 일부 권한이 거부되었을 때
            Log.d(TAG, "일부 권한이 거부됨")
            showRationaleDialog = true
            
            // 개발 모드에서 권한 상태 저장
            if (PermissionManager.DEV_MODE || AppDebugSettings.DEBUG_MODE_ENABLED) {
                PermissionManager.savePermissionResetTime(context)
                PermissionManager.logPermissionStatus(context)
            }
        }
    }
    
    // 설정 화면으로 이동 함수
    fun openAppSettings() {
        try {
            val intent = PermissionManager.getAppSettingsIntent(context)
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "설정 화면 열기 실패: ${e.message}")
        }
    }
    
    // 권한 요청 함수
    fun requestPermissions() {
        Log.d(TAG, "권한 요청 시작")
        
        val permissions = PermissionManager.requiredPermissions
        if (permissions.isEmpty()) {
            Log.d(TAG, "요청할 권한이 없음")
            return
        }
        
        permissionsLauncher.launch(permissions)
        
        // 개발 모드에서 권한 상태 로깅
        if (PermissionManager.DEV_MODE || AppDebugSettings.DEBUG_MODE_ENABLED) {
            PermissionManager.logPermissionStatus(context)
        }
    }
    
    // 개발 모드에서 권한 초기화 알림
    LaunchedEffect(Unit) {
        if (PermissionManager.DEV_MODE && PermissionManager.shouldShowPermissionResetPrompt(context)) {
            showDevModeDialog = true
        }
        
        // 개발 모드에서 권한 상태 로깅
        if (PermissionManager.DEV_MODE || AppDebugSettings.DEBUG_MODE_ENABLED) {
            PermissionManager.logPermissionStatus(context)
        }
    }
    
    // 화면이 다시 표시될 때 권한 상태 확인
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Log.d(TAG, "화면 재진입: 권한 상태 다시 확인")
                
                // 모든 권한이 허용되었는지 확인
                if (checkPermissions()) {
                    Log.d(TAG, "모든 권한이 허용됨 - 화면 재진입")
                    
                    // PIN 설정 화면 건너뛰기 옵션이 활성화되어 있으면 메인 화면으로 바로 이동
                    if (AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_PIN_SETUP)) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(Screen.Permissions.route, true)
                            .build()
                        navController.navigate(Screen.Main.route, navOptions)
                    } else {
                        // PIN 설정 화면으로 이동
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(Screen.Permissions.route, true)
                            .build()
                        navController.navigate(Screen.PinSetup.route, navOptions)
                    }
                }
                
                // 개발 모드에서 권한 상태 로깅
                if (PermissionManager.DEV_MODE || AppDebugSettings.DEBUG_MODE_ENABLED) {
                    PermissionManager.logPermissionStatus(context)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 타이틀
            Text(
                text = "필요한 권한",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            
            // 진행 상태 표시
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .height(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(8.dp)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(8.dp)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(24.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(4.dp)
                        )
                )
            }
            
            // 개발 모드 표시 (개발 모드일 때만 표시)
            if (PermissionManager.DEV_MODE || AppDebugSettings.DEBUG_MODE_ENABLED) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "개발 모드: 앱 종료 시 권한 상태가 로깅됩니다.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // 권한 설명 스크롤 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 안내 메시지
                Text(
                    text = "앱의 원활한 기능 사용을 위해 다음 권한이 필요합니다. 각 권한은 앱의 핵심 기능을 위해 필수적입니다.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                // 통화 상태 감지 권한
                PermissionItem(
                    icon = Icons.Outlined.Phone,
                    title = "전화 상태 읽기 (READ_PHONE_STATE)",
                    description = "통화의 시작과 종료를 감지하기 위해 필요합니다. 이 권한이 없으면 앱이 통화를 감지할 수 없습니다.",
                    isGranted = PermissionManager.isPermissionGranted(context, Manifest.permission.READ_PHONE_STATE)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // 통화 기록 읽기 권한
                PermissionItem(
                    icon = Icons.Outlined.Call,
                    title = "통화 기록 읽기 (READ_CALL_LOG)",
                    description = "수신되는 통화를 감지하고 관리하기 위해 필요합니다.",
                    isGranted = PermissionManager.isPermissionGranted(context, Manifest.permission.READ_CALL_LOG)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // 통화 기록 쓰기 권한
                PermissionItem(
                    icon = Icons.Outlined.Lock,
                    title = "통화 기록 쓰기 (WRITE_CALL_LOG)",
                    description = "사용자 설정에 따라 통화 기록을 관리하기 위해 필요합니다. 이 권한은 지정된 번호의 통화 기록 처리에 사용됩니다.",
                    isGranted = PermissionManager.isPermissionGranted(context, Manifest.permission.WRITE_CALL_LOG)
                )
            }
            
            // 하단 버튼 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 권한 요청 버튼
                Button(
                    onClick = { 
                        scope.launch {
                            requestPermissions()
                        }
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
                        text = "권한 요청하기",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // 나중에 설정 버튼
                OutlinedButton(
                    onClick = { 
                        scope.launch {
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(Screen.Permissions.route, true)
                                .build()
                            navController.navigate(Screen.PinSetup.route, navOptions)
                            
                            // 개발 모드에서 권한 상태 저장
                            if (PermissionManager.DEV_MODE || AppDebugSettings.DEBUG_MODE_ENABLED) {
                                PermissionManager.savePermissionResetTime(context)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "나중에 설정하기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // 도움말 텍스트
                Text(
                    text = "일부 기능은 권한 없이 사용할 수 없습니다",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // 권한이 필요한 이유 다이얼로그
        if (showRationaleDialog) {
            AlertDialog(
                onDismissRequest = { showRationaleDialog = false },
                title = { Text("권한이 필요합니다") },
                text = { 
                    Text(
                        "앱의 핵심 기능을 사용하기 위해서는 요청한 권한이 필요합니다. " +
                        "권한이 없으면 일부 기능이 제한됩니다."
                    ) 
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showRationaleDialog = false
                            requestPermissions()
                        }
                    ) {
                        Text("다시 요청하기")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showRationaleDialog = false
                            showSettingsDialog = true
                        }
                    ) {
                        Text("설정에서 변경")
                    }
                }
            )
        }
        
        // 설정 화면으로 이동 다이얼로그
        if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                icon = { 
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null
                    ) 
                },
                title = { Text("앱 설정에서 권한 변경") },
                text = { 
                    Text(
                        "권한을 허용하려면 앱 설정 화면에서 권한을 활성화해주세요."
                    ) 
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSettingsDialog = false
                            openAppSettings()
                        }
                    ) {
                        Text("설정으로 이동")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { 
                            showSettingsDialog = false 
                        }
                    ) {
                        Text("취소")
                    }
                }
            )
        }
        
        // 개발 모드 알림 대화상자
        if (showDevModeDialog && (PermissionManager.DEV_MODE || AppDebugSettings.DEBUG_MODE_ENABLED)) {
            AlertDialog(
                onDismissRequest = { showDevModeDialog = false },
                icon = { 
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    ) 
                },
                title = { Text("디버깅 모드 알림") },
                text = { 
                    Text(
                        "디버깅 모드에서는 앱 종료 시 권한 상태가 로깅됩니다. " +
                        "앱 데이터를 초기화하면 권한 상태를 초기화할 수 있습니다." +
                        (if (AppDebugSettings.isEnabled(AppDebugSettings.Options.SKIP_PERMISSION_CHECK)) 
                            "\n\n현재 권한 검사 건너뛰기가 활성화되어 있습니다." 
                        else "")
                    ) 
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDevModeDialog = false
                            openAppSettings()
                        }
                    ) {
                        Text("앱 설정으로 이동")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDevModeDialog = false }
                    ) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

@Composable
fun PermissionItem(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            else 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 아이콘
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isGranted) 
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // 텍스트 영역
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    if (isGranted) {
                        Text(
                            text = " (허용됨)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 