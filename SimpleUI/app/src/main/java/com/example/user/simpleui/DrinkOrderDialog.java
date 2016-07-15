package com.example.user.simpleui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;//必須改為和DrinkMenuActivity相同的package
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkOrderDialog.OnDrinkOrderListener} interface
 * to handle interaction events.
 * Use the {@link DrinkOrderDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkOrderDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnDrinkOrderListener mListener;

    public DrinkOrderDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrinkOrderDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static DrinkOrderDialog newInstance(String param1, String param2) {
        DrinkOrderDialog fragment = new DrinkOrderDialog();
        Bundle args = new Bundle();//將變數放進去Bundle在onCreate()出現-->類似Activity的intent(帶資料)
        args.putString(ARG_PARAM1, param1);//(key值,value)用變數做替代ARG_PARAM1替代param1
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_drink_order_dialog, container, false);//new出layout檔
//    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getArguments() != null)
        {
            Bundle bundle = getArguments();
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());//顯示大區塊有標題的那種比較美

        View contentView = getActivity().getLayoutInflater().inflate(R.layout.fragment_drink_order_dialog,null);
        alertDialogBuilder.setView(contentView).setTitle("Hello Dialog").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return alertDialogBuilder.create();
    }

    //兩個Fragment無法互相溝通必須藉由Activity
    @Override
    public void onAttach(Context context) {//生命週期開始
        super.onAttach(context);
        if (context instanceof OnDrinkOrderListener) {//如果Fragment可以跟Activity溝通
            mListener = (OnDrinkOrderListener) context;//將跟Activity溝通的方式設定成mListener
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDrinkOrderListener {//Activity必須要實作這個interface，Fragment跟Activity溝通的方式
        // TODO: Update argument type and name
        void onDrinkOrderFinish();
    }
}
