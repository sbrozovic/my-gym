package com.example.sara.mygym.Modules.Members;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.Modules.Home.BossStartActivity;
import com.example.sara.mygym.Modules.Home.StaffStartActivity;
import com.example.sara.mygym.Modules.Members.Item.MemberChild;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Models.Members.Membership;
import com.example.sara.mygym.Common.Models.Role;
import com.example.sara.mygym.Common.Models.Members.Members;
import com.example.sara.mygym.Modules.Members.Adapters.MembersAdapter;
import com.example.sara.mygym.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sara.mygym.Common.UI.AlertManager.runDialogSingOut;

public class MembersFragment extends Fragment {

    private RadioGroup radioGroupMembershipStatus;
    private RadioGroup radioGroupMembersInGym;
    private RadioButton radioButtonactive, radioButtonexpired, radioButtonPresent, radioButtonNotPresent;
    public boolean isRadioButtonActive = true;
    public boolean isRadioButtonExpired = false;
    public boolean isRadioButtonPresent = false;
    public boolean isRadioButtonNotPresent = false;
    private ImageView imageViewSingOut;
    private EditText search;
    private TextView couchName;
    private SwipeRefreshLayout swipeRefreshLayout;
    RadioGroup.OnCheckedChangeListener listenerMembership;
    RadioGroup.OnCheckedChangeListener listenerInGym;
    private RecyclerView recyclerView;
    private List<Members> membersList = new ArrayList<>();
    private MembersAdapter membersAdapter;
    private List<Integer> listOfExpendParents = new ArrayList<>();
    private Map<Integer, Integer> mapForRemoveDivider = new HashMap<>();
    private List<Members> sortedList = new ArrayList<>();
    private List<Members> currentRecyclerViewList = new ArrayList<>();

    ApiInterface apiInterface;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = ApiClient.getClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_members, container, false);
        couchName = (TextView) layout.findViewById(R.id.textViewCoachName);
        radioGroupMembershipStatus = (RadioGroup) layout.findViewById(R.id.radioGroupMembershipStatus);
        radioButtonactive = (RadioButton) layout.findViewById(R.id.radioButtonActive);
        radioButtonexpired = (RadioButton) layout.findViewById(R.id.radioButtonExpired);
        radioGroupMembersInGym = (RadioGroup) layout.findViewById(R.id.radioGroupMembersInGym);
        radioButtonPresent = (RadioButton) layout.findViewById(R.id.radioButtonPresent);
        radioButtonNotPresent = (RadioButton) layout.findViewById(R.id.radioButtonNotPresent);
        imageViewSingOut = (ImageView) layout.findViewById(R.id.imageViewSingOutBossStaff);
        bundle = this.getArguments();
        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeToRefreshMembersFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initAllData(false);
                        setCouchNames();
                        if (bundle != null) {
                            String openedFragment = (String) bundle.get("Members_opened");
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

        initAllData(true);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerViewMembers);
        search = (EditText) layout.findViewById(R.id.search_members);
        radioButtonactive.setChecked(true);

        FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fabAddMembers);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditMemberActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Add_edit_opened_member", "add");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        imageViewSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDialogSingOut(getContext(), getActivity());
            }
        });
        setCouchNames();
        return layout;
    }

    private void setCouchNames(){
        final List<Person> presentCounchList = new ArrayList<>();
        Call<List<PersonModel>> call = apiInterface.getListOfPersons();
        call.enqueue(new Callback<List<PersonModel>>() {
            @Override
            public void onResponse(Call<List<PersonModel>> call, Response<List<PersonModel>> response) {
                List<PersonModel> personModelList = response.body();
                for (PersonModel el : personModelList) {
                    if (el.getPerson().getRole().getName().equals("staff") && el.getPerson().isInGym()) {
                        presentCounchList.add(el.getPerson());
                    }
                }
                String names = null;
                if (presentCounchList.size() == 0) {
                    couchName.setText("None");
                } else {
                    int i = 1;
                    for (Person person : presentCounchList) {
                        if (i == 1) {
                            names = person.getName();
                        } else {
                            names += person.getName();
                        }
                        if (i < presentCounchList.size()) {
                            names += ", ";
                            i++;
                        }
                    }
                    couchName.setText(names);
                }
            }

            @Override
            public void onFailure(Call<List<PersonModel>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Currently not able to get list of coaches that are present in gym.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setRadioButtonListenerForMembership() {
        listenerMembership = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                radioGroupMembersInGym.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                radioGroupMembersInGym.clearCheck(); // clear the second RadioGroup!
                radioGroupMembersInGym.setOnCheckedChangeListener(listenerInGym); //reset the listener
                if (checkedId == R.id.radioButtonActive) {
                    isRadioButtonActive = true;
                    isRadioButtonExpired = false;
                    isRadioButtonNotPresent = false;
                    isRadioButtonPresent = false;
                    sortMembersByActiveNotActive();
                } else if (checkedId == R.id.radioButtonExpired) {
                    isRadioButtonActive = false;
                    isRadioButtonExpired = true;
                    isRadioButtonNotPresent = false;
                    isRadioButtonPresent = false;
                    sortMembersByActiveNotActive();
                }
                listOfExpendParents.clear();
                mapForRemoveDivider.clear();
                membersAdapter = new MembersAdapter(getContext(), sortedList, MembersFragment.this);
                setExpandCollapseListener(membersAdapter, sortedList);
                recyclerView.setAdapter(membersAdapter);
            }
        };
        radioGroupMembershipStatus.setOnCheckedChangeListener(listenerMembership);
    }

    private void setRadioButtonListenerForInGym() {
        listenerInGym = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                radioGroupMembershipStatus.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                radioGroupMembershipStatus.clearCheck(); // clear the second RadioGroup!
                radioGroupMembershipStatus.setOnCheckedChangeListener(listenerMembership); //reset the listener
                if (checkedId == R.id.radioButtonPresent) {
                    isRadioButtonActive = false;
                    isRadioButtonExpired = false;
                    isRadioButtonNotPresent = false;
                    isRadioButtonPresent = true;
                    sortMembersByActiveNotActive();
                } else if (checkedId == R.id.radioButtonNotPresent) {
                    isRadioButtonActive = false;
                    isRadioButtonExpired = false;
                    isRadioButtonNotPresent = true;
                    isRadioButtonPresent = false;
                    sortMembersByActiveNotActive();
                }
                listOfExpendParents.clear();
                mapForRemoveDivider.clear();
                membersAdapter = new MembersAdapter(getContext(), sortedList, MembersFragment.this);
                setExpandCollapseListener(membersAdapter, sortedList);
                recyclerView.setAdapter(membersAdapter);
            }
            // }
        };

        radioGroupMembersInGym.setOnCheckedChangeListener(listenerInGym);
    }


    private void initAllData(final boolean isFirstSetup) {
        membersList.clear();
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        final MemberChild memberChild = new MemberChild("Logout", "Membership", "Edit");
        final List<MemberChild> list = new ArrayList<>();
        list.add(memberChild);

        Call<List<PersonModel>> call = apiInterface.getListOfPersons();
        call.enqueue(new Callback<List<PersonModel>>() {
            @Override
            public void onResponse(Call<List<PersonModel>> call, Response<List<PersonModel>> response) {
                List<PersonModel> personModelList = response.body();
                for (PersonModel el : personModelList) {
                    if (el.getPerson().getRole().getName().equals("member") && el.getPerson().isActive()) {
                        membersList.add(new Members(el.getPerson().getId(), el.getPerson().getName(), el.getPerson().getSurname(), el.getPerson().getBirthDate(), el.getPerson().isMale(), el.getPerson().getRole(), el.getPerson().getPhone(), el.getPerson().getAddress(), el.getPerson().isInGym(), el.getPerson().isActive(), el.getPerson().getMembership(), el.getPerson().getAccount()));
                    }
                }
                for (Members el : membersList) {
                    el.setChildObjectList(list);
                }
                if (isFirstSetup) {
                    setRecyclerViewAndSearch();
                    setRadioButtonListenerForMembership();
                    setRadioButtonListenerForInGym();
                    sortMembersByActiveNotActive();
                } else {
                    sortMembersByActiveNotActive();
                }
            }

            @Override
            public void onFailure(Call<List<PersonModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setRecyclerViewAndSearch() {
        membersAdapter = new MembersAdapter(getContext(), currentRecyclerViewList, MembersFragment.this);
        setExpandCollapseListener(membersAdapter, currentRecyclerViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
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
        recyclerView.setAdapter(membersAdapter);
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
                final List<Members> filterdList = new ArrayList<>();
                for (int i = 0; i < currentRecyclerViewList.size(); i++) {
                    final String text = currentRecyclerViewList.get(i).getName().toLowerCase() + " " + currentRecyclerViewList.get(i).getSurname().toLowerCase();
                    if (text.contains(s)) {
                        filterdList.add(currentRecyclerViewList.get(i));
                    }
                }
                if (s.equals("")) {
                    filterdList.clear();
                    filterdList.addAll(currentRecyclerViewList);
                }
                //currentRecyclerViewList = filterdList;
                listOfExpendParents.clear();
                mapForRemoveDivider.clear();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                membersAdapter = new MembersAdapter(getContext(), filterdList, MembersFragment.this);
                setExpandCollapseListener(membersAdapter, filterdList);
                recyclerView.setAdapter(membersAdapter);
                membersAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sortMembersByActiveNotActive() {
        sortedList = new ArrayList<>();
        for (Members member : membersList) {
            if (isRadioButtonActive == true) {
                if (member.getMembership().getMembershipActiveUntil().after(new Date())) {
                    sortedList.add(member);
                }
            } else if (isRadioButtonExpired == true) {
                if (!member.getMembership().getMembershipActiveUntil().after(new Date())) {
                    sortedList.add(member);
                }
            } else if (isRadioButtonPresent == true) {
                if (member.isInGym()) {
                    sortedList.add(member);
                }
            } else if (isRadioButtonNotPresent == true) {
                if (!member.isInGym()) {
                    sortedList.add(member);
                }
            }
        }
        currentRecyclerViewList = sortedList;
        listOfExpendParents.clear();
        mapForRemoveDivider.clear();
        search.setText("");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        membersAdapter = new MembersAdapter(getContext(), sortedList, MembersFragment.this);
        setExpandCollapseListener(membersAdapter, sortedList);
        recyclerView.setAdapter(membersAdapter);
        membersAdapter.notifyDataSetChanged();
    }

    private void setExpandCollapseListener(final MembersAdapter membersAdapter, final List<Members> list) {
        membersAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onParentExpanded(final int parentPosition) {
                Members expandedMember = list.get(parentPosition);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == parentPosition) {
                    recyclerView.smoothScrollToPosition(lastVisiblePosition + 1);
                }
                for (int i = 0; i < list.size(); i++) {
                    if (i != parentPosition) {
                        membersAdapter.collapseParent(i);
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
                Members collapsedRecipe = list.get(parentPosition);
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

    public void runDialogForLoginLogout(String textOfMessage, final int parentPosition) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(textOfMessage);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (currentRecyclerViewList.get(parentPosition).isInGym()) {
                           setPersonInGym(parentPosition);
                        } else {
                            setPersonLeftGym(parentPosition);
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void setPersonInGym(int parentPosition){
        Members member = currentRecyclerViewList.get(parentPosition);
        member.setInGym(false);
        Person person = new Person(member.getId(), member.getName(), member.getSurname(), member.getBirthDate(), member.isMale(), member.getRole(), member.getMembership(), member.getAccount(), member.getPhone(), member.getAddress(), member.isInGym(), member.isActive());
        Call<Boolean> call = apiInterface.putPerson(new PersonModel(person));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                initAllData(false);
                if (bundle != null) {
                    String openedFragment = (String) bundle.get("Members_opened");
                    if (openedFragment.equals("boss")) {
                        ((BossStartActivity) getActivity()).setNumberOfPresentMembers();
                    }
                    else{
                        ((StaffStartActivity) getActivity()).setNumberOfPresentMembers();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setPersonLeftGym(int parentPosition){
        Members member = currentRecyclerViewList.get(parentPosition);
        member.setInGym(true);
        Person person = new Person(member.getId(), member.getName(), member.getSurname(), member.getBirthDate(), member.isMale(), member.getRole(), member.getMembership(), member.getAccount(), member.getPhone(), member.getAddress(), member.isInGym(), member.isActive());
        Call<Boolean> call = apiInterface.putPerson(new PersonModel(person));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                initAllData(false);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void runDialogForMembership(Membership membership, final Members member) {
        final TextView nameMembership = new TextView(getContext());
        final TextView price = new TextView(getContext());
        final TextView until = new TextView(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Membership: ");
        builder1.setCancelable(true);

        nameMembership.setText("Name: " + membership.getName());
        nameMembership.setPadding(40, 40, 40, 0);
        layout.addView(nameMembership);

        price.setText(String.valueOf("Price: " + membership.getPrice()));
        price.setPadding(40, 40, 40, 0);
        layout.addView(price);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        until.setText("Active until: " + formatter.format(membership.getMembershipActiveUntil()));
        until.setPadding(40, 40, 40, 0);
        layout.addView(until);

        builder1.setView(layout);

        if (membership.getMembershipActiveUntil().after(new Date())) {
            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            builder1.setPositiveButton(
                    "Update",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            openEditMember(member, true);
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public void openEditMember(Members member, boolean isMembershipExpired) {
        Person person = new Person(member.getId(), member.getName(), member.getSurname(), member.getBirthDate(), member.isMale(), member.getRole(), member.getMembership(), member.getAccount(), member.getPhone(), member.getAddress(), member.isInGym(), member.isActive());
        Membership membership = new Membership(member.getMembership().getId(), member.getMembership().getName(), member.getMembership().getPrice(), member.getMembership().getMembershipStartsOn(), member.getMembership().getMembershipActiveUntil(), member.getMembership().isStatus());
        Intent intent = new Intent(getActivity(), AddEditMemberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Add_edit_opened_member", "edit");
        intent.putExtras(bundle);
        bundle.putSerializable("Member_element", person);
        intent.putExtras(bundle);
        bundle.putSerializable("Membership_element", membership);
        intent.putExtras(bundle);
        bundle.putBoolean("Membership_expired", isMembershipExpired);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
