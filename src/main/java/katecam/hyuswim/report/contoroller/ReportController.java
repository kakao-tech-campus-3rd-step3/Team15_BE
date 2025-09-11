package katecam.hyuswim.report.contoroller;

import katecam.hyuswim.report.dto.ReportReasonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.report.dto.ReportRequest;
import katecam.hyuswim.report.service.ReportService;
import katecam.hyuswim.user.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;
  @PostMapping
  public ResponseEntity<Void> report(@LoginUser User user, @RequestBody ReportRequest request) {
    reportService.report(user, request);
    return ResponseEntity.ok().build();
  }

    @GetMapping("/reasons")
    public ResponseEntity<List<ReportReasonResponse>> getReasons() {
        return ResponseEntity.ok(reportService.getReasons());
    }

}
