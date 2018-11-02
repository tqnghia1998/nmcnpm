package cnpm31.nhom10.studylife;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JSON {
    private static final String TAG = JSON.class.getSimpleName();
    public JSON() {
    }

    public String getJSONStringFromURL(String sUrl) {
        String response = null;
        try {
            URL url = new URL(sUrl);

            // Khởi tạo đối tượng HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Phương thức lấy dữ liệu
            conn.setRequestMethod("GET");

            // Tạo luồng đọc dữ liệu
            InputStream in = new BufferedInputStream(conn.getInputStream());

            // Chuyển đổi dữ liệu thu được
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {

        // Tạo bộ đệm để đọc dòng dữ liệu
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();// Đối tượng xây dựng chuỗi từ những dữ liệu đã được đọc

        String line;
        try {
            while ((line = reader.readLine()) != null) {

                // Đọc và thêm các dữ liệu đã đọc được từ luồng vào chuỗi.
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}