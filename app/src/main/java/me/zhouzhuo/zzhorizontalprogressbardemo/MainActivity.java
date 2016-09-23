package me.zhouzhuo.zzhorizontalprogressbardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ZzHorizontalProgressBar pb = (ZzHorizontalProgressBar) findViewById(R.id.pb);
        final ZzHorizontalProgressBar pb1 = (ZzHorizontalProgressBar) findViewById(R.id.pb1);
        final ZzHorizontalProgressBar pb2 = (ZzHorizontalProgressBar) findViewById(R.id.pb2);
        final ZzHorizontalProgressBar pb3 = (ZzHorizontalProgressBar) findViewById(R.id.pb3);
        final ZzHorizontalProgressBar pb4 = (ZzHorizontalProgressBar) findViewById(R.id.pb4);

        final TextView tvProgress = (TextView) findViewById(R.id.tv_progress);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pb.setProgress(progress);
                pb1.setProgress(progress);
                pb2.setProgress(progress);
                pb3.setProgress(progress);
                pb4.setProgress(progress);
                tvProgress.setText("prgress = " + pb1.getProgress() + ", max = " + pb1.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
