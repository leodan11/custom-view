package com.github.leodan11.customview.core.base;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.github.leodan11.customview.core.R;
import com.github.leodan11.customview.core.ReadMoreOption;

public abstract class ReadMoreOptionBase {
    protected Context context;
    protected int textLength;
    protected int textLengthType;
    protected String moreLabel;
    protected String lessLabel;
    protected int moreLabelColor;
    protected int lessLabelColor;
    protected boolean labelUnderLine;
    protected boolean expandAnimation;

    protected ReadMoreOptionBase(Context context, int textLength, int textLengthType, String moreLabel, String lessLabel, int moreLabelColor, int lessLabelColor, boolean labelUnderLine, boolean expandAnimation) {
        this.context = context;
        this.textLength = textLength;
        this.textLengthType = textLengthType;
        this.moreLabel = moreLabel;
        this.lessLabel = lessLabel;
        this.moreLabelColor = moreLabelColor;
        this.lessLabelColor = lessLabelColor;
        this.labelUnderLine = labelUnderLine;
        this.expandAnimation = expandAnimation;
    }

    /**
     * Set the text displayed in the textview.
     *
     * @param textView The view where the text will be displayed.
     * @param text The text to display.
     */
    public void addReadMoreTo(TextView textView, @StringRes int text) {
        addReadMoreTo(textView, context.getString(text));
    }

    /**
     * Set the text displayed in the textview.
     *
     * @param textView The view where the text will be displayed.
     * @param text The text to display.
     */
    public void addReadMoreTo(TextView textView, CharSequence text) {
        if (textLengthType == ReadMoreOption.TYPE_CHARACTER) {
            if (text.length() <= textLength) {
                textView.setText(text);
                return;
            }
        } else {
            textView.setLines(textLength);
            textView.setText(text);
        }
        textView.post(new Runnable() {
            @Override
            public void run() {
                int textLengthNew = textLength;
                if (textLengthType == ReadMoreOption.TYPE_LINE) {
                    if (textView.getLayout().getLineCount() <= textLength) {
                        textView.setText(text);
                        return;
                    }
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
                    String subString = text.subSequence(textView.getLayout().getLineStart(0), textView.getLayout().getLineEnd(textLength - 1)).toString();
                    textLengthNew = subString.length() - (moreLabel.length() + 4 + lp.rightMargin / 6);
                }
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text.subSequence(0, textLengthNew))
                        .append("…")
                        .append(" ")
                        .append(moreLabel);
                SpannableString ss = SpannableString.valueOf(spannableStringBuilder);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        addReadLess(textView, text);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(labelUnderLine);
                        ds.setColor(moreLabelColor);
                    }
                };
                ss.setSpan(clickableSpan, ss.length() - moreLabel.length(), ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (expandAnimation) {
                    LayoutTransition layoutTransition = new LayoutTransition();
                    layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
                    ((ViewGroup) textView.getParent()).setLayoutTransition(layoutTransition);
                }
                textView.setText(ss);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    private void addReadLess(TextView textView, CharSequence text) {
        textView.setMaxLines(Integer.MAX_VALUE);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text)
                .append(" ")
                .append(lessLabel);
        SpannableString ss = SpannableString.valueOf(spannableStringBuilder);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                new Handler(Looper.getMainLooper()).post(() -> addReadMoreTo(textView, text));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(labelUnderLine);
                ds.setColor(lessLabelColor);
            }
        };
        ss.setSpan(clickableSpan, ss.length() - lessLabel.length(), ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * Creates an [ReadMoreOption] with the arguments supplied to this builder.
     *
     * @property context The parent context
     * @constructor Create empty Builder.
     */
    public abstract static class Builder<D extends ReadMoreOptionBase> {
        protected final Context context;

        // Optional
        protected int textLength = 100;
        protected int textLengthType = ReadMoreOption.TYPE_CHARACTER;
        protected String moreLabel;
        protected String lessLabel;
        protected int moreLabelColor;
        protected int lessLabelColor;
        protected boolean labelUnderLine = false;
        protected boolean expandAnimation = false;

        protected Builder(@NonNull Context context) {
            this.context = context;
            this.moreLabel = context.getString(R.string.text_value_read_more);
            this.lessLabel = context.getString(R.string.text_value_read_less);
            this.moreLabelColor = getColorDefault();
            this.lessLabelColor = getColorDefault();
        }

        /**
         * Set the maximum size in the textview.
         *
         * @param length The maximum size.
         */
        public Builder<D> textLength(int length) {
            this.textLength = length;
            return this;
        }

        /**
         * Set the type of the size of the textview based on the size of the text. By lines or by number of characters.
         * Use [ReadMoreOption.TYPE_LINE] or [ReadMoreOption.TYPE_CHARACTER].
         *
         * @param type The display type.
         */
        public Builder<D> textLengthType(int type) {
            this.textLengthType = type;
            return this;
        }

        /**
         * Set the label text to action read more.
         *
         * @param more The text to display.
         */
        public Builder<D> moreLabel(String more) {
            this.moreLabel = more;
            return this;
        }

        /**
         * Set the label text to action read less.
         *
         * @param less The text to display.
         */
        public Builder<D> lessLabel(String less) {
            this.lessLabel = less;
            return this;
        }

        /**
         * Set the label text color to read more action.
         *
         * @param moreColor Label color. Eg: [Color.RED]
         */
        public Builder<D> moreLabelColor(int moreColor) {
            this.moreLabelColor = moreColor;
            return this;
        }

        /**
         * Set the label text color to read less action.
         *
         * @param lessColor Label color. Eg: [Color.BLUE]
         */
        public Builder<D> lessLabelColor(int lessColor) {
            this.lessLabelColor = lessColor;
            return this;
        }

        /**
         * Set the underline to the text.
         *
         * @param ul A [Boolean] value.
         */
        public Builder<D> labelUnderLine(boolean ul) {
            this.labelUnderLine = ul;
            return this;
        }

        /**
         * Set animation to textview.
         *
         * @param anim A [Boolean] value.
         */
        public Builder<D> expandAnimation(boolean anim) {
            this.expandAnimation = anim;
            return this;
        }

        /**
         * Get color default
         *
         * @return int Color Primary default theme
         */
        private int getColorDefault() {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
            return typedValue.data;
        }

        /**
         * Creates an [ReadMoreOption] with the arguments supplied to this builder.
         *
         */
        public abstract D build();
    }

}
