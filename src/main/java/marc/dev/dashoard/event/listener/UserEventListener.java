package marc.dev.dashoard.event.listener;
import marc.dev.dashoard.event.UserEvent;
import marc.dev.dashoard.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
    @Autowired
    EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent userEvent) {
        switch (userEvent.getType()) {

            case REGISTRATION -> emailService.sendNewAccountEmail(userEvent.getUser().getName(), userEvent.getUser().getEmail(), (String) userEvent.getData().get("key"));
            case RESETPASSWORD -> emailService.sendPasswordResetEmail(userEvent.getUser().getName(), userEvent.getUser().getEmail(), (String) userEvent.getData().get("key"));
            default -> {}

        }

    }


}
