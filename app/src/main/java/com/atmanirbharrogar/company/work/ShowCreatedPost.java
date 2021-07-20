package com.atmanirbharrogar.company.work;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowCreatedPost extends AppCompatActivity {
    private RecyclerView ShowPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_created_post);

        ShowPost=findViewById(R.id.post_list);
        LinearLayoutManager ll=new LinearLayoutManager(ShowCreatedPost.this);
       // ShowPost.setLayoutManager(new LinearLayoutManager(ShowCreatedPost.this));
        ll.setReverseLayout(true);
        ll.setStackFromEnd(true);
        ShowPost.setLayoutManager(ll);
    }


    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {

                    FirebaseRecyclerOptions<ShowPost> option=new FirebaseRecyclerOptions.Builder<ShowPost>().setQuery(reference, ShowPost.class)
                            .build();


                    FirebaseRecyclerAdapter<ShowPost,ShowPostViewHolder> adapter=new FirebaseRecyclerAdapter<ShowPost, ShowPostViewHolder>(option) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowPostViewHolder showPostViewHolder, int position, @NonNull ShowPost showPost) {

                            showPostViewHolder.Profession.setText(showPost.getCategory());
                            showPostViewHolder.NeedWorkerIn.setText(showPost.getWorkerTime());
                            showPostViewHolder.JudgeWork.setText(showPost.getJudgeWork());
                            showPostViewHolder.Title.setText(showPost.getTitle());
                            showPostViewHolder.LongDescription.setText(showPost.getLongDescription());




                        }

                        @NonNull
                        @Override
                        public ShowPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_post_layout, parent, false);
                            ShowPostViewHolder holde = new ShowPostViewHolder(view);
                            return holde;
                        }
                    };
                    ShowPost.setAdapter(adapter);
                    adapter.startListening();

                }



                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}