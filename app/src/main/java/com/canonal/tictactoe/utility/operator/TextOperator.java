package com.canonal.tictactoe.utility.operator;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.canonal.tictactoe.R;

public class TextOperator {

    public static boolean isFieldEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    public static void printEmptyFieldError(EditText editText, Context context) {
        editText.setHint(context.getString(R.string.fill_empty_field));
        editText.setHintTextColor(context.getResources().getColor(R.color.red));

    }
}
