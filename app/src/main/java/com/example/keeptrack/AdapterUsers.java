package com.example.keeptrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keeptrack.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder>{

   private String currentID = FirebaseAuth.getInstance().getCurrentUser().getUid();
   String Current_State;

    Context context;
    List<User> userList;

    DatabaseReference RequestsRef;
    Button SendRequestButton;

    public AdapterUsers(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_user, viewGroup, false);
        SendRequestButton = view.findViewById(R.id.btn_request_accept);
        Current_State = "new";

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int position) {

        final String receiverID = userList.get(position).getUid();
        String userIc = userList.get(position).getImage();
        final String userName = userList.get(position).getName();
        final String userEmail = userList.get(position).getEmail();

        myHolder.mUserName.setText(userName);
        myHolder.mUserEmail.setText(userEmail);

        try{

            Picasso.get().load(userIc)
                    .placeholder(R.drawable.ic_prof)
                    .into(myHolder.mUserIc);

        }catch (Exception e){

        }

        if (!(currentID.equals(receiverID))){

            SendRequestButton.setVisibility(View.VISIBLE);
            SendRequestButton.setMinimumWidth(350);
            SendRequestButton.setText("Send request");

            SendRequestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Current_State.equals("new")) {

                    RequestsRef = FirebaseDatabase.getInstance().getReference().child("Requests");

                    RequestsRef.child(currentID).child(receiverID)
                            .child("request_type").setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        RequestsRef.child(receiverID).child(currentID)
                                                .child("request_type").setValue("received")
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Current_State = "request_sent";
                                                            Toast.makeText(context, "Your request to " + userName + " is successfull!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }

                                }
                    });
                }
            }
        });
        }

        else {
            SendRequestButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class  MyHolder extends RecyclerView.ViewHolder{

        ImageView mUserIc;
        TextView mUserName, mUserEmail;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mUserIc = itemView.findViewById(R.id.user_ic);
            mUserName = itemView.findViewById(R.id.user_name);
            mUserEmail = itemView.findViewById(R.id.user_email);
        }
    }
}
