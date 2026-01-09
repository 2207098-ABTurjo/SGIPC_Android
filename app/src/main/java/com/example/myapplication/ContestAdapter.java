package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ContestViewHolder> {

    private final List<Contest> contestList;
    private final Context context;

    public ContestAdapter(Context context, List<Contest> contestList) {
        this.context = context;
        this.contestList = contestList;
    }

    @NonNull
    @Override
    public ContestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_item, parent, false);
        return new ContestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContestViewHolder holder, int position) {
        Contest contest = contestList.get(position);
        holder.bind(contest);
    }

    @Override
    public int getItemCount() {
        return contestList.size();
    }

    class ContestViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final TextView timeTextView;
        private final TextView countdownTextView;
        private final Button contestLinkButton;
        private CountDownTimer countDownTimer;

        private final List<String> BROWSER_KEYWORDS = Arrays.asList("chrome", "firefox", "opera", "samsung", "edge", "browser");

        public ContestViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.contest_title);
            timeTextView = itemView.findViewById(R.id.contest_time);
            countdownTextView = itemView.findViewById(R.id.countdown_timer);
            contestLinkButton = itemView.findViewById(R.id.contest_link_button);
        }

        void bind(final Contest contest) {
            titleTextView.setText(contest.getTitle());
            timeTextView.setText(String.format("Starts at: %s, Duration: %d minutes", contest.getTime(), contest.getDuration()));

            String rawLink = contest.getLink();
            if (rawLink == null || rawLink.trim().isEmpty()) {
                contestLinkButton.setVisibility(View.GONE);
            } else {
                contestLinkButton.setVisibility(View.VISIBLE);
                final String normalized = normalizeUrl(rawLink.trim());
                contestLinkButton.setOnClickListener(v -> {
                    try {
                        Uri uri = Uri.parse(normalized);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        PackageManager pm = context.getPackageManager();

                        List<ResolveInfo> handlers = pm.queryIntentActivities(intent, 0);
                        String chosenBrowserPackage = null;
                        if (handlers != null && !handlers.isEmpty()) {
                            for (ResolveInfo info : handlers) {
                                String pkg = info.activityInfo.packageName.toLowerCase(Locale.ROOT);
                                for (String kw : BROWSER_KEYWORDS) {
                                    if (pkg.contains(kw)) {
                                        chosenBrowserPackage = info.activityInfo.packageName;
                                        break;
                                    }
                                }
                                if (chosenBrowserPackage != null) break;
                            }
                        }

                        if (chosenBrowserPackage != null) {
                            intent.setPackage(chosenBrowserPackage);
                            context.startActivity(intent);
                        } else {
                            Intent chooser = Intent.createChooser(intent, "Open link with");
                            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (chooser.resolveActivity(pm) != null) {
                                context.startActivity(chooser);
                            } else {
                                Toast.makeText(context, "No application available to open the link", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            startCountdown(contest.getTime());
        }

        private String normalizeUrl(String url) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return "https://" + url;
            }
            return url;
        }

        private void startCountdown(String contestTime) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            try {
                Date contestDate = sdf.parse(contestTime);
                long contestMillis = contestDate.getTime();
                long nowMillis = System.currentTimeMillis();
                long diff = contestMillis - nowMillis;

                if (diff > 0 && diff <= 24 * 60 * 60 * 1000) { // Start countdown within 24 hours
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
                            countdownTextView.setText("Contest Started!");
                        }
                    }.start();
                } else if (diff <= 0) {
                    countdownTextView.setText("Contest Ended");
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