package com.dam05.geopromenade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dam05.geopromenade.R;
import com.dam05.geopromenade.Model.User;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {
    List<User> users;
    Context context;

    public UserRecyclerAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Création d'un layout inflater dans le context ou il est créé
        LayoutInflater inflater = LayoutInflater.from(context);
        /**
         * On remplit layout avec notre custom layout (item recycler layout); 3 params :
         *  @ResLayout int res: le fichier layout
         *  @ViewGroup root: la vue parent
         *  @boolean attachToRoot: à false pour ne pas attaché à son parent
         */
        View view = inflater.inflate(R.layout.user_recycler, parent, false);
        /**
         * On renvoie une instance de notre custom ViewHolder
         */
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        //On bind notre custom ViewHolder "UserViewHolder" avec nos users
        User currentUser = users.get(position);
        holder.tv_userName.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
        holder.tv_bio.setText(users.get(position).getBio());
//        holder.iv_avatar.setImageResource(Integer.parseInt(currentUser.getAvatar()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tv_userName, tv_bio;
        ImageView iv_avatar;
        ConstraintLayout mainLayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_bio = itemView.findViewById(R.id.tv_bio);
            iv_avatar = itemView.findViewById(R.id.iv_userAvatar);
            mainLayout = itemView.findViewById(R.id.mainLayoutHorizontal);
        }
    }
}
