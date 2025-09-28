package com.mobileapp.studentnest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
// listens to changes made to the recyclerview and is responsible for making views too
public class LoanRecyclerViewAdapter extends RecyclerView.Adapter<LoanRecyclerViewAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    private static GameViewModel gameViewModel;
    Context context;
    String loanName;
    int numOfLoans =  0;
    static HashMap<Object, Object> specificLoan;
    static ArrayList<HashMap<Object, Object>> loanNames = new ArrayList<>();

    public LoanRecyclerViewAdapter(Context context, ArrayList<HashMap<Object, Object>> loans, GameViewModel gvm
    , RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        loanNames = loans;
        gameViewModel = gvm;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void iterateLoan() {
        numOfLoans++;
    }

    @NonNull
    @Override
    public LoanRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where we inflate the layout (giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new LoanRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanRecyclerViewAdapter.MyViewHolder holder, int position) {
        // assigning values to the views we created in the recycler_view_row layout file
        // based on the position of the recycler view
        loanName = (String)loanNames.get(position).get("name");
//        specificLoan = loanNames.get(position);
//        specificLoan.put("position", position);
        holder.loanName.setText(this.loanName);
    }

    @Override
    public int getItemCount() {
        // the recycler view wants to know the number of items you want displayed
        return loanNames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabbing the views from our recycler_view_row layout file
        // similar to onCreate method

        ImageButton trashButton, infoButton;
        TextView loanName;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            trashButton = itemView.findViewById(R.id.trashButton);
            infoButton = itemView.findViewById(R.id.infoButton);
            loanName = itemView.findViewById(R.id.loanName);

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        specificLoan = loanNames.get(position);

                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position, specificLoan, view);
                        }
                    }
                }
            });

            trashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        ArrayList<HashMap<Object, Object>> loans = gameViewModel.getLoans();

                        if (position >= 0 && position < loans.size()) {
                            loans.remove(position);

                            // Refresh screen
                            ((FragmentActivity) view.getContext()).recreate();
                        }
                    }
                }
            });
        }
    }
}
