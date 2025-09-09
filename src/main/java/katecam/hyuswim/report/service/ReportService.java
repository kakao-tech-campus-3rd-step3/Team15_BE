package katecam.hyuswim.report.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.comment.repository.CommentRespository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.report.domain.Report;
import katecam.hyuswim.report.domain.ReportType;
import katecam.hyuswim.report.dto.ReportRequest;
import katecam.hyuswim.report.repository.ReportRepository;
import katecam.hyuswim.user.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class ReportService {
  private ReportRepository reportRepository;
  private UserRepository userRepository;
  private PostRepository postRepository;
  private CommentRespository commentRespository;

  @Transactional
  public void report(Long userId, ReportRequest request) {
    User reportedUser;
    User reporter =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    if (request.getReportType() == ReportType.POST) {
      Post post =
          postRepository
              .findById(request.getTargetId())
              .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
      reportedUser = post.getUser();
    } else {
      Comment comment =
          commentRespository
              .findById(request.getTargetId())
              .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
      reportedUser = comment.getUser();
    }

    Report report =
        Report.create(
            reporter,
            reportedUser,
            request.getReportType(),
            request.getTargetId(),
            request.getReasonType(),
            request.getContent());

    reportRepository.save(report);
  }
}
