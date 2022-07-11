package com.github.dergil.hierarchynote.view.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.dergil.hierarchynote.R;

class NoteViewHolder extends RecyclerView.ViewHolder {
    private final TextView wordItemView;
    private final Context context;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;


    NoteViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
//        itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, NewNoteActivity.class);
//            context.startActivity(intent);
////            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
////            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
////                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
//        });
        wordItemView = itemView.findViewById(R.id.textView);
    }

    public void bind(String text) {
        wordItemView.setText(text);
    }

    static NoteViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new NoteViewHolder(view);
    }
}

