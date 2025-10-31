package katecam.hyuswim.mission.event;
import katecam.hyuswim.common.event.DomainEvent;

public record MissionCompletedEvent(Long userId) implements DomainEvent { }
