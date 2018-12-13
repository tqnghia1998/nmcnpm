package cnpm31.nhom10.studylife;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cnpm31.nhom10.studylife.DbModel.ExerciseDataModel;
import cnpm31.nhom10.studylife.DbModel.RegisteredDataModel;

import static android.support.constraint.Constraints.TAG;
import static cnpm31.nhom10.studylife.ExerciseFragment.refreshExercise;
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
                                      List<RegisteredDataModel> data,
                                      final Context mContext,
                                      final CheckBox checkBox,
                                      final Button button) {
        // Chuyển dữ liệu sang định dạng json (GSON ALTERNATIVE)
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mssv", data.get(i).Mssv);
                jsonObject.put("id",  data.get(i).Id);
                jsonObject.put("dayInTheWeek",  data.get(i).DayInTheWeek);
                jsonObject.put("subject",  data.get(i).Subject);
            } catch (JSONException ex) {
                ex.printStackTrace();
                Toast.makeText(mContext, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
            finally {
                jsonArray.put(jsonObject);
            }
        }

        // Tạo một RequestQueue để quản lý việc giao tiếp với network
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Tạo một POST request
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, sUrl, jsonArray,
                response -> {
                    try {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage(response.getString(0));
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();

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
                        if (error.networkResponse != null) {
                            AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                            b.setTitle("Lỗi " + error.networkResponse.statusCode);
                            b.setMessage(new String(error.networkResponse.data, "UTF-8"));
                            b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();

                            // Bật lại button Đăng ký
                            button.setEnabled(true);
                        }
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
        sUrl += "/" + data.Mssv + "/" + data.Id;

        // Tạo một RequestQueue để quản lý việc giao tiếp với network
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Tạo một POST request
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.DELETE, sUrl, null,
                response -> {
                    try {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage(response.getString("response"));
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();

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
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Lỗi " + error.networkResponse.statusCode);
                        b.setMessage(new String(error.networkResponse.data, "UTF-8"));
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();

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

    // Phương thức UPDATE dữ liệu bài tập lên server
    public static void updateExercise(String sUrl,
                                      ExerciseDataModel data,
                                      final Context mContext) {
        AtomicBoolean result = new AtomicBoolean(false);
        // Chuyển dữ liệu sang định dạng json (GSON ALTERNATIVE)
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mssv", data.Mssv);
            jsonObject.put("id", data.Id);
            jsonObject.put("name", data.Name);
            jsonObject.put("subject", data.Subject);
            jsonObject.put("deadline", data.Deadline);
            jsonObject.put("progress", data.Progress);
            jsonObject.put("content", data.Content);
        } catch (JSONException ex) {
            ex.printStackTrace();
            final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
            b.setTitle("Thông báo");
            b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
            b.setMessage("Đã xảy ra lỗi, vui lòng thử lại").show();
        }

        // Tạo một RequestQueue để quản lý việc giao tiếp với network
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Tạo một POST request
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, sUrl, jsonObject,
                response -> {
                    try {
                        final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                        b.setMessage(response.getString("response")).show();
                    } catch (Exception e) {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage("Không thể kết nối đến server");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();
                    }
                },
                error -> {
                    try {
                        final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                        b.setMessage("Lỗi " + error.networkResponse.statusCode + " - "
                                + new String(error.networkResponse.data, "UTF-8")).show();
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
    // Phương thức POST dữ liệu bài tập lên server
    public static void postExercise(String sUrl,
                                    ExerciseDataModel data,
                                    final Context mContext) {
        // Chuyển dữ liệu sang định dạng json (GSON ALTERNATIVE)
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mssv", data.Mssv);
            jsonObject.put("id", data.Id);
            jsonObject.put("name", data.Name);
            jsonObject.put("subject", data.Subject);
            jsonObject.put("deadline", data.Deadline);
            jsonObject.put("progress", data.Progress);
            jsonObject.put("content", data.Content);
        } catch (JSONException ex) {
            ex.printStackTrace();
            final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
            b.setTitle("Thông báo");
            b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
            b.setMessage("Đã xảy ra lỗi, vui lòng thử lại").show();
        }

        // Tạo một RequestQueue để quản lý việc giao tiếp với network
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Tạo một POST request
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, sUrl, jsonObject,
                response -> {
                    try {
                        final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                        b.setMessage(response.getString("response")).show();
                    } catch (Exception e) {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage("Không thể kết nối đến server");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();
                    }
                },
                error -> {
                    try {
                        final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                        b.setMessage("Lỗi " + error.networkResponse.statusCode + " - "
                                + new String(error.networkResponse.data, "UTF-8")).show();
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

    // Phương thức DELETE dữ liệu bài tập lên server
    public static void deleteExercise(String sUrl,
                                      String idExercise,
                                      final Context mContext) {
        // Chuyển sUrl sang đúng định dạng với API của server
        // Vì DELETE request không nhận body, nên đưa các thông tin cần thiết lên url
        sUrl += "/" + idExercise;

        // Tạo một RequestQueue để quản lý việc giao tiếp với network
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Tạo một POST request
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.DELETE, sUrl, null,
                response -> {
                    try {
                        final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                        b.setMessage(response.getString("response")).show();

                        refreshExercise();
                    } catch (Exception e) {
                        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setMessage("Không thể kết nối đến server");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel()).show();
                    }
                },
                error -> {
                    try {
                        final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                        b.setTitle("Thông báo");
                        b.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                        b.setMessage("Lỗi " + error.networkResponse.statusCode + " - "
                                + new String(error.networkResponse.data, "UTF-8")).show();
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