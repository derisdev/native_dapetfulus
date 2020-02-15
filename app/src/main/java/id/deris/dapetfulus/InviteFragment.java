package id.deris.dapetfulus;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;

public class InviteFragment extends Fragment {

    private CardView tabInvite1, tabInvite2, linkLayout, refferalLayout;
    private TextView tabInvite1text, tabInvite2text, refferalCodeOwner, linkRefferal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite, container, false);



        refferalCodeOwner = view.findViewById(R.id.refferal_code_owner);
        linkRefferal = view.findViewById(R.id.link_refferal);
        tabInvite1 = view.findViewById(R.id.tabInvite1);
        tabInvite2 = view.findViewById(R.id.tabInvite2);
        linkLayout = view.findViewById(R.id.link_layout);
        refferalLayout = view.findViewById(R.id.refferal_layout);
        tabInvite1text = view.findViewById(R.id.tabInvite1Text);
        tabInvite2text = view.findViewById(R.id.tabInvite2Text);
        ImageButton menuBtn = view.findViewById(R.id.btn_menu);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });




        tabInvite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabInvite1.setCardBackgroundColor(Color.WHITE);
                tabInvite2.setCardBackgroundColor(getResources().getColor(R.color.tabTransparent));
                tabInvite1text.setTextColor(getResources().getColor(R.color.amber));
                tabInvite2text.setTextColor(Color.BLACK);
                linkLayout.setVisibility(View.VISIBLE);
                refferalLayout.setVisibility(View.GONE);
            }
        });

        tabInvite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabInvite2.setCardBackgroundColor(Color.WHITE);
                tabInvite1.setCardBackgroundColor(getResources().getColor(R.color.tabTransparent));
                tabInvite2text.setTextColor(getResources().getColor(R.color.amber));
                tabInvite1text.setTextColor(Color.BLACK);
                refferalLayout.setVisibility(View.VISIBLE);
                linkLayout.setVisibility(View.GONE);
            }
        });

        refferalCodeOwner.setText(Prefs.getString("refferal_code_owner", "belum ada data"));
        linkRefferal.setText(Prefs.getString("link_refferal", "belum ada data"));

        return view;
    }

}
