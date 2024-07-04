package marc.dev.dashoard.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import marc.dev.dashoard.entity.UserEntity;
import marc.dev.dashoard.enumeration.EventType;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType type;
    private Map<?,?> data;
}
