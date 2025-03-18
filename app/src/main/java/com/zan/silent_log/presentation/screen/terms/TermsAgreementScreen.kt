package com.zan.silent_log.presentation.screen.terms

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.zan.silent_log.presentation.navigation.Screen

@Composable
fun TermsAgreementScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var allAgreed by rememberSaveable { mutableStateOf(false) }
    var privacyPolicyAgreed by rememberSaveable { mutableStateOf(false) }
    var termsOfServiceAgreed by rememberSaveable { mutableStateOf(false) }
    
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showTermsOfServiceDialog by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 헤더 텍스트
            Text(
                text = "이용약관 동의",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 24.dp)
            )
            
            // 설명 텍스트
            Text(
                text = "SilentLog 서비스 이용을 위해\n아래 약관에 동의해 주세요",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // 모두 동의 체크박스
            CheckboxItem(
                text = "모두 동의합니다",
                isChecked = allAgreed,
                onCheckedChange = { checked ->
                    allAgreed = checked
                    privacyPolicyAgreed = checked
                    termsOfServiceAgreed = checked
                },
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 개인정보 처리방침 체크박스
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckboxItem(
                    text = "개인정보 처리방침 (필수)",
                    isChecked = privacyPolicyAgreed,
                    onCheckedChange = { 
                        privacyPolicyAgreed = it
                        updateAllAgreedState(
                            it, 
                            termsOfServiceAgreed
                        ) { newState -> allAgreed = newState }
                    },
                    modifier = Modifier.weight(1f)
                )
                
                TextButton(onClick = { showPrivacyPolicyDialog = true }) {
                    Text(
                        text = "보기",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // 이용약관 체크박스
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckboxItem(
                    text = "이용약관 (필수)",
                    isChecked = termsOfServiceAgreed,
                    onCheckedChange = { 
                        termsOfServiceAgreed = it
                        updateAllAgreedState(
                            privacyPolicyAgreed, 
                            it
                        ) { newState -> allAgreed = newState }
                    },
                    modifier = Modifier.weight(1f)
                )
                
                TextButton(onClick = { showTermsOfServiceDialog = true }) {
                    Text(
                        text = "보기",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 다음 버튼
            Button(
                onClick = {
                    if (privacyPolicyAgreed && termsOfServiceAgreed) {
                        // 다음 화면 (권한 요청 화면)으로 이동
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(Screen.TermsAgreement.route, true)
                            .build()
                        navController.navigate(Screen.Permissions.route, navOptions)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                ),
                enabled = privacyPolicyAgreed && termsOfServiceAgreed
            ) {
                Text(
                    text = "동의하고 시작하기",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // 개인정보 처리방침 다이얼로그
        if (showPrivacyPolicyDialog) {
            TermsDialog(
                title = "개인정보 처리방침",
                content = getPrivacyPolicyText(),
                onDismiss = { showPrivacyPolicyDialog = false }
            )
        }
        
        // 이용약관 다이얼로그
        if (showTermsOfServiceDialog) {
            TermsDialog(
                title = "이용약관",
                content = getTermsOfServiceText(),
                onDismiss = { showTermsOfServiceDialog = false }
            )
        }
    }
}

@Composable
fun CheckboxItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        )
        
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun TermsDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 다이얼로그 제목
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // 다이얼로그 내용
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                // 닫기 버튼
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "닫기",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// 개인정보 처리방침 텍스트 생성 함수
private fun getPrivacyPolicyText(): String {
    return """
        【개인정보 처리방침】
        
        SilentLog('회사')는 사용자의 개인정보를 중요시하며, 「개인정보 보호법」을 준수하고 있습니다. 회사는 개인정보처리방침을 통하여 회사가 수집하는 개인정보의 항목, 수집 및 이용목적, 보유 및 이용기간 등에 대한 사항을 안내드립니다.
        
        1. 수집하는 개인정보 항목
        
        회사는 서비스 제공을 위해 다음과 같은 개인정보를 수집하고 있습니다.
        
        - 필수 수집 항목: 이메일 주소, 기기 정보, 통화 기록
        - 선택 수집 항목: 프로필 이미지, 연락처 정보
        
        2. 개인정보의 수집 및 이용 목적
        
        회사는 수집한 개인정보를 다음의 목적을 위해 이용합니다.
        
        - 서비스 제공 및 운영
        - 회원 관리 및 본인 확인
        - 불만 처리 등 민원 처리
        - 새로운 서비스 및 기능 개발
        - 서비스 이용 통계 및 분석
        
        3. 개인정보의 보유 및 이용 기간
        
        회사는 원칙적으로 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다. 단, 관계법령의 규정에 의하여 보존할 필요가 있는 경우, 회사는 해당 법령에서 정한 기간 동안 개인정보를 보관합니다.
        
        4. 개인정보의 파기 절차 및 방법
        
        회사는 개인정보 보유기간이 만료되거나 수집 및 이용목적이 달성된 경우, 전자적 파일형태로 저장된 개인정보는 기술적 방법을 사용하여 삭제합니다.
        
        5. 개인정보 보호 책임자
        
        회사는 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.
        
        - 개인정보 보호 책임자
          이름: 김프라이버시
          직위: 개인정보보호 책임자
          연락처: privacy@silentlog.com
        
        6. 권리 행사 방법
        
        이용자는 개인정보 열람, 정정, 삭제, 처리정지 요구 등의 권리를 행사할 수 있습니다. 권리 행사는 회사에 대해 서면, 전화, 전자우편 등을 통하여 하실 수 있으며, 회사는 이에 대해 지체 없이 조치하겠습니다.
        
        7. 개인정보 안전성 확보 조치
        
        회사는 개인정보의 안전성 확보를 위해 다음과 같은 조치를 취하고 있습니다.
        
        - 관리적 조치: 내부관리계획 수립 및 시행, 정기적 직원 교육
        - 기술적 조치: 개인정보처리시스템 등의 접근권한 관리, 접속기록 보관, 보안프로그램 설치, 암호화 기술의 적용
        - 물리적 조치: 전산실, 자료보관실 등의 접근통제
        
        8. 개인정보처리방침 변경
        
        본 개인정보처리방침은 2025년 3월 18일부터 적용됩니다. 법령, 정책 또는 보안기술의 변경에 따라 내용의 추가, 삭제 및 수정이 있을 시에는 변경사항의 시행 7일 전부터 공지사항을 통하여 고지할 것입니다.
    """.trimIndent()
}

// 이용약관 텍스트 생성 함수
private fun getTermsOfServiceText(): String {
    return """
        【이용약관】
        
        제 1 조 (목적)
        이 약관은 SilentLog(이하 '회사')가 제공하는 통화기록 관리 서비스 및 관련 제반 서비스(이하 '서비스')의 이용과 관련하여 회사와 이용자의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.
        
        제 2 조 (정의)
        이 약관에서 사용하는 용어의 정의는 다음과 같습니다.
        
        1. '이용자'란 회사가 제공하는 서비스를 이용하는 모든 사용자를 말합니다.
        2. '회원'이란 회사에 개인정보를 제공하고 회원등록을 한 자로서, 회사가 제공하는 서비스를 이용하는 자를 말합니다.
        3. '콘텐츠'란 회사가 제공하는 서비스에서 사용되는 모든 형태의 정보, 문자, 이미지, 소프트웨어, 음성, 동영상 등을 말합니다.
        
        제 3 조 (약관의 효력 및 변경)
        1. 이 약관은 서비스를 이용하고자 하는 모든 이용자에게 적용됩니다.
        2. 회사는 합리적인 사유가 있을 경우 약관을 변경할 수 있으며, 변경된 약관은 서비스 내 공지사항 또는 이메일 등을 통해 공지함으로써 효력이 발생합니다.
        3. 이용자는 변경된 약관에 동의하지 않을 경우 서비스 이용을 중단하고 회원 탈퇴를 요청할 수 있으며, 변경된 약관의 효력 발생일 이후에도 서비스를 계속 이용하는 경우 약관의 변경사항에 동의한 것으로 간주합니다.
        
        제 4 조 (서비스의 제공 및 변경)
        1. 회사는 통화기록 관리, 통화기록 보호, 통화 패턴 분석 등의 서비스를 제공합니다.
        2. 회사는 운영상, 기술상의 필요에 따라 제공하고 있는 서비스를 변경할 수 있습니다.
        3. 회사는 무료로 제공되는 서비스의 일부 또는 전부를 회사의 정책 및 운영의 필요에 따라 수정, 중단, 변경할 수 있으며, 이에 대하여 회원에게 별도의 보상을 제공하지 않습니다.
        
        제 5 조 (이용자의 의무)
        1. 이용자는 관계법령, 이 약관의 규정, 이용안내 및 서비스와 관련하여 공지한 주의사항을 준수하여야 합니다.
        2. 이용자는 다음 행위를 하여서는 안 됩니다.
           a. 서비스 이용 신청 시 허위 내용을 등록하는 행위
           b. 다른 회원의 ID와 비밀번호를 도용하는 행위
           c. 회사의 서비스를 이용하여 법령 또는 이 약관에서 금지하는 행위를 하는 행위
           d. 서비스의 안정적인 운영을 방해하는 행위
           e. 기타 불법적이거나 부당한 행위
        
        제 6 조 (서비스 이용 제한 및 중지)
        1. 회사는 다음과 같은 경우 서비스 이용을 제한하거나 중지할 수 있습니다.
           a. 서비스용 설비의 보수 등 공사로 인한 부득이한 경우
           b. 전기통신사업법에 규정된 기간통신사업자가 전기통신 서비스를 중지했을 경우
           c. 기타 불가항력적 사유가 있는 경우
        2. 회사는 이용자가 제5조의 의무를 위반하거나 서비스의 정상적인 운영을 방해한 경우, 서비스 이용을 경고, 일시정지, 계약해지 등으로 단계적으로 제한할 수 있습니다.
        
        제 7 조 (개인정보보호)
        1. 회사는 이용자의 개인정보 보호를 중요하게 생각하며, 개인정보보호법 등 관련 법령을 준수합니다.
        2. 회사의 개인정보 처리에 관한 사항은 개인정보처리방침에서 별도로 규정합니다.
        
        제 8 조 (이용자의 게시물)
        1. 회사는 이용자가 게시하거나 등록하는 게시물이 다음 각 호에 해당한다고 판단되는 경우 사전 통지 없이 해당 게시물에 대해 삭제, 이동, 등록 거부 등의 조치를 취할 수 있습니다.
           a. 다른 이용자 또는 제3자를 비방하거나 명예를 손상시키는 내용
           b. 공공질서 및 미풍양속에 위반되는 내용
           c. 범죄적 행위에 결부된다고 인정되는 내용
           d. 회사의 저작권, 제3자의 저작권 등 기타 권리를 침해하는 내용
        
        제 9 조 (책임제한)
        1. 회사는 천재지변 또는 이에 준하는 불가항력으로 인하여 서비스를 제공할 수 없는 경우에는 서비스 제공에 관한 책임이 면제됩니다.
        2. 회사는 이용자의 귀책사유로 인한 서비스 이용의 장애에 대하여 책임을 지지 않습니다.
        
        제 10 조 (준거법 및 관할법원)
        1. 이 약관의 해석 및 회사와 이용자간의 분쟁에 대하여는 대한민국의 법률을 적용합니다.
        2. 서비스 이용으로 인하여 발생한 분쟁에 대해 소송이 제기될 경우, 회사의 본사 소재지를 관할하는 법원을 관할법원으로 합니다.
        
        부칙
        이 약관은 2025년 3월 18일부터 시행합니다.
    """.trimIndent()
}

// 모든 약관 동의 상태 업데이트 함수
private fun updateAllAgreedState(
    privacyAgreed: Boolean,
    termsAgreed: Boolean,
    updateAllAgreed: (Boolean) -> Unit
) {
    updateAllAgreed(privacyAgreed && termsAgreed)
} 