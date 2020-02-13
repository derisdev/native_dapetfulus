package id.deris.dapetfulus;

import java.util.ArrayList;

public class InputNotifSementara {

    private static String[] title = {"Pengumuman pemenang dana","Event Follow Instagram", "Pengumuman pemenang dana","Event Follow Instagram"};
    private static String[] time = {"01 Februari 2020","27 Januari 2020", "01 Februari 2020","27 Januari 2020"};
    private static String[] des = {
            "Lorem ipsum dolor sit amet, te noluisse volutpat dissentias his, vel et mazim ludus vivendo, ne pro autem labores offendit. Zril feugiat feugait vis at. Mel partem dolorum id. His omnis exerci temporibus ex. No brute postulant periculis mei, dicam molestie qui at, vim idque zril nemore id.",
            "Lorem ipsum dolor sit amet, te noluisse volutpat dissentias his, vel et mazim ludus vivendo, ne pro autem labores offendit. Zril feugiat feugait vis at. Mel partem dolorum id. His omnis exerci temporibus ex. No brute postulant periculis mei, dicam molestie qui at, vim idque zril nemore id.",
            "Lorem ipsum dolor sit amet, te noluisse volutpat dissentias his, vel et mazim ludus vivendo, ne pro autem labores offendit. Zril feugiat feugait vis at. Mel partem dolorum id. His omnis exerci temporibus ex. No brute postulant periculis mei, dicam molestie qui at, vim idque zril nemore id.",
            "Lorem ipsum dolor sit amet, te noluisse volutpat dissentias his, vel et mazim ludus vivendo, ne pro autem labores offendit. Zril feugiat feugait vis at. Mel partem dolorum id. His omnis exerci temporibus ex. No brute postulant periculis mei, dicam molestie qui at, vim idque zril nemore id."
    };

    static ArrayList<NotifModel> getListData() {
        ArrayList<NotifModel> list = new ArrayList<>();
        for (int position = 0; position < title.length; position++) {
            NotifModel notifModel = new NotifModel();
            notifModel.setTitle(title[position]);
            notifModel.setTime(time[position]);
            notifModel.setDes(des[position]);
            list.add(notifModel);
        }
        return list;
    }

}
