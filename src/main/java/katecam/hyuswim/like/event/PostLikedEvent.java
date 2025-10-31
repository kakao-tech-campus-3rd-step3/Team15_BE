package katecam.hyuswim.like.event;
import katecam.hyuswim.common.event.DomainEvent;

public record PostLikedEvent(Long userId) implements DomainEvent { }
