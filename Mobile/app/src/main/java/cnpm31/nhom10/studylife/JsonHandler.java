package cnpm31.nhom10.studylife;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cnpm31.nhom10.studylife.DbModel.RegisteredDataModel;

import static android.support.constraint.Constraints.TAG;
import static cnpm31.nhom10.studylife.RegisterFragment.adapterRecycler;
import static cnpm31.nhom10.studylife.RegisterFragment.filterSpinner;
import static cnpm31.nhom10.studylife.RegisterFragment.isSearching;
import static cnpm31.nhom10.studylife.RegisterFragment.listSubjectTitle;
import static cnpm31.nhom10.studylife.RegisterFragment.refresh;
import static cnpm31.nhom10.studylife.RegisterFragment.updateSumCredit;

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

            // Nếu chuyển không được, thì đây có thể là JSON đơn
            try {
                JSONObject jsonObject = new JSONObject(response);
                arrayJson.put(jsonObject);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
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

    // Phương thức POST dữ liệu đăng ký lên server
    public static void postRegistered(String sUrl,
                                      RegisteredDataModel data,
                                      final Context mContext,
                                      final CheckBox checkBox,
                                      final Button button) {
        // Chuyển dữ liệu sang định dạng json (GSON ALTERNATIVE)
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mssv", data.Mssv);
            jsonObject.put("id", data.Id);
            jsonObject.put("dayInTheWeek", data.DayInTheWeek);
            jsonObject.put("subject", data.Subject);
        } catch (JSONException ex) {
            ex.printStackTrace();
            Toast.makeText(mContext, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }

        // Tạo một RequestQueue để quản lý việc giao tiếp với network
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Tạo một POST request
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, sUrl, jsonObject,
                response -> {
                    try {
                        Toast.makeText(mContext, response.getString("response"), Toast.LENGTH_SHORT).show();

                        // Bật button và chuyển sang Hủy đăng ký
                        button.setEnabled(true);
                        button.setText("Hủy đăng ký");
                        checkBox.setChecked(true);
                        refresh();
                        updateSumCredit();
                    } catch (Exception e) {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage("Không thể kết nối đến server");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();
                    }
                },
                error -> {
                    try {
                        Toast.makeText(mContext, "Lỗi " + error.networkResponse.statusCode + " - "
                                + new String(error.networkResponse.data, "UTF-8"), Toast.LENGTH_SHORT).show();

                        // Bật lại button Đăng ký
                        button.setEnabled(true);
                    } catch (Exception e) {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage("Không thể kết nối đến server");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();
                    }
                });
        // Thực hiện request
        queue.add(stringRequest);
    }

    // Phương thức DELETE dữ liệu đăng ký lên server
    public static void deleteRegistered(String sUrl,
                                        RegisteredDataModel data,
                                        final Context mContext,
                                        final CheckBox checkBox,
                                        final Button button) {
        // Chuyển sUrl sang đúng định dạng với API của server
        // Vì DELETE request không nhận body, nên đưa các thông tin cần thiết lên url
        sUrl += "/" + data.Mssv + "/" + data.Id + "/" + data.DayInTheWeek;

        // Tạo một RequestQueue để quản lý việc giao tiếp với network
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Tạo một POST request
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.DELETE, sUrl, null,
                response -> {
                    try {
                        Toast.makeText(mContext, response.getString("response"), Toast.LENGTH_SHORT).show();

                        // Bật button và chuyển Đăng ký
                        button.setEnabled(true);
                        button.setText("Đăng ký");
                        checkBox.setChecked(false);
                        refresh();
                        updateSumCredit();
                    } catch (Exception e) {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage("Không thể kết nối đến server");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();
                    }
                },
                error -> {
                    try {
                        Toast.makeText(mContext, "Lỗi " + error.networkResponse.statusCode + " - "
                                + new String(error.networkResponse.data, "UTF-8"), Toast.LENGTH_SHORT).show();

                        // Bật lại button Đăng ký
                        button.setEnabled(true);
                    } catch (Exception e) {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage("Không thể kết nối đến server");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();
                    }
                });
        // Thực hiện request
        queue.add(stringRequest);
    }
}