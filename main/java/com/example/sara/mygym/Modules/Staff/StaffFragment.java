package com.example.sara.mygym.Modules.Staff;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Models.Role;
import com.example.sara.mygym.Common.Models.Staff.Staff;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.Modules.Home.BossStartActivity;
import com.example.sara.mygym.Modules.Home.StaffStartActivity;
import com.example.sara.mygym.Modules.Staff.OnlyMember.StaffAdapter;
import com.example.sara.mygym.Modules.Staff.OnlyBoss.Adapters.StaffExpandableAdapter;
import com.example.sara.mygym.Modules.Staff.OnlyBoss.Item.StaffChild;
import com.example.sara.mygym.Modules.Staff.OnlyBoss.AddEditStaffActivity;
import com.example.sara.mygym.Modules.Staff.OnlyStaff.Adapters.StaffExpandableAdapterForStaff;
import com.example.sara.mygym.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sara.mygym.Common.UI.AlertManager.runDialogSettings;

public class StaffFragment extends Fragment {
    private EditText search;
    private ImageView imageViewSettings;
    private ImageView imageViewLoginLogoutFromGym;
    private TextView textViewLoginLogoutFromGym;
    private RecyclerView recyclerView;
    private StaffExpandableAdapter staffExpandableAdapter;
    private StaffExpandableAdapterForStaff staffExpandableAdapterForStaff;
    private StaffAdapter staffAdapter;
    private List<Staff> staffList = new ArrayList<>();
    private List<Integer> listOfExpendParents = new ArrayList<>();
    private Map<Integer, Integer> mapForRemoveDivider = new HashMap<>();
    private boolean isBossOpened = false;
    private boolean isStaffOpened = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    ApiInterface apiInterface;
    Person user;

    @Override
    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        apiInterface = ApiClient.getClient();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_staff, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerViewStaff);
        search = (EditText) layout.findViewById(R.id.editTextSearchStaff);
        FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fabAddStaff);

        imageViewLoginLogoutFromGym = (ImageView) layout.findViewById(R.id.imageViewStaffLoginLogoutIntoGym);
        textViewLoginLogoutFromGym = (TextView) layout.findViewById(R.id.textViewLoginLogoutFromGym);
        imageViewSettings = (ImageView) layout.findViewById(R.id.imageViewSettingsStaff);

        final Bundle bundle = this.getArguments();

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeToRefreshStaffFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDataSet(false);
                        if (bundle != null) {
                            String openedFragment = (String) bundle.get("Staff_opened");
                            if (openedFragment.equals("boss")) {
                                ((BossStartActivity) getActivity()).setNumberOfPresentMembers();
                            }
                            else{
                                ((StaffStartActivity) getActivity()).setNumberOfPresentMembers();
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        user = (Person) getActivity().getIntent().getSerializableExtra("user");

        imageViewLoginLogoutFromGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginLogout();
            }
        });

        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDialogSettings(getContext(), user, apiInterface);
            }
        });

        if (bundle != null) {
            String openedFragment = (String) bundle.get("Staff_opened");
            if (openedFragment.equals("boss")) {
                isBossOpened = true;
                isStaffOpened = false;
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AddEditStaffActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Add_edit_opened", "add");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            } else if (openedFragment.equals("staff")) {
                isBossOpened = false;
                isStaffOpened = true;
                fab.setVisibility(View.GONE);
                imageViewSettings.setVisibility(View.VISIBLE);
            } else {
                isBossOpened = false;
                isStaffOpened = false;
                fab.setVisibility(View.GONE);
                imageViewLoginLogoutFromGym.setVisibility(View.GONE);
                textViewLoginLogoutFromGym.setVisibility(View.GONE);
            }
        }
        initDataSet(true);
        setLoginLogoutLayout();
        return layout;
    }

    private void setLoginLogout() {
        if (textViewLoginLogoutFromGym.getText().toString().equals("Check in")) {
            user.setInGym(true);
        } else {
            user.setInGym(false);
        }
        Call<Boolean> call = apiInterface.putPerson(new PersonModel(user));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean b = response.body();
                setLoginLogoutLayout();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Unable to change presents in gym.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoginLogoutLayout() {
        if (user.isInGym()) {
            imageViewLoginLogoutFromGym.setImageResource(R.drawable.logout);
            textViewLoginLogoutFromGym.setText("Check out");
        } else {
            imageViewLoginLogoutFromGym.setImageResource(R.drawable.login);
            textViewLoginLogoutFromGym.setText("Check in");
        }
    }

    private void initDataSet(final boolean isFirstSetup) {
        staffList.clear();
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        StaffChild staffChild = new StaffChild("Call", "Edit");
        final List<StaffChild> list = new ArrayList<>();
        list.add(staffChild);

        Call<List<PersonModel>> call = apiInterface.getListOfPersons();
        call.enqueue(new Callback<List<PersonModel>>() {
            @Override
            public void onResponse(Call<List<PersonModel>> call, Response<List<PersonModel>> response) {
                List<PersonModel> personModelList = response.body();
                for (PersonModel el : personModelList) {
                    Person person = el.getPerson();
                    boolean isStaffOrBoss = person.getRole().getName().equals("staff") || person.getRole().getName().equals("boss");
                    boolean isActiveAccount = el.getPerson().isActive();
                    boolean isNotThisUser = el.getPerson().getId() != user.getId();
                    if (isStaffOrBoss && isActiveAccount && isNotThisUser) {
                        staffList.add(new Staff(person.getId(), person.getName(), person.getSurname(), person.getBirthDate(), person.isMale(), person.getRole(), person.getPhone(), person.getAddress(), person.isInGym(), person.getAccount(), person.isActive()));
                    }
                }
                for (Staff el : staffList) {
                    el.setChildObjectList(list);
                }
                if (isFirstSetup) {
                    setRecyclerViewAndSearch();
                }
                else{
                    listOfExpendParents.clear();
                    mapForRemoveDivider.clear();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (isBossOpened) {
                        staffExpandableAdapter = new StaffExpandableAdapter(getContext(), staffList, StaffFragment.this);
                        setExpandCollapseListener(staffExpandableAdapter, staffList);
                        recyclerView.setAdapter(staffExpandableAdapter);
                        staffExpandableAdapter.notifyDataSetChanged();
                    } else if (isStaffOpened) {
                        staffExpandableAdapterForStaff = new StaffExpandableAdapterForStaff(getContext(), staffList, StaffFragment.this);
                        setExpandCollapseListener(staffExpandableAdapterForStaff, staffList);
                        recyclerView.setAdapter(staffExpandableAdapterForStaff);
                        staffExpandableAdapterForStaff.notifyDataSetChanged();
                    } else {
                        staffAdapter = new StaffAdapter(staffList);
                        recyclerView.setAdapter(staffAdapter);
                        staffAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PersonModel>> call, Throwable t) {

            }
        });
    }

    private void setRecyclerViewAndSearch() {
        if (isBossOpened) {
            staffExpandableAdapter = new StaffExpandableAdapter(getContext(), staffList, StaffFragment.this);
            setExpandCollapseListener(staffExpandableAdapter, staffList);
        } else if (isStaffOpened) {
            staffExpandableAdapterForStaff = new StaffExpandableAdapterForStaff(getContext(), staffList, StaffFragment.this);
            setExpandCollapseListener(staffExpandableAdapterForStaff, staffList);
        } else {
            staffAdapter = new StaffAdapter(staffList);
        }
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.WHITE)
                .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        if (mapForRemoveDivider.containsValue(position)) {
                            return true;
                        }
                        return false;
                    }
                })
                .marginResId(R.dimen.left_margin, R.dimen.right_margin)
                .build());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (isBossOpened) {
            recyclerView.setAdapter(staffExpandableAdapter);
        } else if (isStaffOpened) {
            recyclerView.setAdapter(staffExpandableAdapterForStaff);
        } else {
            recyclerView.setAdapter(staffAdapter);
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();
                final List<Staff> filterdList = new ArrayList<>();
                for (int i = 0; i < staffList.size(); i++) {
                    final String text = (staffList.get(i).getName() + " " + staffList.get(i).getSurname()).toLowerCase();
                    if (text.contains(s)) {
                        filterdList.add(staffList.get(i));
                    }
                }
                listOfExpendParents.clear();
                mapForRemoveDivider.clear();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if (isBossOpened) {
                    staffExpandableAdapter = new StaffExpandableAdapter(getContext(), filterdList, StaffFragment.this);
                    setExpandCollapseListener(staffExpandableAdapter, filterdList);
                    recyclerView.setAdapter(staffExpandableAdapter);
                    staffExpandableAdapter.notifyDataSetChanged();
                } else if (isStaffOpened) {
                    staffExpandableAdapterForStaff = new StaffExpandableAdapterForStaff(getContext(), filterdList, StaffFragment.this);
                    setExpandCollapseListener(staffExpandableAdapterForStaff, filterdList);
                    recyclerView.setAdapter(staffExpandableAdapterForStaff);
                    staffExpandableAdapterForStaff.notifyDataSetChanged();
                } else {
                    staffAdapter = new StaffAdapter(filterdList);
                    recyclerView.setAdapter(staffAdapter);
                    staffAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setExpandCollapseListener(final ExpandableRecyclerAdapter expandableAdapter, final List<Staff> list) {
        expandableAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onParentExpanded(final int parentPosition) {
                Staff expandedRecipe = list.get(parentPosition);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == parentPosition) {
                    recyclerView.smoothScrollToPosition(lastVisiblePosition + 1);
                }
                for (int i = 0; i < list.size(); i++) {
                    if (i != parentPosition) {
                        expandableAdapter.collapseParent(i);
                        onParentCollapsed(i);
                    }
                }
                if (!listOfExpendParents.contains(parentPosition)) {
                    listOfExpendParents.add(parentPosition);
                }
                algoritamForDealingWithDividersInRecyclerView(listOfExpendParents, mapForRemoveDivider);
            }

            @Override
            public void onParentCollapsed(int parentPosition) {
                Staff collapsedRecipe = list.get(parentPosition);
                if (listOfExpendParents.contains(parentPosition)) {
                    listOfExpendParents.remove((Integer) parentPosition);
                    mapForRemoveDivider.remove(parentPosition);
                    algoritamForDealingWithDividersInRecyclerView(listOfExpendParents, mapForRemoveDivider);
                }
            }
        });
    }

    private void algoritamForDealingWithDividersInRecyclerView(List<Integer> listOfExpendParents, Map<Integer, Integer> mapForRemoveDivider) {
        for (Integer i : listOfExpendParents) {
            int count = 0;
            for (Integer integer : listOfExpendParents) {
                if (integer < i) {
                    count++;
                }
            }
            mapForRemoveDivider.put(i, count + i);
        }
    }

    public void openEditStaff(Staff staff) {
        Person person = new Person(staff.getId(), staff.getName(), staff.getSurname(), staff.getBirthDate(), staff.isMale(), staff.getRole(), staff.getMembership(), staff.getAccount(), staff.getPhone(), staff.getAddress(), staff.isInGym(), staff.isActive());
        Intent intent = new Intent(getActivity(), AddEditStaffActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Add_edit_opened", "edit");
        intent.putExtras(bundle);
        bundle.putSerializable("Staff_element", person);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void callStaff(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
