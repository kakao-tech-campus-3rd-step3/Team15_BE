package katecam.hyuswim.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import katecam.hyuswim.report.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {}
