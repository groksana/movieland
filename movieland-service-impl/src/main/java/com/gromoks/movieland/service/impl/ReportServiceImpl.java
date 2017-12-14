package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.ReportDao;
import com.gromoks.movieland.entity.ReportInfo;
import com.gromoks.movieland.entity.ReportMovie;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.EmailService;
import com.gromoks.movieland.service.MovieService;
import com.gromoks.movieland.service.ReportService;
import com.gromoks.movieland.service.cache.ReportCache;
import com.gromoks.movieland.service.entity.ReportOutputType;
import com.gromoks.movieland.service.entity.ReportRequest;
import com.gromoks.movieland.service.entity.ReportStatus;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String FONT = "fonts/OpenSans-Italic.ttf";

    @Value("${report.directory}")
    private String reportDirectory;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReportCache reportCache;

    @Autowired
    @Qualifier("ioReportDao")
    private ReportDao reportDao;

    @Autowired
    @Qualifier("jdbcReportDao")
    private ReportDao jdbcReportDao;

    @Autowired
    private EmailService emailService;

    @Override
    public void addReportRequest(ReportRequest reportRequest) {
        reportCache.addRequest(reportRequest);
    }

    @Override
    @Scheduled(fixedRateString = "${report.fixedRate}", initialDelayString = "${report.fixedRate}")
    public void processReportRequest() {
        log.info("Start to process report requests");
        List<ReportRequest> reportRequests = reportCache.getNewRequests();

        for (ReportRequest reportRequest : reportRequests) {
            boolean changeResult = reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), reportRequest.getStatus(), ReportStatus.IN_PROGRESS);
            if (changeResult) {
                generateReport(reportRequest);

                String text = createReportEmailMessage(reportRequest);
                emailService.sendMail(reportRequest.getRequestedUser().getEmail(), text);

                String reportLink = reportRequest.getRequestUrl() + "/" + getFileNameFromRequest(reportRequest);
                String reportType = reportRequest.getReportType().toString();
                String email = reportRequest.getRequestedUser().getEmail();
                jdbcReportDao.insertReportInfo(new ReportInfo(reportType, email, reportLink));

                reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), ReportStatus.IN_PROGRESS, ReportStatus.COMPLETED);
            }
        }

        log.info("Finish to process report requests");
    }

    @Override
    public byte[] getReport(String filename) {
        File file = reportDao.getFile(filename);
        byte[] document;
        try {
            document = FileCopyUtils.copyToByteArray(file);
        } catch (IOException e) {
            log.error("File {} can't be read", filename);
            throw new RuntimeException("File " + filename + "can't be read");
        }
        return document;
    }

    @Override
    public void generateReport(ReportRequest reportRequest) {
        log.info("Start to generate report by report request {}", reportRequest.getRequestUuid());

        if (reportRequest.getReportOutputType() == ReportOutputType.XLSX) {
            try {
                generateXLSXReport(reportRequest);
            } catch (IOException e) {
                log.warn("Report can't be generated for user {}", reportRequest.getRequestedUser().getNickname());
                reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), ReportStatus.IN_PROGRESS, ReportStatus.REJECTED);
            }
        } else if (reportRequest.getReportOutputType() == ReportOutputType.PDF) {
            try {
                generatePDFReport(reportRequest);
            } catch (DocumentException | IOException e) {
                log.warn("Report can't be generated for user {}", reportRequest.getRequestedUser().getNickname());
                reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), ReportStatus.IN_PROGRESS, ReportStatus.REJECTED);
            }
        } else {
            log.warn("Report output type is not supported");
            reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), ReportStatus.IN_PROGRESS, ReportStatus.REJECTED);
        }

        log.info("Finish to generate report by report request");
    }

    @Override
    public List<ReportRequest> getReportRequestStatusByUser(User user) {
        return reportCache.getReportRequestStatusByUser(user);
    }

    @Override
    public List<ReportInfo> getReportLinkByEmail(String email) {
        return jdbcReportDao.getReportLinkByEmail(email);
    }

    @Override
    public void removeReport(ReportRequest reportRequest) {
        log.debug("Start to remove report and report request");

        reportCache.removeReportRequest(reportRequest);
        reportDao.removeFile(getFileNameFromRequest(reportRequest));

        log.debug("Finish to remove report and report request");
    }

    private void generateXLSXReport(ReportRequest reportRequest) throws IOException {
        log.info("Start to generate xlsx report for request = {}", reportRequest.getRequestUuid());

        List<ReportMovie> reportMovies = jdbcReportDao.getAllReportMovie();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(reportRequest.getReportType().toString());
        Row row = sheet.createRow((short) 0);
        row.createCell(0).setCellValue("MovieId");
        row.createCell(1).setCellValue("Title");
        row.createCell(2).setCellValue("Description");
        row.createCell(3).setCellValue("Genres");
        row.createCell(4).setCellValue("Price");
        row.createCell(5).setCellValue("Rating");
        row.createCell(6).setCellValue("ReviewCount");

        for (int i = 0; i < reportMovies.size(); i++) {
            row = sheet.createRow((short) i + 1);
            ReportMovie reportMovie = reportMovies.get(i);
            row.createCell(0).setCellValue(reportMovie.getMovieId());
            row.createCell(1).setCellValue(reportMovie.getTitle());
            row.createCell(2).setCellValue(reportMovie.getDescription());
            row.createCell(3).setCellValue(reportMovie.getGenres());
            row.createCell(4).setCellValue(reportMovie.getPrice());
            row.createCell(5).setCellValue(reportMovie.getRating());
            row.createCell(6).setCellValue(reportMovie.getReviewCount());
        }

        FileOutputStream fileOut = new FileOutputStream(reportDirectory + "/" + getFileNameFromRequest(reportRequest));
        workbook.write(fileOut);
        fileOut.close();

        log.info("Finish to generate xlsx report for request = {}", reportRequest.getRequestUuid());
    }

    private void generatePDFReport(ReportRequest reportRequest) throws DocumentException, IOException {
        log.info("Start to generate pdf report for request = {}", reportRequest.getRequestUuid());

        Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        List<ReportMovie> reportMovies = jdbcReportDao.getAllReportMovie();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(reportDirectory + "/" + getFileNameFromRequest(reportRequest)));
        document.open();

        Paragraph paragraph = new Paragraph("Список фильмов", font);
        document.add(paragraph);

        for (ReportMovie reportMovie : reportMovies) {
            document.add(new Paragraph(String.valueOf(reportMovie.getMovieId()) + " - " + reportMovie.getTitle(), font));
            document.add(new Paragraph(reportMovie.getDescription(), font));
            document.add(new Paragraph(reportMovie.getGenres(), font));
            document.add(new Paragraph("Стоимость: " + String.valueOf(reportMovie.getPrice()), font));
            document.add(new Paragraph("Рейтинг: " + String.valueOf(reportMovie.getRating()), font));
            document.add(new Paragraph("Количество комментариев: " + String.valueOf(reportMovie.getReviewCount()), font));
            document.add(new Paragraph("***"));
        }

        document.close();

        log.info("Finish to generate pdf report for request = {}", reportRequest.getRequestUuid());
    }

    private String getFileNameFromRequest(ReportRequest reportRequest) {
        return reportRequest.getRequestUuid() + "." + reportRequest.getReportOutputType().toString();
    }

    private String createReportEmailMessage(ReportRequest reportRequest) {
        String reportLink = reportRequest.getRequestUrl() + "/" + getFileNameFromRequest(reportRequest);
        StringBuilder stringBuilder = new StringBuilder(
                "Dear " + reportRequest.getRequestedUser().getNickname() + "," +
                        "\n\nPlease find link by requested report:" +
                        "\n" + reportLink);
        String text = stringBuilder.toString();
        return text;
    }
}
