package com.zan.silent_log.util

/**
 * 앱의 디버깅 설정을 관리하는 유틸리티 클래스
 * 다양한 디버깅 옵션을 중앙에서 관리하고 제어하는 기능 제공
 */
object AppDebugSettings {
    // 전체 디버깅 모드 활성화 여부 - 프로덕션 배포 시 false로 설정
    const val DEBUG_MODE_ENABLED = true
    
    // 개별 디버깅 옵션들
    object Options {
        // 온보딩 화면을 건너뛰고 바로 메인 화면으로 이동
        const val SKIP_ONBOARDING = true
        
        // PIN 설정 화면 디버깅 모드
        const val SKIP_PIN_SETUP = true
        
        // 권한 검사 디버깅 모드
        const val SKIP_PERMISSION_CHECK = true
        
        // 로그인 화면 건너뛰기
        const val SKIP_LOGIN = true
    }
    
    /**
     * 특정 디버깅 옵션이 활성화되었는지 확인
     * 전체 디버깅 모드가 비활성화되면 모든 개별 옵션도 자동으로 비활성화됨
     * 
     * @param option 확인할 디버깅 옵션 값
     * @return 디버깅 옵션 활성화 여부
     */
    fun isEnabled(option: Boolean): Boolean {
        return DEBUG_MODE_ENABLED && option
    }
    
    /**
     * 전체 디버깅 상태 로깅
     */
    fun logDebugStatus() {
        if (DEBUG_MODE_ENABLED) {
            android.util.Log.d("AppDebugSettings", "===== 디버깅 설정 상태 =====")
            android.util.Log.d("AppDebugSettings", "전체 디버깅 모드: $DEBUG_MODE_ENABLED")
            android.util.Log.d("AppDebugSettings", "온보딩 건너뛰기: ${isEnabled(Options.SKIP_ONBOARDING)}")
            android.util.Log.d("AppDebugSettings", "PIN 설정 건너뛰기: ${isEnabled(Options.SKIP_PIN_SETUP)}")
            android.util.Log.d("AppDebugSettings", "권한 검사 건너뛰기: ${isEnabled(Options.SKIP_PERMISSION_CHECK)}")
            android.util.Log.d("AppDebugSettings", "로그인 건너뛰기: ${isEnabled(Options.SKIP_LOGIN)}")
            android.util.Log.d("AppDebugSettings", "============================")
        }
    }
} 