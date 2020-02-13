package id.deris.dapetfulus;

import java.util.ArrayList;

public class InputQASementara {

    private static String[] question = {"Kenapa dana belum masuk","kenpa ini","kenpa itu", "Kenapa Pembayaran saya belum masuk?", "Kenapa Pembayaran saya belum masuk?", "Kenapa Pembayaran saya belum masuk?", "Kenapa Pembayaran saya belum masuk?", "Kenapa Pembayaran saya belum masuk?", };
    private static String[] answer = {"Karena belum bla fe","coba ini yang panjang","yang terakhir cob lebih pajjangn teksna  a gitu lih", "Pelangan yang terhormat, kami akan melakukan pembayaran dalam 3 hari jam kerja. tidak termasuk hari minggu dan hari libut nasional", "Pelangan yang terhormat, kami akan melakukan pembayaran dalam 3 hari jam kerja. tidak termasuk hari minggu dan hari libut nasional", "Pelangan yang terhormat, kami akan melakukan pembayaran dalam 3 hari jam kerja. tidak termasuk hari minggu dan hari libut nasional", "Pelangan yang terhormat, kami akan melakukan pembayaran dalam 3 hari jam kerja. tidak termasuk hari minggu dan hari libut nasional", "Pelangan yang terhormat, kami akan melakukan pembayaran dalam 3 hari jam kerja. tidak termasuk hari minggu dan hari libut nasional"};

    static ArrayList<QAModel> getListData() {
        ArrayList<QAModel> list = new ArrayList<>();
        for (int position = 0; position < question.length; position++) {
            QAModel qaModel = new QAModel();
            qaModel.setQuestion(question[position]);
            qaModel.setAnswer(answer[position]);
            list.add(qaModel);
        }
        return list;
    }


}
