package katecam.hyuswim.user.event;
import katecam.hyuswim.common.event.DomainEvent;

public record UserVisitedEvent(Long userId) implements DomainEvent { }
