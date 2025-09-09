package katecam.hyuswim.report.repository;

import katecam.hyuswim.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
