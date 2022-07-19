package com.github.dergil.hierarchynote.view.recycler_view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dergil.hierarchynote.R;
import com.github.dergil.hierarchynote.model.entity.FileEntity;

public class FileListAdapter extends ListAdapter<FileEntity, FileListAdapter.FileViewHolder>
{
    private OnItemClickListener listener;
    public FileEntity currentFile;

    public FileListAdapter(@NonNull DiffUtil.ItemCallback<FileEntity> diffCallback) {
        super(diffCallback);
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        FileEntity current = getItem(position);
        currentFile = current;
        holder.bind(current.getName(), position);
    }

    public static class WordDiff extends DiffUtil.ItemCallback<FileEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull FileEntity oldItem, @NonNull FileEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull FileEntity oldItem, @NonNull FileEntity newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;
        private final Context context;

        private FileViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(pos));
                    }
                }
            });
            wordItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(String text, int position) {
            wordItemView.setText(text);
            if (getItem(position).isDir())
                wordItemView.setBackgroundColor(Color.parseColor("#FF03DAC5"));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(FileEntity file);
    }

    public FileEntity getFileAt(int pos) {
        return getItem(pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
