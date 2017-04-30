package com.am.demo.taskapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.demo.taskapp.R;
import com.am.demo.taskapp.database.TaskDAO;
import com.am.demo.taskapp.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by malbor806 on 23.04.2017.
 */

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {
    private List<Task> tasks;
    private OnTaskClickListener onTaskClickListener;
    private Context context;

    public TasksRecyclerViewAdapter(Context context) {
        this.context = context;
        tasks = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setOnTaskClickListener(onTaskClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size() > 0 ? tasks.size() : 0;
    }


    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
    }

    @Override
    public void onItemDismiss(int position) {
        TaskDAO taskDAO = TaskDAO.getInstance(context);
        taskDAO.deleteTaskById(tasks.get(position).getId());
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private OnTaskClickListener onTaskClickListener;
        private Task task;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_listTaskTitle);
            descriptionTextView = (TextView) itemView.findViewById(R.id.tv_listDescription);
            itemView.setOnClickListener(v -> {
                        if (onTaskClickListener != null) {
                            onTaskClickListener.onTaskClick(task);
                        }
                    }
            );
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }

        void bind(Task task) {
            this.task = task;
            titleTextView.setText(task.getTitle());
            descriptionTextView.setText(task.getDescription());
        }

        public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener) {
            this.onTaskClickListener = onTaskClickListener;
        }
    }

}
