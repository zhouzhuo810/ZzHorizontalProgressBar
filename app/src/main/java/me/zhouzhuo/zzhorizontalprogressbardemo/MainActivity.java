package me.zhouzhuo.zzhorizontalprogressbardemo;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final ZzHorizontalProgressBar pb = findViewById(R.id.pb);
        final ZzHorizontalProgressBar pb1 = findViewById(R.id.pb1);
        final ZzHorizontalProgressBar pb2 = findViewById(R.id.pb2);
        final ZzHorizontalProgressBar pb3 = findViewById(R.id.pb3);
        final ZzHorizontalProgressBar pb4 = findViewById(R.id.pb4);
        final ZzHorizontalProgressBar pb5 = findViewById(R.id.pb5);
        final ZzHorizontalProgressBar pb6 = findViewById(R.id.pb6);
        
        final TextView tvProgress = findViewById(R.id.tv_progress);
        
        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pb.setProgress(progress);
                pb1.setProgress(progress);
                pb1.setSecondProgress(progress - 20);
                pb2.setProgress(progress);
                pb2.setSecondProgress(progress - 30);
                pb3.setProgress(progress);
                pb4.setProgress(progress);
                pb5.setProgress(progress);
                pb6.setProgress(progress);
                if (progress > 80) {
                    pb3.setProgressColor(0xff00ff00);
                    pb5.setGradientColorAndBorderColor(0x7ff5515f, 0x7f9f041b, 0xffff001f);
                } else {
                    pb3.setProgressColor(0xffff0000);
                    pb5.setGradientColorAndBorderColor(0x7fb4ec51, 0x7f429321, 0xff85ff00);
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            
            }
        });
        
        
        pb4.setOnProgressChangedListener(new ZzHorizontalProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(ZzHorizontalProgressBar progressBar, int max, int progress) {
                tvProgress.setText("prgress = " + progress + ", max = " + max);
            }
            
            @Override
            public void onSecondProgressChanged(ZzHorizontalProgressBar progressBar, int max, int progress) {
            
            }
        });
        
    }
}
