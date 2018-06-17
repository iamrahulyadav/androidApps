package com.latesttrendingapps.template;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByDetailsFragment extends Fragment {

    public static String state_name, district_name, constitution_name = null, gender = "Male";
    private View mainView;
    private EditText dob, name, fatherName;
    private NiceSpinner genderSpinner, statesSpinner, districtsSpinner, constitutionSpinner;
    private  Calendar calendar = Calendar.getInstance();
    private List<Object>  states = new ArrayList<>();
    private List<Object>  districts = new ArrayList<>();
    private List<Object>  constitutionsList = new ArrayList<>();

    private ResultAdapter resultAdapter;

    private RecyclerView bottomRecyclerView;
    private BottomSheetBehavior bottomSheetBehavior;
    private  View bottomSheet;
    private Button searchBtn;
    List<String> genderSet;

    public SearchByDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainView = inflater.inflate(R.layout.fragment_search_by_details, container, false);
        dob = (EditText) mainView.findViewById(R.id.search_view_dob);
        name = (EditText) mainView.findViewById(R.id.search_view_name);
        fatherName = (EditText) mainView.findViewById(R.id.search_view_father_name);
        genderSpinner = (NiceSpinner) mainView.findViewById(R.id.gender_spinner);
        statesSpinner = (NiceSpinner) mainView.findViewById(R.id.states_spinner);
        districtsSpinner = (NiceSpinner) mainView.findViewById(R.id.district_spinner);
        constitutionSpinner = (NiceSpinner) mainView.findViewById(R.id.constitution_spinner);

        bottomSheet = mainView.findViewById(R.id.result_bottom_sheet);
        bottomRecyclerView = mainView.findViewById(R.id.bottomSheetLayoutRecyclerView);
        bottomSheet.setVisibility(View.INVISIBLE);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        searchBtn = (Button) mainView.findViewById(R.id.search_btn_details);

        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        String[] GenderList = {"Male", "Female", "Others"};
        genderSet = new LinkedList<>(Arrays.asList(GenderList));
        genderSpinner.attachDataSource(genderSet);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        genderSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gender = genderSet.get(position);
                System.out.println(gender);
            }
        });
        states.add("Select State");
        districts.add("Select District");
        constitutionsList.add("Select Constitution");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String person_name_final = name.getText().toString();
                if (!person_name_final.equals("")) {
                    String dob_final = dob.getText().toString();
                    String father_name_final = fatherName.getText().toString();
                    String gender_final = gender;
                    String state_name_final = state_name;
                    String district_final = district_name;
                    String constitution_final = constitution_name;
                    if (state_name_final != null) {
                        if (state_name_final.equals("Select State")) {
                            state_name_final = null;
                        }
                        if (state_name_final.contains(" "))
                            state_name_final = state_name_final.replace(" ", "");

                    }
                    if (district_final != null) {
                        if (district_final.equals("Select District")) {
                            district_final = null;
                        }
                        if (district_final.contains(" "))
                            district_final = district_final.replace(" ", "");

                    }
                    if (constitution_final !=null) {
                        if (constitution_final.equals("Select Constitution")) {
                            constitution_final = null;
                        }
                        if (constitution_final.contains(" "))
                            constitution_final = constitution_final.replace(" ", "");

                    }

                    String url = getUrl(person_name_final, father_name_final, dob_final,
                            gender_final, state_name_final,
                            district_final, constitution_final);
                    System.out.println(url);
                    Object[] DataTransfer = new Object[10];
                    bottomSheet.setVisibility(View.VISIBLE);
                    String[] GenderList = {"Male", "Female", "Others","Male", "Female", "Others","Male", "Female", "Others","Male", "Female", "Others","Male", "Female", "Others"};
                    resultAdapter = new ResultAdapter(getContext(), GenderList);
                    bottomRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager mLinearLayout = new LinearLayoutManager(getContext());
                    bottomRecyclerView.setLayoutManager(mLinearLayout);
                    bottomRecyclerView.setAdapter(resultAdapter);

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final List<Object> states = loadStatesSpinner();

        statesSpinner.attachDataSource(states);
        districtsSpinner.attachDataSource(districts);
        constitutionSpinner.attachDataSource(constitutionsList);

        statesSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state_name = states.get(position).toString();
                String json = null;
                JSONArray dists = new JSONArray();
                JSONArray constis = new JSONArray();
                try {
                    InputStream is = getContext().getAssets().open("districts/"+state_name+".json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                    JSONObject jsonObject = new JSONObject(json);
                    dists = jsonObject.getJSONArray("dist");
                    districts = new ArrayList<>();
                    districts.add("Select District");
                        for (int j = 0; j<dists.length(); j++) {
                            JSONObject jsonDists = (JSONObject) dists.get(j);
                            districts.add(jsonDists.getString("show"));
                            districtsSpinner.attachDataSource(districts);
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {

                    if (state_name.equals("Select State")) {
                        districts = new ArrayList<>();
                        districts.add("Select District");
                        districtsSpinner.attachDataSource(districts);
                        constitutionsList = new ArrayList<>();
                        constitutionsList.add("Select Constitution");
                        constitutionSpinner.attachDataSource(constitutionsList);
                    } else {
                        constitutionsList = new ArrayList<>();
                        constitutionsList.add("Select Constitution");
                        constitutionSpinner.attachDataSource(constitutionsList);
                    }

                    district_name = null;
                    constitution_name = null;
                }
            }
        });
        districtsSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                district_name = districts.get(position).toString();
                String json = null;
                JSONArray dists = new JSONArray();
                JSONArray constis = new JSONArray();
                try {
                    InputStream is = getContext().getAssets().open("districts/"+state_name+".json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                    JSONObject jsonObject = new JSONObject(json);
                    dists = jsonObject.getJSONArray("dist");
                    for (int i = 0; i<dists.length(); i++) {
                        JSONObject jsonDists = (JSONObject) dists.get(i);
                        if (district_name.equals(jsonDists.getString("show"))){
                            constis = jsonDists.getJSONArray("acs");
                            constitutionsList = new ArrayList<>();
                            constitutionsList.add("Select Constitution");
                            for (int j = 0; j<constis.length(); j++) {
                                JSONObject jsonConstits = (JSONObject) constis.get(j);
                                constitutionsList.add(jsonConstits.getString("show"));
                                constitutionSpinner.attachDataSource(constitutionsList);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    if (district_name.equals("Select District")) {
                        constitutionsList = new ArrayList<>();
                        constitutionsList.add("Select Constitution");
                        constitutionSpinner.attachDataSource(constitutionsList);
                    }
                    constitution_name = null;
                }
            }
        });

        constitutionSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constitution_name = constitutionsList.get(position).toString();
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDOBTextView();
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
            }
        });
    }

    private String getUrl(String person_name_final, String father_name_final, String dob_final,
                          String gender_final, String state_name_final,
                          String district_final, String constitution_final) {
        StringBuilder stringBuilder = new StringBuilder("http://electoralsearch.in/VoterSearch/SASSearch?search_type=details");
        stringBuilder.append("&name="+person_name_final);
        if (!father_name_final.equals(""))
        stringBuilder.append("&rln_name="+father_name_final);
        if (!dob_final.equals(""))
        stringBuilder.append("&dob="+dob_final);
        stringBuilder.append("&gender="+gender_final);
        if (state_name_final != null)
        stringBuilder.append("&state="+state_name_final);
        if (district_final != null)
        stringBuilder.append("&pc_name="+district_final);
        if (constitution_final != null)
        stringBuilder.append("&ac_name="+constitution_final);
        return  (stringBuilder.toString());
    }

    public List<Object> loadStatesSpinner() {
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("states.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONArray array = new JSONArray(json);
            states = new ArrayList<>();
            states.add("Select State");
            for (int i = 0; i<array.length(); i++){
                JSONObject jobj = (JSONObject) array.get(i);
                states.add(jobj.getString("name"));
            }
            System.out.print(states.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return states;
    }
    private void updateDOBTextView() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(calendar.getTime()));
    }
}
