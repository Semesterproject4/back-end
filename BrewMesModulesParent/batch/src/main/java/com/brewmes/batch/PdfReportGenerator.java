package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.util.Products;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class PdfReportGenerator {
    private static final Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font textFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static PdfWriter pdfWriter;
    private static DataOverTime dataOverTime;
    private static Batch batch;
    static String xaxis = "Time (s)";


    private PdfReportGenerator() {

    }

    /**
     * Method makes a pdf report of a batch named "batch_report.pdf"
     * @param data is an object of {@code DataOverTime} which represents a batch and its data which is reliant on time
     */
    public static void generatePdf(DataOverTime data, String destinationPath) {
        try {
            dataOverTime = data;
            batch = dataOverTime.getBatch();
            MachineData lastMachineData = batch.getData().get(batch.getData().size() - 1);

            String destination = destinationPath;
            File file = new File(destination);
            Document document = new Document();
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Add title and timestamp
            addTitlePage(document);

            // Create a table with 6 columns
            PdfPTable table = new PdfPTable(6);
            // Create the table content
            table.addCell("Beer type");
            table.addCell("Amount to produce");
            table.addCell("Total produced");
            table.addCell("Acceptable products");
            table.addCell("Defect products");
            table.addCell("Machine speed");


            table.addCell(String.valueOf(batch.getProductType().productName));
            table.addCell(String.valueOf(batch.getAmountToProduce()));
            table.addCell(String.valueOf(lastMachineData.getProcessed()));
            table.addCell(String.valueOf(lastMachineData.getAcceptableProducts()));
            table.addCell(String.valueOf(lastMachineData.getDefectProducts()));
            table.addCell(String.format("%.2f", batch.getDesiredSpeed()));
            // Add table to document
            document.add(table);

            //add OEE
            addOEESection(document);

            // Add bar chart over time in states
            addTimeSection(document);
            // Add a new page
            document.newPage();

            // Add Humidity graph and table
            addHumiditySection(document);
            // Add a new page
            document.newPage();

            // Add Vibration graph and table
            addVibrationSection(document);
            // Add a new page
            document.newPage();

            // Add Temperature graph and table
            addTemperatureSection(document);

            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addOEESection(Document document) throws DocumentException {
        Paragraph oee = new Paragraph();

        addEmptyLine(oee, 1);

        oee.add(new Paragraph("The Overall Equipment Effectiveness (OEE) of the batch is: " + batch.getOee() + "%"));

        addEmptyLine(oee, 1);

        document.add(oee);
    }

    private static void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        // Write the title
        preface.add(new Paragraph("Batch Report", titleFont));
        addEmptyLine(preface, 1);

        // Write batch id
        preface.add(new Paragraph("Batch id: " + batch.getID(), textFontBold));

        // Write machine id
        preface.add(new Paragraph("Machine id: " + batch.getConnectionID(), textFontBold));

        // Create a timestamp for report created
        preface.add(new Paragraph("The report is generated at: " + new Date(), textFontBold));
        addEmptyLine(preface, 2);

        // Add preface to document
        document.add(preface);
    }


    private static void addHumiditySection(Document document) throws DocumentException {
        Paragraph humidity = new Paragraph();
        addEmptyLine(humidity, 1);

        int width = 500;
        int height = 400;

        // Create line chart of humidity over time
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Humidity");
        long totalTime = 0L;

        if (!batch.getData().isEmpty()) {
            LocalDateTime startTime;

            startTime = batch.getData().get(0).getTimestamp();
            for (MachineData data : batch.getData()) {
                long timeElapsed = Math.abs(startTime.toEpochSecond(ZoneOffset.MAX) - data.getTimestamp().toEpochSecond(ZoneOffset.MAX));
                if (totalTime < timeElapsed) {
                    totalTime = timeElapsed;
                }
                series.add((Number) Math.abs(startTime.toEpochSecond(ZoneOffset.MAX) - data.getTimestamp().toEpochSecond(ZoneOffset.MAX)), data.getHumidity());
            }
        }
        dataset.addSeries(series);
        JFreeChart lineChart = ChartFactory.createXYLineChart("Humidity", xaxis, "Humidity"
                , dataset, PlotOrientation.VERTICAL, false, true, false);
        makeTables(document, humidity, width, height, lineChart, dataOverTime.getAvgHumidity(), dataOverTime.getMinHumidity(), dataOverTime.getMaxHumidity());
    }

    private static void addVibrationSection(Document document) throws DocumentException {
        Paragraph vibration = new Paragraph();
        addEmptyLine(vibration, 1);

        int width = 500;
        int height = 400;

        // Create line chart of vibration over time
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Vibration");
        long totalTime = 0L;
        if (!batch.getData().isEmpty()) {
            LocalDateTime startTime;

            startTime = batch.getData().get(0).getTimestamp();
            for (MachineData data : batch.getData()) {
                long timeElapsed = Math.abs(startTime.toEpochSecond(ZoneOffset.MAX) - data.getTimestamp().toEpochSecond(ZoneOffset.MAX));
                if (totalTime < timeElapsed) {
                    totalTime = timeElapsed;
                }
                series.add((Number) Math.abs(startTime.toEpochSecond(ZoneOffset.MAX) - data.getTimestamp().toEpochSecond(ZoneOffset.MAX)), data.getVibration());
            }
        }
        dataset.addSeries(series);
        JFreeChart lineChart = ChartFactory.createXYLineChart("Vibration", xaxis, "Vibration"
                , dataset, PlotOrientation.VERTICAL, false, true, false);
        makeTables(document, vibration, width, height, lineChart, dataOverTime.getAvgVibration(), dataOverTime.getMinVibration(), dataOverTime.getMaxVibration());
    }

    private static void addTemperatureSection(Document document) throws DocumentException {
        Paragraph temperature = new Paragraph();
        addEmptyLine(temperature, 1);

        int width = 500;
        int height = 400;

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Temperature");
        long totalTime = 0;

        if (!batch.getData().isEmpty()) {
            LocalDateTime startTime;

            startTime = batch.getData().get(0).getTimestamp();
            for (MachineData data : batch.getData()) {
                long timeElapsed = Math.abs(startTime.toEpochSecond(ZoneOffset.MAX) - data.getTimestamp().toEpochSecond(ZoneOffset.MAX));
                if (totalTime < timeElapsed) {
                    totalTime = timeElapsed;
                }
                series.add((Number) Math.abs(startTime.toEpochSecond(ZoneOffset.MAX) - data.getTimestamp().toEpochSecond(ZoneOffset.MAX)), data.getTemperature());
            }
        }
        dataset.addSeries(series);
        JFreeChart lineChart = ChartFactory.createXYLineChart("Temperature", xaxis, "Temperature"
                , dataset, PlotOrientation.VERTICAL, false, true, false);
        makeTables(document, temperature, width, height, lineChart, dataOverTime.getAvgTemp(), dataOverTime.getMinTemp(), dataOverTime.getMaxTemp());
    }


    private static void makeTables(Document document, Paragraph paragraph, int width, int height, JFreeChart lineChart, double average, double min, double max) throws DocumentException {
        NumberAxis range = (NumberAxis) lineChart.getXYPlot().getRangeAxis();

        range.setRange((min - 0.5), (max + 0.5));

        PdfContentByte contentByte = pdfWriter.getDirectContent();
        PdfTemplate template = contentByte.createTemplate(width, height);
        Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
        Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);

        lineChart.draw(graphics2d, rectangle2d);
        graphics2d.dispose();
        Image chartImage = Image.getInstance(template);
        paragraph.add(chartImage);

        // Create the table for minimum, maximum and average.
        PdfPTable table = new PdfPTable(3);
        table.addCell("Minimum");
        table.addCell("Maximum");
        table.addCell("Average");
        table.addCell(String.valueOf(min));
        table.addCell(String.valueOf(max));
        table.addCell(String.valueOf(average));
        // Add table to document underneath the graph
        addEmptyLine(paragraph, 1);
        paragraph.add(table);

        document.add(paragraph);
    }

    private static void makeStepPlot(Document document, Paragraph paragraph, int width, int height, JFreeChart stepChart) throws DocumentException {
        stepChart.getXYPlot().setDomainAxis(new NumberAxis());
        NumberAxis range = (NumberAxis) stepChart.getXYPlot().getRangeAxis();

        range.setRange(0, 20);

        PdfContentByte contentByte = pdfWriter.getDirectContent();
        PdfTemplate template = contentByte.createTemplate(width, height);
        Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
        Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);

        stepChart.draw(graphics2d, rectangle2d);
        graphics2d.dispose();
        Image chartImage = Image.getInstance(template);
        paragraph.add(chartImage);

        paragraph.add(new Paragraph("                                                                             " + xaxis));
        addEmptyLine(paragraph, 1);
        document.add(paragraph);
    }

    private static void addTimeSection(Document document) throws DocumentException {
        Paragraph timeState = new Paragraph();
        addEmptyLine(timeState, 1);

        int width = 500;
        int height = 400;


        // Create line chart of vibration over time
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("States");
        long totalTime = 0L;

        if (!batch.getData().isEmpty()) {
            LocalDateTime startTime;

            startTime = batch.getData().get(0).getTimestamp();
            for (MachineData data : batch.getData()) {
                long timeElapsed = Math.abs(startTime.toEpochSecond(ZoneOffset.MAX) - data.getTimestamp().toEpochSecond(ZoneOffset.MAX));
                if (totalTime < timeElapsed) {
                    totalTime = timeElapsed;
                }
                series.add((Number) Math.abs(startTime.toEpochSecond(ZoneOffset.MAX) - data.getTimestamp().toEpochSecond(ZoneOffset.MAX)), data.getState().value);
            }
        }

        dataset.addSeries(series);
        JFreeChart lineChart = ChartFactory.createXYStepChart("Machine state", xaxis, "State"
                , dataset, PlotOrientation.VERTICAL, false, true, false);
        makeStepPlot(document, timeState, width, height, lineChart);
    }

    // Add line space in text
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph("\n"));
        }
    }

}
