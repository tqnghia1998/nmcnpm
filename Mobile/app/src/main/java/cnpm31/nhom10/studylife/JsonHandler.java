package cnpm31.nhom10.studylife;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;

import cnpm31.nhom10.studylife.DbModel.RegisteredDataModel;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

import static android.support.constraint.Constraints.TAG;

public class JsonHandler {


    // Phương thức lấy Json từ một địa chỉ
    public static JSONArray getJsonFromUrl(String sUrl) {

        // Tạo chuỗi response
        String response = new String();
        try {
            URL url = new URL(sUrl);

            // Khởi tạo đối tượng HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(100);
            conn.setReadTimeout(1000);

            // Phương thức lấy dữ liệu
            conn.setRequestMethod("GET");

            // Tạo luồng đọc dữ liệu
            InputStream in = new BufferedInputStream(conn.getInputStream());

            // Chuyển đổi dữ liệu thu được
            response = convertStreamToString(in);

            // Tắt kết nối
            conn.disconnect();

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        // Chuyển chuỗi response thành kiểu Json
        JSONArray arrayJson = new JSONArray();
        try {
            arrayJson = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayJson;
    }

    // Phương thức đọc chuỗi từ một InputStream
    private static String convertStreamToString(InputStream is) {

        // Tạo bộ đệm để đọc dòng dữ liệu
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        // Đối tượng xây dựng chuỗi từ những dữ liệu đã được đọc
        StringBuilder sb = new StringBuilder();

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

    // Phương thức gửi dữ liệu đăng ký lên server
    public static String postRegistered(String sUrl, RegisteredDataModel data, final Context mContext) {

        final String[] result = new String[1];
        JSONObject jsonObject = new JSONObject();

        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest stringRequest = null;

        try {
            jsonObject.put("mssv", data.Mssv);
            jsonObject.put("id", data.Id);
            jsonObject.put("dayInTheWeek", data.DayInTheWeek);
            jsonObject.put("subject", data.Subject);


            stringRequest = new JsonObjectRequest(Request.Method.POST, sUrl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) { result[0] = response.toString(); }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { result[0] = error.getMessage(); }
            });
        }
        catch(JSONException ex){
            ex.printStackTrace();
        }
        queue.add(stringRequest);

        return result[0];
    }
}