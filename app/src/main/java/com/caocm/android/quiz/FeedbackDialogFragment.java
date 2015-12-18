package com.caocm.android.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by caocm_000 on 12/10/2015.
 */
public class FeedbackDialogFragment extends DialogFragment {
    public interface ResultDialogListener{
        public void nextQuestion();
    }
    ResultDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.feedback_dialog, null);

        Bundle bundle = getArguments();
        int result = bundle.getInt("RESULT");
        boolean islast = bundle.getBoolean("ISLAST");
        TextView resultView = (TextView)layout.findViewById(R.id.result);
        ImageView resultPic = (ImageView)layout.findViewById(R.id.result_pic);
        if(result == 1) {
            resultView.setText("Very Good!!!");
            resultPic.setImageResource(R.drawable.verygood);
        }else if(result == -1){
            resultView.setText("Please Select Your Answer!!!");
            resultPic.setImageResource(R.drawable.confused);
        }else{
            resultView.setText("Sorry!!!");
            resultPic.setImageResource(R.drawable.tryagain);
        }
        builder.setView(layout)
                .setNeutralButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FeedbackDialogFragment.this.dismiss();
                    }
                });
        if(islast) {
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    FeedbackDialogFragment.this.dismiss();
                }
            });
        }else{
            builder.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    mListener.nextQuestion();
                }
            });
        }

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mListener = (ResultDialogListener) activity;
    }
}
