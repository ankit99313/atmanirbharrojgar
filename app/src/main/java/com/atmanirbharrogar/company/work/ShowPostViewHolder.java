package com.atmanirbharrogar.company.work;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Profession,NeedWorkerIn,JudgeWork,Title,LongDescription,Address,Mobile,pin;

    public ShowPostViewHolder(@NonNull View itemView) {
        super(itemView);

        Profession=(TextView)itemView.findViewById(R.id.Post_professin);
        NeedWorkerIn=(TextView)itemView.findViewById(R.id.Post_NeedWorkr);
        JudgeWork=(TextView)itemView.findViewById(R.id.Post_JudgeWork);
        Title=(TextView)itemView.findViewById(R.id.Post_Title);
        LongDescription=(TextView)itemView.findViewById(R.id.Post_LongDescription);
        Address=(TextView)itemView.findViewById(R.id.Post_Adress);
        Mobile=(TextView)itemView.findViewById(R.id.Post_Mobile);
        pin=(TextView)itemView.findViewById(R.id.Post_Pin);
    }



    @Override
    public void onClick(View v) {

    }
}
