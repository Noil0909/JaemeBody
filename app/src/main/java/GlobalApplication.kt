import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "66d89193ef726cfb74ed65c33a6ec219")
    }
}