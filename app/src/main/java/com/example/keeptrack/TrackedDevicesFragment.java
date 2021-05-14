package com.example.keeptrack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keeptrack.Models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrackedDevicesFragment extends Fragment {

    public TrackedDevicesFragment() {
        // Required empty public constructor
    }

    private View DevicesFragmentView;

    private RecyclerView myDevicesList;

    private DatabaseReference UserRef, DevicesRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DevicesFragmentView = inflater.inflate(R.layout.fragment_tracked_devices, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        DevicesRef = FirebaseDatabase.getInstance().getReference().child("Devices");

        myDevicesList = DevicesFragmentView.findViewById(R.id.devices_list);
        myDevicesList.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        myDevicesList.setLayoutManager(manager);

        return DevicesFragmentView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(DevicesRef.child(currentUserID), User.class)
                        .build();

        FirebaseRecyclerAdapter<User, TrackedDevicesFragment.DevicesViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, TrackedDevicesFragment.DevicesViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull final TrackedDevicesFragment.DevicesViewHolder holder, int position, @NonNull User model) {

                        holder.itemView.findViewById(R.id.btn_request_decline).setVisibility(View.VISIBLE);
                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference getTypeRef = getRef(position).child("Device").getRef();

                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists() ){

                                    String type = dataSnapshot.getValue().toString();

                                    if (type.equals("Saved")&& list_user_id!=null){

                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("image")){
                                                    //final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                                    //final String requestUserEmail = dataSnapshot.child("email").getValue().toString();
                                                    final String requestProfileImage = dataSnapshot.child("image").getValue().toString();

                                                    //holder.userName.setText(requestUserName);
                                                    //holder.userEmail.setText(requestUserEmail);
                                                    Picasso.get().load(requestProfileImage).into(holder.profileImage);

                                                }
                                                //else{
                                                final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                                final String requestUserEmail = dataSnapshot.child("email").getValue().toString();

                                                holder.userName.setText(requestUserName);
                                                holder.userEmail.setText(requestUserEmail);
                                                //}

                                                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        DevicesRef.child(currentUserID).child(list_user_id).child("Device")
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()) {

                                                                            DevicesRef.child(list_user_id).child(currentUserID).child("Keeper")
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            if (task.isSuccessful()) {

                                                                                                Toast.makeText(getContext(), "Track deleted", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }

                                                                    }
                                                                });
                                                    }

                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public TrackedDevicesFragment.DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_user, viewGroup, false);
                        TrackedDevicesFragment.DevicesViewHolder holder = new TrackedDevicesFragment.DevicesViewHolder(view);
                        return holder;
                    }
                };

        myDevicesList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class DevicesViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userEmail;
        CircleImageView profileImage;
        Button btnDelete;

        public DevicesViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            userEmail = itemView.findViewById(R.id.user_email);
            profileImage = itemView.findViewById(R.id.user_ic);

            btnDelete = itemView.findViewById(R.id.btn_request_decline);
            btnDelete.setText("Delete");
        }
    }
}
