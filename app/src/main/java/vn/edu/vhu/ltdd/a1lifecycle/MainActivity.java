package vn.edu.vhu.ltdd.a1lifecycle;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // =========================================================
    // TAG - PHẢI CHỨA MSSV
    // =========================================================

    private static final String TAG = "A1_221A310077";


    // =========================================================
    // VIEW
    // =========================================================

    private TextView tvLog;
    private TextView tvCallbackCount;


    // =========================================================
    // LƯU LỊCH SỬ LOG
    // =========================================================

    private final StringBuilder history = new StringBuilder();


    // =========================================================
    // NC1
    // ĐẾM SỐ LẦN GỌI CALLBACK
    // =========================================================

    private final LinkedHashMap<String, Integer> callbackCounts =
            new LinkedHashMap<>();


    // Danh sách callback cần đếm
    private static final String[] CALLBACK_NAMES = {
            "onCreate",
            "onStart",
            "onResume",
            "onPause",
            "onStop",
            "onRestart",
            "onDestroy",
            "onSaveInstanceState"
    };


    // Số thứ tự log
    private int step = 0;


    // =========================================================
    // GHI LOG
    // =========================================================

    private void logEvent(String event) {

        step++;

        // Lấy thời gian hiện tại
        String time = new SimpleDateFormat(
                "HH:mm:ss.SSS",
                Locale.getDefault()
        ).format(new Date());


        // Tạo nội dung log
        String line = step
                + ". ["
                + time
                + "] "
                + event;


        // Ghi vào Logcat
        Log.d(TAG, line);


        // Lưu lịch sử
        history.append(line).append('\n');


        // Hiển thị lịch sử trên màn hình
        if (tvLog != null) {
            tvLog.setText(history.toString());
        }


        // =====================================================
        // NC1
        // CHỈ ĐẾM CALLBACK
        // Không đếm các log khác như NC2
        // =====================================================

        String callbackName = getCallbackName(event);

        if (callbackName != null) {

            int count = callbackCounts.containsKey(callbackName)
                    ? callbackCounts.get(callbackName) + 1
                    : 1;

            callbackCounts.put(callbackName, count);

            updateCallbackCount();
        }
    }


    // =========================================================
    // XÁC ĐỊNH EVENT CÓ PHẢI CALLBACK HAY KHÔNG
    // =========================================================

    private String getCallbackName(String event) {

        for (String callback : CALLBACK_NAMES) {

            if (event.equals(callback)
                    || event.startsWith(callback + " ")) {

                return callback;
            }
        }

        return null;
    }


    // =========================================================
    // NC1
    // HIỂN THỊ BẢNG ĐẾM CALLBACK
    // =========================================================

    private void updateCallbackCount() {

        if (tvCallbackCount == null) {
            return;
        }


        StringBuilder result = new StringBuilder();


        for (Map.Entry<String, Integer> entry
                : callbackCounts.entrySet()) {

            result.append(
                    String.format(
                            Locale.getDefault(),
                            "%-22s : %d%n",
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }


        tvCallbackCount.setText(result.toString());
    }


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // Edge-to-edge
        EdgeToEdge.enable(this);


        // Gắn layout
        setContentView(R.layout.activity_main);


        // Xử lý khoảng cách với system bars
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );


                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );


                    return insets;
                }
        );


        // =====================================================
        // ÁNH XẠ VIEW
        // =====================================================

        tvLog = findViewById(R.id.tvLog);

        tvCallbackCount =
                findViewById(R.id.tvCallbackCount);


        Button btnClear =
                findViewById(R.id.btnClear);


        Button btnCrash =
                findViewById(R.id.btnCrash);


        Button btnFinish =
                findViewById(R.id.btnFinish);


        // =====================================================
        // NÚT XÓA LOG
        // =====================================================

        btnClear.setOnClickListener(v -> {

            // Xóa lịch sử
            history.setLength(0);


            // Xóa bộ đếm callback
            callbackCounts.clear();


            // Reset số thứ tự
            step = 0;


            // Xóa màn hình
            tvLog.setText("");

            tvCallbackCount.setText("");


            // Ghi thông báo vào Logcat
            Log.i(
                    TAG,
                    "---- Đã xóa lịch sử ----"
            );
        });


        // =====================================================
        // NC2
        // NÚT GÂY LỖI
        // BẮT NULLPOINTEREXCEPTION
        // =====================================================

        btnCrash.setOnClickListener(v -> {

            try {

                // Cố tình tạo biến null
                String ten = null;


                // Cố tình gây NullPointerException
                int doDai = ten.length();


                // Dòng này sẽ không được chạy
                Log.d(
                        TAG,
                        "Độ dài tên: " + doDai
                );


            } catch (NullPointerException e) {

                // ---------------------------------------------
                // Bắt lỗi - app KHÔNG bị crash
                // ---------------------------------------------

                Log.w(
                        TAG,
                        "NC2: Đã bắt lỗi NullPointerException"
                );


                // Hiển thị lỗi trên LOG TRÊN ĐIỆN THOẠI
                logEvent(
                        "NC2: Bắt được NullPointerException"
                );


                // Toast
                Toast.makeText(
                        MainActivity.this,
                        "Đã bắt lỗi: tên đang null",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        // =====================================================
        // NÚT THOÁT
        // =====================================================

        btnFinish.setOnClickListener(v -> finish());


        // =====================================================
        // GHI CALLBACK ONCREATE
        // =====================================================

        String state;

        if (savedInstanceState == null) {
            state = "null";
        } else {
            state = "!= null";
        }


        logEvent(
                "onCreate (savedInstanceState = " + state + ")"
        );
    }


    // =========================================================
    // ON START
    // =========================================================

    @Override
    protected void onStart() {

        super.onStart();

        logEvent("onStart");
    }


    // =========================================================
    // ON RESUME
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        logEvent("onResume");
    }


    // =========================================================
    // ON PAUSE
    // =========================================================

    @Override
    protected void onPause() {

        super.onPause();

        logEvent("onPause");
    }


    // =========================================================
    // ON STOP
    // =========================================================

    @Override
    protected void onStop() {

        super.onStop();

        logEvent("onStop");
    }


    // =========================================================
    // ON RESTART
    // =========================================================

    @Override
    protected void onRestart() {

        super.onRestart();

        logEvent("onRestart");
    }


    // =========================================================
    // ON DESTROY
    // =========================================================

    @Override
    protected void onDestroy() {

        // Ghi log trước super.onDestroy()
        logEvent("onDestroy");

        super.onDestroy();
    }


    // =========================================================
    // ON SAVE INSTANCE STATE
    // =========================================================

    @Override
    protected void onSaveInstanceState(
            Bundle outState
    ) {

        super.onSaveInstanceState(outState);

        logEvent("onSaveInstanceState");
    }
}