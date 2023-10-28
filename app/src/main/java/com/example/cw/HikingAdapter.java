package com.example.cw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HikingAdapter extends RecyclerView.Adapter<HikingViewHolder> {
    private Context context;
    private List<Hiking> dataList;
    DatabaseHelper dbHelper;
    MainActivity mainActivity;

    public HikingAdapter(Context context, List<Hiking> dataList, DatabaseHelper dbHelper, MainActivity mainActivity) {
        this.context = context;
        this.dataList = dataList;
        this.dbHelper = dbHelper;
        this.mainActivity = mainActivity;
    }

    public void resetDataList(List<Hiking> newDataList) {
        dataList = newDataList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HikingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hiking_card_item, parent, false);
        return new HikingViewHolder(view, dataList, dbHelper, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HikingViewHolder holder, int position) {
        holder.textViewName.setText("Name: "+ dataList.get(position).getName());
        holder.textViewLocation.setText("Location: "+ dataList.get(position).getLocation());
        holder.textViewDate.setText("Date: "+ dataList.get(position).getDate());
        holder.textViewParkingAvailable.setText("Parking Available: "+ dataList.get(position).getParkingAvailable());
        holder.textViewLengthOfHike.setText("Length Of Hike: "+ dataList.get(position).getLengthOfHike());
        holder.textViewDifficultLevel.setText("Difficult Level: "+ dataList.get(position).getDifficultLevel());
        holder.textViewDescription.setText("Description: "+ dataList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<Hiking> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class HikingViewHolder extends RecyclerView.ViewHolder {
    TextView textViewName,textViewLocation, textViewDate, textViewParkingAvailable, textViewLengthOfHike, textViewDifficultLevel, textViewDescription;
    CardView recCard;
    Button buttonDelete, buttonUpdate, buttonViewDetails;
    List<Hiking> dataList;
    DatabaseHelper dbHelper;
    HikingAdapter adapter;


    public HikingViewHolder(@NonNull View itemView, List<Hiking> dataList, DatabaseHelper dbHelper, HikingAdapter adapter) {
        super(itemView);
        this.dataList = dataList;
        this.dbHelper = dbHelper;
        this.adapter = adapter;

        recCard = itemView.findViewById(R.id.recCard);
        textViewName = itemView.findViewById(R.id.textView_Name);
        textViewLocation = itemView.findViewById(R.id.textView_location);
        textViewDate = itemView.findViewById(R.id.textView_date);
        textViewParkingAvailable = itemView.findViewById(R.id.textView_parkingAvailable);
        textViewLengthOfHike = itemView.findViewById(R.id.textView_lengthOfHike);
        textViewDifficultLevel = itemView.findViewById(R.id.textView_difficultLevel);
        textViewDescription = itemView.findViewById(R.id.textView_description);

        // Initialize the buttons
        buttonDelete = itemView.findViewById(R.id.btnDelete);
        buttonUpdate = itemView.findViewById(R.id.btnUpdate);
        buttonViewDetails = itemView.findViewById(R.id.btnViewDetails);

        // Set click listeners for the buttons
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the Delete button click
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Hiking hikingData = dataList.get(position);
                    int id = hikingData.getId();

                    // Show a confirmation dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this item?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User confirmed deletion
                            dbHelper.deleteHiking(id);

                            // Remove the deleted item from the data list
                            dataList.remove(position);

                            // Notify the adapter that the data has changed
                            adapter.resetDataList(dataList);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User canceled deletion, do nothing
                        }
                    });
                    builder.show();
                }
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the Update button click
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Lấy dữ liệu của mục được nhấp
                    Hiking hikingData = dataList.get(position);
                    int id = hikingData.getId();

                    // Tạo Intent để chuyển sang UpdateActivity
                    Intent intent = new Intent(itemView.getContext(), UpdateHikingActivity.class);

                    // Đính kèm dữ liệu của mục được nhấp vào Intent để chuyển sang UpdateActivity
                    intent.putExtra("id", String.valueOf(id)); // Đảm bảo id được truyền đúng kiểu dữ liệu

                    // Khởi chạy UpdateActivity
                    itemView.getContext().startActivity(intent);
                }
            }
        });

//        buttonViewDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    Hiking hikingData = dataList.get(position);
//                    int selectedHikingId = hikingData.getId(); // Lấy hikingId từ dữ liệu HikingData
//
//                    // Truyền hikingId qua DetailActivity
//                    Intent intent = new Intent(itemView.getContext(), ViewObservationActivity.class);
//                    intent.putExtra("hikingId", selectedHikingId);
//                    itemView.getContext().startActivity(intent);
//                }
//            }
//        });
    }}
