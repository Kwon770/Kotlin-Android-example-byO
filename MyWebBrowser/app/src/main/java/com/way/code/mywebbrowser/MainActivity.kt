package com.way.code.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.apply {
            // 자바스크립트 기능 켜기
            settings.javaScriptEnabled = true
            // 자체 웹브라우저가 아닌 웹뷰 사용
            webViewClient = WebViewClient()
        }
        webView.loadUrl("https://www.google.com")

        urlEditText.setOnEditorActionListener{_, actionId, _ -> // 글자가 입력될 때마다 호출 (반응한 뷰, 액션ID, 이벤트)
            if(actionId == EditorInfo.IME_ACTION_SEARCH) { // actionId를 EditorInfo 클래스 값 중 검색 버튼의 상수와 비교
                webView.loadUrl(urlEditText.text.toString())
                true // 반환으로 이벤트 종료
            } else {
                false
            }
        }

        registerForContextMenu(webView) // 컨텍스트 메뉴 등록
    }

    override fun onBackPressed() { // 뒤로가기 재정의
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu) // menuInflater 객체의 inflate() 메소드를 사용하여 메뉴 리소스 지정
        return true // true 반환하여 액티비티에 메뉴가 있다고 인식
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) { // 메뉴 아이템으로 분기 수행
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("https://www.google.com")
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl("https://www.naver.com")
                return true
            }
            R.id.action_home -> {
                webView.loadUrl("https://www.daum.net")
                return true
            }
            R.id.action_call -> { // 전화 앱 열기 (암시적 인텐트)
                val intent = Intent(Intent.ACTION_DIAL) // Intent 클래스에 정의된 액션으로 인텐트 정의 (ACTION_DIAL 전화 다이얼)
                intent.data = Uri.parse("tel:010-1234-1234") // 인텐트에 데이터 지정, Uri.parse()로 Uri 객체 표현
                if(intent.resolveActivity(packageManager) != null) { // intent.resolveActivity() 메서드는 이 인텐트를 수행하는 액티비티가 있는지를 검사하여 반환 (전화 앱이 없는 태블릿)
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> { //
                sendSMS("010-1234-1234", webView.url) // anko 암시적 인텐트 문자 보내기 (번호, 내용)
                return true
            }
            R.id.action_email -> { //
                email("test@example.com", "좋은 사이트", webView.url) // anko 암시적 인텐트 이메일 보내기 (이메일, 제목, 내용)
                return true
            }
        }
        return super.onOptionsItemSelected(item) // 내가 처리하고자 하는 경우를 제외한 경우에는 super 메소드를 호출하는 것이 안드 시스템에서의 보편적 규칙
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu) // menuInflater 객체의 inflate() 메소드를 사용하여 메뉴 리소스 지정
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                share(webView.url) // anko 암시적 인텐트 웹 페이지 주소 문자열을 공유하는 앱 사용하여 공유
                return true
            }
            R.id.action_browser -> {
                browse(webView.url) // anko 암시적 인텐트 기본 브라우저로 주소 열기
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
