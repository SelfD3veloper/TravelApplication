package self.d3veloper.travelapplication;

import android.os.Bundle;


import cbedoy.cblibrary.MainActivity;


public class MasterViewController extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_view_controller);
        mViewFlipper = (android.widget.ViewFlipper) findViewById(R.id.view_flipper);
    }

}
