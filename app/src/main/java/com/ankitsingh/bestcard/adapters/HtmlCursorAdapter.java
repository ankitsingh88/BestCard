package com.ankitsingh.bestcard.adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HtmlCursorAdapter extends SimpleCursorAdapter {

    public HtmlCursorAdapter(
            final Context context,
            final int layout,
            final Cursor c,
            final String[] from,
            final int[] to,
            final int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void setViewText(
            final TextView view,
            final String text) {
        view.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
    }
}
