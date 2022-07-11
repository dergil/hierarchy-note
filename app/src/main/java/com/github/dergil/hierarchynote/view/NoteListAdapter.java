package com.github.dergil.hierarchynote.view;

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
import com.github.dergil.hierarchynote.model.entity.NoteEntity;

public class NoteListAdapter extends ListAdapter<NoteEntity, NoteListAdapter.NoteViewHolder>
{
    private OnItemClickListener listener;
    public NoteEntity currentNote;

//    Context context;
    public NoteListAdapter(@NonNull DiffUtil.ItemCallback<NoteEntity> diffCallback) {
        super(diffCallback);
//        context = (Context)this;
    }

//    public interface mClickListener {
//        public void mClick(View v, int position);
//    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        NoteEntity current = getItem(position);
        currentNote = current;
        holder.bind(current.getName(), position);
    }

//    @Override
//    public void onClick(View view) {
//
//    }

    public static class WordDiff extends DiffUtil.ItemCallback<NoteEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;
        private final Context context;
        public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;


        private NoteViewHolder(View itemView) {
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
//        itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, NewNoteActivity.class);
//            context.startActivity(intent);
////            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
////            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
////                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
//        });
            wordItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(String text, int position) {
            wordItemView.setText(text);
            if (getItem(position).isDir())
                wordItemView.setBackgroundColor(Color.parseColor("#FF03DAC5"));
        }

//        static com.example.roomwordsample.NoteViewHolder create(ViewGroup parent) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.recyclerview_item, parent, false);
//            return new com.example.roomwordsample.NoteViewHolder(view);
//        }
    }

    public interface OnItemClickListener {
        void onItemClick(NoteEntity note);
    }

    public NoteEntity getNoteAt(int pos) {
        return getItem(pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
