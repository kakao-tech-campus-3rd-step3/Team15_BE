package katecam.hyuswim.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import katecam.hyuswim.auth.login.LoginUser;
import katecam.hyuswim.report.dto.ReportRequest;
import katecam.hyuswim.report.service.ReportService;
import katecam.hyuswim.user.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/reports")
    public ResponseEntity<Void> report(
            @LoginUser User user, @RequestBody ReportRequest request) {
        reportService.report(user, request);
        return ResponseEntity.ok().build();
    }
}


