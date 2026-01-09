package com.example.myapplication;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.WorkshopViewHolder> {

    private final List<Workshop> workshopList;
    private final Context context;

    public WorkshopAdapter(Context context, List<Workshop> workshopList) {
        this.context = context;
        this.workshopList = workshopList;
    }

    @NonNull
    @Override
    public WorkshopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workshop_item, parent, false);
        return new WorkshopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopViewHolder holder, int position) {
        Workshop workshop = workshopList.get(position);
        holder.bind(workshop);
    }

    @Override
    public int getItemCount() {
        return workshopList.size();
    }

    class WorkshopViewHolder extends RecyclerView.ViewHolder {

        private final TextView topicTextView;
        private final TextView timeTextView;
        private final TextView countdownTextView;
        private CountDownTimer countDownTimer;

        public WorkshopViewHolder(@NonNull View itemView) {
            super(itemView);
            topicTextView = itemView.findViewById(R.id.workshop_topic);
            timeTextView = itemView.findViewById(R.id.workshop_time);
            countdownTextView = itemView.findViewById(R.id.countdown_timer);
        }

        void bind(final Workshop workshop) {
            topicTextView.setText(workshop.getTopic());
            timeTextView.setText(String.format("Starts at: %s, Duration: %d minutes", workshop.getTime(), workshop.getDuration()));

            startCountdown(workshop.getTime());
        }

        private void startCountdown(String workshopTime) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            try {
                Date workshopDate = sdf.parse(workshopTime);
                long workshopMillis = workshopDate.getTime();
                long nowMillis = System.currentTimeMillis();
                long diff = workshopMillis - nowMillis;

                if (diff > 0 && diff <= 24 * 60 * 60 * 1000) {
                    countDownTimer = new CountDownTimer(diff, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long hours = millisUntilFinished / (60 * 60 * 1000);
                            long minutes = (millisUntilFinished / (60 * 1000)) % 60;
                            long seconds = (millisUntilFinished / 1000) % 60;
                            countdownTextView.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
                        }

                        @Override
                        public void onFinish() {
                            countdownTextView.setText("Workshop Started!");
                        }
                    }.start();
                } else if (diff <= 0) {
                    countdownTextView.setText("Workshop Ended");
                } else {
                    countdownTextView.setText("Upcoming");
                }
            } catch (ParseException e) {
                e.printStackTrace();
                countdownTextView.setText("Invalid Time");
            }
        }
    }
}