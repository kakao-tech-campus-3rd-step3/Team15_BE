package katecam.hyuswim.report.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import katecam.hyuswim.comment.domain.Comment;
import katecam.hyuswim.comment.repository.CommentRepository;
import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.post.domain.Post;
import katecam.hyuswim.post.repository.PostRepository;
import katecam.hyuswim.report.domain.Report;
import katecam.hyuswim.report.domain.ReportReasonType;
import katecam.hyuswim.report.domain.ReportType;
import katecam.hyuswim.report.dto.ReportReasonResponse;
import katecam.hyuswim.report.dto.ReportRequest;
import katecam.hyuswim.report.repository.ReportRepository;
import katecam.hyuswim.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRespository;

  public List<ReportReasonResponse> getReasons() {
    return Arrays.stream(ReportReasonType.values())
        .map(katecam.hyuswim.report.dto.ReportReasonResponse::from)
        .toList();
  }

  @Transactional
  public void report(User reporter, ReportRequest request) {
    User reportedUser;

    switch(request.getReportType()){
        case POST -> {
            Post post = postRepository
                    .findById(request.getTargetId())
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
            reportedUser = post.getUser();
        }
        case COMMENT -> {
            Comment comment = commentRespository
                    .findById(request.getTargetId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
            reportedUser = comment.getUser();
        }
        default -> throw new CustomException(ErrorCode.INVALID_REPORT_TYPE);
    }


    Report report =
        Report.create(
            reporter,
            reportedUser,
            request.getReportType(),
            request.getTargetId(),
            request.getReportReasonType(),
            request.getContent());

    reportRepository.save(report);
  }
}
