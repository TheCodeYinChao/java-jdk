package persion.timier;

import java.util.Timer;
import java.util.TimerTask;

/**
 * dsc: TimierDemo
 * date: 2021/3/29 14:56
 * author: zyc
 */
public class TimierDemo {
    public static void main(String[] args) {

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("timer task ");
            }
        };
        timer.schedule(timerTask,2,5000);

    }
}
