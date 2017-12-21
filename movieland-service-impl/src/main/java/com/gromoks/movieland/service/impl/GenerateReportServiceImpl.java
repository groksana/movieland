package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.InfoReportDao;
import com.gromoks.movieland.entity.ReportMovie;
import com.gromoks.movieland.service.GenerateReportService;
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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.gromoks.movieland.service.impl.util.ReportUtil.getReportNameFromRequest;

@Service
public class GenerateReportServiceImpl implements GenerateReportService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${report.font}")
    private String reportFont;

    @Value("${report.directory}")
    private String reportDirectory;

    @Autowired
    private ReportCache reportCache;

    @Autowired
    private InfoReportDao infoReportDao;

    @Override
    public void generateReport(ReportRequest reportRequest) {
        log.info("Start to generate report by report request {}", reportRequest.getRequestUuid());

        if (reportRequest.getReportOutputType() == ReportOutputType.XLSX) {
            try {
                generateXLSXReport(reportRequest);
            } catch (IOException e) {
                log.warn("Report can't be generated for user {}", reportRequest.getRequestedUser().getNickname());
                reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), ReportStatus.REJECTED);
            }
        } else if (reportRequest.getReportOutputType() == ReportOutputType.PDF) {
            try {
                generatePDFReport(reportRequest);
            } catch (DocumentException | IOException e) {
                log.warn("Report can't be generated for user {}", reportRequest.getRequestedUser().getNickname());
                reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), ReportStatus.REJECTED);
            }
        } else {
            log.warn("Report output type is not supported");
            reportCache.changeReportRequestStatus(reportRequest.getRequestUuid(), ReportStatus.REJECTED);
        }

        log.info("Finish to generate report by report request");
    }

    private void generateXLSXReport(ReportRequest reportRequest) throws IOException {
        log.info("Start to generate xlsx report for request = {}", reportRequest.getRequestUuid());

        List<ReportMovie> reportMovies = infoReportDao.getAllReportMovie();

        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(reportRequest.getReportType().toString());
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("MovieId");
        row.createCell(1).setCellValue("Title");
        row.createCell(2).setCellValue("Description");
        row.createCell(3).setCellValue("Genres");
        row.createCell(4).setCellValue("Price");
        row.createCell(5).setCellValue("Rating");
        row.createCell(6).setCellValue("ReviewCount");

        int index = 0;
        for (ReportMovie reportMovie : reportMovies) {
            row = sheet.createRow(index + 1);
            row.createCell(0).setCellValue(reportMovie.getMovieId());
            row.createCell(1).setCellValue(reportMovie.getTitle());
            row.createCell(2).setCellValue(reportMovie.getDescription());
            row.createCell(3).setCellValue(reportMovie.getGenres());
            row.createCell(4).setCellValue(reportMovie.getPrice());
            row.createCell(5).setCellValue(reportMovie.getRating());
            row.createCell(6).setCellValue(reportMovie.getReviewCount());
            index++;
        }

        File outputFile = new File(reportDirectory, getReportNameFromRequest(reportRequest));
        try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
            workbook.write(fileOut);
        }

        log.info("Finish to generate xlsx report for request = {}", reportRequest.getRequestUuid());
    }

    private void generatePDFReport(ReportRequest reportRequest) throws DocumentException, IOException {
        log.info("Start to generate pdf report for request = {}", reportRequest.getRequestUuid());

        Font font = FontFactory.getFont(reportFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        List<ReportMovie> reportMovies = infoReportDao.getAllReportMovie();

        Document document = null;
        try {
            document = new Document();
            File outputFile = new File(reportDirectory, getReportNameFromRequest(reportRequest));
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
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
        } finally {
            if (document != null) {
                document.close();
            }
        }

        log.info("Finish to generate pdf report for request = {}", reportRequest.getRequestUuid());
    }

}
