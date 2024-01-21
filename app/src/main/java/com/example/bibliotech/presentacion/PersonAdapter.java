package com.example.bibliotech.presentacion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.User;

import org.w3c.dom.Text;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private List<User> userList;

    public PersonAdapter(List<User> personList) {
        this.userList = personList;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtSurname;
        private TextView txtEmail;
        private TextView txtCategory;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtSurname = itemView.findViewById(R.id.txtSurname);
            txtEmail = itemView.findViewById(R.id.txtCorreo);
            txtCategory = itemView.findViewById(R.id.txtUserType);
        }

        public void bind(User user) {
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
            txtSurname.setText(user.getSurnames());
            txtCategory.setText(user.getRole());
        }
    }
}
