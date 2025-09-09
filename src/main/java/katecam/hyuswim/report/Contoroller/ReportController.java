package katecam.hyuswim.report.Contoroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.report.dto.ReportRequest;
import katecam.hyuswim.report.service.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @PostMapping("/reports")
  public ResponseEntity<Void> report(
      @RequestParam Long userId, @RequestBody ReportRequest request) {
    reportService.report(userId, request);
    return ResponseEntity.ok().build();
  }
}
