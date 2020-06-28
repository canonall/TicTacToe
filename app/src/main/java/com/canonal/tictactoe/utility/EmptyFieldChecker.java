package com.canonal.tictactoe.utility;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.canonal.tictactoe.R;

public class EmptyFieldChecker {

    public static boolean isFieldEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    public static void printEmptyError(EditText editText, Context context) {
        editText.setHint("Please fill this field");
        editText.setHintTextColor(context.getResources().getColor(R.color.red));

    }
}
