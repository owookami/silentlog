package com.zan.silent_log.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.edit

/**
 * 앱 권한 관리를 위한 유틸리티 클래스
 * 권한 상태 관리, 권한 요청, 개발 모드에서의 권한 초기화 등의 기능 제공
 */
object PermissionManager {
    private const val TAG = "PermissionManager"
    
    // 개발 모드 상수 - 프로덕션 환경에서는 false로 설정
    const val DEV_MODE = true
    
    // 권한 상태 저장을 위한 SharedPreferences 이름과 키
    private const val PREF_NAME = "permission_prefs"
    private const val PREF_KEY_LAST_RESET = "last_permission_reset"
    
    // 앱에서 필요한 권한 목록
    val requiredPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG
    )
    
    /**
     * 모든 필수 권한이 부여되었는지 확인
     * @param context 컨텍스트
     * @return 모든 권한이 부여되었으면 true, 아니면 false
     */
    fun checkAllPermissionsGranted(context: Context): Boolean {
        return requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    /**
     * 특정 권한의 부여 상태 확인
     * @param context 컨텍스트
     * @param permission 확인할 권한
     * @return 권한이 부여되었으면 true, 아니면 false
     */
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 앱 설정 화면으로 이동하는 Intent 반환
     * @param context 컨텍스트
     * @return 앱 설정 화면으로 이동하는 Intent
     */
    fun getAppSettingsIntent(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${context.packageName}")
        return intent
    }
    
    /**
     * 현재 모든 권한 상태를 로그로 출력 (개발 디버깅용)
     * @param context 컨텍스트
     */
    fun logPermissionStatus(context: Context) {
        if (DEV_MODE) {
            Log.d(TAG, "===== 권한 상태 확인 (개발 모드) =====")
            requiredPermissions.forEach { permission ->
                val isGranted = isPermissionGranted(context, permission)
                Log.d(TAG, "권한: $permission = $isGranted")
            }
            Log.d(TAG, "====================================")
        }
    }
    
    /**
     * 개발 모드에서 앱 종료 시 권한 상태를 기록
     * @param context 컨텍스트
     */
    fun savePermissionResetTime(context: Context) {
        if (DEV_MODE) {
            getPrefs(context).edit {
                putLong(PREF_KEY_LAST_RESET, System.currentTimeMillis())
            }
            Log.d(TAG, "권한 초기화 시간 기록됨: ${System.currentTimeMillis()}")
        }
    }
    
    /**
     * 권한 상태가 변경되었는지 확인 (개발 모드용)
     * @param context 컨텍스트
     * @return 권한 상태가 변경되었으면 true, 아니면 false
     */
    fun hasPermissionStatusChanged(context: Context): Boolean {
        if (!DEV_MODE) return false
        
        // 권한 상태를 가져와서 이전 상태와 비교
        val currentStatus = requiredPermissions.associateWith { permission ->
            isPermissionGranted(context, permission)
        }
        
        // 로그로 출력
        Log.d(TAG, "권한 상태 변경 확인: $currentStatus")
        
        return true // 개발 모드에서는 항상 변경된 것으로 간주
    }
    
    /**
     * 앱이 다시 시작될 때 권한 상태 확인 및 초기화 안내 (개발 모드용)
     * @param context 컨텍스트
     * @return 개발 모드에서 권한을 초기화해야 하면 true, 아니면 false
     */
    fun shouldShowPermissionResetPrompt(context: Context): Boolean {
        if (!DEV_MODE) return false
        
        val lastReset = getPrefs(context).getLong(PREF_KEY_LAST_RESET, 0)
        val currentTime = System.currentTimeMillis()
        val hoursPassed = (currentTime - lastReset) / (1000 * 60 * 60)
        
        // 마지막 초기화 후 4시간 이상 지났으면 알림 표시
        return hoursPassed >= 4
    }
    
    /**
     * SharedPreferences 인스턴스 가져오기
     */
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
} 