package com.atmanirbharrogar.company.work;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import Interface.ItemClickListner;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView  txtpersonName, txtpersonphone, txtpersonProfession,txtpersoncity,txtaltpersonphone,txtgender,txtstate,txtAddress ;
    public ImageView perimageView;
    public ItemClickListner listner;

    public  PersonViewHolder(@NonNull View itemView)
    {
        super(itemView);
        perimageView = (ImageView) itemView.findViewById(R.id.person_image);
        txtpersonName = (TextView) itemView.findViewById(R.id.person_name);
        txtpersonphone = (TextView) itemView.findViewById(R.id.person_phone);
        txtaltpersonphone = (TextView) itemView.findViewById(R.id.personalt_phone);
        txtpersoncity = (TextView) itemView.findViewById(R.id.person_city);
        txtpersonProfession = (TextView) itemView.findViewById(R.id.person_Profession);
        txtgender= (TextView) itemView.findViewById(R.id.person_Gender);
        txtstate= (TextView) itemView.findViewById(R.id.person_state);
        txtAddress= (TextView) itemView.findViewById(R.id.person_address);
    }
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
