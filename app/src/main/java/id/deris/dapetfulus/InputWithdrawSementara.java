package id.deris.dapetfulus;

import java.util.ArrayList;

public class InputWithdrawSementara {

    private static String[] via = {"DANA","OVO","DANA","Gopay","DANA","OVO","DANA","Gopay",};
    private static String[] amount = {"IDR 10.000","IDR 10.000","IDR 10.000","IDR 10.000","IDR 10.000","IDR 10.000","IDR 10.000","IDR 10.000",};
    private static String[] status = {"Pending","Pending","DPendingANA","Pending","Pending","Pending","Pending","Berhasil"};
    private static String[] time = {"20 Februari 2020","20 Februari 2020","20 Februari 2020","20 Februari 2020","20 Februari 2020","20 Februari 2020","20 Februari 2020","20 Februari 2020",};




    static ArrayList<WithdrawModel> getListData() {
        ArrayList<WithdrawModel> list = new ArrayList<>();
        for (int position = 0; position < via.length; position++) {
            WithdrawModel withdrawModel = new WithdrawModel();
            withdrawModel.setVia(via[position]);
            withdrawModel.setAmount(amount[position]);
            withdrawModel.setStatus(status[position]);
            withdrawModel.setTime(time[position]);
            list.add(withdrawModel);
        }
        return list;
    }
}
