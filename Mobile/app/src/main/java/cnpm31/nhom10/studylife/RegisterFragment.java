package cnpm31.nhom10.studylife;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends android.app.Fragment {

    private OnRegisterFragmentListener mListener;

    // Control để hiển thị danh sách
    ExpandableListView expandableListView;
    CustomExpandableListView customExpandableListView;

    // Danh sách các khoa
    List<String> listDataHeader;
    // Danh sách các môn của khoa
    HashMap<String, ArrayList<ArrayList<String>>> listDataChild;

    Context context;

    // Mảng môn học được lấy từ Web Server
    ArrayList<SubjectDataModel> listSubject;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_register, null);

        // Thêm dữ liệu vào Group và Child
        try {
            addControl(view);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Cài adapter cho control và hiển thị
        customExpandableListView = new CustomExpandableListView(getActivity(), listDataHeader, listDataChild);
        expandableListView.setAdapter(customExpandableListView);
        return view;
    }

    // Thêm dữ liệu
    public void addControl(View view) throws InterruptedException {

        // Khởi tạo các biến
        expandableListView = view.findViewById(R.id.expandableListView);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        context = getActivity();

        // Tạo thread để kết nối internet và lấy JSON
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String JsonString = new JSON().getJSONStringFromURL("http://10.0.2.2:51197/api/subject");
                try {
                    JSONArray array = new JSONArray(JsonString);
                    listSubject = FromArrayJson(array);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();

        // Thêm dữ liệu vào Group và Child (Group là major, Child là subject)
        int index = 0;
        for (int i = 0; i < listSubject.size(); i++) {

            // Kiểm tra Group title đã có hay chưa
            boolean addThisMajor = true;
            String currentMajor = listSubject.get(i).Major;

            for (int k = 0; k < i; k++) {
                if (currentMajor.equals(listSubject.get(k).Major)) {
                    addThisMajor = false;
                    break;
                }
            }

            // Nếu Group title chưa có thì thêm vào
            if (addThisMajor) {

                // Thêm Group title vào listDataHeader
                listDataHeader.add(listSubject.get(i).Major);

                // Tạo danh sách Child element cho từng Group
                ArrayList<ArrayList<String>> allList = new ArrayList<>();

                // Từng Child element lại là một danh sách string
                ArrayList<String> list = new ArrayList<>();
                for (int j = 0; j < listSubject.size(); j++) {
                    if (listSubject.get(j).Major.equals(currentMajor)) {
                        list.add(listSubject.get(j).Subject);
                    }
                }
                allList.add(list);

                // Thêm Child
                listDataChild.put(listDataHeader.get(index++), allList);
            }
        }
    }

    // Phương thức lấy danh sách Subject và ArrayJson
    public ArrayList<SubjectDataModel> FromArrayJson(JSONArray response) {

        ArrayList<SubjectDataModel> list = new ArrayList<SubjectDataModel>();
        int size = response.length();
        JSONObject json = null;

        // Lấy từng ObjectJson trong ArrayJson
        for (int i = 0; i < size; ++ i)
        {
            try {
                json = response.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Và chuyển sang class SubjectDataModel
            SubjectDataModel data = FromJson(json);
            list.add(data);
        }
        return list;
    }

    // Phương thức lấy một Subject từ ObjectJson
    public SubjectDataModel FromJson(JSONObject json) {

        SubjectDataModel data = new SubjectDataModel();
        try {
            data.Id = json.getString("id");
            data.Major = json.getString("major");
            data.Subject = json.getString("subject");
            data.Credit = Integer.parseInt(json.getString("credit"));
            data.Teacher = json.getString("teacher");
            data.TimeStart = json.getString("timestart");
            data.TimeFinish = json.getString("timefinish");
            data.Status = Byte.parseByte(json.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentListener) {
            mListener = (OnRegisterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRegisterFragmentListener {
        // TODO: Update argument type and name
        void onItemPressed(String content);
    }
}
    // Adapter tự tạo cho ExpandableListView và RecyclerView
    class CustomExpandableListView extends BaseExpandableListAdapter {

    Context context;
    List<String> listHeader;
    HashMap<String, ArrayList<ArrayList<String>>> listChild;

    public CustomExpandableListView(Context context, List<String> listHeader, HashMap<String, ArrayList<ArrayList<String>>> listChild) {
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.fragment_register_group, null);

        // Lấy text trong các Group để hiển thị
        TextView txtHeader = convertView.findViewById(R.id.textViewHeader);
        txtHeader.setText(getGroup(groupPosition).toString());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.fragment_register_child, null);

        // Lấy danh sách của từng Child
        List<String> list = listChild.get(listHeader.get(groupPosition)).get(childPosition);

        // Đưa vào RecylerView
        RecyclerView recyclerView = convertView.findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // Adapter tự tạo cho RecyclerView
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

        private List<String> data;

        public RecyclerViewAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.fragment_register_child_item, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.txtItem.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {
            TextView txtItem;
            public RecyclerViewHolder(View itemView) {
                super(itemView);
                txtItem = itemView.findViewById(R.id.item);
            }
        }
    }

}

