package katecam.hyuswim.comment.event;

import katecam.hyuswim.common.event.DomainEvent;

public record CommentCreatedEvent(Long userId) implements DomainEvent { }
