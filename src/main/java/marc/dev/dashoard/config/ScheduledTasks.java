package marc.dev.dashoard.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTasks {

    private final ThreadPoolTaskScheduler taskScheduler;

    public ScheduledTasks(ThreadPoolTaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Scheduled(fixedRate = 3600000)  // 1 hour
    public void performTask() {
        taskScheduler.schedule(() -> {
            // Your periodic task logic here
        }, new Date());
    }
}
